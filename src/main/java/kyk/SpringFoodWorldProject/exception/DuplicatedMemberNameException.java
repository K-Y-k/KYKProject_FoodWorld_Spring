package kyk.SpringFoodWorldProject.exception;

public class DuplicatedMemberNameException extends RuntimeException {

    public DuplicatedMemberNameException(String message) {
        super(message);
    }
}