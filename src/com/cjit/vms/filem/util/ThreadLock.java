package com.cjit.vms.filem.util;

import java.util.HashMap;
import java.util.Map;

public class ThreadLock {

	private static final Map lockMap = new HashMap();

	/**
	 * 返回锁状态，并加锁
	 * @param key 机构
	 * @return true为锁定状态，false为未锁定状态
	 */
	public static boolean getLockState(String key) {
		boolean locked = true;
		synchronized (lockMap) {
			Object obj = lockMap.get(key);
			if (obj == null) {
				lockMap.put(key, Boolean.TRUE);
				locked = false;
			} else {
				locked = Boolean.parseBoolean(obj.toString());
			}
		}
		return locked;
	}
	/**
	 * 释放锁
	 * @param key 机构
	 */
	public static void releaseLock(String key){
		lockMap.put(key, Boolean.FALSE);
	}
	
//	public static void main(String[] args) {
//		new Test1().start();
//		new Test2().start();
//	}

}
