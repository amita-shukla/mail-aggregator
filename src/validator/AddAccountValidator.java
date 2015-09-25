package validator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import database.DB_Queries;
import Utils.ResponseStatus;
import Utils.Status;

public class AddAccountValidator {
	private static AddAccountValidator instance = new AddAccountValidator();

	private AddAccountValidator() {
	}

	public static AddAccountValidator getInstance() {
		return instance;
	}

	public Status validate(int accessToken, String email, String password,
			String accountAccessType, String incoming_email_server,
			String outgoing_email_server, int incoming_email_port,
			int outgoing_email_port) {
		Status status = new Status();

		DB_Queries d = DB_Queries.getInstance();
		ResultSet rs = null;

		if (accessToken == 0 || email.isEmpty() || password.isEmpty()
				|| accountAccessType.isEmpty()
				|| incoming_email_server.isEmpty()
				|| outgoing_email_server.isEmpty() || incoming_email_port == 0
				|| outgoing_email_port == 0) {
			status.setErrMessage("Empty Field(s)");
			status.setResponseStatus(ResponseStatus.FAILURE);
			return status;
		} else {
			try {
				if (!(rs = d.selectUserFromToken(accessToken)).first()) {// there
																			// are
																			// no
																			// rows
																			// in
																			// the
																			// resultSet
					status.setErrMessage("User Logged Out");
					status.setResponseStatus(ResponseStatus.FAILURE);

				} else {
					// if the user for the given access token exists, compare
					// with timestamp
					//int userId = rs.getInt(1);
					Timestamp timeStamp = rs.getTimestamp(2);
					Date curTimestamp = new Date();
					if (curTimestamp.after(new Timestamp(timeStamp.getTime()
							+ (5 * 60 * 1000)))) {
						status.setErrMessage("Session Timed Out");
						status.setResponseStatus(ResponseStatus.FAILURE);
					} else {
						status.setResponseStatus(ResponseStatus.SUCCESS);
					}
				}
			} catch (SQLException e) {
				status.setErrMessage("SQL Exception while extracting User from Token");
				status.setResponseStatus(ResponseStatus.FAILURE);
				e.printStackTrace();
				return status;
			}
			return status;
		}

	}

}
