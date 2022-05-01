package com.example.demo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;
import java.util.stream.Collectors;

import com.example.demo.exception.TradeException;

public class TradeUtil {

	public static String readFile(String fileName, Charset charset) throws TradeException {
		InputStream inputStream = TradeUtil.class.getResourceAsStream("/" + fileName);
		String result = fileName;
		try {
			if (null != inputStream) {
				try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
					result = buffer.lines().collect(Collectors.joining(System.lineSeparator()));
				}
			} else {
				File file = new File(fileName);
				if (file.exists()) {
					result = new String(Files.readAllBytes(file.toPath()), charset);
				} else {
					String error = "File Does Not Exist at Given Location";
					System.out.println(error);
					throw new TradeException(error + ":" + fileName);
				}
			}
		} catch (Exception e) {
			String error = "Exception in readFile()";
			System.out.println(error);
			throw new TradeException(error + ":" + e);
		}

		return result;
	}

	public static boolean isEmpty(Object obj) {
		return !isNotEmpty(obj);
	}

	public static boolean isNotEmpty(Object obj) {
		boolean flag = true;
		if (obj == null) {
			flag = false;
		} else {
			if (obj instanceof String) {
				String objString = (String) obj;
				if (objString.trim().length() == 0) {
					flag = false;
				}
			}
			if (obj instanceof Collection<?>) {
				Collection<?> objColl = (Collection<?>) obj;
				if (objColl.isEmpty()) {
					flag = false;
				}
			}
		}
		return flag;
	}
}
