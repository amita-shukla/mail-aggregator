package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import validator.AddAccountValidator;
import Utils.AesEncryption;
import Utils.EncryptionUtil;
import Utils.ResponseStatus;
import Utils.ServerResponse;
import Utils.Status;
import database.DB_Queries;

public class AddEmailController {
	private static AddEmailController instance = new AddEmailController();

	private AddEmailController() {
	}

	public static AddEmailController getInstance() {
		return instance;
	}

	public ServerResponse addEmail(int accessToken, String email,
			String password, String accountAccessType,
			String incoming_email_server, String outgoing_email_server,
			int incoming_email_port, int outgoing_email_port,
			boolean require_ssl) {

		AddAccountValidator aav = AddAccountValidator.getInstance();

		ServerResponse serverResponse = new ServerResponse();

		Status status = aav
				.validate(accessToken, email, password, accountAccessType,
						incoming_email_server, outgoing_email_server,
						incoming_email_port, outgoing_email_port);

		if (status.getResponseStatus() == ResponseStatus.SUCCESS) {
			try {

				// encrypt the password using 2 way encryption (symmetric key algorithm) 
				AesEncryption ae = AesEncryption.getInstance();
				String cipherPassword = null;
				try {
					
					final String originalText = password;
					// Encrypt the string using the public key
					
					
					final String cipherText = ae.encrypt(originalText);
					cipherPassword = cipherText.toString();
					// Printing the Original, Encrypted Text
					System.out.println("Original: " + originalText);
					System.out.println("Encrypted: " + cipherPassword);

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Exception while encryption of password");
					serverResponse.setObject(false);
					status.setErrMessage("Exception while encryption of password");
					status.setResponseStatus(ResponseStatus.FAILURE);
					serverResponse.setStatus(status);
					e.printStackTrace();
					return serverResponse;
				}

				// insert this cipherPassword in database added_email

				DB_Queries d = DB_Queries.getInstance();
				int user_id = 0;
				ResultSet rs = d.selectUserIdFromToken(accessToken);
				if (rs.next()) {
					user_id = rs.getInt(1);
				}
				if (d.addEmail(user_id, email, cipherPassword.toString(),
						accountAccessType, incoming_email_server,
						outgoing_email_server, incoming_email_port,
						outgoing_email_port, require_ssl)) {
					serverResponse.setObject(true);
					serverResponse.setStatus(status);

				} else {
					serverResponse.setObject(false);
					status.setErrMessage("Error while adding email");
					status.setResponseStatus(ResponseStatus.FAILURE);
					serverResponse.setStatus(status);

				}
				return serverResponse;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out
						.println("Exception while inserting account detail in db");
				serverResponse.setObject(false);
				status.setErrMessage("SQL Exception");
				status.setResponseStatus(ResponseStatus.FAILURE);
				serverResponse.setStatus(status);
				e.printStackTrace();
				return serverResponse;
			}
		} else {
			serverResponse.setObject(false);
			serverResponse.setStatus(status);
			return serverResponse;
		}

	}
}
