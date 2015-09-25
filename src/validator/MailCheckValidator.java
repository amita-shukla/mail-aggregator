package validator;

import java.sql.SQLException;

import database.DB_Queries;
import Utils.ResponseStatus;
import Utils.Status;

public class MailCheckValidator {
	private static MailCheckValidator instance = new MailCheckValidator();

	private MailCheckValidator() {
	}

	public static MailCheckValidator getInstance() {
		return instance;
	}

	public Status validate(int accessToken) throws SQLException {
		Status status = new Status();
		DB_Queries d = DB_Queries.getInstance();
		// ResultSet rs = null;

		if (!(d.selectUserFromToken(accessToken)).first()) {
			status.setErrMessage("User Logged Out");
			status.setResponseStatus(ResponseStatus.FAILURE);

		} else {
			status.setResponseStatus(ResponseStatus.SUCCESS);
		}

		return status;
	}
}
