package org.example.controller.corporatesettlement;

import org.example.api.corporatesettlement.CreateInstanceResponseDto;
import org.example.exception.EntityAlreadyExistsException;
import org.example.exception.EntityNotFoundException;
import org.example.service.corporatesettlement.InstanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InstanceController.class)
public class InstanceControllerTest {
    private static final String URL = "/app/v1/corporate-settlement-instance/create";
    private static final String CONTENT_SUCCESS = """
            {
              "instanceId": 0,
              "productType": "string",
              "productCode": "string",
              "registerType": "string",
              "mdmCode": "string",
              "contractNumber": "string",
              "contractDate": "2024-03-11",
              "priority": 0,
              "interestRatePenalty": 0,
              "minimalBalance": 0,
              "thresholdAmount": 0,
              "accountingDetails": "string",
              "rateType": "DIFFERENTIATED",
              "taxPercentageRate": 0,
              "technicalOverdraftLimitAmount": 0,
              "contractId": 0,
              "branchCode": "string",
              "isoCurrencyCode": "str",
              "urgencyCode": "string",
              "referenceCode": 0,
              "additionalProperties": [
                {
                  "key": "string",
                  "value": "string"
                }
              ],
              "instanceArrangements": [
                {
                  "generalAgreementId": "string",
                  "supplementaryAgreementId": "string",
                  "arrangementType": "string",
                  "shedulerJobId": 0,
                  "number": "string",
                  "openingDate": "2024-03-11",
                  "closingDate": "2024-03-11",
                  "cancelDate": "2024-03-11",
                  "validityDuration": 0,
                  "cancellationReason": "string",
                  "status": "string",
                  "interestCalculationDate": "2024-03-11",
                  "interestRate": 0,
                  "coefficient": 0,
                  "coefficientAction": "PLUS",
                  "minimumInterestRate": 0,
                  "minimumInterestRateCoefficient": 0,
                  "minimumInterestRateCoefficientAction": "PLUS",
                  "maximalInterestRate": 0,
                  "maximalInterestRateCoefficient": 0,
                  "maximalInterestRateCoefficientAction": "PLUS"
                }
              ]
            }""";

    private static final String CONTENT_MISSING_REQUIRED_PARAMS = """
            {
              "instanceId": 0,
              "productCode": "string",
              "registerType": "string",
              "mdmCode": "string",
              "contractNumber": "string",
              "contractDate": "2024-03-11",
              "priority": 0,
              "interestRatePenalty": 0,
              "minimalBalance": 0,
              "thresholdAmount": 0,
              "accountingDetails": "string",
              "rateType": "DIFFERENTIATED",
              "taxPercentageRate": 0,
              "technicalOverdraftLimitAmount": 0,
              "contractId": 0,
              "branchCode": "string",
              "isoCurrencyCode": "str",
              "urgencyCode": "string",
              "referenceCode": 0
            }""";
    private static final String RESPONSE_BODY_SUCCESS
            = "{\"data\":{\"instanceId\":\"99\",\"registerId\":[4,5],\"supplementaryAgreementId\":[1,2,3]}}";
    private static final String RESPONSE_BODY_MISSING_REQUIRED_PARAMS
            = "productType(Тип Экземпляра Продукта): must not be empty";
    private static final String PROCESSED_PRODUCT_ID = "99";

    @MockBean
    private InstanceService instanceService;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        Mockito.reset(instanceService);
    }

    @Test
    public void createSuccessTest() throws Exception {
        Mockito.when(instanceService.createInstance(any()))
                .thenReturn(CreateInstanceResponseDto
                        .builder()
                        .data(CreateInstanceResponseDto.InstanceData
                                .builder()
                                .instanceId(PROCESSED_PRODUCT_ID)
                                .supplementaryAgreementId(List.of(1L, 2L, 3L))
                                .registerId(List.of(4L, 5L))
                                .build())
                        .build());

        var result = mvc.perform(post(URL)
                        .content(CONTENT_SUCCESS)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        var responseBody = result.getResponse().getContentAsString();
        assertEquals(RESPONSE_BODY_SUCCESS, responseBody);
    }

    @Test
    public void createMissingRequiredParamsError() throws Exception {
        Mockito.when(instanceService.createInstance(any()))
                .thenReturn(CreateInstanceResponseDto
                        .builder()
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
        Mockito.when(instanceService.createInstance(any()))
                .thenThrow(EntityNotFoundException.class);

        mvc.perform(post(URL)
                        .content(CONTENT_SUCCESS)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createThrowsEntityAlreadyExistsExceptionError() throws Exception {
        Mockito.when(instanceService.createInstance(any()))
                .thenThrow(EntityAlreadyExistsException.class);

        mvc.perform(post(URL)
                        .content(CONTENT_SUCCESS)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createThrowsOtherExceptionError() throws Exception {
        Mockito.when(instanceService.createInstance(any()))
                .thenThrow(NullPointerException.class);

        mvc.perform(post(URL)
                        .content(CONTENT_SUCCESS)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
    }
}
