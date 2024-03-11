package org.example.controller.corporatesettlement;

import org.example.api.corporatesettlement.CreateAccountResponseDto;
import org.example.exception.EntityAlreadyExistsException;
import org.example.exception.EntityNotFoundException;
import org.example.service.corporatesettlement.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    private static final String URL = "/app/v1/corporate-settlement-account/create";

    private static final String CONTENT_SUCCESS = """
            {
              "instanceId": 0,
              "registryTypeCode": "string",
              "accountType": "string",
              "currencyCode": "str",
              "branchCode": "string",
              "priorityCode": "string",
              "mdmCode": "147721348976873816026065937752681176991293841218613988",
              "clientCode": "string",
              "trainRegion": "string",
              "counter": "string",
              "salesCode": "string"
            }""";

    private static final String CONTENT_MISSING_REQUIRED_PARAMS = """
            {
              "registryTypeCode": "string",
              "accountType": "string",
              "currencyCode": "str",
              "branchCode": "string",
              "priorityCode": "string",
              "mdmCode": "147721348976873816026065937752681176991293841218613988",
              "clientCode": "string",
              "trainRegion": "string",
              "counter": "string",
              "salesCode": "string"
            }""";

    private static final String CREATED_ACCOUNT_ID = "99";

    private static final String RESPONSE_BODY_SUCCESS = "{\"data\":{\"accountId\":\"99\"}}";
    private static final String RESPONSE_BODY_MISSING_REQUIRED_PARAMS = "instanceId(Идентификатор экземпляра продукта): must not be null";

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        Mockito.reset(accountService);
    }

    @Test
    public void createSuccessTest() throws Exception {
        Mockito.when(accountService.createAccount(any()))
                .thenReturn(CreateAccountResponseDto.builder()
                        .data(CreateAccountResponseDto.AccountData.builder().accountId(CREATED_ACCOUNT_ID).build())
                        .build());

        var result = mvc.perform(post(URL)
                        .content(CONTENT_SUCCESS)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        var responseBody = result.getResponse().getContentAsString();
        assertEquals(RESPONSE_BODY_SUCCESS, responseBody);
    }

    @Test
    public void createMissingRequiredParamsError() throws Exception {
        Mockito.when(accountService.createAccount(any()))
                .thenReturn(CreateAccountResponseDto.builder()
                        .data(CreateAccountResponseDto.AccountData.builder().accountId(CREATED_ACCOUNT_ID).build())
                        .build());

        var result = mvc.perform(post(URL)
                        .content(CONTENT_MISSING_REQUIRED_PARAMS)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        var responseBody = result.getResponse().getContentAsString();
        assertEquals(RESPONSE_BODY_MISSING_REQUIRED_PARAMS, responseBody);
    }

    @Test
    public void createThrowsEntityNotFoundExceptionError() throws Exception {
        Mockito.when(accountService.createAccount(any()))
                .thenThrow(EntityNotFoundException.class);

        mvc.perform(post(URL)
                        .content(CONTENT_SUCCESS)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createThrowsEntityAlreadyExistsExceptionError() throws Exception {
        Mockito.when(accountService.createAccount(any()))
                .thenThrow(EntityAlreadyExistsException.class);

        mvc.perform(post(URL)
                        .content(CONTENT_SUCCESS)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createThrowsOtherExceptionError() throws Exception {
        Mockito.when(accountService.createAccount(any()))
                .thenThrow(NullPointerException.class);

        mvc.perform(post(URL)
                        .content(CONTENT_SUCCESS)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
    }

}
