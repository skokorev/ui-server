package ru.scrumtrek.uiserver.time;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeGetterExceptionTest {
    @Test
    public void shouldCreateExceptionWithParent() {
        TimeGetterException e = new TimeGetterException(new NullPointerException());
        assertThat(e).isNotNull();
        assertThat(e).isInstanceOf(TimeGetterException.class);
        assertThat(e.getCause()).isInstanceOf(NullPointerException.class);
    }
}
