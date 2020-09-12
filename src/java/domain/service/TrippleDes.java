package domain.service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * 
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class TrippleDes {

    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private final KeySpec ks;
    private final SecretKeyFactory skf;
    private final Cipher cipher;
    byte[] arrayBytes;
    private final String myEncryptionKey;
    private final String myEncryptionScheme;
    SecretKey key;

    public static TrippleDes instance;

    /**
     * Get an instance of the TrippleDes.
     * If instance of this class are different decryption
     * will not work correctly.
     * 
     * @return an instance of TripleDes.
     * @throws Exception 
     */
    public static TrippleDes getInstance() throws Exception {
        if (instance == null) {
            instance = new TrippleDes();
        }
        return instance;
    }

    /**
     * Configuration of the algorithm TripleDes.
     * 
     * @throws Exception 
     */
    public TrippleDes() throws Exception {
        myEncryptionKey = "!$£vaoqp%&VS=£GM_;ç@KL£fkn";
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = skf.generateSecret(ks);
    }

    /**
     * Encrypt a string passed using TripleDes algorithm.
     * 
     * @param unencryptedString: string to encrypt.
     * @return an encrypted string.
     */
    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encodeBase64(encryptedText));
        } catch (UnsupportedEncodingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    /**
     * Decrypt a string passed.
     * 
     * @param encryptedString:: string to decrypt.
     * @return a string decrypted.
     */
    public String decrypt(String encryptedString) {
        String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decodeBase64(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = new String(plainText);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

}
