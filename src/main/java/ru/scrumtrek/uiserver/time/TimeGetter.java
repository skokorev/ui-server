package ru.scrumtrek.uiserver.time;

public interface TimeGetter {
    String getTime(TimeType timeType) throws TimeGetterException;
}
