package com.cjit.gjsz.cache;

public interface Cacheable{

	public void init();

	public void clear();

	public Object getCacheObject();
}
