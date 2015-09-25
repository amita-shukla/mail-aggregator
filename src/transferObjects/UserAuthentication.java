package transferObjects;

/**
 * 
 * @author Amita
 *
 *As the package name suggests, only one parameter can be sent to a web service. 
 *This class is used to pass the parameters to the login web service. 
 */
public class UserAuthentication {

	private String email;
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
