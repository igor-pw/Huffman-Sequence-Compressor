package pl.edu.pw.ee.aisd2025zex5.exception;

public class InvalidValueOfFlagException extends RuntimeException {
    public InvalidValueOfFlagException(char typeOfFlag, String valueOfFlag) {
        super("Niepoprawna wartosc flagi '-" + typeOfFlag + "' (" + valueOfFlag + ")");
    }
}
