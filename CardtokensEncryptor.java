import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class CardtokensEncryptor {
    public static String Encrypt(String originalString, String pubKeyPEM) throws Exception {
        String pubKey = pubKeyPEM.replace("-----BEGIN PUBLIC KEY-----\n", "")
                                 .replace("-----END PUBLIC KEY-----", "")
                                 .replaceAll("\\s", ""); // remove whitespaces

        byte[] publicKeyBytes = Base64.getDecoder().decode(pubKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);

        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] secretMessageBytes = originalString.getBytes("UTF-8");
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);

        return Base64.getEncoder().encodeToString(encryptedMessageBytes);
    }
}