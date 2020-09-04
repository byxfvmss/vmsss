package com.cjit.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;

/**
 * 说明:通用DAO接口
 * 
 * @version
 * @since 2008-6-30 下午11:25:45
 */
public interface GenericDao{

	/**
	 * 保存（持久化）一个对象
	 * @param object 要保存的对象
	 * 
	 * @since Jun 24, 2008
	 */
	public int save(String statements, Map map);

	/**
	 * 保存（持久化）一个对象
	 * @param object 要保存的对象
	 * 
	 * @since Jun 24, 2008
	 */
	public void update(String statements, Map map);

	/**
	 * 删除一个对象
	 * @param object 要更新的对象
	 * 
	 * @since Jun 24, 2008
	 */
	public void delete(String statements, Serializable id);

	/**
	 * 删除对象
	 * @param statements
	 * @param map
	 */
	public void delete(String statements, Map map);

	/**
	 * 批量删除
	 * @param Collection 需要删除的对象集合
	 * 
	 * @since Jun 24, 2008
	 */
	public void deleteAll(String statements, Serializable[] id);

	/**
	 * 根据HQL语句查询结果集数量
	 * @param countHql 负责查询结果集数量的HQL语句
	 * @return Integer 查询结果
	 * 
	 * @since Jun 24, 2008
	 */
	public Long getResultCount(String statements);

	/**
	 * 根据预设SQL查询结果集
	 * @param query hql语句
	 * @param parameters 要传的参数集合
	 * @return List 查询结果集合
	 */
	public List find(final String query, final Map parameters);

	/**
	 * 根据QBC查询结果集数量
	 * @param query hql语句
	 * @return parameters 要传的参数集合
	 */
	public Long getRowCount(final String query, final Map parameters);

	/**
	 * 根据预设SQL查询结果集
	 * @param query hql语句
	 * @param parameters 要传的参数集合
	 * @param pageInfo 翻页实例
	 * @return List 查询结果集合
	 */
	public List find(final String hql, final Map parameters,
			final PaginationList paginationList);
	
	public List findLargeData(final String hql, final Map parameters,
			final PaginationList paginationList);
	
	public Double findForDouble(final String query, final Map parameters);
	
	public Object findForObject(final String query,final Map parameters);
	/**
	 * 批量插入数据
	 * @param statements 状态
	 * @param data 数据列表
	 */
	public void insertBatch(final String statements,final List data);
	
	/**
	 * 批量更新数据
	 * @param statements 状态
	 * @param data 数据列表
	 */
	public void updateBatch(final String statements,final List data);
	public void deleteBatch(final String statements,final List data);
}
