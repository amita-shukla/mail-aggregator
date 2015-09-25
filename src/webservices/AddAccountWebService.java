package webservices;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import transferObjects.Account;
import transferObjects.UserAuthentication;
import Utils.ServerResponse;
import controller.AddEmailController;
import controller.LoginController;

@Path("/addAccount")
public class AddAccountWebService {
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ServerResponse getAddAccount(Account account) {
		AddEmailController ac = AddEmailController.getInstance();
		ServerResponse serverResponse = ac.addEmail(account.getAccessToken(), account.getEmail(),account.getPassword(), account.getAccountAccessType(),
				account.getIncomingEmailServer(), account.getOutgoingEmailServer(), account.getIncomingEmailPort(), account.getOutoingEmailPort(), account.isRequireSsl());
		return serverResponse;
	}
}
