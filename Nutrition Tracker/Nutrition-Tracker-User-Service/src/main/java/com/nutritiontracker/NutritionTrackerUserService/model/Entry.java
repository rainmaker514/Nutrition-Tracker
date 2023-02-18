package com.nutritiontracker.NutritionTrackerUserService.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

//model for user created progress entries
@Entity
@Table(name="entries")
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="entry_id", nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;
    private Date date;
    private int weight;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User users;

    public Entry(){}

    public Entry(Long id, Date date, int weight){
        this.id = id;
        this.date = date;
        this.weight = weight;
    }

    @Override
    public String toString(){
        return "ProgressEntry{ " + this.id + " date = " + this.date + ", weight = " + this.weight + "}";
    }

    public Long getId() { return id; }
    public Date getDate() { return date; }
    public int getWeight() {
        return weight;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
}
