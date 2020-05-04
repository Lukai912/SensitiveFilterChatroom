package com.ubuntu.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author luowendong
 * @version 1.0
 */
public class SensitiveWordFilter extends BaseWordFilter{

	public static final SensitiveWordFilter FILTER_INSTANCE = new SensitiveWordFilter();

	/**
	 * 加载一个文件中的词典，并构建filter<br/>
	 * 文件中，每行一个敏感词条<br/>
	 * <b>注意：</b>读取完成后会调用{@link BufferedReader#close()}方法。<br/>
	 * <b>注意：</b>读取中的{@link IOException}不会抛出
	 */
	public SensitiveWordFilter(){
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				ClassLoader.getSystemResourceAsStream("cult"), StandardCharsets.UTF_8));
		addDict(reader);
		reader = new BufferedReader(new InputStreamReader(
				ClassLoader.getSystemResourceAsStream("illegalwords"), StandardCharsets.UTF_8));
		addDict(reader);
		reader = new BufferedReader(new InputStreamReader(
				ClassLoader.getSystemResourceAsStream("names"), StandardCharsets.UTF_8));
		addDict(reader);
		reader = new BufferedReader(new InputStreamReader(
				ClassLoader.getSystemResourceAsStream("political"), StandardCharsets.UTF_8));
		addDict(reader);
		reader = new BufferedReader(new InputStreamReader(
				ClassLoader.getSystemResourceAsStream("news"), StandardCharsets.UTF_8));
		addDict(reader);
		reader = new BufferedReader(new InputStreamReader(
				ClassLoader.getSystemResourceAsStream("otherSensitive"), StandardCharsets.UTF_8));
		addDict(reader);
	}

}