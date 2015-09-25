package webservices;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import transferObjects.UserVerification;
import controller.VerificationController;
import Utils.ServerResponse;

@Path("/verification")
public class VerificationWebService {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ServerResponse getVerificationLink(UserVerification userVerification){
				
		VerificationController vs = VerificationController.getInstance();
		ServerResponse serverResponse = vs.confirmAccount(userVerification.getLink());
		return serverResponse;
	}
}
