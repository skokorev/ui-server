package ru.scrumtrek.uiserver.time;

import java.time.LocalDateTime;

public class LocalTimeGetter implements TimeGetter {
    @Override
    public String getTime(TimeType timeType) throws TimeGetterException {
        return LocalDateTime.now().toString();
    }
}
