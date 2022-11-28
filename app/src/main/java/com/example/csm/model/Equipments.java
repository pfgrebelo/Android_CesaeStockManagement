package com.example.csm.model;

public class Equipments {

    String name, brand, description, photo, roomId, assign, category;

    public Equipments() {
    }

    public Equipments(String name, String brand, String description, String photo, String roomId, String assign, String category) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.photo = photo;
        this.roomId = roomId;
        this.assign = assign;
        this.category = category;

    }

    public Equipments(String name, String brand, String description, String photo) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getAssign() {
        return assign;
    }

    public void setAssign(String assign) {
        this.assign = assign;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
