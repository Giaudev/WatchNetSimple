package com.poly.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.constant.SessionAttr;
import com.poly.dto.UserDto;
import com.poly.dto.VideoLikedInfo;
import com.poly.entity.User;
import com.poly.service.StatsService;
import com.poly.service.UserService;
import com.poly.service.impl.StatsServiceImpl;
import com.poly.service.impl.UserServiceImpl;


@WebServlet(urlPatterns = {"/admin", "/admin/favorites"}, name = "HomeControllerOfAdmin")
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StatsService statsService = new StatsServiceImpl();
	private UserService userService = new UserServiceImpl();
    public HomeController() {
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);
		if (currentUser != null && currentUser.isAdmin() == true) {

			String path = request.getServletPath(); //localhost/Asm-Java4/login - Path re nhanh phia sau ten Project
			switch (path) {
			case "/admin":
				doGetHome(request, response);
				break;
			case "/admin/favorites":
				doGetFavorites(request, response);
				break;
			}

		} else {
			response.sendRedirect("index");
		}
	}

	private void doGetHome(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//Thong ke tat ca video va luot
		List<VideoLikedInfo> videos = statsService.findVideoLikedInfo();
		req.setAttribute("videos", videos);
		RequestDispatcher dispatcher = req.getRequestDispatcher("/views/admin/home.jsp");
		dispatcher.forward(req, resp);
	}

	// localhost:8080/asm-java4/admin/favorite?href={videoHref}
	private void doGetFavorites(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//Set data thay doi theo CheckBox
		PrintWriter out = resp.getWriter();
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");// Set kieu tra ve la JSON  (ajax)
		String videoHref = req.getParameter("href");
		List<UserDto> users = userService.findUsersLikedByVideoHref(videoHref);
		if (users.isEmpty()) {
			resp.setStatus(400);
		} else {
			// Phai map User tu Object sang Json
			ObjectMapper mapper = new ObjectMapper();
			String dataResponse = mapper.writeValueAsString(users);
			//
			resp.setStatus(200); // co tra ve data json
			out.print(dataResponse); // in ra mang Users vua dc convert sang Json
			out.flush(); // xả stream - day ra

		}

	}

}
