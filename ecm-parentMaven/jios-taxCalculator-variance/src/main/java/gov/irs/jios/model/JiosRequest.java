package gov.irs.jios.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import gov.irs.jios.validator.CheckLineItem;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiosRequest {

	@Valid
	private JiosHeader header;
	
	@Valid
	@CheckLineItem
	private JiosBody body;
	

}
