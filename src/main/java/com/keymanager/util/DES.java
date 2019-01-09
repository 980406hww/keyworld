package com.keymanager.util;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DES {
    public static String vncPasswordEncode(String rawStr) {
         //待加密内容
         //密码，长度要是8的倍数
         byte[] password = {(byte) 232,74,(byte) 214, 96, (byte) 196, 114, 26, (byte) 224};
         byte[] result = DES.encrypt(rawStr.getBytes(),password);
//         System.out.println("加密后："+new String(result));
         StringBuffer resultStr = new StringBuffer();
         for(byte b:result) {
        	 resultStr.append(String.format("%02x", new Integer(b & 0xff)));
         }
         return resultStr.substring(0,16);
//         //解密
//         try {
//                 byte[] decryResult = DES.decrypt(result, password);
//                 System.out.println("解密后："+new String(decryResult));
//         } catch (Exception e1) {
//                 e1.printStackTrace();
//         }
    }
    /**
     * 加密
     * @param datasource byte[]
     * @param password String
     * @return byte[]
     */
    public static  byte[] encrypt(byte[] datasource, byte[] password) {            
        try{
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(password);
        //创建一个密匙工厂，然后用它把DESKeySpec转换成
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        //Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");
        //用密匙初始化Cipher对象,ENCRYPT_MODE用于将 Cipher 初始化为加密模式的常量
        cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
        //现在，获取数据并加密
        //正式执行加密操作
        return cipher.doFinal(datasource); //按单部分操作加密或解密数据，或者结束一个多部分操作
        }catch(Throwable e){
                e.printStackTrace();
        }
        return null;
}
    /**
     * 解密
     * @param src byte[]
     * @param password String
     * @return byte[]
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, byte[] password) throws Exception {
            // DES算法要求有一个可信任的随机数源
            SecureRandom random = new SecureRandom();
            // 创建一个DESKeySpec对象
            DESKeySpec desKey = new DESKeySpec(password);
            // 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");//返回实现指定转换的 Cipher 对象
            // 将DESKeySpec对象转换成SecretKey对象
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正开始解密操作
            return cipher.doFinal(src);
        }
}
