package ru.scrumtrek.uiserver.time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SafeTimeGetterImpl implements SafeTimeGetter {
    @Autowired private TimeGetter timeGetter;
    private NoArgsSafeTimeGetter localTimeGetter = new LocalTimeGetter();

    @Override
    public String getTime(TimeType timeType) {
        try {
            return timeGetter.getTime(timeType);
        } catch (TimeGetterException e) {
            return localTimeGetter.getTime();
        }
    }
}
