package com.keymanager.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

public class AESUtils {

    public static final String myKey = "ss-kj-1234569999";
    private static final byte[] myKeyBytes = myKey.getBytes(StandardCharsets.UTF_8);

    // 二进制转十六进制
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (byte aBuf : buf) {
            String hex = Integer.toHexString(aBuf & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    // 十六进制转二进制
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    // 加密
    public static String encrypt(Object obj) {
        try {
            // iv
            String uuid = UUID.randomUUID().toString();
            String[] a = uuid.split("-");
            String myIv = a[3] + a[4];
            byte[] myIvBytes = myIv.getBytes(StandardCharsets.UTF_8);

            // 明文
//            String data = JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

            //通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
            //Include.Include.ALWAYS 默认
            //Include.NON_DEFAULT 属性为默认值不序列化
            //Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
            //Include.NON_NULL 属性为NULL 不序列化
            String data = mapper.writeValueAsString(obj);


            Cipher cipher = Cipher.getInstance("AES/CFB8/NOPadding");
            SecretKeySpec sks = new SecretKeySpec(myKeyBytes, "AES");
            IvParameterSpec ips = new IvParameterSpec(myIvBytes);
            cipher.init(Cipher.ENCRYPT_MODE, sks, ips);
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // iv + 密文
            byte[] ivParam = myIvBytes;
            int ivLen = ivParam.length;
            ivParam = Arrays.copyOf(ivParam, ivParam.length + encrypted.length);
            System.arraycopy(encrypted, 0, ivParam, ivLen, encrypted.length);

            return parseByte2HexStr(ivParam);
        } catch (Exception e) {
            e.printStackTrace();
            return "加密异常！请及时处理";
        }
    }

    // 解密
    public static String decrypt(String data) {
        try {
            byte[] bytes = parseHexStr2Byte(data);

            byte[] ivBytes = new byte[16];
            System.arraycopy(bytes, 0, ivBytes, 0, 16);
            byte[] content = new byte[bytes.length - 16];
            System.arraycopy(bytes, 16, content, 0, bytes.length - 16);

            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            SecretKeySpec sks = new SecretKeySpec(myKeyBytes, "AES");
            IvParameterSpec ips = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.DECRYPT_MODE, sks, ips);
            byte[] result = cipher.doFinal(content);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return "解密异常！请及时处理";
        }
    }

    public static byte[] compress(byte[] inputByte) throws IOException {
        int len = 0;
        Deflater defl = new Deflater();
        defl.setInput(inputByte);
        defl.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] outputByte = new byte[1024];
        try {
            while (!defl.finished()) {
                // 压缩并将压缩后的内容输出到字节输出流bos中
                len = defl.deflate(outputByte);
                bos.write(outputByte, 0, len);
            }
            defl.end();
        } finally {
            bos.close();
        }
        return bos.toByteArray();
    }

    public static byte[] compress1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = null;
        GZIPOutputStream gzip = null;
        byte[] compress;
        try {
            out = new ByteArrayOutputStream();
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes("utf-8"));
            gzip.close();
            compress = out.toByteArray();
            return compress;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}