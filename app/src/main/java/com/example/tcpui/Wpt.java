package com.example.tcpui;

import java.io.Serializable;

public class Wpt implements Serializable {
    private Location location;
    private Double elevation;
    private Double time;

    public Wpt(Location location, Double elevation, Double time){
        this.location = location;
        this.elevation = elevation;
        this.time = time;
    }

    public Location getLocation(){
        return this.location;
    }

    public Double getElevation() {
        return this.elevation;
    }

    public Double getTime() {
        return this.time;
    }

    @Override
    public String toString(){
        return "Location{"+location.toString()
                + "}, Elevation{" +elevation
                +"}, Time{" + time +"}";
    }
}


