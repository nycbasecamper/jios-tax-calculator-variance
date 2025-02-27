package gov.irs.jios.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import gov.irs.jios.exception.InvalidRequestException;
import gov.irs.jios.exception.ProcessFormException;
import gov.irs.jios.model.JiosRequest;
import gov.irs.jios.model.JiosResponse;
import gov.irs.jios.model.LineItem;
import gov.irs.jios.utility.ETCSPUtility;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ETCSExceptionHandler {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ProcessFormException.class)
	@ResponseBody
	public JiosResponse handleProcessFormException(ProcessFormException e, WebRequest webRequest) {
		JiosRequest request = (JiosRequest) webRequest.getAttribute("etcsrequest", WebRequest.SCOPE_REQUEST);
		JiosResponse response = ETCSPUtility.prepareResponse(request);
		response.getBody().getErrors().setError(
				Arrays.asList(LineItem.builder().errorMessage("Failed to Process Request " + e.getMessage()).build()));

		return response;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidRequestException.class)
	@ResponseBody
	public JiosResponse handleInvalidRequestException(InvalidRequestException e, WebRequest webRequest) {
		JiosRequest request = (JiosRequest) webRequest.getAttribute("etcsrequest", WebRequest.SCOPE_REQUEST);
		JiosResponse response = ETCSPUtility.prepareResponse(request);
		response.getBody().getErrors().setError(Arrays.asList(LineItem.builder().errorMessage(e.getMessage()).build()));
		return response;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	public JiosResponse handleConstraintViolationExceptions(ConstraintViolationException e, WebRequest webRequest) {
		JiosRequest request = (JiosRequest) webRequest.getAttribute("etcsrequest", WebRequest.SCOPE_REQUEST);
		JiosResponse response = ETCSPUtility.prepareResponse(request);
		
		log.error(e.getMessage());
		List<LineItem> lineItems = new ArrayList<>();
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		if (!CollectionUtils.isEmpty(violations)) {
			for (ConstraintViolation violation : violations) {
				log.info("violation :" + violation.getMessage());
				String[] messageArray = violation.getMessage().split("#");
				log.info("message Arrays:" + messageArray.length);
				if (messageArray.length == 4) {

					lineItems.add(LineItem.builder().errorMessage(messageArray[0]).lineNameTxt(messageArray[1])
							.perReturnValueTxt(messageArray[2].trim()).lineItemReferenceKeyId(messageArray[3]).build());

				} else if (messageArray.length == 1) {
					lineItems.add(LineItem.builder().errorMessage(messageArray[0]).build());
				} else if (messageArray.length == 2) {
					lineItems.add(LineItem.builder().errorMessage(messageArray[0]).lineNameTxt(messageArray[1]).build());
				} else if (messageArray.length == 3) {
					lineItems.add(LineItem.builder().errorMessage(messageArray[0]).lineNameTxt(messageArray[1])
							.perReturnValueTxt(messageArray[2].trim()).build());
				}
			}
			response.getBody().getErrors().setError(lineItems);
		}
		return response;
	}
}
