package ru.scrumtrek.uiserver.webclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UrlConnectionWebJsonGetter implements WebJsonGetter {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public JsonNode getJsonContents(String url) throws IOException {
        HttpURLConnection conn = createConnection(url);
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();
        if (200 != responseCode) throw HttpClientErrorException.create(HttpStatus.BAD_REQUEST,
                "",
                HttpHeaders.EMPTY,
                new byte[0],
                StandardCharsets.UTF_8);
        byte[] contents = IOUtils.toByteArray(conn.getInputStream());
        return mapper.readTree(contents);
    }

    protected HttpURLConnection createConnection(String url) throws IOException {
        return (HttpURLConnection) new URL(url).openConnection();
    }
}
