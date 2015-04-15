package edu.csci.shiftyencryption;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author William
 */
public class ShiftyCipherTest {

    /**
     * Test of encrypt method, of class CipherNC.
     */
    @Test
    public void testEncryptDecryptString() {
        ShiftyCipher ciph = new ShiftyCipher();
        System.out.println("encrypting/decrypt test initialized.");
        String inString = "This is a fancy Test)(*#)(*#$@##$(*#&$&*#$&*^#$&*^#$///////8383838388hnadlf   sdlfkjsdlkfjs df  ";
        String encryptedString = ciph.encrypt(inString);
        String decryptedString = ciph.decrypt(encryptedString);
        System.out.println("This is the decrypted string: " + decryptedString);
        assertEquals(inString, decryptedString);
        inString = "This is a test to make sure that the class can handle multiple iterations...   ";
        encryptedString = ciph.encrypt(inString);
        decryptedString = ciph.decrypt(encryptedString);
        assertEquals(inString, decryptedString);
        System.out.println("This is the second decrypted string: " + decryptedString);
    }
//
//    /**
//     * Test of encryptFile method, of class CipherNC.
//     */
//    @Test
//    public void testEncryptFile() {
//        System.out.println("encryptFile");
//        File input = null;
//        CipherNC instance = new CipherNC();
//        File expResult = null;
//        File result = instance.encryptFile(input);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of decryptFile method, of class CipherNC.
//     */
//    @Test
//    public void testDecryptFile() {
//        System.out.println("decryptFile");
//        File decInput = null;
//        CipherNC instance = new CipherNC();
//        File expResult = null;
//        File result = instance.decryptFile(decInput);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of decrypt method, of class CipherNC.
//     */
//    @Test
//    public void testDecrypt() {
//        System.out.println("decrypt");
//        String str = "";
//        CipherNC instance = new CipherNC();
//        String expResult = "";
//        String result = instance.decrypt(str);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}
