package ru.scrumtrek.uiserver.time;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class WorldTimeGetter {
    private ObjectMapper mapper = new ObjectMapper();

    public String getTime(String timeType) throws TimeGetterException {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(timeUrl(timeType)).openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (200 != responseCode) throw new TimeGetterException(
                    HttpClientErrorException.create(HttpStatus.BAD_REQUEST,
                            "",
                            HttpHeaders.EMPTY,
                            new byte[0],
                            StandardCharsets.UTF_8));
            byte[] contents = IOUtils.toByteArray(conn.getInputStream());
            JsonNode timeContents = mapper.readTree(contents);
            return timeContents.get("currentDateTime").textValue();
        } catch (MalformedURLException e) {
            return LocalDateTime.now().toString();
        } catch (IOException e1) {
            throw new TimeGetterException(e1);
        }
    }

    private String timeUrl(String timeType) {
        switch (timeType) {
            case "est": return "http://worldclockapi.com/api/json/est/now";
            case "utc": return "http://worldclockapi.com/api/json/utc/now";
            default: return null;
        }
    }
}
