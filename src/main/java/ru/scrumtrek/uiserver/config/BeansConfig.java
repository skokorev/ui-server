package ru.scrumtrek.uiserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.scrumtrek.uiserver.time.TimeGetter;
import ru.scrumtrek.uiserver.time.WorldTimeGetter;
import ru.scrumtrek.uiserver.webclient.UrlConnectionWebJsonGetter;
import ru.scrumtrek.uiserver.webclient.WebJsonGetter;

@Configuration
public class BeansConfig {
    @Bean
    public WebJsonGetter jsonGetter() {
        return new UrlConnectionWebJsonGetter();
    }

    @Bean
    public TimeGetter timeGetter() {
        return new WorldTimeGetter();
    }
}
