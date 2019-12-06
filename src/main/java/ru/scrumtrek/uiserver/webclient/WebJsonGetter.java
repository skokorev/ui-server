package ru.scrumtrek.uiserver.webclient;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public interface WebJsonGetter {
    JsonNode getJsonContents(String url) throws IOException;
}
