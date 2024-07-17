package com.poly.dao;


import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;

import com.poly.util.JpaUtil;



public class AbstactDao<T> {
	public static final EntityManager entityManager = JpaUtil.getEntityManager();
	
	@SuppressWarnings("deprecation")
	@Override
	protected void finalize() throws Throwable {
		entityManager.close();
		super.finalize();
	}
	
	public T findById(Class<T> clazz, Integer id) {
		return entityManager.find(clazz, id);
	}
	public List<T> findAll(Class<T> clazz,boolean exitsIsActive) {
		String entityName = clazz.getSimpleName();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT o FROM ").append(entityName).append(" o");
		if (exitsIsActive == true) {
			sql.append(" WHERE isActive = 1");
		}
		TypedQuery<T> query = entityManager.createQuery(sql.toString(), clazz);
		return query.getResultList();
		// SELECT O FROM History o WHERE isActive = 1
	}
	public List<T> findAll(Class<T> clazz,boolean exitsIsActive, int pageNumber, int pageSize) {
		String entityName = clazz.getSimpleName();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT o FROM ").append(entityName).append(" o");
		if (exitsIsActive == true) {
			sql.append(" WHERE isActive = 1");
		}
		TypedQuery<T> query = entityManager.createQuery(sql.toString(), clazz);
		query.setFirstResult((pageNumber - 1 )  * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
		// SELECT O FROM History o WHERE isActive = 1
		
		/*
		 * có 5 phần tử 1 trang chứa 2 phần tử 
		 * tổng số trang là 3 
		 * trang 1: [0] [1]
		 * trang 2: [2] [3]
		 * trang 3: [4] 
		 * pageNumber = 2 , pageSize = 2
		 * 1 * 2 = 2 
		 * >> lấy bắt đầu từ phần tử thứ 2 và lấy 2 phần tử
		 */ 
	}
	
	// SELECT o FROM user o where o.username = ?0 and o.password = ?1;
	public T findOne(Class<T> clazz, String sql, Object... params) {
		TypedQuery<T> query = entityManager.createQuery(sql,clazz);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		List<T> result = query.getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}
	public List<T> findMany(Class<T> clazz, String sql, Object... params) {
		TypedQuery<T> query = entityManager.createQuery(sql,clazz);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		return query.getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<Object[]> findManyByNativeQuery(Class<T> clazz, String sql, Object... params) {
		Query query = entityManager.createNativeQuery(sql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findManyByNativeQuery(String sql, Object... params) {
		Query query = entityManager.createNativeQuery(sql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		return query.getResultList();
	}
	
	public T create(T entity) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(entity); // insert
			entityManager.getTransaction().commit();
			System.out.println("create succsecc");
			return entity;
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			System.out.println("Cannot insert entity "+entity.getClass().getSimpleName()+ " to DB");
			throw new RuntimeException(e);
		}
	}
	public T update(T entity) {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(entity); // update
			entityManager.getTransaction().commit();
			System.out.println("update succsecc");
			return entity;
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			System.out.println("Cannot update entity "+entity.getClass().getSimpleName()+ " to DB");
			throw new RuntimeException(e);
		}
	}
	public T delete(T entity) {
		try {
			entityManager.getTransaction().begin();
			entityManager.remove(entity); // remove
			entityManager.getTransaction().commit();
			System.out.println("delete succsecc");
			return entity;
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			System.out.println("Cannot delete entity "+entity.getClass().getSimpleName()+ " to DB");
			throw new RuntimeException(e);
		}
	}
													// list param dùng Map
	public List<T> callStore(String namedStore, Map<String, Object> params){
		//ten Store - list Param - key la tham so truyen vao trong cau SP
		// Value la gia tri truyen vao trong dieu kien where
		StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery(namedStore);
		
		params.forEach((key,value) -> query.setParameter(key, value));  // JAVA 8
		//duyet het cac phan tu
		return (List<T>) query.getResultList();
	}
}
