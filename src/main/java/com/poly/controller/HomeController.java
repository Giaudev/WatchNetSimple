package com.poly.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
@WebServlet(urlPatterns = {"/index","/favorites", "/history"})
public class HomeController extends HttpServlet{

	private static final long serialVersionUID = 1517115637537021552L;
	private static final int VIDEO_MAX_PAGE_SIZE = 4;
	private VideoService videoService = new VideoServiceImpl();
	private HistoryService historyService = new HistoryServiceImpl();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		// DoGet tong de? lay path cua tung DoGet theo case
		String path = req.getServletPath(); // localhost/Asm-Java4/login - Path re nhanh phia sau ten Project
		switch (path) {
		case "/index":
			doGetIndex(req, resp);
			break;
		case "/favorites":
			doGetFavorites(session, req, resp);
			break;
		case "/history":
			doGetHistory(session, req, resp);
			break;
		}
	}
	
	private void doGetIndex(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// Chia page
		List<Video> countVideo = videoService.findAll();
		int maxPage = (int) Math.ceil(countVideo.size() / (double) VIDEO_MAX_PAGE_SIZE);
		req.setAttribute("maxPage", maxPage);
		// For example:
		// 10 video , muon chia 1 trang co 4 video >>> 10/4 thi 2.5 trang => 3 TRANG
		List<Video> videos;
		String pageNumber = req.getParameter("page");
		if (pageNumber == null ||  Integer.valueOf(pageNumber) > maxPage ) {
			//Return to Page 1 neu ma Page rong or tham so truyen cua page > maxPage trong List 
			videos = videoService.findAll(1, VIDEO_MAX_PAGE_SIZE);
			req.setAttribute("currentPage", 1);
		} else {
			videos = videoService.findAll(Integer.valueOf(pageNumber), VIDEO_MAX_PAGE_SIZE);
			req.setAttribute("currentPage",Integer.valueOf(pageNumber));
		}
//		List<Video> videos = videoService.findAll();
		req.setAttribute("videos", videos);
		RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/user/index.jsp");
		requestDispatcher.forward(req, res);
	}

	private void doGetFavorites(HttpSession session, HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		User user = (User) session.getAttribute(SessionAttr.CURRENT_USER); // Check da login
		List<History> histories = historyService.findByUserAndIsLiked(user.getUsername()); // Kiem
		List<Video> videos = new ArrayList<>();
		histories.forEach(item -> videos.add(item.getVideo())); // Java8 forEach
		/*
		 * for(int i = 0 ; i < histories.size(); i++) {
		 * 		videos.add(histories.get(i).getVideo());
		 * }
		 * */
		req.setAttribute("videos", videos);
		RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/user/favorites.jsp");
		requestDispatcher.forward(req, res);
	}

	private void doGetHistory(HttpSession session, HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		User user = (User) session.getAttribute(SessionAttr.CURRENT_USER); // Check da login
		List<History> histories = historyService.findByUser(user.getUsername()); // Kiem
		List<Video> videos = new ArrayList<>();
		histories.forEach(item -> videos.add(item.getVideo())); // Java8 forEach
		req.setAttribute("videos", videos);
		RequestDispatcher requestDispatcher = req.getRequestDispatcher("/views/user/history.jsp");
		requestDispatcher.forward(req, res);
	}
		
}
