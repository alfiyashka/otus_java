package model;


import orm.Id;

public class Account {

    private @Id
    long no;
    private String type;
    private float rest;

    public Account(long no, String type, float rest) {
        this.type = type;
        this.rest = rest;
        this.no = no;
    }

    public Account() {
    }

    public long getNo() {
        return no;
    }

    public float getRest() {
        return rest;
    }

    public String getType() {
        return type;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public void setRest(float rest) {
        this.rest = rest;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Account)) {
            return false;
        }

        Account account = (Account) obj;
        return this.no == account.no
                && this.rest == account.rest
                && this.type == account.type;

    }

}


