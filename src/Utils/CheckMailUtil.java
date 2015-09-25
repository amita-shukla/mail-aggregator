package Utils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

public class CheckMailUtil {

	ArrayList<Message> mesg = new ArrayList<Message>();

	private static CheckMailUtil instance = new CheckMailUtil();

	private CheckMailUtil() {
	}

	public static CheckMailUtil getInstance() {
		return instance;
	}

	public ArrayList<Message> checkMail(String host, String storeType,
			String username, String password) {
		CheckMailUtil chmUtil = null;

		if (storeType.equals("pop3")) {
			chmUtil = CheckMailUtil.getInstance();
			chmUtil.checkMailbypop(host, storeType, username, password, false);
		} else if (storeType.equals("imap")) {
			chmUtil = CheckMailUtil.getInstance();
			chmUtil.checkMailbyImap(host, username, password, false);
		} else {
			System.out.println("Unknown account access type");
		}
		return mesg;
	}

	public void fetchMail(String host, String storeType, String username,
			String password) {
		CheckMailUtil chmUtil = null;
		if (storeType.equals("pop3")) {
			chmUtil = CheckMailUtil.getInstance();
			chmUtil.checkMailbypop(host, storeType, username, password, true);
		} else if (storeType.equals("imap")) {
			chmUtil = CheckMailUtil.getInstance();
			chmUtil.checkMailbyImap(host, username, password, true);
		} else {
			System.out.println("Unknown account access type");
		}
	}

	public void checkMailbypop(String host, String storeType, String user,
			String password, boolean fetch) {

		try {
			Properties props = new Properties();

			props.put("mail.pop3.host", host);
			props.put("mail.pop3.port", "995");
			props.put("mail.pop3.stattls.enable", "true");
			Session emailSession = Session.getInstance(props);

			// create the pop3 store object and connect with the pop server
			Store store = emailSession.getStore("pop3s");
			store.connect(host, user, password);

			// create the folder and open it
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);
			int messageCount = emailFolder.getMessageCount();

			// retrieve the messages from the folder in an array and print it

			Message[] messages = emailFolder.getMessages();

			System.out.println("message.length= " + messages.length);

			for (int i = messages.length - 1; (i > messages.length - 51) && (i>0); i--) {
				Message message = messages[i];
				mesg.add(message);
				
				CheckMailUtil u = CheckMailUtil.getInstance();
				System.out
						.println("---------------------------------------------------------------------------------------------------------------------------------");
				u.writeEnvelope(message);

				if (fetch == true) {
					 CheckMailUtil chmUtil = CheckMailUtil.getInstance();
					 chmUtil.writePart(message);
					/*
					Object content = message.getContent();
					String body = null;
					Multipart mp = null;
					BodyPart bp = null;
					if (content instanceof String) {
						body = (String) content;
						System.out.println("Text: " + body);

					} else if (content instanceof Multipart) {
						mp = (Multipart) content;
						bp = mp.getBodyPart(0);
						System.out.println("Text: " + bp.getContent());
					}
	*/
					// System.out.println("Text: "+text);
				}
				
			}
		
			
			// close the store and folder objects
			//emailFolder.close(false);
			//store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void checkMailbyImap(String host, String user, String password,
			boolean fetch) {
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");

		Session session = Session.getInstance(props, null);
		Store store;
		try {
			store = session.getStore();
			store.connect(host, user, password);
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			Message[] messages = inbox.getMessages();
			for (int i = messages.length - 1; i > messages.length - 51; i--) {
				Message message = messages[i];
				CheckMailUtil u = CheckMailUtil.getInstance();
				System.out
						.println("---------------------------------------------------------------------------------------------------------------------------------");
				u.writeEnvelope(message);

				if (fetch == true) {
					// CheckMailUtil chmUtil = CheckMailUtil.getInstance();
					 //chmUtil.writePart(message);
					u.writePart(message);
					/*
					Object content = message.getContent();
					String body = null;
					Multipart mp = null;
					BodyPart bp = null;
					if (content instanceof String) {
						body = (String) content;
						System.out.println("Text: " + body);

					} else if (content instanceof Multipart) {
						mp = (Multipart) content;
						bp = mp.getBodyPart(0);
						System.out.println("Text: " + bp.getContent());
					}
					*/
				}
			}

			/*
			 * Address[] in = msg.getFrom(); for (Address address : in) {
			 * System.out.println("FROM:" + address.toString()); } Multipart mp
			 * = (Multipart) msg.getContent(); BodyPart bp = mp.getBodyPart(0);
			 * System.out.println("SENT DATE:" + msg.getSentDate());
			 * System.out.println("SUBJECT:" + msg.getSubject());
			 * System.out.println("CONTENT:" + bp.getContent());
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * This method checks for content-type based on which, it processes and
	 * fetches the content of the message
	 */
	public void writePart(Part p) throws Exception {

		System.out.println("CONTENT-TYPE: " + p.getContentType());

		// check if the content is plain text
		if (p.isMimeType("text/plain")) {
			System.out.println("This is plain text");
			System.out.println("---------------------------");
			System.out.println((String) p.getContent());
		}
		// check if the content has attachment
		else if (p.isMimeType("multipart/*")) {
			System.out.println("This is a Multipart");
			System.out.println("---------------------------");
			Multipart mp = (Multipart) p.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++)
				writePart(mp.getBodyPart(i));
		}
		// check if the content is a nested message
		else if (p.isMimeType("message/rfc822")) {
			System.out.println("This is a Nested Message");
			System.out.println("---------------------------");
			writePart((Part) p.getContent());
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
			FileOutputStream f2 = new FileOutputStream("/tmp/image.jpg");
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
			}
		} else {
			Object o = p.getContent();
			if (o instanceof String) {
				System.out.println("This is a string");
				System.out.println("---------------------------");
				System.out.println((String) o);
			} else if (o instanceof InputStream) {
				System.out.println("This is just an input stream");
				System.out.println("---------------------------");
				InputStream is = (InputStream) o;
				is = (InputStream) o;
				int c;
				while ((c = is.read()) != -1)
					System.out.write(c);
			} else {
				System.out.println("This is an unknown type");
				System.out.println("---------------------------");
				System.out.println(o.toString());
			}
		}

	}

	/*
	 * 
	 * private boolean textIsHtml = false;
	 * 
	 * private String getText(Part p) throws MessagingException, IOException {
	 * if (p.isMimeType("text/*")) { String s = (String) p.getContent();
	 * textIsHtml = p.isMimeType("text/html"); return s; }
	 * 
	 * if (p.isMimeType("multipart/alternative")) { // prefer html text over
	 * plain text Multipart mp = (Multipart) p.getContent(); String text = null;
	 * for (int i = 0; i < mp.getCount(); i++) { Part bp = mp.getBodyPart(i); if
	 * (bp.isMimeType("text/plain")) { if (text == null) text = getText(bp);
	 * continue; } else if (bp.isMimeType("text/html")) { String s =
	 * getText(bp); if (s != null) return s; } else { return getText(bp); } }
	 * return text; } else if (p.isMimeType("multipart/*")) { Multipart mp =
	 * (Multipart) p.getContent(); for (int i = 0; i < mp.getCount(); i++) {
	 * String s = getText(mp.getBodyPart(i)); if (s != null) return s; } }
	 * 
	 * return null; }
	 */

	public void writeEnvelope(Message message) {
		System.out.println("This is the message envelope");
		Address[] a;

		try {

			// FROM
			if ((a = message.getFrom()) != null) {
				for (int i = 0; i < a.length; i++) {
					System.out.println("FROM: " + a[i].toString());
				}
			}

			// TO
			if ((a = message.getRecipients(Message.RecipientType.TO)) != null) {
				for (int i = 0; i < a.length; i++) {
					System.out.println("TO: " + a[i].toString());
				}
			}

			// SUBJECT
			if (message.getSubject() != null) {
				System.out.println("SUBJECT: " + message.getSubject());
			}

			// DATE
			if (message.getSentDate() != null) {
				System.out.println("DATE: " + message.getSentDate());
			}

		} catch (MessagingException e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//String host = "pop.gmail.com";
		//String mailStoreType = "pop3";
		String host ="imap.gmail.com";
		String mailStoreType ="imap";
		String username = "amitashukla0906@gmail.com";
		String password = "asit,s9!";

		CheckMailUtil cmUtil = CheckMailUtil.getInstance();

		 cmUtil.checkMail(host, mailStoreType, username, password);
	//	cmUtil.fetchMail(host, mailStoreType, username, password);
	//	System.out.println("IMAP");
		// cmUtil.checkMail("imap.gmail.com","imap",username, password);
		//cmUtil.fetchMail(host, mailStoreType, username, password);
	}
}
