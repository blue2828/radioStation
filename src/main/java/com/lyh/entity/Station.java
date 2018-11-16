package com.lyh.entity;

import java.util.Date;

public class Station implements java.io.Serializable {
    private int id, stationState, lastTimeMemberId;
    private String name;
    private Date lastTimeBroadcast;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStationState() {
        return stationState;
    }

    public void setStationState(int stationState) {
        this.stationState = stationState;
    }

    public int getLastTimeMemberId() {
        return lastTimeMemberId;
    }

    public void setLastTimeMemberId(int lastTimeMemberId) {
        this.lastTimeMemberId = lastTimeMemberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastTimeBroadcast() {
        return lastTimeBroadcast;
    }

    public void setLastTimeBroadcast(Date lastTimeBroadcast) {
        this.lastTimeBroadcast = lastTimeBroadcast;
    }

    public Station() {
    }
}
