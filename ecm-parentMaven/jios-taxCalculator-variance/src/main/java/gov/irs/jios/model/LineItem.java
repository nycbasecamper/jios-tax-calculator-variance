package gov.irs.jios.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineItem {

	@JsonInclude(Include.NON_NULL)
	private String lineItemReferenceKeyId;
	
	@JsonInclude(Include.NON_NULL)
	private String lineNameTxt;
	
	@JsonInclude(Include.NON_NULL)
	private String perReturnValueTxt;
	
	@JsonInclude(Include.NON_NULL)
	private String taxCalculatorValueTxt;
	
	@JsonInclude(Include.NON_NULL)
	private String errorMessage;
	
	@JsonInclude(Include.NON_NULL)
	private String varianceValueTxt;

	@JsonInclude(Include.NON_NULL)
	private String totalAdjustmentValueTxt;

	@JsonInclude(Include.NON_NULL)
	private String agreedAdjustmentValueTxt;

	@JsonInclude(Include.NON_NULL)
	private String adjustmentStatusCd;

	@JsonInclude(Include.NON_NULL)
	private String userAdjustedLineInd;

	@JsonInclude(Include.NON_NULL)
	private String issuePenaltyTypeTxt;

	@JsonInclude(Include.NON_NULL)
	private String totalAdjTaxCalcValueTxt;

	@JsonInclude(Include.NON_NULL)
	private String agreedAdjTaxCalcValueTxt;

}
