package com.poly.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.poly.dao.UserDao;
import com.poly.dao.impl.UserDaoImpl;
import com.poly.dto.UserDto;
import com.poly.entity.User;
import com.poly.service.UserService;

public class UserServiceImpl implements UserService{

	private UserDao dao;
	
	public UserServiceImpl() {
		dao = new UserDaoImpl();
	}
	@Override
	public User findByID(Integer id) {
		return dao.findById(id);
	}

	@Override
	public User findByEmail(String email) {
		return dao.findByEmail(email);
	}

	@Override
	public User findByUsername(String username) {
		return dao.findByUsername(username);
	}

	@Override
	public User login(String username, String password) {
		return dao.findByUsernameAndPassword(username, password);
	}

	@Override
	public User resetPassword(String email) {
		User existUser = findByEmail(email); // Tim xem da ton tai User voiw Email nay chua
		if(existUser != null) {
			//Math.random -> pass
			//(Math.random()) * ((max - min) + 1)) + min
			String newPass = String.valueOf((int)(Math.random() * ((9999 - 1000) + 1)) + 1000);
			existUser.setPassword(newPass);
			return dao.update(existUser);
		}
		return null;
	}

	@Override
	public List<User> findAll() {
		return dao.findAll();
	}

	@Override
	public List<User> findAll(int pageNumber, int pageSize) {
		return dao.findAll(pageNumber, pageSize);
	}

	@Override
	public User register(String username, String password, String email) {
		User newUser = new User();
		newUser.setUsername(username);
		newUser.setPassword(password);
		newUser.setEmail(email);
		newUser.setAdmin(Boolean.FALSE);
		newUser.setActive(Boolean.TRUE);
		return dao.create(newUser);
	}

	@Override
	public User update(User entity) {
		return dao.update(entity);
	}	

	@Override
	public User delete(String username) {
		User user = dao.findByUsername(username);
		user.setActive(Boolean.FALSE);
		return dao.update(user);
	}

	@Override
	public List<UserDto> findUsersLikedByVideoHref(String href) {
		Map<String, Object> params = new HashMap<>();	
		params.put("videoHref", href);
		List<User> users =  dao.findUsersLikedByVideoHref(params);
		List<UserDto> result = new ArrayList<UserDto>();
		users.forEach(user -> {
			UserDto dto = new UserDto();
			dto.setId(user.getId());
			dto.setUsername(user.getUsername());
			dto.setEmail(user.getEmail());
			result.add(dto);
		});
		return result; 
	}


}
