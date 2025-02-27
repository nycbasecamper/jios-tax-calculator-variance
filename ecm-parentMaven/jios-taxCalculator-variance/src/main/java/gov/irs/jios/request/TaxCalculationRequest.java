package gov.irs.jios.request;

import java.util.Map;

import gov.irs.jios.common.request.ValidatableRequest;
import lombok.Data;

@Data
public class TaxCalculationRequest implements ValidatableRequest {
    private Map<String, Object> header = null;
    private Map<String, Object> body = null;
}