package ru.scrumtrek.uiserver.time;

import java.time.LocalDateTime;

public class LocalTimeGetter implements NoArgsSafeTimeGetter {
    @Override
    public String getTime() {
        return LocalDateTime.now().toString();
    }
}
