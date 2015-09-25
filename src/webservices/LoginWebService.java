package webservices;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import transferObjects.UserAuthentication;
import controller.LoginController;
import Utils.ServerResponse;

@Path("/login")
public class LoginWebService {
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ServerResponse getLogin(UserAuthentication userAuthentication) {
		LoginController lc = LoginController.getInstance();
		ServerResponse serverResponse = lc.login(userAuthentication.getEmail(),
				userAuthentication.getPassword());
		return serverResponse;
	}
}
