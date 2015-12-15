package cn.edu.cnu.training.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BaseDAO<T> {

	@Autowired
	private SessionFactory sessionFactory;
	
	private static Logger logger = LogManager.getLogger(BaseDAO.class);
	
	/**
	 * 添加一条记录
	 * @param object
	 * @return
	 */
	@Transactional(readOnly = false)
	public T create(T object) {
		return (T)sessionFactory.getCurrentSession().save(object);
	}

	/**
	 * 更新一条记录
	 * @param object
	 */
	@Transactional(readOnly = false)
	public boolean update(T object) {
		try{
			sessionFactory.getCurrentSession().update(object);
			return true;
		}catch(Exception e){
			logger.error(e);
			return false;
		}
	}

	/**
	 * 删除一条记录
	 * @param object
	 */
	@Transactional(readOnly = false)
	public boolean delete(T object) {
		try{
			sessionFactory.getCurrentSession().delete(object);
			return true;
		}catch(Exception e){
			logger.error(e.getStackTrace());
			return false;
		}
	}
	

	/**
	 * 查询某一个类中特定id的记录，空指针异常。
	 * @param clazz
	 * @param id
	 * @return
	 */
	@Transactional
	public T lookup(Class<? extends T> clazz, Serializable id) {
		return (T)sessionFactory.getCurrentSession().get(clazz, id);
	}

	/**
	 * 查询一个列表
	 * @param hql
	 * @return
	 */
	@Transactional
	public List<T> list(String hql) {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List<T> lTs = (List<T>)query.list();
		if(lTs == null){
			lTs = new ArrayList<T>();
		}
		return lTs;
	}
	
}