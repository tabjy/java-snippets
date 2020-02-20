package com.tabjy.snippets.net;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class DownOrJustMe {
	private static final int PING_TIMEOUT = 5000;
	private static final int HTTP_TIMEOUT = 10000;

	private static final String[] DISALLOWED_SUBNETS = new String[] {"10.0.0.0/8"};

	private static final String HTTP_UA = "Mozilla/5.0 (X11; Fedora; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36";

	public static void main(String[] args) throws IOException {
		Scanner kbd = new Scanner(System.in);
		while (true) {
			check(kbd.nextLine());
		}
	}

	public static void check(String hostnameOrUrl) {
		InetAddress destination = null;
		try {
			destination = getHostNetAddress(hostnameOrUrl);
		} catch (UnknownHostException e) {
			System.err.println("Nope, it's not just you. The hostname is not resolvable from here, too!");
			return;
		}

		try {
			validateDestination(destination);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			return;
		}

		URL url = null;
		try {
			url = new URL(hostnameOrUrl);
		} catch (MalformedURLException e) {
			try {
				url = new URL("http", hostnameOrUrl, 80, "/");
			} catch (MalformedURLException ex) {
				System.err.println("That was nonsense!");
				return;
			}
		}

		int status = httpGet(url);
		if (status >= 400) {
			System.err.println("It responded with a non-2xx status code (" + status + "), but the host is up.");
			return;
		} else if (status != -1) {
			System.err.println("LGTM! It's just you.");
			return;
		}

		if (icmpPing(destination)) {
			System.err.println("Not sure about web services, but the host is definitely up.");
		} else {
			System.err.println("Nope, it's not just you  The host is down from here, too!");
		}
	}

	public static InetAddress getHostNetAddress(String hostnameOrUrl) throws UnknownHostException {
		try {
			return InetAddress.getByName(new URL(hostnameOrUrl).getHost());
		} catch (MalformedURLException e) {
			// noop
		}

		return InetAddress.getByName(hostnameOrUrl);
	}

	public static void validateDestination(InetAddress destination) {
		if (destination.isMulticastAddress()) {
			throw new IllegalArgumentException("A multicast address? Seriously? Stop flooding the network already.");
		}

		if (destination.isLoopbackAddress()) {
			throw new IllegalArgumentException("You wouldn't think I was that vulnerable, right?");
		}

		for (String subnet : DISALLOWED_SUBNETS) {
			InetAddress id = null;
			try {
				id = InetAddress.getByName(subnet.split("/")[0]);
			} catch (UnknownHostException e) {
				throw new RuntimeException("Check DISALLOWED_SUBNETS constant validity!");
			}
			int maskLength = Integer.parseInt(subnet.split("/")[1]);

			byte[] idBytes = id.getAddress();
			byte[] destinationBytes = destination.getAddress();
			if (idBytes.length != destinationBytes.length) {
				continue; // Don't match between IPv4 and IPv6
			}

			if (new BigInteger(idBytes).shiftRight(idBytes.length * 8 - maskLength)
					.equals(new BigInteger(destinationBytes).shiftRight(destinationBytes.length * 8 - maskLength))) {
				throw new IllegalArgumentException("Ah-ha! Destinations in subnet " + subnet + " is not allowed.");
			}
		}
	}

	public static boolean icmpPing(InetAddress destination) {
		try {
			return destination.isReachable(PING_TIMEOUT);
		} catch (IOException e) {
			return false;
		}
	}

	public static int httpGet(URL url) {
		try {
			HttpURLConnection httpClient = (HttpURLConnection) url.openConnection();
			httpClient.setConnectTimeout(HTTP_TIMEOUT);
			httpClient.setReadTimeout(HTTP_TIMEOUT);
			httpClient.setRequestMethod("GET");
			httpClient.setRequestProperty("User-Agent",
					HTTP_UA); // Some sites check for this and returns non-2xx status. Ridiculous!
			return httpClient.getResponseCode();
		} catch (IOException e) {
			return -1;
		}
	}
}