package controller;

import java.sql.SQLException;

import database.DB_Queries;
import Utils.ResponseStatus;
import Utils.ServerResponse;
import Utils.Status;

public class LogoutController {
	private static LogoutController instance = new LogoutController();
	private LogoutController(){}
	public static LogoutController getInstance(){
		return instance;
	}
	
	public ServerResponse logout(int accessToken){
		ServerResponse serverResponse = new ServerResponse();
		Status status = new Status();
		DB_Queries d = DB_Queries.getInstance();
		try {
			if(d.deleteToken(accessToken)){
				status.setErrMessage(null);
				status.setResponseStatus(ResponseStatus.SUCCESS);
				serverResponse.setObject(true);
				serverResponse.setStatus(status);
			}else{
				status.setErrMessage("Error while Deleting Token");
				status.setResponseStatus(ResponseStatus.FAILURE);
				serverResponse.setObject(false);
				serverResponse.setStatus(status);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			status.setErrMessage("SQL Exception");
			status.setResponseStatus(ResponseStatus.SUCCESS);
			serverResponse.setObject(false);
			serverResponse.setStatus(status);
			e.printStackTrace();
		}
		return serverResponse;
	}
}
