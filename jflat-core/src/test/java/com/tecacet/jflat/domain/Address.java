package com.tecacet.jflat.domain;

public class Address {

    public enum State {
        CA, IL, NV, MA, WA
    }

    private State state;
    private String city;
    private int zip;
    private String numberAndStreet;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getNumberAndStreet() {
        return numberAndStreet;
    }

    public void setNumberAndStreet(String numberAndStreet) {
        this.numberAndStreet = numberAndStreet;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %d", numberAndStreet, city, state, zip);
    }
}
