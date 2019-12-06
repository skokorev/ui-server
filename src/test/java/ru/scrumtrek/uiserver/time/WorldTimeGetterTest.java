package ru.scrumtrek.uiserver.time;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.scrumtrek.uiserver.webclient.WebJsonGetter;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class WorldTimeGetterTest {
    private static final String EST_RESPONSE = "{\"$id\":\"1\",\"currentDateTime\":\"2019-12-06T03:11-05:00\"," +
            "\"utcOffset\":\"-05:00:00\",\"isDayLightSavingsTime\":false,\"dayOfTheWeek\":\"Friday\"," +
            "\"timeZoneName\":\"Eastern Standard Time\",\"currentFileTime\":132200754729065887," +
            "\"ordinalDate\":\"2019-340\",\"serviceResponse\":null}";
    private static final String UTC_RESPONSE = "{\"$id\":\"1\",\"currentDateTime\":\"2019-12-06T08:12Z\"," +
            "\"utcOffset\":\"00:00:00\",\"isDayLightSavingsTime\":false,\"dayOfTheWeek\":\"Friday\"," +
            "\"timeZoneName\":\"UTC\",\"currentFileTime\":132200935376492051,\"ordinalDate\":\"2019-340\"," +
            "\"serviceResponse\":null}";

    @TestConfiguration
    static class LocalTestConfiguration {
        @Bean
        public TimeGetter timeGetter() {
            return new WorldTimeGetter();
        }
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired private TimeGetter timeGetter;
    @MockBean private WebJsonGetter jsonGetter;

    @Test
    public void checkEst() throws Exception {
        when(jsonGetter.getJsonContents(TimeType.EST.getUrl())).thenReturn(getJsonNode(EST_RESPONSE));
        String time = timeGetter.getTime(TimeType.EST);
        assertThat(time).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}-\\d{2}:\\d{2}");
    }

    @Test
    public void checkUtc() throws Exception {
        when(jsonGetter.getJsonContents(TimeType.UTC.getUrl())).thenReturn(getJsonNode(UTC_RESPONSE));
        String time = timeGetter.getTime(TimeType.UTC);
        assertThat(time).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}Z");
    }

    @Test
    public void shouldThrowExceptionOnRemoteException() throws Exception {
        when(jsonGetter.getJsonContents(TimeType.UTC.getUrl())).thenThrow(new IOException());
        Assertions.assertThatThrownBy(() -> timeGetter.getTime(TimeType.UTC)).isInstanceOf(TimeGetterException.class);
    }

    private JsonNode getJsonNode(String responce) throws Exception {
        return mapper.readTree(responce);
    }
}
