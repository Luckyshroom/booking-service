package ru.doublegis.model;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class Hall implements Serializable {

    private int num;
    private UUID uuid;
    private Map<Integer, Seat> seatMap;

    public Hall(int num, Map<Integer, Seat> seatMap) {
        this.num = num;
        this.uuid = UUID.randomUUID();
        this.seatMap = seatMap;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Hall) {
            Hall hall = (Hall) obj;
            return (this.uuid == hall.uuid);
        } else return false;
    }

    @Override
    public int hashCode() {
        return num * 31 + uuid.hashCode();
    }

    public int getNum() {
        return num;
    }
    public UUID getUuid() {
        return uuid;
    }
    public Map<Integer, Seat> getSeatMap() {
        return seatMap;
    }

    public void setNum(int num) {
        this.num = num;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    public void setSeatMap(Map<Integer, Seat> seatMap) {
        this.seatMap = seatMap;
    }
}
