package dev.trabalho.xfragil.entities;


import dev.trabalho.xfragil.utils.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "senha", nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "perfil", nullable = false)
    private Role role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
