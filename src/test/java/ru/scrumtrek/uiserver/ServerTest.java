package ru.scrumtrek.uiserver;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.scrumtrek.uiserver.domain.Line;
import ru.scrumtrek.uiserver.time.SafeTimeGetter;
import ru.scrumtrek.uiserver.time.SafeTimeGetterImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestPropertySource(properties = {"timetype=utc"})
public class ServerTest {
    @TestConfiguration
    @ComponentScan(basePackages = {"ru.scrumtrek.uiserver"})
    static class LocalTestConfiguration {
        @Bean
        public SafeTimeGetter safeTimeGetter() {
            return Mockito.mock(SafeTimeGetterImpl.class);
        }
    }

    @Autowired private SafeTimeGetter safeTimeGetter;
    @Autowired private WebApplicationContext context;
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        when(safeTimeGetter.getTime(any())).thenReturn("2019-12-06T08:12Z");
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.ANY);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @After
    public void tearDown() throws Exception {
        assertDeleteLines();
    }

    @Test
    public void shouldCheckEmptyList() throws Exception {
        assertLines(new String[] {});
    }

    @Test
    public void shouldCheckAdditionOfLine() throws Exception {
        assertLines(new String[] {});
        assertAddLine("1", 200);
        assertLines("1");
        assertAddLine("2", 200);
        assertLines("1", "2");
        assertAddLine("3", 200);
        assertLines("1", "2", "3");
    }

    @Test
    public void shouldNotAddSameLineTwice() throws Exception {
        assertLines(new String[] {});
        assertAddLine("1", 200);
        assertLines("1");
        assertAddLine("1", 400);
        assertLines("1");
    }

    @Test
    public void shouldDeleteAllLines() throws Exception {
        assertLines(new String[] {});
        assertAddLine("1", 200);
        assertAddLine("2", 200);
        assertAddLine("3", 200);
        assertLines("1", "2", "3");
        assertDeleteLines();
        assertLines(new String[] {});
    }

    private void assertLines(String... expectedLines) throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/lines").
                accept(new MediaType[]{MediaType.APPLICATION_JSON_UTF8})).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        List<Line> lines = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Line>>() {});
        assertThat(lines.stream().map(Line::getLineChars)).containsOnly(expectedLines);
    }

    private void assertAddLine(String line, int expectedStatus) throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/lines").content(line)).
                andExpect(MockMvcResultMatchers.status().is(expectedStatus)).andReturn();
    }

    private void assertDeleteLines() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/lines")).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }
}
