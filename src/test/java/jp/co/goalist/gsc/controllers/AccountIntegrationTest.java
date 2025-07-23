package jp.co.goalist.gsc.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jp.co.goalist.gsc.BaseIntegrationTest;

class AccountIntegrationTest extends BaseIntegrationTest {

    @Test
    void createNewOperatorAccount_ShouldReturnSuccess() throws Exception {
        mockMvc.perform(get("/public/new-operator-account")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("New operator admin account created successfully"));
    }
}
