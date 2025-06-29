package com.example.bds.component;
import java.util.Random;

public class MaHoaDonUtil {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int DEFAULT_LENGTH = 10;
    private static final Random random = new Random();

    public static String taoMaHoaDon() {
        StringBuilder sb = new StringBuilder("HD-"); // prefix HD for "Hóa Đơn"
        for (int i = 0; i < DEFAULT_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public static String taoMaHoaDonVNP() {
        StringBuilder sb = new StringBuilder("VNPAY-"); // prefix HD for "Hóa Đơn"
        for (int i = 0; i < DEFAULT_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
