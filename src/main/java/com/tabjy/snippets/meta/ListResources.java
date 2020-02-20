package com.tabjy.snippets.meta;

import java.net.URL;

public class ListResources {
	public static void main(String[] args) {
		URL url = ListResources.class.getResource("/");
		System.out.println(url);
	}
}
