package gov.irs.jios.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Form {

    @NotEmpty(message = "formNum cannot be empty or null")
    @Pattern(regexp = "[a-zA-Z0-9\"/-]{0,250}", message = "Only alphanumeric characters / and - allowed here.")
    private String formNum;

    @Valid
    List<LineItem> lineItems = new ArrayList<LineItem>();
}
