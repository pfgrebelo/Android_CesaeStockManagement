package com.example.csm.model;

public class Rooms {
    String roomName, designation;

    public Rooms() {
    }

    public Rooms(String roomName, String designation) {
        this.roomName = roomName;
        this.designation = designation;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
