package com.tabjy.mymodule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListResources {
	public static void main(String[] args) throws IOException {
		URL url = ListResources.class.getResource("jfrprobes.xml");
		System.out.println(url);
//		System.out.println(getResourceFiles("/"));
		
//		Package.getPackage("com.tabjy.mymodule").
	}

//	private static List<String> getResourceFiles(String path) throws IOException {
//		List<String> filenames = new ArrayList<>();
//
//		try (InputStream in = getResourceAsStream(path); 
//				BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
//			String resource;
//
//			while ((resource = br.readLine()) != null) {
//				filenames.add(resource);
//			}
//		}
//
//		return filenames;
//	}
//
//	private static InputStream getResourceAsStream(String resource) {
//		final InputStream in = getContextClassLoader().getResourceAsStream(resource);
//
//		return in == null ? ListResources.class.getResourceAsStream(resource) : in;
//	}
//
//	private static ClassLoader getContextClassLoader() {
//		return Thread.currentThread().getContextClassLoader();
//	}
}
