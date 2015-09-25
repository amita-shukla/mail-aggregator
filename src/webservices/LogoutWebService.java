package webservices;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import transferObjects.AccessToken;
import transferObjects.UserAuthentication;
import Utils.ServerResponse;
import controller.LoginController;
import controller.LogoutController;

@Path("/logout")
public class LogoutWebService {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ServerResponse getLogout(AccessToken accessToken) {
		LogoutController lc = LogoutController.getInstance();
		ServerResponse serverResponse = lc.logout(accessToken.getAccessToken());
		return serverResponse;
	}
}
