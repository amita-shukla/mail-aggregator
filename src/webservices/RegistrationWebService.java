package webservices;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import transferObjects.UserRegistration;
import Utils.ServerResponse;
import controller.RegistrationController;

@Path("/register")
public class RegistrationWebService {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ServerResponse getRegister(UserRegistration userRegistration) {
		RegistrationController rc = RegistrationController.getInstance();
		ServerResponse serverResponse = rc.register(userRegistration.getName(),
				userRegistration.getEmail(), userRegistration.getPassword(),
				userRegistration.getConfirmPassword());
		return serverResponse;
	}

}
