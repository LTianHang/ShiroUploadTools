package Util;


import java.util.Base64;
//

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;


public class AESUtils {

    /**
     * 实现一个加密分类器 由工具中的加密值确定用什么类型的加密
     *
     * @param plainText
     * @param key
     * @param AESType
     * @throws Exception
     */
    public static byte[] AESEncodeType(byte[] plainText, String key, String AESType) throws Exception {
        if (AESType.equals("CBC")) {
            return NoShiroCBCencrypt(plainText, key);
        } else if (AESType.equals("GCM")) {
            return NoShiroGCMencrypt(plainText, key);
        }
        return null;
    }

    /**
     * 不需要Shiro依赖的CBC加密 低版本Shiro加解密不需要iv值
     *
     * @param plainText
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] NoShiroCBCencrypt(byte[] plainText, String key) throws Exception {
        byte[] typeKey = Base64.getDecoder().decode(key);
        SecretKeySpec secretKey = new SecretKeySpec(typeKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[cipher.getBlockSize()]; // 获取块大小作为随机向量的长度
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] cipherText = cipher.doFinal(plainText);

        // 将随机向量和密文合并
        byte[] result = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(cipherText, 0, result, iv.length, cipherText.length);

        return result;
    }

    /**
     * 不需要Shiro依赖的GCM加密 高版本为GCM加密
     *
     * @param plainText
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] NoShiroGCMencrypt(byte[] plainText, String key) throws Exception {

        byte[] typeKey = Base64.getDecoder().decode(key);
        SecretKeySpec secretKey = new SecretKeySpec(typeKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[12]; // 生成12字节的随机向量
        random.nextBytes(iv);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        byte[] cipherText = cipher.doFinal(plainText);

        // 将随机向量和密文合并
        byte[] result = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(cipherText, 0, result, iv.length, cipherText.length);

        return result;
    }


//    /**
//     * 低版本CBC加密序列化数据
//     *
//     * @param serialized
//     * @param key
//     * @return
//     */
//    public static byte[] CBCencrypt(byte[] serialized, String key) {
//        byte[] value = serialized;
//        CipherService cipherService = new AesCipherService();
//
//        if (cipherService != null) {
//            ByteSource byteSource = cipherService.encrypt(serialized, Base64.decode(key));
//            value = byteSource.getBytes();
//        }
//        return value;
//    }


}
