package jp.co.goalist.gsc;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.goalist.gsc.entities.Account;
import jp.co.goalist.gsc.mocks.MockAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockAuthenticationService mockAuthService;

    protected ResultActions performAuthenticatedRequest(MockHttpServletRequestBuilder requestBuilder, Account account) throws Exception {
        mockAuthService.mockAuthenticatedAccount(account.getUsername(), account.getRole());
        try {
            return mockMvc.perform(requestBuilder
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("authorization", "token"));
        } finally {
            mockAuthService.clearAuthentication();
        }
    }
}
