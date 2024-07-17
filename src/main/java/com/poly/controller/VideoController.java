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
import com.poly.entity.History;
import com.poly.entity.User;
import com.poly.entity.Video;
import com.poly.service.HistoryService;
import com.poly.service.VideoService;
import com.poly.service.impl.HistoryServiceImpl;
import com.poly.service.impl.VideoServiceImpl;

/**
 * Servlet implementation class VideoController
 */
@WebServlet(urlPatterns = "/video")
public class VideoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VideoService videoService = new VideoServiceImpl();
	private HistoryService historyService = new HistoryServiceImpl();
    public VideoController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		String href = request.getParameter("id");
		HttpSession session = request.getSession();
		switch (action) {
		case "watch":
			doGetWatch(session, href, request, response);
			break;
		case "like":
			doGetLike(session, href, request, response);
			break;
		}
	}
	// localhost:8080/asm-java4/video?action=watch&id={href}
	private void doGetWatch(HttpSession session, String href, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Video video = videoService.findByHref(href);
		req.setAttribute("video", video);
		User currrentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);
		
		//Neu da dang nhap thi click vao xem thi them vao History - xem xem no da like hay chua
		if (currrentUser != null) {
			History history = historyService.create(currrentUser, video);
			req.setAttribute("flagLikedBtn", history.isLiked());
		}

		RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/user/video-detail.jsp");
		requestDispatcher.forward(req, resp);
	}
	// localhost:8080/asm-java4/video?action=like&id={href}
	private void doGetLike(HttpSession session, String href, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json");
		User currrentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);
		boolean result = historyService.updateLikeOrUnlike(currrentUser, href);
		if (result == true) {
			resp.setStatus(204);// Success nhma k tra ve data
		} else {
			resp.setStatus(400);// Loi
		}

	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
