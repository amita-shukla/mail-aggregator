package controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import Utils.Hash;
import Utils.ResponseStatus;
import Utils.SendMail;
import Utils.ServerResponse;
import Utils.Status;
import validator.RegisterValidator;
import database.DB_Queries;

public class RegistrationController {
	private static RegistrationController instance = new RegistrationController();

	private RegistrationController() {
	}

	public static RegistrationController getInstance() {
		return instance;
	}

	public ServerResponse register(String name, String email, String password,
			String confirmPassword) {
		RegisterValidator rv = RegisterValidator.getInstance();
		Hash h = Hash.getInstance();
		ServerResponse serverResponse = new ServerResponse();
		Status status = rv.validate(name, email, password, confirmPassword);
		try {
			// make a verification link
			String temp_password = h.generateHash(email);

			// encrypt password
			String encrypted_password = h.generateHash(password);
			if (status.getResponseStatus() == ResponseStatus.SUCCESS) {
				DB_Queries d = DB_Queries.getInstance();
				// insert user's name, email, encrypted password, and generated
				// temp_pass
				if (d.registerUser(name, email, encrypted_password,
						temp_password)) {

					// get the user_id and token for creating verification link
					ResultSet rs = d.selectTokenForConfirmation(email);
					int user_id = 0;
					String temp_pass = null;
					while (rs.next()) {
						user_id = rs.getInt(1);
						temp_pass = rs.getString(2);
					}

					String verificationLink = "http://localhost:8080/MailApplication/UI/html/index.html?userid="
							+ Integer.toString(user_id) + "&" + temp_pass;
					System.out
							.println("verification Link: " + verificationLink);

					// send verification link
					/*
					 * // get the recipient email address ResultSet rs2 =
					 * d.selectReceiverEmail(user_id); String to_address = null;
					 * while (rs2.next()) { to_address = rs.getString(1); }
					 */
					SendMail sm = SendMail.getInstance();
					if (sm.sendVerificationLink(verificationLink, email)) {
						status.setResponseStatus(ResponseStatus.SUCCESS);
						serverResponse.setStatus(status);
						serverResponse.setObject(true);
						return serverResponse;
					} else {
						status.setResponseStatus(ResponseStatus.FAILURE);
						status.setErrMessage("Error while sending Verification Link");
						serverResponse.setStatus(status);
						serverResponse.setObject(false);
						return serverResponse;
					}

				} else {
					status.setResponseStatus(ResponseStatus.FAILURE);
					status.setErrMessage("Error during Registration");
					serverResponse.setStatus(status);
					serverResponse.setObject(false);
					return serverResponse;
				}

			} else {

				serverResponse.setStatus(status);
				serverResponse.setObject(false);
				return serverResponse;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			status.setResponseStatus(ResponseStatus.FAILURE);
			status.setErrMessage("SQL Exception");
			serverResponse.setStatus(status);
			serverResponse.setObject(false);
			e.printStackTrace();
			return serverResponse;

		}

	}
}
