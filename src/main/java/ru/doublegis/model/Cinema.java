package ru.doublegis.model;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class Cinema implements Serializable {

    private int num;
    private UUID uuid;
    private Map<Integer, Hall> hallMap;

    public Cinema(int num, Map<Integer, Hall> hallMap) {
        this.num = num;
        this.uuid = UUID.randomUUID();
        this.hallMap = hallMap;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cinema) {
            Cinema cinema = (Cinema) obj;
            return (this.uuid == cinema.uuid);
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
    public Map<Integer, Hall> getHallMap() {
        return hallMap;
    }

    public void setNum(int num) {
        this.num = num;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    public void setHallMap(Map<Integer, Hall> hallMap) {
        this.hallMap = hallMap;
    }
}
