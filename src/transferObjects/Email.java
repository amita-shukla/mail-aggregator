package transferObjects;

public class Email {
	private int messageid;
	private String from;
	private String to;
	private String subject;
	private String sentDate;
	private String body;
	public int getMessageid() {
		return messageid;
	}
	public void setMessageid(int messageid) {
		this.messageid = messageid;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSentDate() {
		return sentDate;
	}
	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
