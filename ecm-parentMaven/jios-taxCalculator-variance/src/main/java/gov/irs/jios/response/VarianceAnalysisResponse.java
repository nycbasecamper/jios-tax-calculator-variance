package gov.irs.jios.response;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import gov.irs.jios.common.request.ValidatableRequest;

public class VarianceAnalysisResponse implements ValidatableRequest{
	 @JsonProperty("header")
	    private Map<String, Object> header = null;;

	    @JsonProperty("body")
	    private Map<String, Object> body = null;

	    @Override
	    public Map<String, Object> getHeader() {
	        return header;
	    }

	    @Override
	    public Map<String, Object> getBody() {
	        return body;
	    }

	    public void setHeader(Map<String, Object> header) {
	        this.header = header != null ? header : new HashMap<>();
	    }

	    public void setBody(Map<String, Object> body) {
	        this.body = body != null ? body : new HashMap<>();
	    }
}
