package com.rumenz;

public class SuperRumenz extends Rumenz {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SuperRumenz{" +
                "type='" + type + '\'' +
                "} " + super.toString();
    }
}
