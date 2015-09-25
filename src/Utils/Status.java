package Utils;

public class Status {
	
	private String errMessage;
	private ResponseStatus responseStatus;
	
	//getters and setters
	public String getErrMessage() {
		return errMessage;
	}
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(ResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
	}
	
}
