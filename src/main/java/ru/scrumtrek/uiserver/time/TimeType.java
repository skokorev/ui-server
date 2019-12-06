package ru.scrumtrek.uiserver.time;

import lombok.Getter;

public enum TimeType {
    EST("est", "http://worldclockapi.com/api/json/est/now"),
    UTC("utc", "http://worldclockapi.com/api/json/utc/now");

    private String representation;
    @Getter private String url;
    private TimeType(String representation, String url) {
        this.representation = representation;
        this.url = url;
    }

    public static TimeType ofRepresentation(String name) {
        for(TimeType type : TimeType.values())
            if (type.representation.equals(name)) return type;
        throw new IllegalArgumentException("Can't find constant with " + name + " representation");
    }
}
