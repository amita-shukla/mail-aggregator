package Utils;

/**
Each Controller needs to send this class as its response.
the object type is used so that we can pass whatever we wish as the result of the respective controller
e.g. the login controller sends the token as the response if the login is successful.
Each request is validated using the status.
Also the status is SUCCESS if everything remains fine.
*/
public class ServerResponse {

	Object obj;
	Status status;	
	public void setStatus(Status st){
		this.status = st;
	}
	public Object getObj() {
		return obj;
	}
	public Status getStatus() {
		return status;
	}
	public void setObject(Object obj){
		this.obj = obj;
	}
	
}
