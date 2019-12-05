package ru.scrumtrek.uiserver.time;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WorldTimeGetterTest {
    private WorldTimeGetter sut = new WorldTimeGetter();

    @Test
    public void checkEst() throws Exception {
        String time = sut.getTime("est");
        assertThat(time).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}-\\d{2}:\\d{2}");
    }

    @Test
    public void checkUtc() throws Exception {
        String time = sut.getTime("utc");
        assertThat(time).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}Z");
    }

    @Test
    public void checkUnknownType() throws Exception {
        String time = sut.getTime("unknown");
        assertThat(time).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(:\\d{2}(.\\d{3,9})?)?");
    }
}
