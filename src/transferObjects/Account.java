package transferObjects;

public class Account {
	private int accessToken;
	private String email;
	private String password;
	private String accountAccessType;
	private String incomingEmailServer;
	private String outgoingEmailServer;
	private int incomingEmailPort;
	private int outoingEmailPort;
	private boolean requireSsl;
	public int getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(int accessToken) {
		this.accessToken = accessToken;
	}
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
	public String getAccountAccessType() {
		return accountAccessType;
	}
	public void setAccountAccessType(String accountAccessType) {
		this.accountAccessType = accountAccessType;
	}
	public String getIncomingEmailServer() {
		return incomingEmailServer;
	}
	public void setIncomingEmailServer(String incomingEmailServer) {
		this.incomingEmailServer = incomingEmailServer;
	}
	public String getOutgoingEmailServer() {
		return outgoingEmailServer;
	}
	public void setOutgoingEmailServer(String outgoingEmailServer) {
		this.outgoingEmailServer = outgoingEmailServer;
	}
	public int getIncomingEmailPort() {
		return incomingEmailPort;
	}
	public void setIncomingEmailPort(int incomingEmailPort) {
		this.incomingEmailPort = incomingEmailPort;
	}
	public int getOutoingEmailPort() {
		return outoingEmailPort;
	}
	public void setOutoingEmailPort(int outoingEmailPort) {
		this.outoingEmailPort = outoingEmailPort;
	}
	public boolean isRequireSsl() {
		return requireSsl;
	}
	public void setRequireSsl(boolean requireSsl) {
		this.requireSsl = requireSsl;
	}
	
}
