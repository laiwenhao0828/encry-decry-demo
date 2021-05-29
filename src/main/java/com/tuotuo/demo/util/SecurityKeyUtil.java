package com.tuotuo.demo.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

public class SecurityKeyUtil {

	private static Logger log = LoggerFactory.getLogger(TuoTuoManageFacade.class);
	/**
	 * 根据字符串获取公钥
	 * 
	 * @param key
	 * @param algorithm
	 * @return
	 */
	public static PublicKey getPubKeyByString(String key,String algorithm) {
		PublicKey publicKey = null;
		try {
			byte[] keybytes = Base64.decodeBase64(key.getBytes(Constant.UTF8));
			KeyFactory kf = KeyFactory.getInstance(algorithm);
			X509EncodedKeySpec keySpec1 = new X509EncodedKeySpec(keybytes);
			publicKey = kf.generatePublic(keySpec1);
		} catch (IOException iOException) {
			log.error("SecurityKeyUtil.getPubKeyByString IOException:",iOException);
		} catch (InvalidKeySpecException invalidKeySpecException) {
			log.error("SecurityKeyUtil.getPubKeyByString InvalidKeySpecException:",invalidKeySpecException);
		} catch (NoSuchAlgorithmException noSuchAlgorithmException) {
			log.error("SecurityKeyUtil.getPubKeyByString NoSuchAlgorithmException:",noSuchAlgorithmException);
		}
		return publicKey;
	}

	/**
	 * 根据字符串获取私钥
	 * 
	 * @param key
	 * @param algorithm
	 * @return
	 */
	public static PrivateKey getPriKeyByString(String key,String algorithm) {
		PrivateKey privateKey = null;
		try {
			byte[] keybytes = Base64.decodeBase64(key.getBytes(Constant.UTF8));
			KeyFactory kf = KeyFactory.getInstance(algorithm);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keybytes);
			privateKey = kf.generatePrivate(keySpec);// 自己的私钥
		} catch (NoSuchAlgorithmException noSuchAlgorithmException) {
			log.error("SecurityKeyUtil.getPriKeyByString NoSuchAlgorithmException:",noSuchAlgorithmException);
		} catch (IOException iOException) {
			log.error("SecurityKeyUtil.getPriKeyByString IOException:",iOException);
		} catch (InvalidKeySpecException invalidKeySpecException) {
			log.error("SecurityKeyUtil.getPriKeyByString InvalidKeySpecException:",invalidKeySpecException);
		}
		return privateKey;
	}

	/**
	 * 生成16位AESKey
	 * @return
     */
	public static String createAesKey(){
		String s = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		Random r = new Random();
		StringBuilder result= new StringBuilder();
		while (result.length()<16){
			int n = r.nextInt(62);
			result.append(s.charAt(n));
		}
		return result.toString();
	}
}
