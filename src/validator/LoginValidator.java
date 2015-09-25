package validator;

import Utils.ResponseStatus;
import Utils.Status;

public class LoginValidator {

	private static LoginValidator instance = new LoginValidator();

	private LoginValidator() {
	}

	public static LoginValidator getInstance() {
		return instance;
	}

	public Status validate(String email, String password) {
		Status status = new Status();
		//validator to check if the entered values is null
		if (email.isEmpty() || password.isEmpty()) {

			status.setErrMessage("Empty userid or password field");

			status.setResponseStatus(ResponseStatus.FAILURE);

		} else {
			status.setResponseStatus(ResponseStatus.SUCCESS);
		}
		return status;
	}
}
