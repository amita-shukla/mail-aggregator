package webservices;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import transferObjects.AccessToken;
import Utils.ServerResponse;
import controller.MailCheckController;

@Path("/mailCheck")
public class MailCheckWebService {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ServerResponse getMessages(AccessToken accessToken) {
		MailCheckController mcc = MailCheckController.getInstance();
		ServerResponse serverResponse = mcc.checkMail(accessToken.getAccessToken());
		return serverResponse;
	}
}
