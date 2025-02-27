package gov.irs.jios.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JiosUnauthorizedResponse {

    private String errorMessage;
}
