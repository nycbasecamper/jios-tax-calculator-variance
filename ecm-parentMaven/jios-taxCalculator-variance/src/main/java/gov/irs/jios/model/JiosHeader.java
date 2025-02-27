package gov.irs.jios.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiosHeader {

    @Pattern(regexp = "[a-zA-Z0-9\"/-]{0,250}", message = "{error.header.tax_period")
    private String taxPrd;

    @Pattern(regexp = "[a-zA-Z0-9\"/-]{0,250}", message = "{error.header.mft_code}")
    private String mftCd;

    @Pattern(regexp = "[a-zA-Z0-9\"/-]{0,250}", message = "{error.header.unique_calculation_id}")
    private String uniqueCalculationId;

    @Pattern(regexp = "[a-zA-Z0-9\"/-]{0,250}", message = "{error.header.calc_date}")
    private String calcDt;
    
    @Pattern(regexp = "[a-zA-Z0-9\"/-]{0,250}", message = "{error.header.report_date}")
    private String reportDt;

    @Pattern(regexp = "[a-zA-Z0-9\" /-]{0,250}", message = "{error.header.case_id}")
    private String caseId;

    @Pattern(regexp = "[a-zA-Z0-9\"/-]{0,250}", message = "{error.header.submitter_id}")
    private String submitterId;

    @Pattern(regexp = "[a-zA-Z0-9\"/-]{0,250}", message = "{error.header.calc_type}")
    private String calcTypeTxt;

    @Pattern(regexp = "[a-zA-Z0-9\"/-]{0,250}", message = "{error.header.calc_tax_ind}")
    private String calculateTaxInd;
    
    @Pattern(regexp = "[a-zA-Z0-9\"/-]{0,250}", message = "{error.header.calc_variance_ind}")
    private String calculateVarianceInd;
    
    @Pattern(regexp = "[a-zA-Z0-9\"/-]{0,250}", message = "{error.header.calc_penalty_ind}")
    private String calculatePenaltyInd;
    
    @Pattern(regexp = "[a-zA-Z0-9\"/-]{0,250}", message = "{error.header.calc_interest_ind}")
    private String calculateInterestInd;
    
    @Pattern(regexp = "[a-zA-Z0-9\"/-]{0,250}", message = "{error.header.create_data_for_RAR_ind}")
    private String createDataForRARInd;

     private String taxComputationPIIStripInd ;
}
