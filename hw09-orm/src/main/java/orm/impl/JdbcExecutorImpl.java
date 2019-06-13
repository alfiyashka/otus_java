package orm.impl;

import orm.JdbcExecutor;

import java.lang.reflect.Field;
import java.sql.*;

public class JdbcExecutorImpl<T> implements JdbcExecutor<T> {
    private final Connection connection;

    public JdbcExecutorImpl() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/~/test_db",
                    "sa", "");
            connection.setAutoCommit(false);
        }
        catch (Exception e){
            throw new RuntimeException("Cannot connect to database H2 because of following error:"
                    + e.getMessage());
        }
    }

    private boolean checkObject(Class<?> dataClazz) {
        var fields = dataClazz.getDeclaredFields();
        for (var field: fields){
            if (field.getAnnotation(Id.class) != null) {
                return true;
            }
        }
        throw new RuntimeException("Cannot use JdbcExecutor because class '"
                + dataClazz.getName() + "' does not have field with annotation 'Id'");
    }

    private String insertStatement(T obj) {
        var clazz = obj.getClass();
        try {
            String tableName = clazz.getSimpleName();
            StringBuilder statement = new StringBuilder("insert into " + tableName);

            var fields = clazz.getDeclaredFields();
            boolean needSeparator = false;
            StringBuilder columnNames = new StringBuilder(" (");
            StringBuilder columnValues = new StringBuilder(" (");
            for (var field : fields) {
                if (field.getAnnotation(Id.class) != null) {
                    if (!field.canAccess(obj)) {
                        field.setAccessible(true);
                    }
                    if (field.get(obj) == null) {
                        continue;
                    }
                }
                if (needSeparator) {
                    columnValues.append(", ");
                    columnNames.append(", ");
                }
                columnValues.append("?");
                columnNames.append(field.getName());
                needSeparator = true;
            }
            columnValues.append(")");
            columnNames.append(")");
            statement.append(columnNames);
            statement.append(" values ");
            statement.append(columnValues);
            return statement.toString();
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot create insert statement because of error: "
                    + e.getMessage());
        }
    }

    private void setDataIntoPreparedStatement(PreparedStatement preparedStatement,
                                              Class<?> fieldType,
                                              Object fieldData,
                                              int rowInd) {
        try {
            if (Integer.class.isAssignableFrom(fieldType)
                    || int.class.isAssignableFrom(fieldType)) {
                preparedStatement.setInt(rowInd, (int) fieldData);
            } else if (String.class.isAssignableFrom(fieldType)) {
                preparedStatement.setString(rowInd, (String) fieldData);
            } else if (char.class.isAssignableFrom(fieldType)
                    || Character.class.isAssignableFrom(fieldType)) {
                preparedStatement.setString(rowInd, ((Character) fieldData).toString());
            } else if (Float.class.isAssignableFrom(fieldType)
                    || float.class.isAssignableFrom(fieldType)) {
                preparedStatement.setFloat(rowInd, (Float) fieldData);
            } else if (Long.class.isAssignableFrom(fieldType)
                    || long.class.isAssignableFrom(fieldType)) {
                preparedStatement.setLong(rowInd, (Long) fieldData);
            } else if (Double.class.isAssignableFrom(fieldType)
                    || double.class.isAssignableFrom(fieldType)) {
                preparedStatement.setDouble(rowInd, (Double) fieldData);
            } else if (Short.class.isAssignableFrom(fieldType)
                    || short.class.isAssignableFrom(fieldType)) {
                preparedStatement.setShort(rowInd, (Short) fieldData);
            } else if (Byte.class.isAssignableFrom(fieldType)
                    || byte.class.isAssignableFrom(fieldType)) {
                preparedStatement.setByte(rowInd, (Byte) fieldData);
            } else if (Boolean.class.isAssignableFrom(fieldType)
                    || boolean.class.isAssignableFrom(fieldType)) {
                preparedStatement.setBoolean(rowInd, (Boolean) fieldData);
            } else {
                throw new RuntimeException("Unsupported data type - '" + fieldType.getName() + "'");
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot set data into prepared statement because of error: "
                    + e.getMessage());
        }
    }

    @Override
    public void create(T objectData) throws SQLException {
        if (objectData == null) {
            throw new RuntimeException("Cannot create object because object data is not defined");
        }

        checkObject(objectData.getClass());
        Field idField = null;
        Savepoint savepoint = connection.setSavepoint("InsertSavepoint");
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStatement(objectData))) {
            var fields = objectData.getClass().getDeclaredFields();
            int rowInd = 1;
            for (var field: fields) {
                if (!field.canAccess(objectData)) {
                    field.setAccessible(true);
                }
                if (field.getAnnotation(Id.class) != null) {
                    idField = field;
                    if (field.get(objectData) == null) {
                        continue;
                    }
                }
                var fieldData = field.get(objectData);
                var fieldType = field.getType();
                setDataIntoPreparedStatement(preparedStatement, fieldType, fieldData, rowInd);
                rowInd++;
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
            if (idField.get(objectData) == null) {
                // if idField is null, then it was created with auto increment column
                int idOfObjectData = maxId(objectData);
                idField.setInt(objectData, idOfObjectData);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot determine id of object because of error: " + e.getMessage());
        }
    }

    private String idColumnName(Class<?>  objClazz) {
        var fields = objClazz.getDeclaredFields();
        for (var field: fields) {
            if (field.getAnnotation(Id.class) != null) {
                return field.getName();
            }
        }
        throw new RuntimeException("Cannot find column name from field with Id annotation");
    }

    private String maxIdQuery(Class<?>  objClazz) {
        return "Select max(" + idColumnName(objClazz) + ") from " + objClazz.getSimpleName();
    }

    private int maxId(T objectData) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(maxIdQuery(objectData.getClass()))) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot get max id value from table "
                    + objectData.getClass().getSimpleName() + " because of error: " + e.getMessage());
        }
    }

    private String updateStatement(T obj) {
        var clazz = obj.getClass();
        String tableName = clazz.getSimpleName();
        StringBuilder statement = new StringBuilder("update " + tableName +" set ");

        var fields = clazz.getDeclaredFields();
        Field idField = null;
        boolean needSeparator = false;
        for (int i = 0; i < fields.length; i++) {
            if (needSeparator) {
                statement.append(", ");
            }
            if (fields[i].getAnnotation(Id.class) != null) {
                idField = fields[i];
                continue;
            }
            statement.append(fields[i].getName() + " = ?");
            needSeparator = true;
        }
        statement.append(" where " + idField.getName() + " = ? ");
        return statement.toString();
    }

    @Override
    public void update(T objectData) throws SQLException {
        if (objectData == null) {
            throw new RuntimeException("Cannot update object because object data is not defined");
        }
        checkObject(objectData.getClass());
        Savepoint savepoint = connection.setSavepoint("UpdateSavepoint");
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateStatement(objectData))) {
            var fields = objectData.getClass().getDeclaredFields();
            Field idField = null;
            int rowInd = 1;
            for (var field : fields) {
                if (!field.canAccess(objectData)){
                    field.setAccessible(true);
                }
                if (field.getAnnotation(Id.class) != null) {
                    idField = field;
                    continue;
                }
                var fieldData = field.get(objectData);
                var fieldType = field.getType();
                setDataIntoPreparedStatement(preparedStatement, fieldType, fieldData, rowInd);
                rowInd++;
            }

            var idFieldData = idField.get(objectData);
            var idFieldType = idField.getType();
            preparedStatement.setString(rowInd, idFieldData.toString());
            setDataIntoPreparedStatement(preparedStatement, idFieldType, idFieldData, rowInd);

            preparedStatement.execute();
            connection.commit();
        }
        catch (Exception e) {
            connection.rollback(savepoint);
            throw new RuntimeException("Cannot update data into table "
                    + objectData.getClass().getSimpleName() + " because of error: " + e.getMessage());
        }
    }

    public long getIdData(T objectData) {
        try {
            var fields = objectData.getClass().getDeclaredFields();

            for (var field : fields) {
                if (!field.canAccess(objectData)) {
                    field.setAccessible(true);
                }
                if (field.getAnnotation(Id.class) != null) {
                    return field.getLong(objectData);
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot get data from field with annotation 'Id' because of error: "
                    + e.getMessage());
        }
        return -1;
    }

    @Override
    public void createOrUpdate(T objectData) throws SQLException {
        if (objectData == null) {
            throw new RuntimeException("Cannot create or update object because object data is not defined");
        }
        checkObject(objectData.getClass());
        var objDataClazz = objectData.getClass();
        checkObject(objDataClazz);
        long idData = getIdData(objectData);
        if (load(idData, objDataClazz) == null) {
            create(objectData);
        }
        else {
            update(objectData);
        }
    }

    private String selectStatement(Class<?> clazz) {
        StringBuilder statement = new StringBuilder("Select ");
        var fields = clazz.getDeclaredFields();
        Field idField = null;
        boolean needSeparator = false;
        for (var field: fields) {
            if (field.getAnnotation(Id.class) != null) {
                idField = field;
            }
            if (needSeparator) {
                statement.append(", ");
            }

            statement.append(field.getName());
            needSeparator = true;
        }
        statement.append(" from " + clazz.getSimpleName());
        statement.append(" where " + idField.getName() + " = ?");
        return statement.toString();
    }

    private <T> T newObject(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        }
        catch(Exception e) {
            throw new RuntimeException("Cannot create object of class " + clazz.getName()
                    + " because of following error: " + e.getMessage());
        }
    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
        if (!checkObject(clazz)) {
            return null;
        }
        final T objNew = newObject(clazz);

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectStatement(clazz))) {
            preparedStatement.setLong(1, id);
            int rowInd = 1;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                for (var field: clazz.getDeclaredFields()) {
                    if (!field.canAccess(objNew)) {
                        field.setAccessible(true);
                    }
                    var fieldType = field.getType();
                    if (Integer.class.isAssignableFrom(fieldType)
                            || int.class.isAssignableFrom(fieldType)){
                        field.setInt(objNew, resultSet.getInt(rowInd));
                    }
                    else if (String.class.isAssignableFrom(fieldType)) {
                        field.set(objNew, resultSet.getString(rowInd));
                    }
                    else if (char.class.isAssignableFrom(fieldType)
                            || Character.class.isAssignableFrom(fieldType)) {
                        String data = resultSet.getString(rowInd);
                        field.setChar(objNew, data != null ? data.charAt(0) : null);
                    }
                    else if (Float.class.isAssignableFrom(fieldType)
                            || float.class.isAssignableFrom(fieldType)) {
                        field.setFloat(objNew, resultSet.getFloat(rowInd));
                    }
                    else if (Long.class.isAssignableFrom(fieldType)
                            || long.class.isAssignableFrom(fieldType)) {
                        field.setLong(objNew, resultSet.getLong(rowInd));
                    }
                    else if (Double.class.isAssignableFrom(fieldType)
                            || double.class.isAssignableFrom(fieldType)) {
                        field.setDouble(objNew, resultSet.getDouble(rowInd));
                    }
                    else if (Short.class.isAssignableFrom(fieldType)
                            || short.class.isAssignableFrom(fieldType)) {
                        field.setShort(objNew, resultSet.getShort(rowInd));
                    }
                    else if (Byte.class.isAssignableFrom(fieldType)
                            || byte.class.isAssignableFrom(fieldType)) {
                        field.setByte(objNew, resultSet.getByte(rowInd));
                    }
                    else if (Boolean.class.isAssignableFrom(fieldType)
                            || boolean.class.isAssignableFrom(fieldType)) {
                        field.setBoolean(objNew, resultSet.getBoolean(rowInd));
                    }
                    else {
                        throw new RuntimeException("Cannot set data into field '" + field.getName()
                                + "' because field type '" + fieldType.getName() +"' is unsupported");
                    }
                    rowInd++;
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot select data from table "
                    + clazz.getSimpleName() + " because of error: " + e.getMessage());
        }
        return objNew;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}

