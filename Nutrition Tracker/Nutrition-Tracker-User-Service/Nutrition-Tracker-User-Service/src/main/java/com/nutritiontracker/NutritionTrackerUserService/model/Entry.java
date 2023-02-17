package com.nutritiontracker.NutritionTrackerUserService.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//model for user created progress entries
@Entity
@Table(name="entries")
public class Entry implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="entry_id", nullable = false, updatable = false)
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;
    private String date;
    private int weight;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User users;

    public Entry(){}

    public Entry(String date, int weight){
        //this.id = id;
        this.date = date;
        this.weight = weight;
    }

    @Override
    public String toString(){
        return "ProgressEntry{ " + this.id + " date = " + this.date + ", weight = " + this.weight + "}";
    }

    public Long getId() { return id; }
    public String getDate() { return date; }
    public int getWeight() {
        return weight;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public User getUsers() {
        return users;
    }
    public void setUsers(User users) {
        this.users = users;
    }
}
