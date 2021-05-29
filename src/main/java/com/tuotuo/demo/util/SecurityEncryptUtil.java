package com.tuotuo.demo.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.HashMap;
import java.util.Map;

public class SecurityEncryptUtil {

	public static void main(String[] args) {
		Map<String, String> keys = createKeys(2048);
		System.out.println(JSON.toJSONString(keys));
	}
	/**
	 * 加密
	 * @param origData
	 * @param publicKey
	 * @param transformation
	 * @return
	 */
	public static String encrypt(String origData, PublicKey publicKey, String transformation) {
		try {
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			
			byte[] output = cipher.doFinal(origData.getBytes(Constant.UTF8));
			
			byte[] baseEncodedBytes = Base64.encodeBase64(output);
			
			return new String(baseEncodedBytes,Constant.UTF8);
		} catch (Exception e) {
			throw new RuntimeException("encrypt fail!", e);
		}
	}
    /**
     * 解密
     * @param encryptedData
     * @param privateKey
     * @param transformation
     * @return
     */
    public static String decrypt(String encryptedData, PrivateKey privateKey, String transformation){  
        try {  
        	Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] encryptedBytes = Base64.decodeBase64(encryptedData.getBytes());
			byte[] output =  cipher.doFinal(encryptedBytes);
			return new String(output,Constant.UTF8);
        } catch (Exception e) {
			throw new RuntimeException("decrypt fail!", e);
		}    
    }
	/**
	 * 加密
	 * @param data
	 * @param key
	 * @param algorithm 算法
	 * @return
	 */
	public static byte[] encrypt(byte[] data, byte[] key, String algorithm) {
		if(data == null){
			throw new RuntimeException("data can not be null");
		}
		if(key == null){
			throw new RuntimeException("data can not be null");
		}
		if(key.length!=16){
			throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
		}
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, Constant.AES_ALGORITHM);
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, Constant.AES_ALGORITHM);
			Cipher cipher = Cipher.getInstance(algorithm);// 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, seckey);// 初始化
			return cipher.doFinal(data); // 加密
		} catch (Exception e){
			throw new RuntimeException("encrypt fail!", e);
		}
	}
	/**
	 * 用ASE方式加密后转base64
	 * @param data
	 * @param key
	 * @param algorithm 算法
	 * @return
	 */
	public static String encryptByAES(String data, String key, String algorithm){
		try {
			byte[] valueByte = encrypt(data.getBytes(Constant.UTF8), key.getBytes(Constant.UTF8),algorithm);
			return new String(Base64.encodeBase64(valueByte));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("encrypt fail!", e);
		}
	}
	/**
	 * 解密
	 * @param data
	 * @param key
	 * @param algorithm 算法
	 * @return
	 */
	public static byte[] decrypt(byte[] data, byte[] key,String algorithm) {
		if(data == null){
			throw new RuntimeException("data can not be null");
		}
		if(key == null){
			throw new RuntimeException("data can not be null");
		}
		if(key.length!=16){
			throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
		}
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, Constant.AES_ALGORITHM);
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, Constant.AES_ALGORITHM);
			Cipher cipher = Cipher.getInstance(algorithm);// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, seckey);// 初始化
			return  cipher.doFinal(data);
		} catch (Exception e){
			throw new RuntimeException("decrypt fail!", e);
		}
	}

	/**
	 * 用ASE方式进行解密
	 * @param data
	 * @param key
	 * @param algorithm
     * @return
     */
	public static String decryptByAES(String data, String key, String algorithm){
		try {
			byte[] originalData = Base64.decodeBase64(data.getBytes());
			byte[] valueByte = decrypt(originalData, key.getBytes(Constant.UTF8),algorithm);
			return new String(valueByte, Constant.UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("decrypt fail!", e);
		}
	}

	private static Map<String, String> createKeys(int keySize) {
		//为RSA算法创建一个KeyPairGenerator对象
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance(Constant.RSA_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm-->[" + Constant.RSA_ALGORITHM + "]");
		}
		//初始化KeyPairGenerator对象,密钥长度
		kpg.initialize(keySize);
		//生成密匙对
		KeyPair keyPair = kpg.generateKeyPair();
		//得到公钥
		Key publicKey = keyPair.getPublic();
		String publicKeyStr = null;
		try {
			publicKeyStr = new String(Base64.encodeBase64(publicKey.getEncoded()), Constant.UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("get publicKeyStr error:", e);
		}
		//得到私钥
		Key privateKey = keyPair.getPrivate();
		String privateKeyStr = null;
		try {
			privateKeyStr = new String(Base64.encodeBase64(privateKey.getEncoded()), Constant.UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("get privateKeyStr error:", e);
		}
		Map<String, String> keyPairMap = new HashMap<String, String>();
		keyPairMap.put("publicKey", publicKeyStr);
		keyPairMap.put("privateKey", privateKeyStr);
		return keyPairMap;
	}
}
