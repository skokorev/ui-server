package ru.scrumtrek.uiserver.time;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalTimeGetterTest {
    private NoArgsSafeTimeGetter sut = new LocalTimeGetter();

    @Test
    public void shouldTestLocalTime() {
        String time = sut.getTime();
        assertThat(time).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(:\\d{2}(\\.\\d{3,9})?)?");
    }
}
