package edu.csci.shiftyencryption;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class is the entire API that allows for both encryption and decryption
 * of any file or arbitrary string value.
 *
 * 09-2014
 *
 * @author William
 */
public class ShiftyCipher {

    private Cipher ecipher;
    private Cipher dcipher;

    private SecretKey key;

    public ShiftyCipher() {
        try {
            char[] psswd = {'$', '$', '5', '5', '$', 'K', ' ', ';', '+', '-', '$', '$', '$', '<', '<', '<'};
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(psswd, ("(DB!l3;D").getBytes(), 65536, 128);
            SecretKey tmp = factory.generateSecret(spec);
            key = new SecretKeySpec(tmp.getEncoded(), "AES");
            try {
                ecipher = Cipher.getInstance("AES");
                dcipher = Cipher.getInstance("AES");

                ecipher.init(Cipher.ENCRYPT_MODE, key);
                dcipher.init(Cipher.DECRYPT_MODE, key);

            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
                e.printStackTrace(System.err);
            }

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * This method encrypts a given string value using our pre-built cipher from
     * the default constructor.
     *
     * @param str the string value to be encrypted.
     * @return the encrypted string.
     */
    public String encrypt(String str) {
        try {
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);

            enc = BASE64EncoderStream.encode(enc);

            return new String(enc);
        } catch (UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

    /**
     * This method decrypts a single string value using the pre-build decipher
     * in the constructor.
     *
     * @param str the single string value to be decryped.
     * @return the decrypted string.
     */
    public String decrypt(String str) {
        try {
            byte[] dec = BASE64DecoderStream.decode(str.getBytes());
            byte[] utf8 = dcipher.doFinal(dec);

            return new String(utf8, "UTF8");
        } catch (UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace(System.out);
        }
        return null;
    }
}
