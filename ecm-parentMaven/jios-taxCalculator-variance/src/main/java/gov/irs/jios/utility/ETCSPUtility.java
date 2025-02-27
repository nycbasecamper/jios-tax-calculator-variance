package gov.irs.jios.utility;

import gov.irs.jios.model.JiosBody;
import gov.irs.jios.model.JiosError;
import gov.irs.jios.model.JiosRequest;
import gov.irs.jios.model.JiosResponse;

public  class ETCSPUtility {
	
	public static JiosResponse prepareResponse(JiosRequest request) {
		JiosBody body = request.getBody();
		body.setForms(request.getBody().getForms());
		body.setErrors(new JiosError());

		JiosResponse response = new JiosResponse();
		response.setHeader(request.getHeader());
		response.setBody(body);
		response.setHeader(request.getHeader());
		return response;
	}
}
