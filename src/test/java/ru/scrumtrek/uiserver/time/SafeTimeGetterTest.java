package ru.scrumtrek.uiserver.time;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class SafeTimeGetterTest {
    @TestConfiguration
    static class LocalTestConfiguration {
        @Bean
        public SafeTimeGetter safeTimeGetter() {
            return new SafeTimeGetterImpl();
        }
    }

    @MockBean private TimeGetter timeGetter;
    @Autowired private SafeTimeGetter safeTimeGetter;

    @Test
    public void shouldCheckWorldTime() throws Exception {
        when(timeGetter.getTime(any())).thenReturn("2019-12-06T08:12Z");
        assertThat(safeTimeGetter.getTime(TimeType.UTC)).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}Z");
    }

    @Test
    public void shouldCheckLocalTime() throws Exception {
        when(timeGetter.getTime(any())).thenThrow(new TimeGetterException(new IOException()));
        assertThat(safeTimeGetter.getTime(TimeType.UTC)).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(:\\d{2}(\\.\\d{3,9})?)?");
    }
}
