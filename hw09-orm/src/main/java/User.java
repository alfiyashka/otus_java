import orm.impl.Id;

public class User {
    private @Id
    long id;
    private char name;
    private int age;

    public User(int id, char name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    public User() {

    }

    public int getAge() {
        return age;
    }

    public long getId() {
        return id;
    }

    public char getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(char name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }

        User user = (User) obj;
        return this.name == user.name
                && this.age == user.age
                && this.id == user.id;

    }
}

