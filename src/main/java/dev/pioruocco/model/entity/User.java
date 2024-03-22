package dev.pioruocco.model.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

//entity representing the user in the database
//we implements UserDetails so that we can set the information of the user
@Entity
@Table(name = "user")
@CrossOrigin
public class User implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING) //we say to the database to store its value as string
    private Role role;


    //getter e setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    //method of USerDetails
    //authorithies of the user (its roles)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    //checking if the account is expired
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //checking if the account is locked
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //checking if the credentials are exprired or not
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //properties if the account is enabled
    @Override
    public boolean isEnabled() {
        return true;
    }
}
