package com.nutritiontracker.NutritionTrackerUserService.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.nutritiontracker.NutritionTrackerUserService.enumeration.Role;
import jakarta.persistence.*;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
//merge with user principal
@Entity
@Table(name="users")
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id", nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String height;
    private int weight;
    private int age;
    private String activityLevel;
    private String goal;
    @Enumerated(EnumType.STRING)
    private Role role;
    private int startingWeight;
    @OneToMany(mappedBy = "users")
    private List<Entry> entries;

    public User(){}

    //user constructor
    public User(Long id, String firstname, String lastname, String email, String password, String height, int weight, int age,
                String activityLevel, String goal, Role role, String[] authorities, int startingWeight, List<Entry> entries) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.role = role;
        this.startingWeight = startingWeight;
        this.entries = entries;
    }

    @Override
    public String toString(){
        return "User{ " + this.id + " firstname = " + this.firstname + ", lastname = " + this.lastname +
                ", email = " + this.email + ", password = " + this.password + ", height = " + this.height +
                ", weight = " + this.weight + ", age = " + this.age + ", activity level = " + this.activityLevel +
                ", goal = " + this.goal + ", entries = " + this.entries + "}";
    }

    //getters and setters
    public Long getId() { return id; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getEmail() { return email; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public String getHeight() { return height; }
    public int getWeight() { return weight; }
    public int getAge() { return age; }
    public String getActivityLevel() { return activityLevel; }
    public String getGoal() { return goal; }
    public Role getRole() { return role; }
    public void setId(Long id) { this.id = id; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setHeight(String height) { this.height = height; }
    public void setWeight(int weight) { this.weight = weight; }
    public void setAge(int age) { this.age = age; }
    public void setGoal(String goal) { this.goal = goal; }
    public void setRole(Role role) { this.role = role; }
    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }
    public int getStartingWeight() { return startingWeight; }
    public void setStartingWeight(int startingWeight) { this.startingWeight = startingWeight; }
    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
    public List<Entry> getEntries() {
        return entries;
    }
}