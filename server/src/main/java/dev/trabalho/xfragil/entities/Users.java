package dev.trabalho.xfragil.entities;


import dev.trabalho.xfragil.utils.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String login;
    
    @Column(nullable = true, unique = true)
    private String email;

    @Column(name = "senha", nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "perfil", nullable = false)
    private Role role;
    
    public Users(){}

    public Users(String login, String email, String password, Role role) 
    {
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;
    }  
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String username) {
        this.login = username;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
