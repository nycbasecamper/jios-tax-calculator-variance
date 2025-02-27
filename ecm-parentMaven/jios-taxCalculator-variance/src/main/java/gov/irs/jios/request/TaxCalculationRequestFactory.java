package gov.irs.jios.request;

import org.springframework.stereotype.Component;

import gov.irs.jios.common.request.ValidatableRequest;
import gov.irs.jios.common.request.ValidatableRequestFactory;

@Component
public class TaxCalculationRequestFactory implements ValidatableRequestFactory {
    @Override
    public ValidatableRequest createRequest() {
        return new TaxCalculationRequest();
    }
}
