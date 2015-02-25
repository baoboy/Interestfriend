/**
 * @(#) Province.java Created on 2012-6-13
 *
 * Copyright (c) 2012 Aspire. All Rights Reserved
 */
package com.interestfriend.data;

import java.util.List;

/**
 * The class <code>Province</code>
 * 
 * @author ouyangfeng
 * @version 1.0
 */
public class Province {

	/**
	 * province name
	 */
	private String name;

	/**
	 * province code
	 */
	private int code;

	private String name_key = "";
	/**
	 * province have cities
	 */
	private List<City> cities;

	public Province(String name, String name_key, int code) {
		super();
		this.name = name;
		this.name_key = name_key;
		this.code = code;
	}

	public Province(String name, String name_key, int code, List<City> cities) {
		super();
		this.name = name;
		this.name_key = name_key;
		this.code = code;
		this.cities = cities;
	}

	/**
	 * Getter of name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter of name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getName_key() {
		return name_key;
	}

	public void setName_key(String name_key) {
		this.name_key = name_key;
	}

	/**
	 * Getter of code
	 * 
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Setter of code
	 * 
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * Getter of cities
	 * 
	 * @return the cities
	 */
	public List<City> getCities() {
		return cities;
	}

	/**
	 * Setter of cities
	 * 
	 * @param cities
	 *            the cities to set
	 */
	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
}
