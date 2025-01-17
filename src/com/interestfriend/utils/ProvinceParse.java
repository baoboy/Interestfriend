package com.interestfriend.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.interestfriend.data.City;
import com.interestfriend.data.Province;

public class ProvinceParse {

	private static final String SPLIT_REGEX = ",";

	private Context mContext;

	private int province_id;

	private int cities_id;

	private List<Province> provinces;

	private ProvinceParse() {
	}

	public static ProvinceParse build(Context mContext, int province_id,
			int cities_id) {
		final ProvinceParse parse = new ProvinceParse();
		parse.mContext = mContext;
		parse.province_id = province_id;
		parse.cities_id = cities_id;
		parse.parse();
		return parse;
	}

	/**
	 * parse from file
	 */
	private void parse() {
		try {
			parseProvince();
			final List<City> cities = parseCity();
			List<City> tempCities = null;
			for (Province province : provinces) {
				tempCities = new ArrayList<City>();
				for (City city : cities) {
					if (city.getProvince_code() == province.getCode()) {
						tempCities.add(city);
					}
				}
				province.setCities(tempCities);
			}
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parseProvince() throws IOException {
		final List<String> strings = readLine(mContext, province_id);
		provinces = new ArrayList<Province>();
		Province province = null;
		String[] splitstr = null;
		for (String str : strings) {
			splitstr = splitLine(str, SPLIT_REGEX);
			if (splitstr.length == 3) {
				province = new Province(splitstr[0], splitstr[2],
						Integer.parseInt(splitstr[1]));
				provinces.add(province);
			}

		}
	}

	private List<City> parseCity() throws IOException {
		final List<String> strings = readLine(mContext, cities_id);
		final List<City> cities = new ArrayList<City>();
		City city = null;
		String[] splitstr = null;
		for (String str : strings) {
			splitstr = splitLine(str, SPLIT_REGEX);
			if (splitstr.length == 6) {
				city = new City(splitstr[1], Integer.parseInt(splitstr[0]),
						Integer.parseInt(splitstr[2]),
						Integer.parseInt(splitstr[3]));
				cities.add(city);
			}
		}
		return cities;
	}

	/**
	 * Getter of provinces
	 * 
	 * @return the provinces
	 */
	public List<Province> getProvinces() {
		return provinces;
	}

	private static String[] splitLine(String str, String regex) {
		return str.split(regex);
	}

	/**
	 * read file by read line
	 * 
	 * @param mContext
	 * @param id
	 * @return
	 * @throws IOException
	 */
	private static List<String> readLine(Context mContext, int id)
			throws IOException {
		final InputStream in = mContext.getResources().openRawResource(id);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				in, "GBK"));
		final List<String> strings = new ArrayList<String>();
		String line = null;
		while (null != (line = reader.readLine())) {
			strings.add(line);
		}
		reader.close();
		return strings;
	}

}
