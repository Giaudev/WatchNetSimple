package com.poly.service.impl;

import java.sql.Timestamp;
import java.util.List;

import com.poly.dao.AbstactDao;
import com.poly.dao.HistoryDao;
import com.poly.dao.impl.HistoryDaoImpl;
import com.poly.entity.History;
import com.poly.entity.User;
import com.poly.entity.Video;
import com.poly.service.HistoryService;
import com.poly.service.VideoService;

public class HistoryServiceImpl extends AbstactDao<History> implements HistoryService{
	private HistoryDao dao;
	private VideoService videoService = new VideoServiceImpl();
	public HistoryServiceImpl() {
		dao =  new HistoryDaoImpl();
	}
	
	@Override
	public List<History> findByUser(String username) {
		return dao.findByUser(username);
	}

	@Override
	public List<History> findByUserAndIsLiked(String username) {
		return dao.findByUserAndIsLiked(username);
	}

	@Override
	public History findByUserIdAndVideoId(Integer userId, Integer videoId) {
		return dao.findByUserIdAndVideoId(userId, videoId);
	}

	@Override
	public History create(User user, Video video) {
		History existHistory = findByUserIdAndVideoId(user.getId(), video.getId());
		if (existHistory == null) {
			existHistory = new History();
			existHistory.setUser(user);
			existHistory.setVideo(video);
			existHistory.setViewedDate(new Timestamp(System.currentTimeMillis()));
			existHistory.setLiked(Boolean.FALSE);
			return dao.create(existHistory);
			
		}
		return existHistory;
	}

	@Override
	public boolean updateLikeOrUnlike(User user, String videoHref) {
		Video video = videoService.findByHref(videoHref);
		History existHistory = findByUserIdAndVideoId(user.getId(), video.getId());
		
		 if(existHistory.isLiked() == Boolean.FALSE) {
			existHistory.setLiked(Boolean.TRUE);
			existHistory.setLikedDate(new Timestamp(System.currentTimeMillis()));
		}else {
			existHistory.setLiked(Boolean.FALSE);
			existHistory.setLikedDate(null);
		}
		 
		History updateHistory = dao.update(existHistory);
		return updateHistory != null ? true : false ;
	}

}
