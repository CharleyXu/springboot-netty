package com.xu.springbootnetty.handler;

import com.xu.springbootnetty.server.HttpRequestHandler;

import java.net.URISyntaxException;
import java.net.URL;


public class Demo {
	public static void main(String[] args) throws URISyntaxException {
		URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
		String string = location.toURI().toString();
		System.out.println(string);
	}
}
