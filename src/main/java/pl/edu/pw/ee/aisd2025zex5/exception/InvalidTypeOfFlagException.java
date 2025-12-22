package pl.edu.pw.ee.aisd2025zex5.exception;

public class InvalidTypeOfFlagException extends RuntimeException {

    public InvalidTypeOfFlagException(char flag) {
        super("flaga '" + flag + "' nie istnieje");
    }
}
