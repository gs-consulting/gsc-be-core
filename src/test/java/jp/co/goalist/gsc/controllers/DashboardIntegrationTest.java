package jp.co.goalist.gsc.controllers;

import jp.co.goalist.gsc.BaseIntegrationTest;
import jp.co.goalist.gsc.entities.Account;
import jp.co.goalist.gsc.enums.Role;
import jp.co.goalist.gsc.enums.SubRole;
import jp.co.goalist.gsc.mocks.MockAuthenticationService;
import jp.co.goalist.gsc.mocks.MockTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DashboardIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockAuthenticationService mockAuthService;

    @Autowired
    private MockTestData mockTestData;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        // Mock data
        mockTestData.createMockDashboardData();
        testAccount = mockTestData.createMockAccount(
                "test@test.com",
                "Test User",
                Role.CLIENT,
                SubRole.OEM
        );
    }

    @Test
    void getApplicationStatistics_ShouldReturnOk() throws Exception {
        try {
            performAuthenticatedRequest(get("/dashboard/applications"), testAccount)
                    .andExpect(status().isOk());
        } finally {
            mockAuthService.clearAuthentication();
        }
    }

    @Test
    void getProjects_ShouldReturnOk() throws Exception {
        try {
            performAuthenticatedRequest(get("/dashboard/projects"), testAccount)
                    .andExpect(status().isOk());
        } finally {
            mockAuthService.clearAuthentication();
        }
    }
}
