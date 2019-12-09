package ru.scrumtrek.uiserver.webclient;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.HttpClientErrorException;

import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class UrlConnectionWebJsonGetterTest {
    private UrlConnectionWebJsonGetter sut = spy(new UrlConnectionWebJsonGetter());
    private HttpURLConnection connectionMock = mock(HttpURLConnection.class);

    @Before
    public void setUp() throws Exception {
        doReturn(connectionMock).when(sut).createConnection(anyString());
        doNothing().when(connectionMock).setRequestMethod(anyString());
    }

    @Test
    public void shouldReturnJson() throws Exception {
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getInputStream()).thenReturn(
                IOUtils.toInputStream("{ \"object\": \"value\" }", StandardCharsets.UTF_8));
        JsonNode node = sut.getJsonContents("http://someurl.com/service");
        assertThat(node).isNotNull();
        assertThat(node.fieldNames()).containsOnly("object");
        assertThat(node.get("object").textValue()).isEqualTo("value");
    }

    @Test
    public void shouldThrowException() throws Exception {
        when(connectionMock.getResponseCode()).thenReturn(500);
        assertThatThrownBy(() -> sut.getJsonContents("http://someurl.com/service")).
                isInstanceOf(HttpClientErrorException.class);
    }
}
