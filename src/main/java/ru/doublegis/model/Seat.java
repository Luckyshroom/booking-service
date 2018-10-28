package ru.doublegis.model;

import java.io.Serializable;
import java.util.UUID;

public class Seat implements Serializable {

    private int num;
    private int row;
    private int col;
    private boolean available;
    private String holderName;
    private String holderEmail;
    private UUID uuid;

    public Seat(int num, int row, int col) {
        this.num = num;
        this.row = row;
        this.col = col;
        this.available = true;
        this.uuid = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Seat) {
            Seat seat = (Seat) obj;
            return (this.uuid == seat.uuid);
        } else return false;
    }

    @Override
    public int hashCode() {
        return num * 31 + uuid.hashCode();
    }

    public int getNum() {
        return num;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public boolean isAvailable() {
        return available;
    }
    public String getHolderName() {
        return holderName;
    }
    public String getHolderEmail() {
        return holderEmail;
    }
    public UUID getUuid() {
        return uuid;
    }

    public void setNum(int num) {
        this.num = num;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public void setCol(int col) {
        this.col = col;
    }
    public Seat setAvailable(boolean available) {
        this.available = available;
        return this;
    }
    public Seat setHolderName(String holderName) {
        this.holderName = holderName;
        return this;
    }
    public Seat setHolderEmail(String holderEmail) {
        this.holderEmail = holderEmail;
        return this;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
