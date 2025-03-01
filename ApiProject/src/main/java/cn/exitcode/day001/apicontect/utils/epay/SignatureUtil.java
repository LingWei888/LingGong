package cn.exitcode.day001.apicontect.utils.epay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

@Component
public class SignatureUtil {

    private String key;

    public SignatureUtil(@Value("${epay.key}") String key) {
        this.key = key;
    }

    public String getSign(Map<String, String> param) {
        // 对参数进行字典序排序
        TreeMap<String, String> sortedParams = new TreeMap<>(param);
        StringBuilder signstr = new StringBuilder();

        // 拼接参数字符串
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (!"sign".equals(k) && !"sign_type".equals(k) && v != null && !v.isEmpty()) {
                signstr.append(k).append("=").append(v).append("&");
            }
        }

        // 去掉最后一个'&'字符
        if (signstr.length() > 0) {
            signstr.setLength(signstr.length() - 1);
        }

        // 拼接密钥
        signstr.append(this.key);

        // 计算MD5签名
        return md5(signstr.toString());
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }


}