package org.dukecon.keycoak.user.doag;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
public class DoagUser {

    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String realUsername;

    public DoagUser(String id, String username, String firstName, String lastName, String realUsername) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = username;
        this.realUsername = realUsername;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRealUsername() {
        return realUsername;
    }

    public void setRealUsername(String realUsername) {
        this.realUsername = realUsername;
    }

    public String toString() {
        return "User[" + String.join(";", id, realUsername, firstName, lastName, email) + "]";
    }
}
