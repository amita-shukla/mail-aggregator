package controller;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String name = "tanu";
		String email = "";
		String password = "";
		String confirmPassword = "java90";
		String accountAccessType = "pop3";
		String incoming_email_server = "pop.gmail.com";
		int incoming_email_port = 995;
		String outgoing_email_server = "smtp.gmail.com";
		int outgoing_email_port = 465;
		boolean require_ssl = true;
		int user_id = 17;
		String token = "b53c0095dcff6ff109bfe57d51e17cfb269d39055974cbf6f74150f2d9861339";

		int controller=5;

		switch (controller) {
		case 1:
			// Login

			LoginController lc = LoginController.getInstance();
			try {
				if (lc.login(email, password))
					System.out.println("Login Successful");
				else
					System.out.println("Login Failed");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 2:
			// Register User

			RegistrationController rc = RegistrationController.getInstance();
			if (rc.register(name, email, password, confirmPassword)) {
				System.out.println("User Registered");
			}
			break;
		case 3:
			// Add Email
			AddEmailController ae = AddEmailController.getInstance();
			if (ae.addEmail(user_id, email, password, accountAccessType,
					incoming_email_server, outgoing_email_server,
					incoming_email_port, outgoing_email_port, require_ssl))
				System.out.println("Email added");

			break;
		case 4:
			// Verify
			VerificationController vc = VerificationController.getInstance();
			vc.confirmAccount(17, token);
			break;
		case 5:
			//Check Mails
			MailCheckController mc = MailCheckController.getInstance();
			mc.checkMail(17);
			break;
		default:
			System.out.println("please select the appropriate program");
		}
	}

}
