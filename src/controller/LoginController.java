package controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import Utils.Hash;
import Utils.ResponseStatus;
import Utils.ServerResponse;
import Utils.Status;
import database.DB_Queries;
import validator.LoginValidator;

public class LoginController {

	private static LoginController instance = new LoginController();

	private LoginController() {
	}

	public static LoginController getInstance() {
		return instance;
	}

	public ServerResponse login(String email, String password) {

		DB_Queries d = DB_Queries.getInstance();
		LoginValidator lv = LoginValidator.getInstance();
		Hash h = Hash.getInstance();
		String hashed_password = h.generateHash(password);
		ServerResponse serverResponse = new ServerResponse();
		Status status = lv.validate(email, password);

		// the token must be unique. Its unlikely that 2 people login at the
		// same
		// instant. For now atleast :D
		int token = (int) (System.currentTimeMillis() % 1000000000);

		try {
			if (status.getResponseStatus() == ResponseStatus.SUCCESS) { // the
																		// status
																		// is
																		// SUCCESS
																		// for
																		// now
																		// after
																		// validation

				ResultSet rs = d.selectUserForLogin(email, hashed_password);
				while (rs.next()) {
					if (rs.getInt(1) == 1 && rs.getInt(2) == 1) { // user
																	// authenticated
						serverResponse.setStatus(status);
						// return the token to js. the js keeps the token as a
						// global variable and returns it when it calls any
						// other web service.
						serverResponse.setObject(token);
						// insert the token in the token table. the token from
						// this table (along with the user id) is
						// checked before calling any controller in the validate
						// function.
						// the validate class will also compare the request with
						// the timestamp.
						// If the request is taken longer than requested, the
						// session is supposed to destroy, i.e.,
						// the service should not be called with an apt error
						// message.

						// Fetch userId corresponding the authenticated user
						ResultSet rs1 = d.selectUserId(email, hashed_password);
						
						while (rs1.next()) {
							int user_id = rs1.getInt(1);
							d.insertToken(user_id, token);
						}
						
					} else if (rs.getInt(1) == 0) { // No record is found with
													// matching user name or
													// password.
						status.setResponseStatus(ResponseStatus.FAILURE);
						status.setErrMessage("Invalid Credentials");
						serverResponse.setStatus(status);
						serverResponse.setObject(false);
					} else if (rs.getInt(2) != 1) { // matching record is found,
													// but the account is not
													// activated.
						status.setResponseStatus(ResponseStatus.FAILURE);
						status.setErrMessage("Account not Activated");
						serverResponse.setStatus(status);
						serverResponse.setObject(false);
					}
				}

				return serverResponse;

			} else {
				// the status object received after validation is directly
				// passed
				serverResponse.setStatus(status);
				serverResponse.setObject(false);
				return serverResponse;
			}
		} catch (SQLException e) {
			status.setResponseStatus(ResponseStatus.FAILURE);
			status.setErrMessage("SQL Exception");
			serverResponse.setStatus(status);
			serverResponse.setObject(false);
			e.printStackTrace();
			return serverResponse;
		}
	}
}
