package com.example.alfa_cafe_2.ui.Request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StringRequest {
    @SerializedName("image")
    public String bytes;
    @SerializedName("item")
    public String[] items;
    @SerializedName("group")
    public String[] types;
    @SerializedName("price")
    public int[] prices;
    @SerializedName("top")
    public String top;
    @SerializedName("rec")
    public RecRequest rec;

    public String getByte() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }

    public String[] getStrings() {
        return items;
    }

    public void setStrings(String[] items) {
        this.items = items;
    }

    public String getBytes() {
        return bytes;
    }

    public int[] getPrices() {
        return prices;
    }

    public void setPrices(int[] prices) {
        this.prices = prices;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public RecRequest getRec() {
        return rec;
    }

    public void setRec(RecRequest rec) {
        this.rec = rec;
    }
}
