package orm.impl;

import orm.Id;
import orm.JdbcExecutor;
import orm.JdbcH2ConnectorHelper;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JdbcExecutorImpl<T> implements JdbcExecutor<T> {
    private final Connection connection;
    private Field idField;
    private List<Field> fields;
    private String updateStatement;
    private String selectStatement;
    private String selectMaxIdStatement;
    private String insertStatementWithId;
    private String insertStatement;

    public JdbcExecutorImpl() {
        JdbcH2ConnectorHelper h2ConnectorHelper = new JdbcH2ConnectorHelperImpl(
                "jdbc:h2:tcp://localhost:9092/~/test_db",
                "sa",
                "");
        h2ConnectorHelper.connect();
        connection = h2ConnectorHelper.connection();
    }

    private void parseObject(Class<?> dataClazz) {
        if (fields != null && idField != null) {
            return;
        }
        fields =  Arrays.asList(dataClazz.getDeclaredFields());
        fields.stream().forEach( it -> { it.setAccessible(true); });
        idField = fields.stream().filter(f -> f.getAnnotation(Id.class) != null).findAny()
                .orElseThrow(() -> new RuntimeException("Cannot use JdbcExecutor because class '"
                        + dataClazz.getName() + "' does not have field with annotation 'Id'")
                );
    }

    private String insertStatement(T obj) {
        var clazz = obj.getClass();
        try {
            boolean idIsExist = idField.getLong(obj) != 0;
            if (idIsExist && insertStatementWithId != null && !insertStatementWithId.isEmpty()) {
                return insertStatementWithId;
            }
            else if (!idIsExist && insertStatement != null && !insertStatement.isEmpty()) {
                return  insertStatement;
            }
            else {
                String tableName = clazz.getSimpleName();
                var columnNamesList = fields.stream()
                        .filter(it -> {
                            try {
                                return (!it.equals(idField) || it.getLong(obj) != 0);
                            } catch (Exception e) {
                                throw new RuntimeException("Cannot collect column names list for insert statement"
                                        + " because of error: " + e.getMessage());
                            }
                        })
                        .map(it -> it.getName()).collect(Collectors.toList());
                var columnValues = new String[columnNamesList.size()];
                Arrays.fill(columnValues, "?");

                var statement = String.format(" insert into %s (%s) values (%s)",
                        tableName,
                        columnNamesList.stream().collect(Collectors.joining(", ")),
                        Arrays.asList(columnValues).stream().collect(Collectors.joining(", ")));

                if (idIsExist) {
                    insertStatementWithId = statement;
                } else {
                    insertStatement = statement;
                }
                return statement;
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot create insert statement because of error: "
                    + e.getMessage());
        }
    }

    @Override
    public void create(T objectData) throws SQLException {
        if (objectData == null) {
            throw new RuntimeException("Cannot create object because object data is not defined");
        }

        parseObject(objectData.getClass());
        Savepoint savepoint = connection.setSavepoint("InsertSavepoint");
        try (PreparedStatement preparedStatement
                     = connection.prepareStatement(insertStatement(objectData))) {
            int paramInd = 1;
            for (var field : fields) {
                if (field.equals(idField)) {
                    if (field.getLong(objectData) == 0) {
                        continue;
                    }
                }
                var fieldData = field.get(objectData);
                preparedStatement.setObject(paramInd, fieldData);
                paramInd++;
            }
            preparedStatement.execute();
            connection.commit();
        }
        catch (Exception e) {
            connection.rollback(savepoint);
            throw new RuntimeException("Cannot insert data into table "
                    + objectData.getClass().getSimpleName() + " because of error: " + e.getMessage());
        }
        try {
            if (idField.getLong(objectData) == 0) {
                // if idField is null, then it was created with auto increment column
                long idOfObjectData = maxId(objectData);
                idField.setLong(objectData, idOfObjectData);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot determine id of object because of error: " + e.getMessage());
        }
    }

    private String maxIdQuery(Class<?>  objClazz) {
        if (selectMaxIdStatement == null) {
            selectMaxIdStatement = String.format("Select max(%s) from %s",
                    idField.getName(),
                    objClazz.getSimpleName());
        }
        return selectMaxIdStatement;
    }

    private long maxId(T objectData) {
        try (PreparedStatement preparedStatement
                     = connection.prepareStatement(maxIdQuery(objectData.getClass()))) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getLong(1);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot get max id value from table "
                    + objectData.getClass().getSimpleName() + " because of error: " + e.getMessage());
        }
    }

    private String updateStatement(T obj) {
        if (updateStatement == null) {
            var clazz = obj.getClass();
            String tableName = clazz.getSimpleName();
            updateStatement = String.format("update %s set %s where %s = ?", tableName,
                    fields.stream()
                            .filter(it -> !it.equals(idField))
                            .map(it -> String.format(" %s = ?", it.getName()))
                            .collect(Collectors.joining(", ")),
                    idField.getName()
            );
        }
        return updateStatement;
    }

    @Override
    public void update(T objectData) throws SQLException {
        if (objectData == null) {
            throw new RuntimeException("Cannot update object because object data is not defined");
        }
        parseObject(objectData.getClass());
        Savepoint savepoint = connection.setSavepoint("UpdateSavepoint");
        try (PreparedStatement preparedStatement
                     = connection.prepareStatement(updateStatement(objectData))) {
            int parameterIndex = 1;
            for (var field : fields) {
                if (field.equals(idField)) {
                    continue;
                }
                var fieldData = field.get(objectData);
                preparedStatement.setObject(parameterIndex, fieldData);
                parameterIndex++;
            }

            var idFieldData = idField.get(objectData);
            preparedStatement.setObject(parameterIndex, idFieldData);

            preparedStatement.execute();
            connection.commit();
        }
        catch (Exception e) {
            connection.rollback(savepoint);
            throw new RuntimeException("Cannot update data into table "
                    + objectData.getClass().getSimpleName() + " because of error: " + e.getMessage());
        }
    }

    @Override
    public void createOrUpdate(T objectData) {
        if (objectData == null) {
            throw new RuntimeException("Cannot create or update object because object data is not defined");
        }
        var objDataClazz = objectData.getClass();
        try {
            parseObject(objDataClazz);
            long idData = idField.getLong(objectData);
            if (load(idData, objDataClazz) == null) {
                create(objectData);
            } else {
                update(objectData);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot perform Create or Update object of class "
                    + objDataClazz.getName() + " because of error: " + e.getMessage());
        }
    }

    private String selectStatement(Class<?> clazz) {
        if (selectStatement == null) {
            selectStatement = String.format("Select %s from %s where %s = ?",
                    fields.stream().map(it -> it.getName()).collect(Collectors.joining(", ")),
                    clazz.getSimpleName(),
                    idField.getName());
        }
        return selectStatement;
    }

    private void setPrimitiveValues(Field field, Class<?> fieldType, Object objNew, ResultSet resultSet, int columnInd) {
        try {
            if (int.class.isAssignableFrom(fieldType)) {
                field.setInt(objNew, resultSet.getInt(columnInd));
            } else if (char.class.isAssignableFrom(fieldType)) {
                String data = resultSet.getString(columnInd);
                field.setChar(objNew, data != null ? data.charAt(0) : null);
            } else if (float.class.isAssignableFrom(fieldType)) {
                field.setFloat(objNew, resultSet.getFloat(columnInd));
            } else if (long.class.isAssignableFrom(fieldType)) {
                field.setLong(objNew, resultSet.getLong(columnInd));
            } else if (double.class.isAssignableFrom(fieldType)) {
                field.setDouble(objNew, resultSet.getDouble(columnInd));
            } else if (short.class.isAssignableFrom(fieldType)) {
                field.setShort(objNew, resultSet.getShort(columnInd));
            } else if (byte.class.isAssignableFrom(fieldType)) {
                field.setByte(objNew, resultSet.getByte(columnInd));
            } else if (boolean.class.isAssignableFrom(fieldType)) {
                field.setBoolean(objNew, resultSet.getBoolean(columnInd));
            } else {
                throw new RuntimeException("Field type '" + fieldType.getName() + "' is unsupported");
            }
        }
        catch (Exception e) {
            throw new RuntimeException(String.format("Cannot set data Cannot set data into field '%s' with field type '%s' because of following error: %s",
                    field.getName(),
                    fieldType.getName(),
                    e.getMessage()));
        }
    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectStatement(clazz))) {
            preparedStatement.setLong(1, id);
            int columnInd = 1;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                final T objNew = clazz.getConstructor().newInstance();
                for (var field: fields) {
                    var fieldType = field.getType();

                    if (!fieldType.isPrimitive()) {
                        field.set(objNew, resultSet.getObject(columnInd));
                    }
                    else {
                        setPrimitiveValues(field, fieldType, objNew, resultSet, columnInd);
                    }
                    columnInd++;
                }
                return objNew;
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot select data from table "
                    + clazz.getSimpleName() + " because of error: " + e.getMessage());
        }
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}

