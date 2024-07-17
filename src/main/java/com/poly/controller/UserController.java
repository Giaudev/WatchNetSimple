package com.poly.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.poly.constant.SessionAttr;
import com.poly.entity.User;
import com.poly.service.EmailService;
import com.poly.service.UserService;
import com.poly.service.impl.EmailServiceImpl;
import com.poly.service.impl.UserServiceImpl;

/**
 * Servlet implementation class UserController
 */

@WebServlet(urlPatterns = { "/login", "/logout", "/register", "/forgotPass","/changePass" })
public class UserController extends HttpServlet {
	
	private static final long serialVersionUID = 910241445867414681L;
	private UserService userService = new UserServiceImpl();
	private EmailService emailService = new EmailServiceImpl();
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String path = request.getServletPath(); // localhost/asm/login - Path is Login - know? - re~ nhanh'
		switch (path) {
		case "/login":
			doGetLogin(request, response);
			break;
		case "/register":
			doGetRegister(request, response);
			break;
		case "/logout":
			doGetLogout(session, request, response);
			break;
		case "/forgotPass":
			doGetForgotPass(request, response);
			break;
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		String path = request.getServletPath(); // localhost/asm/login - Path is Login - know? - re~ nhanh'
		switch (path) {
		case "/login":
			doPostLogin(session, request, response);
			break;
		case "/register":
			doPostRegister(session, request, response);
			break;
		case "/forgotPass":
			doPostForgotPass(request, response);
			break;
		case "/changePass":
			doPostChangePass(session,request, response);
			break;
//		case "/shareVideo":
//			doPostShareVideo(session,req, res);
//			break;
		}
	}


	private void doGetLogin(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/user/login.jsp");
		requestDispatcher.forward(req, res);
	}
	
	private void doGetRegister(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/user/register.jsp");
		requestDispatcher.forward(req, res);
	}

	private void doGetForgotPass(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/user/forgot-pass.jsp");
		requestDispatcher.forward(req, res);
	}

	private void doGetLogout(HttpSession session, HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		session.removeAttribute(SessionAttr.CURRENT_USER);
		res.sendRedirect("index");
	}
	
	private void doPostLogin(HttpSession session, HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String errorBtn = req.getParameter("errorBtn");
		
		User user = userService.login(username, password);
		if (user != null) {
			session.setAttribute(SessionAttr.CURRENT_USER, user);
			res.sendRedirect("index");
		} else {
			res.sendRedirect("login");	
		}
	}
	
	private void doPostRegister(HttpSession session, HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		
		User user = userService.register(username, password, email);
		if (user != null) {
			emailService.sendMail(getServletContext(), user, "welcome");
			session.setAttribute(SessionAttr.CURRENT_USER, user);
			res.sendRedirect("index");
		} else {
			res.sendRedirect("register");	
		}
	}
	
	private void doPostForgotPass(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		String email = request.getParameter("email");
		User userWithNewPass = userService.resetPassword(email);
		
		if (userWithNewPass != null) {
			emailService.sendMail(getServletContext(), userWithNewPass, "forgot");
			response.setStatus(204);
		}else {
			response.setStatus(400);
		}
	}
	
	private void doPostChangePass(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		String currentPass = request.getParameter("currentPass");
		String newPass = request.getParameter("newPass");
		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);
		
		
			if (currentUser.getPassword().equals(currentPass)) {
				currentUser.setPassword(newPass);
				User userUpdate =  userService.update(currentUser);
				if (userUpdate != null) {
					session.setAttribute(SessionAttr.CURRENT_USER, userUpdate);
					response.setStatus(204);
				}else {
					response.setStatus(400);
				}
				
			}else {
				response.setStatus(400);
			}
		}
}
