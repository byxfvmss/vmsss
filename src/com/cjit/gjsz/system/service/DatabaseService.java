/**
 * Authority Service
 */
package com.cjit.gjsz.system.service;

/**
 * @author huboA
 */
public interface DatabaseService{

	/**
	 * 备份
	 */
	public void backup(String path);

	/**
	 * 还原
	 */
	public void restore(String path);
}
