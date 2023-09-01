package gz.helpp.utils;



import gz.helpp.Model.modelSession;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Hash256 {
    private Hash256(){}
    public static String hash256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        }
        catch (Exception e) {
            ModelSession.getLogger().error("Model file, hash256 function error", e);
        }
        return "";
    }
}