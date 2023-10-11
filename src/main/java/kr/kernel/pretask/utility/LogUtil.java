package kr.kernel.pretask.utility;

public class LogUtil {
    public static void printError(String errorMessage, Exception e) {
        System.out.println(errorMessage + ": " + e.toString());
    }
}
