package com.cjit.gjsz.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;

public class CacheabledMap implements Cacheable{

	private Map map = null;
	public static String WEBAPP_PATH = null;

	protected CacheabledMap(){
		map = new ConcurrentHashMap();
	}

	public Collection values(){
		return map.values();
	}

	public CacheabledMap(boolean isIncreasing){
		if(isIncreasing){
			map = new ConcurrentHashMap();
		}else{
			map = new HashMap();
		}
	}

	public boolean isEmpty(){
		return map.isEmpty();
	}

	public CacheabledMap(int size){
		map = Collections
				.synchronizedMap(new org.apache.commons.collections.map.LRUMap(
						size));
	}

	public CacheabledMap(int size, int localFileStorage){
		map = Collections
				.synchronizedMap(new org.apache.commons.collections.map.LRUMap(
						size));
	}

	public Object getCacheObject(){
		return this;
	}

	public Object get(Object key){
		return map.get(key);
	}

	public Object put(Object key, Object value){
		return map.put(key, value);
	}

	public void init(){
		// FixedSizeMapPool only permit the 10 cache.
	}

	public void clear(){
		map.clear();
	}

	public boolean containsKey(Object key){
		return map.containsKey(key);
	}

	public Object remove(Object key){
		return map.remove(key);
	}

	public int size(){
		return map.size();
	}

	public boolean containsValue(Object value){
		return map.containsValue(value);
	}

	public Set keySet(){
		return map.keySet();
	}
}
