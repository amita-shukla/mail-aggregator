function invokeLogin() {
	$.ajax({
		type : "POST",
		url : 'http://localhost:8080/MailApplication/rest/login/',
		data : JSON.stringify({
			"email" : $("#login_email").val(),
			"password" : $("#login_password").val()
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			if (data.status.responseStatus === "FAILURE") {
				$("#login_result").text(
						"Sorry! Your login failed due to "
								+ data.status.errMessage + ".");
			} else {
				openHome();
				localStorage.setItem("accessToken", data.obj);
			}

		},
		error : function(msg, url, linenumber) {
			alert('Error message: ' + msg + '\nURL: ' + url + '\nLine Number: '
					+ linenumber);
		}
	});
}

function openRegister() {
	$("#login").hide();
	$("#register").show();
}

function closeRegister() {
	$("#register").hide();
	$("#login").show();
}

function invokeRegister() {
	$
			.ajax({
				type : "POST",
				url : 'http://localhost:8080/MailApplication/rest/register',
				data : JSON.stringify({
					"name" : $("#register_name").val(),
					"email" : $("#register_email").val(),
					"password" : $("#register_password").val(),
					"confirmPassword" : $("#register_confirmPassword").val()
				}),
				contentType : "application/json; charset=utf-8",
				dataType : "json",
				success : function(data) {
					if (data.status.responseStatus === "FAILURE") {
						$("#register_result").text(
								"Sorry! Your registration failed due to "
										+ data.status.errMessage + ".");
					} else {
						$("#register_success")
								.text(
										"Thank you for registration. Please confirm your registration by clicking on the verification link emailed to you.");
					}
				},
				error : function(msg, url, line) {
					alert('error trapped in error: function(msg, url, line)');
					alert('msg = ' + msg + ', url = ' + url + ', line = '
							+ line);
				}
			});
}

function getMessage() {
	var x = location.search;
	if (x != "") {
		var message = invokeVerification(x);
		return message;
	} else {
		return null;
	}
}

function invokeVerification(x) {

	$.ajax({
		type : "POST",
		url : 'http://localhost:8080/MailApplication/rest/verification',
		data : JSON.stringify({
			"link" : x
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			$("#message").text(
					"Your account has been confirmed! Login to Continue!");
		},
		error : function(msg, url, line) {
			alert('error trapped in error: function(msg, url, line)');
			alert('msg = ' + msg + ', url = ' + url + ', line = ' + line);
		}
	});
	return message;
}

function openHome() {
	$("#login").hide();
	$("#home").show();
	$("#login_result").text(" ");
}

function logout() {
	if (localStorage.getItem("accessToken") != null) {
		invokeLogout(localStorage.getItem("accessToken"));
		localStorage.removeItem("accessToken");
	}

	$("#home").hide();
	$("#login").show();
	location.reload();
}

function toggleSettings() {
	$("#settings").toggle();
	if ($('#settings').is(':visible')) {
		$("#messages").hide();
	} else {
		$("#messages").show();
	}
}

function invokeLogout(accessToken) {
	$.ajax({
		type : "POST",
		url : 'http://localhost:8080/MailApplication/rest/logout',
		data : JSON.stringify({
			"accessToken" : accessToken
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			return;
		},
		error : function(msg, url, line) {
			alert('error trapped in error: function(msg, url, line)');
			alert('msg = ' + msg + ', url = ' + url + ', line = ' + line);
		}
	});
}

function invokeAddAccount() {
	$.ajax({
		type : "POST",
		url : 'http://localhost:8080/MailApplication/rest/addAccount',
		data : JSON.stringify({
			"accessToken" : localStorage.getItem("accessToken"),
			"email" : $("#settings_email").val(),
			"password" : $("#settings_password").val(),
			"accountAccessType" : $("#account_access_type").val(),
			"incomingEmailServer" : $("#incoming_email_server").val(),
			"outgoingEmailServer" : $("#outgoing_email_server").val(),
			"incomingEmailPort" : $("#incoming_email_port").val(),
			"outoingEmailPort" : $("#outgoing_email_port").val(),
			"requireSsl" : $("#requires_ssl").val()
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			if (data.status.responseStatus === "FAILURE") {
				if (data.status.errMessage === "Session Timed Out") {
					logout(localStorage.getItem("accessToken"));
					$("#message").text("Sorry! Your Session Has Expired.");

				} else if (data.status.errMessage === "User Logged Out") {
					logout();
					$("#message").text("You have already been logged out.");
				} else {
					$("#settings_result").text(
							"Sorry! Your account could not be added due to "
									+ data.status.errMessage + ".");
				}
			} else {
				$('#settings-form')[0].reset(); // reset all the form fields
				$("#settings_success").text("Account added successfully");
				// invokeFetchEmail();
			}
		},
		error : function(msg, url, line) {
			alert('error trapped in error: function(msg, url, line)');
			alert('msg = ' + msg + ', url = ' + url + ', line = ' + line);
		}
	});
}

var index = 0;
function invokeMailCheck() {
	var accessToken = localStorage.getItem("accessToken");
	$
			.ajax({
				type : "POST",
				url : 'http://localhost:8080/MailApplication/rest/mailCheck',
				data : JSON.stringify({
					"accessToken" : accessToken
				}),
				contentType : "application/json; charset=utf-8",
				dataType : "json",
				success : function(data) {
					if (data.status.responseStatus === "FAILURE") {
						if (data.status.errMessage === "Session Timed Out") {
							logout(localStorage.getItem("accessToken"));
							$("#message").text(
									"Sorry! Your Session Has Expired.");

						} else if (data.status.errMessage === "User Logged Out") {
							logout();
							$("#message").text(
									"You have already been logged out.");
						} else {
							$("#error").text(
									"Emails could not be fetched due to "
											+ data.status.errMessage + ".");
						}
					} else {
						$("#error").hide();
						var r = new Array(), j = -1;
						r[++j] = '<tr><th>Message Id </th> <th>From</th><th>To</th><th>Subject</th><th>Sent Date</th></tr>';
						for (var key = 0; key < data.obj.length; key++) {
							r[++j] = '<tr><td>';
							r[++j] = data.obj[key].messageid;
							r[++j] = '</td><td>';
							r[++j] = data.obj[key].from;
							r[++j] = '</td><td>';
							r[++j] = data.obj[key].to;
							r[++j] = '</td><td>';
							r[++j] = data.obj[key].subject;
							r[++j] = '</td><td>';
							r[++j] = data.obj[key].sentDate;
							r[++j] = '</td></tr>';
							r[++j] = '<tr><td colspan="4"><iframe id="message_body"><html><head></head><body>';
							r[++j] = data.obj[key].body;
							r[++j] = '</body></html></iframe></td></tr>';

							
						}
						$("#emails_table").html(r.join(' '));
					}

				},
				error : function(msg, url, line) {
					alert('error trapped in error: function(msg, url, line)');
					alert('msg = ' + msg + ', url = ' + url + ', line = '
							+ line);
				}
			});

}

function openMessage() {
	$(".message-body").toggle();
}
