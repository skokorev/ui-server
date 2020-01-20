package ru.scrumtrek.uiserver.pact;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.scrumtrek.uiserver.Server;
import ru.scrumtrek.uiserver.domain.Line;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@Provider("LinesProvider")
@PactFolder("target/pacts")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8082")
@TestPropertySource(properties = {"timetype=utc"})
public class ContractTest {

    @MockBean
    private Server server;

    @BeforeEach
    void setupTestTarget(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", 8082, "/"));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("any state")
    public void checkGetState() {
        when(server.getLines()).thenReturn(Arrays.asList(
                new Line(1, "123", "2020-01-20T05:12Z"),
                new Line(2, "321", "2020-01-20T05:12Z")
        ));
    }
}
