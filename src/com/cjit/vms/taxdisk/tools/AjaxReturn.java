package com.cjit.vms.taxdisk.tools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AjaxReturn implements Serializable {

	private boolean isNormal;
	private String message;
	private Map<String, Object> attributes;

	public AjaxReturn() {

	}

	public AjaxReturn(boolean isNormal) {
		this.isNormal = isNormal;
	}
	
	public AjaxReturn(boolean isNormal, String message) {
		this.isNormal = isNormal;
		this.message = message;
	}

	public boolean getIsNormal() {
		return isNormal;
	}

	public void setIsNormal(boolean isNormal) {
		this.isNormal = isNormal;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public void putAttribute(String key, Object attribute) {
		if (attributes == null) {
			attributes = new HashMap<String, Object>();
		}
		attributes.put(key, attribute);
	}

}
