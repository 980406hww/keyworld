package com.keymanager.util;

        import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
        import java.nio.charset.StandardCharsets;
        import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AESTest {

    public static void main(String args[]) throws UnsupportedEncodingException, InvalidAlgorithmParameterException{
        System.out.println(System.getProperty("file.encoding"));

        //加密内容
        String content = "密码学中的高级加密标准（AdvancedEncryptionStandard，AES）。";
        //String content = "test123456";
        //为与Delphi编码统一，将字符转为UTF8编码（其他语言也相同）
        //String ss=new String(content.getBytes(),"UTF-8");
        //密钥
        String password = "1234567890123456";
        System.out.println("加密前：" + content);
        byte[] encryptResult = encrypt(content, password,16); //16位密钥长度128位、24位密钥长度192、32位密钥长度256（在delphi中对应kb128、kb192、快播56）
        System.out.println("加密后：" + parseByte2HexStr(encryptResult));//将加密后编码转为16进制编码

        String myKey = "ss-kj-1234569999";
        encryptResult = parseHexStr2Byte("29dd051cc89d462469bfbac26cb2261da1f988103193b0b9fa007300abaa95bf9d025328681e58c8e2903c5cc55059da8f0164c04d6a0b3b2badc6455f6f9be34c5bf1e5a75ddb2f7357d0a39a779a0c8f87b8dc1cbe565d52ff7d66358895c28aa508ef417012c6331e676379008c3ab8cfef60761bb24fea9fbcfcc76609c232edf89110eab912544be6beeced00ee2501b7f103d196aef52c9c017751cc36dab80ddceaff7135237ae7c522d97e7f58ce2f9d17a70fb52b6f53844be4e4c4d6805c9d009aa45dc3bc02d78deb2c1ff8cb71107ebdc6f894e127f7c3ce4ab4e1d983a87deb9f1a952d65abb94a2f34244e87f33ee63d741d3d5fcdc5b24818cc28b5822270914b8199d920efd92c4a08aaba41a1fe457caaf9cee9019ff85dd1feb9f86f528f60673d217ba8908d90f67bb1167b0b4fc6f5c4a1d8e181e2bbe0b607b0cf1dd992e7ec9cc378910e9aa4c9c90e7da14c19170925e2f851e83c1a06d71c7077890d13037961805f1b9aa7edd0a6bdc95e8fd246c5024bc3624ac434ae9ecedec1d25043c9f03962c8524ff062f2347a6489df833e1593d5a08af4e830f2050ef5fc19f9e0c4721ad00665b19efa8e26b6750f52bbe37825a05081b49e41c9250b98c62c8a8dbcc29a5894402dc98825cb7dc9fe853ede81c9a4094634520a7af5399cd97e75193c843da7f32ed27e51164a3866b7c946c6bdca90ff213ac51e96c38278199d53c725dcf927e77021d0b29f023a513f696665215af9c984bbcde110565425850b8e139c070cdfc6817c2679ced917140890854f4dfc0c284a9d15c1eb919967d1f92a382ab3f4713c731d6a766d851fe16d0a9ad3a681e2b8a87e083076cb103796d7084ee523e71102b8288b6d9a9e1496c4c66d9a39cbff963f4d731c4bea88163cc70f98fceca90cfa69e2308099b42c0d347be0e966b5655e6943458beabb5f3e77a3d2cd7279afe036f1fddbb999d5e6aac12464312434d8d5a7ed13e2e3140f4b89ec275f894f2509a44535b2dd5dfae373a52d2638ae51be54a586df3d27b47e");
        byte[] decryptResult = decrypt(encryptResult,myKey,16);
        System.out.println("解密后：" + new String(decryptResult));
    }


    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param password  加密密码
     * @param keySize 密钥长度16,24,32
     * @return
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     */

    public static byte[] encrypt(String content, String password, int keySize) throws UnsupportedEncodingException, InvalidAlgorithmParameterException {
        try {
            //密钥长度不够用0补齐。
            SecretKeySpec key = new SecretKeySpec(ZeroPadding(password.getBytes(), keySize), "AES");
            //定义加密算法AES、算法模式ECB、补码方式PKCS5Padding
            //Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            //定义加密算法AES 算法模式CBC、补码方式PKCS5Paddingt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //CBC模式模式下初始向量 不足16位用0补齐
            IvParameterSpec iv = new IvParameterSpec(ZeroPadding("1234567890123456".getBytes(),16));
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            //初始化加密
            //ECB
            //cipher.init(Cipher.ENCRYPT_MODE, key);
            //CBC
            cipher.init(Cipher.ENCRYPT_MODE, key,iv);
            byte[] result = cipher.doFinal(byteContent);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**解密
     * @param content  待解密内容
     * @param password 解密密钥
     * @param keySize 密钥长度16,24,32
     * @return
     * @throws InvalidAlgorithmParameterException
     */
    public static byte[] decrypt(byte[] content, String password, int keySize) throws InvalidAlgorithmParameterException {
        try {
            //密钥长度不够用0补齐。
            SecretKeySpec key = new SecretKeySpec(ZeroPadding(password.getBytes(), keySize), "AES");
            //定义加密算法AES、算法模式ECB、补码方式PKCS5Padding
            //Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            //定义加密算法AES 算法模式CBC、补码方式PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //CBC模式模式下初始向量 不足16位用0补齐
            IvParameterSpec iv = new IvParameterSpec(ZeroPadding("1234567890123456".getBytes(),16));
            // 初始化解密
            //ECB
            //cipher.init(Cipher.DECRYPT_MODE, key);
            //CBC
            cipher.init(Cipher.DECRYPT_MODE, key,iv);

            byte[] result = cipher.doFinal(content);
            return result;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static byte[] ZeroPadding(byte[] in,Integer blockSize){
        Integer copyLen = in.length;
        if (copyLen > blockSize) {
            copyLen = blockSize;
        }
        byte[] out = new byte[blockSize];
        System.arraycopy(in, 0, out, 0, copyLen);
        return out;
    }
}
