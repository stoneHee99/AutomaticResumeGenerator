package kr.kernel.pretask.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class InputValidationController {

    public static boolean isValidPhoto(String fileName) {
        String projectRoot = System.getProperty("user.dir");
        String filePath = projectRoot + File.separator + fileName;
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    public static boolean isValidDate(String strDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            dateFormat.setLenient(false);
            dateFormat.parse(strDate);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidTokens(String[] tokens) {
        for (String token : tokens) {
            if (token.isEmpty()) return false;
        }
        return true;
    }
}
