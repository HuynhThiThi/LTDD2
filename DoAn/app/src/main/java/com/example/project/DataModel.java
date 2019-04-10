package com.example.project;


public class DataModel {


    String name;
    String rank;
    int id_;


    public DataModel(String name, String rank, int id_ ) {
        this.name = name;
        this.rank = rank;
        this.id_ = id_;

    }


    public String getName() {
        return name;
    }


    public String getRank() {
        return rank;
    }


    public int getId() {
        return id_;
    }
}