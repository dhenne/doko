package de.vinode.dokobackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
class DokoBackendApplicationTests {

    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:15.3-alpine");

    @DynamicPropertySource
    static void setPostgreSQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {


        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

    }

    @Test
    void createsGameWith4Players() throws Exception {

        var player1 = "player1";
        var player2 = "player2";
        var player3 = "player3";
        var player4 = "player4";

        var response = mockMvc
                .perform(
                        post("/api/v1/game")
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                .param("playerNames[0]", player1)
                                .param("playerNames[1]", player2)
                                .param("playerNames[2]", player3)
                                .param("playerNames[3]", player4)

                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/api/v1/game/")))
                .andExpect(jsonPath("$.shareId").isString())
                .andReturn();

        String location = Objects.requireNonNull(response.getResponse().getHeaderValue("Location")).toString();

        mockMvc.perform(get(location)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.players").isArray())
                .andExpect(jsonPath("$.players[0].name", is(player1)))
                .andExpect(jsonPath("$.players[1].name", is(player2)))
                .andExpect(jsonPath("$.players[2].name", is(player3)))
                .andExpect(jsonPath("$.players[3].name", is(player4)))

                .andReturn();
    }

}
