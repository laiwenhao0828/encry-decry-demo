package com.tuotuo.demo.util;

import org.apache.commons.codec.binary.Base64;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class SecuritySignUtil {

	/**
	 * 签名
	 * @param origData
	 * @param key
	 * @param algorithm
	 * @return
	 */
	public static String sign(String origData,PrivateKey key,String algorithm) {
		try {
			Signature signature = Signature.getInstance(algorithm);
			signature.initSign(key);
			signature.update(origData.getBytes(Constant.UTF8));
			byte[] signedBytes = signature.sign();
			byte[] baseEncodedBytes = Base64.encodeBase64(signedBytes);
			return new String(baseEncodedBytes,Constant.UTF8);
		} catch (Exception e) {
			throw new RuntimeException("sign fail!", e);
		}
	}
	/**
	 * 验证签名
	 * @param origData
	 * @param signedData
	 * @param publicKey
	 * @param algorithm
	 * @return
	 */
	public static boolean checkSign(String origData, String signedData, PublicKey publicKey,String algorithm) {
		try {
			Signature signature = Signature.getInstance(algorithm);
			signature.initVerify(publicKey);
			signature.update(origData.getBytes(Constant.UTF8));
			byte[] baseSignedBytes = signedData.getBytes(Constant.UTF8);
			byte[] signedBytes =  Base64.decodeBase64(baseSignedBytes);
			return signature.verify(signedBytes);
		} catch (Exception e) {
			throw new RuntimeException("checkSign fail!", e);
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
