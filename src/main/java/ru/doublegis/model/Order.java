package ru.doublegis.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
public class Order {

    private int hallNum;
    private JsonArray seatNums;
    private String holderName;
    private String holderEmail;

    public Order() {
    }

    public Order(JsonObject obj) {
        OrderConverter.fromJson(obj, this);
    }

    public Order(int hallNum, JsonArray seatNums, String holderName, String holderEmail) {
        this.hallNum = hallNum;
        this.seatNums = seatNums;
        this.holderName = holderName;
        this.holderEmail = holderEmail;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        OrderConverter.toJson(this, json);
        return json;
    }

    public int getHallNum() {
        return hallNum;
    }
    public JsonArray getSeatNums() {
        return seatNums;
    }
    public String getHolderName() {
        return holderName;
    }
    public String getHolderEmail() {
        return holderEmail;
    }

    public void setHallNum(int hallNum) {
        this.hallNum = hallNum;
    }
    public void setSeatNums(JsonArray seatNums) {
        this.seatNums = seatNums;
    }
    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }
    public void setHolderEmail(String holderEmail) {
        this.holderEmail = holderEmail;
    }
}
