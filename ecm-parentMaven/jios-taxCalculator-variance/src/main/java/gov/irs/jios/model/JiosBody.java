package gov.irs.jios.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiosBody {

	@JsonIgnore
    private String calcType;
	
    @Valid
    @JsonInclude(Include.NON_EMPTY)
    List<Form> forms = new ArrayList<>();
	
    @JsonInclude(Include.NON_EMPTY)
    private JiosError errors;
}
