package domain;

import datamapper.InstructorMapper;

public class User extends DomainObject{
    private Name name = null;
    private String email = null;
    private String password = null;

    public User(){super();};

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
