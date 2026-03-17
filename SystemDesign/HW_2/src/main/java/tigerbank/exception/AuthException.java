package tigerbank.exception;

public class AuthException {
    public static class BankAccountIdExistsException extends RuntimeException {
        public BankAccountIdExistsException() {
            super("Банковский аккаунт с данным id уже существует.");
        }
    }

    public static class CategoryIdExistsException extends RuntimeException {
        public CategoryIdExistsException() {
            super("Категория с данным id уже существует.");
        }
    }
}
