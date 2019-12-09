package ru.scrumtrek.uiserver.time;

import java.time.LocalDateTime;

public class LocalTimeGetter implements SafeTimeGetter {
    @Override
    public String getTime(TimeType timeType) {
        return LocalDateTime.now().toString();
    }
}
