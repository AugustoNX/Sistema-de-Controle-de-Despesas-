package util;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.List;

public class Utils {

    public static List<String> readAllLines(File f) throws IOException {
        return Files.readAllLines(f.toPath());
    }

    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static long readLongSafe(java.util.Scanner sc) {
        while (!sc.hasNextLong()) {
            sc.next();
            System.out.print("Digite um número válido: ");
        }
        return sc.nextLong();
    }

    public static double readDoubleSafe(java.util.Scanner sc) {
        while (!sc.hasNextDouble()) {
            sc.next();
            System.out.print("Digite um número válido: ");
        }
        return sc.nextDouble();
    }

    public static String readLineSkip(java.util.Scanner sc) {
        String s = sc.nextLine();
        if (s.isEmpty()) s = sc.nextLine();
        return s;
    }
}
