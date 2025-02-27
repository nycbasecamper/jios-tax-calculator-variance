package gov.irs.jios.controller;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gov.irs.jios.common.client.tr.pojo.RetrieveFieldsResponseDTO;
import gov.irs.jios.common.client.tr.service.ErrorTransformationService;
import gov.irs.jios.common.client.tr.service.VarianceAnalysisService;
import gov.irs.jios.common.exception.ValidationException;
import gov.irs.jios.common.validation.FormValidator;
import gov.irs.jios.common.validation.FormValidatorRegistry;
import gov.irs.jios.common.validation.GenericValidator;
import gov.irs.jios.request.TaxCalculationRequest;
import gov.irs.jios.request.VarianceAnalysisRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name = "Variance Analysis", description = "Operations related to variance analysis")
@RequestMapping("/jios-taxcalculator")
public class VarianceAnalysisController {
	
    @SuppressWarnings("unused")
	private final GenericValidator validator;
    
    @Autowired
    private FormValidatorRegistry validatorRegistry;
    
    @Autowired
    private ErrorTransformationService errorTransformationService;
    
    @Autowired
    private VarianceAnalysisService varianceAnalysisService;
    
    public VarianceAnalysisController() {
        this.validator = new GenericValidator("validation-rules/variance-analysis-validation-rules.json");
    }
    
    // Added this method for backward compatibility for people testing with Postman
    @PostMapping(path="/api/v1.0/varianceAnalysis", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> varianceAnalysis(@RequestBody VarianceAnalysisRequest request){
    	log.info("In JSON Version");
    	return varianceAnalysisMultipart(request, null);
    }
    
    @PostMapping(path="/api/v1.0/varianceAnalysis", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)  //TODO /api/v1.0 - should be property driven or if there is any other better way to handle versioning
    public ResponseEntity<?> varianceAnalysisMultipart(@RequestPart(value = "formData") VarianceAnalysisRequest request,
    										@RequestPart(value = "snapshot", required = false) MultipartFile file) {
        
    	log.debug("Received Variance Analysis Request in Multipart Format");
        HttpHeaders headers = new HttpHeaders();         
        headers.setContentType(MediaType.APPLICATION_JSON);       

        try {
            if (request == null) {
                log.warn("Received null request");
                return ResponseEntity.badRequest().body("Invalid request");
            }
            
            if (request.getHeader() == null) {
                log.warn("header object is missing in the request");
                return ResponseEntity.badRequest().body("Header object is missing in the request");
            }

            String transactionId = (String) request.getHeader().get("transactionId");
            if (transactionId != null) {
            	int lastIndex = transactionId.lastIndexOf("-");
            	if (lastIndex > 0) {
            		MDC.put("requestId", transactionId.substring(lastIndex + 1, transactionId.length()));
            	}
            }

            // if snapshot is present, locatorId is a required field
            if(file != null && ObjectUtils.isEmpty(request.getHeader().get("locatorId"))) {
            	log.warn("locatorId is missing in the request");
                return ResponseEntity.badRequest().body("LocatorId is missing in the request header");
            }
            
            /*List<String> validationErrors = validator.validate(request);
            if (!validationErrors.isEmpty()) {
                log.warn("Validation errors: {}", validationErrors);
                return ResponseEntity.badRequest().body(validationErrors); //TODO - enable validation back after it is refactored for the latest ECM payload
            }*/
            
            // Run all form validators
            List<FormValidator> validators = validatorRegistry.getAllValidators();
            for (FormValidator validator : validators) {
            	try {
            		validator.validateRequest(request, errorTransformationService);
            	}
            	catch(ValidationException e) {
            		return new ResponseEntity<>(e.getErrorResponse(), headers, HttpStatus.BAD_REQUEST);
            	}
            }
            
            RetrieveFieldsResponseDTO response = varianceAnalysisService.performVarianceAnalysisOrTaxCalc(request, file);
            
            log.info("Variance Analysis Completed Successfully");
            return new ResponseEntity<>(response.getJsonResponse(), headers, HttpStatus.OK);
        }
        catch (ValidationException e) {
            log.error("ValidationException occurred during Variance Analysis: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            log.error("Exception occurred during Variance Analysis", e);
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        finally {
            MDC.remove("requestId");
        }
    }
    
    @PostMapping(path="/api/v1.0/taxCalculation", consumes = MediaType.APPLICATION_JSON_VALUE) //TODO /api/v1.0 - should be property driven or if there is any other better way to handle versioning
    public ResponseEntity<?> taxCalculation(@RequestBody TaxCalculationRequest request) {
        log.debug("Received Tax Calculation Request in JSON Format");
        return taxCalculationMultipart(request, null);
    }
    
    @PostMapping(path="/api/v1.0/taxCalculation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> taxCalculationMultipart(@RequestPart(value = "formData") TaxCalculationRequest request,
    												@RequestPart(value = "snapshot", required = false) MultipartFile file) {
        log.debug("Received Tax Calculation Request in Multipart Format");
        HttpHeaders headers = new HttpHeaders();         
        headers.setContentType(MediaType.APPLICATION_JSON);       

        try {
        	 if (request == null) {
                 log.warn("Received null request");
                 return ResponseEntity.badRequest().body("Invalid request");
             }
             
             if (request.getHeader() == null) {
                 log.warn("header object is missing in the request");
                 return ResponseEntity.badRequest().body("Header object is missing in the request");
             }

             String transactionId = (String) request.getHeader().get("transactionId");
             if (transactionId != null) {
             	int lastIndex = transactionId.lastIndexOf("-");
             	if (lastIndex > 0) {
             		MDC.put("requestId", transactionId.substring(lastIndex + 1, transactionId.length()));
             	}
             }
             
             // if snapshot is present, locatorId is a required field
             if(file != null && ObjectUtils.isEmpty(request.getHeader().get("locatorId"))) {
             	log.warn("locatorId is missing in the request");
                 return ResponseEntity.badRequest().body("LocatorId is missing in the request header");
             }
            
            /*List<String> validationErrors = validator.validate(request);
            if (!validationErrors.isEmpty()) {
                log.warn("Validation errors: {}", validationErrors);
                return ResponseEntity.badRequest().body(validationErrors); //TODO - enable validation back after it is refactored for the latest ECM payload
            }*/
            
            RetrieveFieldsResponseDTO response = varianceAnalysisService.performVarianceAnalysisOrTaxCalc(request, file);
            
            log.info("Tax Calculation Completed Successfully");
            return new ResponseEntity<>(response.getJsonResponse(), headers, HttpStatus.OK);
        }
        catch (ValidationException e) {
            log.error("ValidationException occurred during Tax Calculation", e);
            return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            log.error("Exception occurred during Tax Calculation", e);
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        finally {
            MDC.remove("requestId");
        }
    }
}