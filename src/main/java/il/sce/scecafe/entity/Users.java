package il.sce.scecafe.entity;


import javax.persistence.*;
import java.util.List;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String login;
    private String password;
    private String birthDay;
    private String firstname;
    private String lastname;
    private String email;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="users_roles",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Users(Long id, String login, String password, String birthDay, String firstname, String lastname, String email, String phoneNumber, List<Role> roles) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.birthDay = birthDay;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }

    public Users(String login, String password, String birthDay, String firstname, String lastname, String email, String phoneNumber, List<Role> roles) {
        this.login = login;
        this.password = password;
        this.birthDay = birthDay;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }

    public Users(Users newUser) {
        this.login = newUser.login;
        this.password = newUser.password;
        this.birthDay = newUser.birthDay;
        this.firstname = newUser.firstname;
        this.lastname = newUser.lastname;
        this.email = newUser.email;
        this.phoneNumber = newUser.phoneNumber;
        this.roles = newUser.roles;
    }

    private String phoneNumber;
    public Users() {
    }

}
