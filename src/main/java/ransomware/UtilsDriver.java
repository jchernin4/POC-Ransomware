package ransomware;

import ransomware.exceptions.CryptoException;

import java.io.File;

public class UtilsDriver {
    public static void main(String[] args) {
        String key = "1234567890QWERTYUIOPASDFGHJKLZXC";
        //File inputFile = new File("testJavaFile.java");
        //File encryptedFile = new File("testJavaFile.encrypted");
        //File decryptedFile = new File("testJavaFile.decrypted");

        File folder = new File("./testfiles/");
        File[] files = folder.listFiles();
        
        for (File inputFile : files) {
            System.out.println(inputFile.getName());
        }
        /*for (File inputFile : files) {
            File encryptedFile = new File(inputFile.getName() + ".encrypted");
            File decryptedFile = new File(inputFile.getName() + ".decrypted");
            try {
                Utils.encrypt(key, inputFile, encryptedFile);
                Utils.decrypt(key, encryptedFile, decryptedFile);
            } catch (CryptoException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }*/
    }
}