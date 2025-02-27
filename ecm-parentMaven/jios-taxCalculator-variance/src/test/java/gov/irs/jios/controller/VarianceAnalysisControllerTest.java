package gov.irs.jios.controller;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import gov.irs.jios.common.client.tr.pojo.RetrieveFieldsResponseDTO;
import gov.irs.jios.common.client.tr.service.ErrorTransformationService;
import gov.irs.jios.common.client.tr.service.VarianceAnalysisService;
import gov.irs.jios.common.ecm.pojo.ErrorDetail;
import gov.irs.jios.common.ecm.pojo.ErrorResponse;
import gov.irs.jios.common.ecm.pojo.TransactionInfo;
import gov.irs.jios.common.exception.ValidationException;
import gov.irs.jios.common.validation.FormValidator;
import gov.irs.jios.common.validation.FormValidatorRegistry;
import gov.irs.jios.request.TaxCalculationRequest;
import gov.irs.jios.request.VarianceAnalysisRequest;

@ExtendWith(MockitoExtension.class)
class VarianceAnalysisControllerTest {

    @Mock
    private FormValidatorRegistry validatorRegistry;

    @Mock
    private ErrorTransformationService errorTransformationService;

    @Mock
    private VarianceAnalysisService varianceAnalysisService;

    @InjectMocks
    private VarianceAnalysisController controller;

    private VarianceAnalysisRequest varianceRequest;
    private TaxCalculationRequest taxRequest;
    private RetrieveFieldsResponseDTO mockResponse;
    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        // Initialize test data
        varianceRequest = new VarianceAnalysisRequest();
        Map<String, Object> header = new HashMap<>();
        header.put("transactionId", "test-123");
        varianceRequest.setHeader(header);

        taxRequest = new TaxCalculationRequest();
        taxRequest.setHeader(header);

        mockResponse = new RetrieveFieldsResponseDTO();
        mockResponse.setJsonResponse("{\"test\":\"response\"}");

        mockFile = new MockMultipartFile(
            "snapshot",
            "test.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "test content".getBytes()
        );
    }

    @Test
    void varianceAnalysis_WithValidRequest_ShouldReturnSuccess() {
        // Arrange
        when(validatorRegistry.getAllValidators()).thenReturn(Collections.emptyList());
        try {
			when(varianceAnalysisService.performVarianceAnalysisOrTaxCalc(any(), isNull()))
			    .thenReturn(mockResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}

        // Act
        ResponseEntity<?> response = controller.varianceAnalysis(varianceRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse.getJsonResponse(), response.getBody());
    }

    @Test
    void varianceAnalysisMultipart_WithValidRequestAndFile_ShouldReturnSuccess() {
        // Arrange
        varianceRequest.getHeader().put("locatorId", "test-locator");
        when(validatorRegistry.getAllValidators()).thenReturn(Collections.emptyList());
        try {
			when(varianceAnalysisService.performVarianceAnalysisOrTaxCalc(any(), any()))
			    .thenReturn(mockResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}

        // Act
        ResponseEntity<?> response = controller.varianceAnalysisMultipart(varianceRequest, mockFile);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse.getJsonResponse(), response.getBody());
    }

    @Test
    void varianceAnalysisMultipart_WithNullRequest_ShouldReturnBadRequest() {
        // Act
        ResponseEntity<?> response = controller.varianceAnalysisMultipart(null, mockFile);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid request", response.getBody());
    }

    @Test
    void varianceAnalysisMultipart_WithMissingHeader_ShouldReturnBadRequest() {
        // Arrange
        varianceRequest.setHeader(null);

        // Act
        ResponseEntity<?> response = controller.varianceAnalysisMultipart(varianceRequest, mockFile);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Header object is missing in the request", response.getBody());
    }

    @Test
    void varianceAnalysisMultipart_WithFileAndNoLocatorId_ShouldReturnBadRequest() {
        // Act
        ResponseEntity<?> response = controller.varianceAnalysisMultipart(varianceRequest, mockFile);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("LocatorId is missing in the request header", response.getBody());
    }

    @Test
    void varianceAnalysisMultipart_WithValidationError_ShouldReturnBadRequest() {
       // Arrange
       FormValidator mockValidator = mock(FormValidator.class);
       
       // Create required objects
       ErrorResponse errorResponse = new ErrorResponse();
       List<ErrorDetail> errorDetails = new ArrayList<>();
       ErrorDetail errorDetail = new ErrorDetail();
       // Set error detail properties
       errorDetails.add(errorDetail); 
       errorResponse.setErrorDetails(errorDetails);
       
       TransactionInfo transactionInfo = new TransactionInfo();
       // Set transaction info properties 
       errorResponse.setTransactionInfo(transactionInfo);
       
       // Mock validator behavior
       ValidationException validationException = new ValidationException(errorResponse);
       doThrow(validationException).when(mockValidator).validateRequest(any(), any());
       when(validatorRegistry.getAllValidators()).thenReturn(Collections.singletonList(mockValidator));
       
       // Set required header fields
       varianceRequest.getHeader().put("locatorId", "test-locator");

       // Act
       ResponseEntity<?> response = controller.varianceAnalysisMultipart(varianceRequest, null);

       // Assert
       assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
       assertNotNull(response.getBody(), "Response body should not be null");
       assertEquals(errorResponse, response.getBody());
    }

    @Test
    void taxCalculation_WithValidRequest_ShouldReturnSuccess() {
        // Arrange
        try {
			when(varianceAnalysisService.performVarianceAnalysisOrTaxCalc(any(), isNull()))
			    .thenReturn(mockResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}

        // Act
        ResponseEntity<?> response = controller.taxCalculation(taxRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse.getJsonResponse(), response.getBody());
    }

    @Test
    void taxCalculationMultipart_WithValidRequestAndFile_ShouldReturnSuccess() {
        // Arrange
        taxRequest.getHeader().put("locatorId", "test-locator");
        try {
			when(varianceAnalysisService.performVarianceAnalysisOrTaxCalc(any(), any()))
			    .thenReturn(mockResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}

        // Act
        ResponseEntity<?> response = controller.taxCalculationMultipart(taxRequest, mockFile);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse.getJsonResponse(), response.getBody());
    }

    @Test
    void testMDCCleanup() {
        // Arrange
        try {
			when(varianceAnalysisService.performVarianceAnalysisOrTaxCalc(any(), isNull()))
			    .thenThrow(new RuntimeException("Test exception"));
		} catch (IOException e) {
			e.printStackTrace();
		}

        // Act
        controller.varianceAnalysis(varianceRequest);

        // Assert
        assertNull(MDC.get("requestId"));  // Remove the message string
    }
}