package ru.scrumtrek.uiserver.time;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import ru.scrumtrek.uiserver.webclient.WebJsonGetter;

import java.io.IOException;

@Component
public class WorldTimeGetter implements TimeGetter {
    @Autowired private WebJsonGetter jsonGetter;

    public String getTime(TimeType timeType) throws TimeGetterException {
        try {
            JsonNode timeContents = jsonGetter.getJsonContents(timeType.getUrl());
            return timeContents.get("currentDateTime").textValue();
        } catch (IOException | HttpClientErrorException e1) {
            throw new TimeGetterException(e1);
        }
    }
}
