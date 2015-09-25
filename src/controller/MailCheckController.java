//com.sun.mail.util.MailConnectException is caused when you r not connected to internet.
package controller;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Message;
import javax.mail.MessagingException;

import transferObjects.Email;
import validator.MailCheckValidator;
import Utils.AesEncryption;
import Utils.CheckMailUtil;
import Utils.CheckMessagesUtil;
import Utils.ResponseStatus;
import Utils.ServerResponse;
import Utils.Status;
import database.DB_Queries;

public class MailCheckController implements Comparator<Message> {

	private static MailCheckController instance = new MailCheckController();

	private MailCheckController() {
	}

	public static MailCheckController getInstance() {
		return instance;
	}

	public ServerResponse checkMail(int token) {
		
	
		MailCheckValidator mcv = MailCheckValidator.getInstance();

		ServerResponse serverResponse = new ServerResponse();

		Status status= new Status();
		try {
			status = mcv.validate(token);
		} catch (SQLException e1) {
			
			status.setErrMessage("Sql Exception");
			status.setResponseStatus(ResponseStatus.FAILURE);
			serverResponse.setObject(false);
			serverResponse.setStatus(status);
			e1.printStackTrace();
		}

		if (status.getResponseStatus() == ResponseStatus.SUCCESS) {

			DB_Queries d = DB_Queries.getInstance();

			ResultSet rs1;
			try {
				rs1 = d.selectUserFromToken(token);

				int user_id = 0;
				if (rs1.next()) {
					user_id = rs1.getInt(1);
				}

				ResultSet rs = d.selectEmail(user_id);
				ArrayList<Message> messages = new ArrayList<Message>();
				while (rs.next()) {

					String email = rs.getString(1);
					String password = rs.getString(2);
					//call a validator function here that checks the credentials
					String accountAccessType = rs.getString(3);
					//call a validator that checks that mail server must be either pop or imap
					String incomingMailServer = rs.getString(4);
					int incomingEmailPort = rs.getInt(5);
					int requiresSsl = rs.getInt(6);

					// decrypt the password
					// -------------------------------------------------------------
					
					AesEncryption ae = AesEncryption.getInstance();
					
					
					final String plainText = ae.decrypt(password);

					System.out.println("Original: " + password);
					System.out.println("Decrypted: " + plainText);
					// --------------------------------------------------------------
					
					//CheckMailUtil cmUtil = CheckMailUtil.getInstance();
					CheckMessagesUtil cmUtil = CheckMessagesUtil.getInstance();
					messages.addAll(cmUtil.checkMail(incomingMailServer, accountAccessType, email, plainText, incomingEmailPort, requiresSsl, 1));

					System.out
							.println("---------------------------------------------------next account-----------------------------------------------------");
				}
				// sort according to latest mail first

				Collections.sort(messages, new MailCheckController());
				List<Email> emails = new ArrayList<>(messages.size());
				for (int i = 0; i < messages.size(); i++) {
					System.out.println("message number: " + (i + 1));
					Message msg = messages.get(i);
					Email email = new Email();
					email.setMessageid(i);
					CheckMessagesUtil u = CheckMessagesUtil.getInstance();
					u.writeEnvelope(msg, email);
					u.writePart(msg,email);
					emails.add(email);
					System.out
							.println("-------------------------------------------------------------next mail----------------------------------------------");
					
				}
				
				serverResponse.setObject(emails);
				serverResponse.setStatus(status);
				return serverResponse;
			} catch (Exception e) {
				String exType = e.getClass().getName();
				serverResponse.setObject(false);
				status.setErrMessage(exType);
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

	@Override
	public int compare(Message o1, Message o2) {

		try {
			if (o1.getSentDate().after(o2.getSentDate()))
				return -1;
			else if (o1.getSentDate().equals(o2.getSentDate()))
				return 0;
			else
				return 1;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}

}
