package Utils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import transferObjects.Email;

public class CheckMessagesUtil {
	public static CheckMessagesUtil instance = new CheckMessagesUtil();

	private CheckMessagesUtil() {
	}

	public static CheckMessagesUtil getInstance() {
		return instance;
	}

	ArrayList<Message> mesg;

	public ArrayList<Message> checkMail(String incomingMailServer,
			String accountAccessType, String email, String password,
			int incomingEmailPort, int requiresSsl, int index)
			throws MessagingException {

		CheckMessagesUtil chmUtil = null;
		if (accountAccessType.equals("pop3")) {
			chmUtil = CheckMessagesUtil.getInstance();
			chmUtil.checkMailByPop(incomingMailServer, accountAccessType,
					email, password, incomingEmailPort, requiresSsl, index);
		} else if (accountAccessType.equals("imap")) {
			chmUtil = CheckMessagesUtil.getInstance();
			chmUtil.checkMailByImap(incomingMailServer, accountAccessType,
					email, password, incomingEmailPort, requiresSsl, index);
		} else {
			// this check goes in validator. Only an if-else should exist.
			System.out.println("Unknown account access type");
		}
		return mesg;
	}

	public void checkMailByPop(String incomingMailServer,
			String account_access_type, String email, String password,
			int incomingEmailPort, int requiresSsl, int index)
			throws MessagingException {

		Properties props = new Properties();

		props.put("mail.pop3.host", incomingMailServer);
		props.put("mail.pop3.port", incomingEmailPort);
		if (requiresSsl == 1)
			props.put("mail.pop3.starttls.enable", "true");
		else
			props.put("mail.pop3.starttls.enable", "false");

		Session emailSession = Session.getInstance(props);

		// create the pop3 store object and connect with the pop server
		Store store = emailSession.getStore("pop3s");
		store.connect(incomingMailServer, email, password);

		// create the folder and open it
		Folder emailFolder = store.getFolder("INBOX");
		emailFolder.open(Folder.READ_ONLY);
		int messageCount = emailFolder.getMessageCount();

		// retrieve the messages from the folder in an array
		Message[] messages = null;
		if ((index + 10) < messageCount) {
			messages = emailFolder.getMessages(index, index + 10);
		} else {
			messages = emailFolder.getMessages(index, messageCount);
		}

		mesg = new ArrayList<>(Arrays.asList(messages));

		// close the store and folder objects
//		emailFolder.close(false);
	//	store.close();

	}

	public void checkMailByImap(String incomingMailServer,
			String accountAccessType, String email, String password,
			int incoming_email_port, int requires_ssl, int index) throws MessagingException {

		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");

		Session session = Session.getInstance(props, null);
		Store store;
		
			store = session.getStore();
			store.connect(incomingMailServer, email, password);
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			int messageCount = inbox.getMessageCount();
			// retrieve the messages from the folder in an array
			Message[] messages = null;
			if ((index + 50) < messageCount) {
				messages = inbox.getMessages(index, index + 50);
			} else {
				messages = inbox.getMessages(index, messageCount);
			}

			mesg = new ArrayList<>(Arrays.asList(messages));

			// close the store and folder objects
			//inbox.close(false);
			//store.close();
	}

	/*
	 * This method checks for content-type based on which, it processes and
	 * fetches the content of the message
	 */
	public void writePart(Part p, Email email) throws Exception {

		System.out.println("CONTENT-TYPE: " + p.getContentType());

		// check if the content is plain text
		if (p.isMimeType("text/plain")) {
			System.out.println("This is plain text");
			System.out.println("---------------------------");
			System.out.println((String) p.getContent());
			email.setBody((String) p.getContent());
		}
		// check if the content has attachment
		else if (p.isMimeType("multipart/*")) {
			System.out.println("This is a Multipart");
			System.out.println("---------------------------");
			Multipart mp = (Multipart) p.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++)
				writePart(mp.getBodyPart(i),email);
		}
		// check if the content is a nested message
		else if (p.isMimeType("message/rfc822")) {
			System.out.println("This is a Nested Message");
			System.out.println("---------------------------");
			writePart((Part) p.getContent(),email);
		}
		// check if the content is an inline image
		else if (p.isMimeType("image/jpeg")) {
			System.out.println("--------> image/jpeg");
			Object o = p.getContent();

			InputStream x = (InputStream) o;
			// Construct the required byte array
			System.out.println("x.length = " + x.available());
			int i = 0;
			byte[] bArray = new byte[x.available()];

			while ((i = x.available()) > 0) {
				int result = (x.read(bArray));
				if (result == -1)
					break;
			}
			FileOutputStream f2 = new FileOutputStream("C:/Users/user/workspace/MailApplication/tmp/image.jpg");
			f2.write(bArray);
		} else if (p.getContentType().contains("image/")) {
			System.out.println("content type" + p.getContentType());
			File f = new File("image" + new Date().getTime() + ".jpg");
			DataOutputStream output = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(f)));
			com.sun.mail.util.BASE64DecoderStream test = (com.sun.mail.util.BASE64DecoderStream) p
					.getContent();
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = test.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
				email.setBody(new String(buffer));
			}
		} else {
			Object o = p.getContent();
			if (o instanceof String) {
				System.out.println("This is a string");
				System.out.println("---------------------------");
				System.out.println((String) o);
				email.setBody((String) o);
			} else if (o instanceof InputStream) {
				System.out.println("This is just an input stream");
				System.out.println("---------------------------");
				InputStream is = (InputStream) o;
				is = (InputStream) o;
				int c;
				while ((c = is.read()) != -1)
					System.out.write(c);
				email.setBody(String.valueOf(c));
			} else {
				System.out.println("This is an unknown type");
				System.out.println("---------------------------");
				System.out.println(o.toString());
				email.setBody(o.toString());
			}
		}

	}
	
	public void writeEnvelope(Message message, Email email) {
		System.out.println("This is the message envelope");
		Address[] a;

		try {

			// FROM
			if ((a = message.getFrom()) != null) {
				for (int i = 0; i < a.length; i++) {
					System.out.println("FROM: " + a[i].toString());
					email.setFrom(a[i].toString());
				}
			}

			// TO
			if ((a = message.getRecipients(Message.RecipientType.TO)) != null) {
				for (int i = 0; i < a.length; i++) {
					System.out.println("TO: " + a[i].toString());
					email.setTo(a[i].toString());
				}
			}

			// SUBJECT
			if (message.getSubject() != null) {
				System.out.println("SUBJECT: " + message.getSubject());
				email.setSubject(message.getSubject());
			}

			// DATE
			if (message.getSentDate() != null) {
				System.out.println("DATE: " + message.getSentDate());
				email.setSentDate(message.getSentDate().toString());
			}

		} catch (MessagingException e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
