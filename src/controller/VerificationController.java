package controller;

import java.sql.SQLException;

import Utils.ResponseStatus;
import Utils.ServerResponse;
import Utils.Status;
import validator.VerificationValidator;
import database.DB_Queries;

public class VerificationController {
	private static VerificationController instance = new VerificationController();

	private VerificationController() {
	}

	public static VerificationController getInstance() {
		return instance;
	}

	public ServerResponse confirmAccount(String link) {
		DB_Queries d = DB_Queries.getInstance();
		ServerResponse serverResponse = new ServerResponse();
		VerificationValidator vv = VerificationValidator.getInstance();
		Status status = vv.validate(link);
		if (status.getResponseStatus() == ResponseStatus.SUCCESS) { // if the
																	// verification
																	// link is
																	// valid
			StringBuilder link2 = new StringBuilder(link);
			int index = link2.indexOf("?userid=");
			int index2 = link2.indexOf("&");
			// the link is validated so that StringIndexOutOfBound Exception is
			// not thrown when calculating substring
			int user_id = Integer.parseInt(link2.substring(index + 8, index2));
			String token = link2.substring((index2) + 1);
			
			try {
				if (d.updateActivated(user_id, token)) {
					status.setResponseStatus(ResponseStatus.SUCCESS);
					serverResponse.setStatus(status);
					serverResponse.setObject(true);
					return serverResponse;
				}else{
					status.setResponseStatus(ResponseStatus.FAILURE);
					status.setErrMessage("Error during Account Activation");
					serverResponse.setStatus(status);
					serverResponse.setObject(false);
					return serverResponse;
				}

			} catch (SQLException e) {
				System.out.println("Exception while confirming Account");
				e.printStackTrace();
				status.setResponseStatus(ResponseStatus.FAILURE);
				status.setErrMessage("SQL Exception");
				serverResponse.setStatus(status);
				serverResponse.setObject(false);
				return serverResponse;
			}
		} else {
			status.setResponseStatus(ResponseStatus.FAILURE);
			status.setErrMessage("Invalid Verification Link");
			serverResponse.setStatus(status);
			serverResponse.setObject(false);
			return serverResponse;
		}
	}

}
