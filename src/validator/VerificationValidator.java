package validator;

import Utils.ResponseStatus;
import Utils.Status;

public class VerificationValidator {
	private static VerificationValidator instance = new VerificationValidator();
	
	private VerificationValidator(){}
	
	public static VerificationValidator getInstance(){
		return instance;
	}
	
	public Status validate(String link){
		Status status = new Status();
		int index = link.indexOf("userid");
		int index2 = link.indexOf("&");
		//validator to check if the entered values is null
		if (index==-1 || index2==-1) {

			status.setErrMessage("Invalid Verification Link");

			status.setResponseStatus(ResponseStatus.FAILURE);

		} else {
			status.setResponseStatus(ResponseStatus.SUCCESS);
		}
		return status;
	}
}
