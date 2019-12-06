package ru.scrumtrek.uiserver.time;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TimeTypeTest {
    @Test
    public void shouldGetEstTime() {
        TimeType sut = TimeType.ofRepresentation("est");
        assertThat(sut).isEqualTo(TimeType.EST);
    }

    @Test
    public void shouldGetUtcTime() {
        TimeType sut = TimeType.ofRepresentation("utc");
        assertThat(sut).isEqualTo(TimeType.UTC);
    }

    @Test
    public void shouldThrowExceptionOnUnknownType() {
        assertThatThrownBy(() -> {
            TimeType.ofRepresentation("unknown");
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
