package validator;

import Utils.ResponseStatus;
import Utils.Status;

public class RegisterValidator {

	private static RegisterValidator instance = new RegisterValidator();

	private RegisterValidator() {
	}

	public static RegisterValidator getInstance() {
		return instance;
	}

	public Status validate(String name, String email, String password,
			String confirmPassword) {
		Status status = new Status();
		if(name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
			status.setErrMessage("Empty field(s)");
			status.setResponseStatus(ResponseStatus.FAILURE);
			return status;
		}else if (!password.equals(confirmPassword)) {
			status.setErrMessage("Confirm Password field should be same as Password Field");
			status.setResponseStatus(ResponseStatus.FAILURE);
			return status;
		} else {
			status.setResponseStatus(ResponseStatus.SUCCESS);
			return status;
		}
	}
}
