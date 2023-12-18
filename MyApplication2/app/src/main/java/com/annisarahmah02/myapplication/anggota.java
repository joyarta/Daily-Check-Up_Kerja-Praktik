package com.annisarahmah02.myapplication;

public class anggota {

    private int id ;
    private String nobadge;
    private String name;
    private String address;

    public anggota(int id, String name,String nobadge, String address){
        this.id= id;
        this.nobadge=nobadge;
        this.name=name;
        this.address=address;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNobadge() {
        return nobadge;
    }

    public void setNobadge(String nobadge) {
        this.nobadge = nobadge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
