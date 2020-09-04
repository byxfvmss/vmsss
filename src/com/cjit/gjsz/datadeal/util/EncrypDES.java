package com.cjit.gjsz.datadeal.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cjit.crms.util.StringUtil;

public class EncrypDES{

	private SecretKey deskey;
	private Cipher c;
	private final String ENCODE = "UTF-8";
	private final String KEY = "CHINAJESITFMSS";
	private static final EncrypDES single = new EncrypDES();
	private transient Log log = LogFactory.getLog(this.getClass());

	private EncrypDES(){
		try{
			c = Cipher.getInstance("DES");
			DESKeySpec desKeySpec = new DESKeySpec(KEY.getBytes(ENCODE));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			deskey = keyFactory.generateSecret(desKeySpec);
		}catch (Exception e){
			log.error(e);
		}
	}

	public static EncrypDES getInstance(){
		return single;
	}

	/**
	 * 对字符串加密
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public byte[] Encrytor(byte[] src) throws Exception{
		c.init(Cipher.ENCRYPT_MODE, deskey);
		return c.doFinal(src);
	}

	public String Encrytor(String str){
		try{
			if(StringUtil.IsEmptyStr(str))
				str = "";
			return new String(Base64
					.encodeBase64(Encrytor(str.getBytes(ENCODE))), ENCODE);
		}catch (Exception e){
			log.error(e);
			return "";
		}
	}

	/**
	 * 对字符串解密
	 * @param buff
	 * @return
	 * @throws Exception
	 */
	public byte[] Decryptor(byte[] buff) throws Exception{
		c.init(Cipher.DECRYPT_MODE, deskey);
		return c.doFinal(buff);
	}

	public String Decryptor(String str){
		try{
			if(StringUtil.IsEmptyStr(str))
				str = "";
			return new String(Decryptor(Base64.decodeBase64(str
					.getBytes(ENCODE))), ENCODE);
		}catch (Exception e){
			log.error(e);
			return "";
		}
	}

	public static void main(String[] args) throws Exception{
		EncrypDES de1 = EncrypDES.getInstance();
		String msg = "如果我输入的是汉字呢";
		String encontent = de1.Encrytor(msg);
		String decontent = de1.Decryptor(encontent);
		System.out.println("明文是:" + msg);
		System.out.println("加密后:" + encontent);
		System.out.println("解密后:" + decontent);
	}
}
