package com.ambbarees.shrink.utils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.security.crypto.codec.Hex;

public class utils {

	static MessageDigest digest;

	public static void init() {
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static String Hash(String value) {
		final byte[] hashbytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
		String sha3_256hex = new String(Hex.encode(hashbytes));
		return sha3_256hex;
	}

	public static String randomString() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
	}

	public static boolean isValidURL(String url) {
		try {
			new URL(url);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
