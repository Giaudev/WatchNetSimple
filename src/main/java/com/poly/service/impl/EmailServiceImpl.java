package com.poly.service.impl;

import javax.servlet.ServletContext;

import com.poly.entity.User;
import com.poly.service.EmailService;
import com.poly.util.SendEmailUtils;

public class EmailServiceImpl implements EmailService {
	private static final String EMAIL_WELCOME_SUBJECT = "Welcome to WatchNet";
	private static final String EMAIL_FORGOT_PASSWORD = "WatchNet - New Password";
	private static final String EMAIL_SHARE_VIDEOS = "WatchNet - Another has just send the video to you!!!";

	@Override
	public void sendMail(ServletContext context, User recipient, String type) {
		String host = context.getInitParameter("host");
		String port = context.getInitParameter("port");
		String user = context.getInitParameter("user");
		String pass = context.getInitParameter("pass");
		try {
			String content = null;
			String subject = null;
			switch (type) {
			case "welcome":
				subject = EMAIL_WELCOME_SUBJECT;
				content = "Dear " + recipient.getUsername() +", Hope you have the best time ?? watch our video!!";
				break;
			case "forgot":
				subject = EMAIL_FORGOT_PASSWORD;
				content = "Dear " + recipient.getUsername() + ", new password here:" + recipient.getPassword();
				break;
			case "share":
				subject = EMAIL_SHARE_VIDEOS;
				content =  recipient.getUsername() + " just send you a video!";
				break;
			default:
				subject = "WatchNet";
				content = "this email does not exist! check it!!";
			}
			SendEmailUtils.sendEmail(host, port, user, pass, recipient.getEmail(), subject, content);
		} catch (Exception e) {	
			e.printStackTrace();
		}

	}

}
