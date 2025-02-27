package gov.irs.jios.exception;

public class ProcessFormException extends RuntimeException{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProcessFormException(String errorMessage) {
        super(errorMessage);
    }
}
