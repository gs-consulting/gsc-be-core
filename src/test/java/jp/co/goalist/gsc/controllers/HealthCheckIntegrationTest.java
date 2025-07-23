package jp.co.goalist.gsc.controllers;

import jp.co.goalist.gsc.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HealthCheckIntegrationTest extends BaseIntegrationTest {

    @Test
    void healthcheck_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/healthcheck")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("gsc-be-core is running"));
    }
}
