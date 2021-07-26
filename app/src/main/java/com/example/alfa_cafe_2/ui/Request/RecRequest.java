package com.example.alfa_cafe_2.ui.Request;

import com.google.gson.annotations.SerializedName;

public class RecRequest {
    @SerializedName("Супы")
    public String[] soups;
    @SerializedName("Выпечка")
    public String[] pechs;
    @SerializedName("Десерты")
    public String[] deserts;
    @SerializedName("Напитки")
    public String[] drinks;
    @SerializedName("Салаты")
    public String[] salads;
    @SerializedName("Горячее")
    public String[] seconds;
    @SerializedName("Закуски")
    public String[] dops;
    @SerializedName("Гарниры")
    public String[] garnirs;
    @SerializedName("Соусы")
    public String[] souses;

    public String[] getSoups() {
        return soups;
    }

    public void setSoups(String[] soups) {
        this.soups = soups;
    }

    public String[] getPechs() {
        return pechs;
    }

    public void setPechs(String[] pechs) {
        this.pechs = pechs;
    }

    public String[] getDeserts() {
        return deserts;
    }

    public void setDeserts(String[] deserts) {
        this.deserts = deserts;
    }

    public String[] getDrinks() {
        return drinks;
    }

    public void setDrinks(String[] drinks) {
        this.drinks = drinks;
    }

    public String[] getSalads() {
        return salads;
    }

    public void setSalads(String[] salads) {
        this.salads = salads;
    }

    public String[] getSeconds() {
        return seconds;
    }

    public void setSeconds(String[] seconds) {
        this.seconds = seconds;
    }

    public String[] getDops() {
        return dops;
    }

    public void setDops(String[] dops) {
        this.dops = dops;
    }

    public String[] getGarnirs() {
        return garnirs;
    }

    public void setGarnirs(String[] garnirs) {
        this.garnirs = garnirs;
    }

    public String[] getSouses() {
        return souses;
    }

    public void setSouses(String[] souses) {
        this.souses = souses;
    }
}
