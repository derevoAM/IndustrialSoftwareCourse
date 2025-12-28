public class CreditAccount extends Account {

    private double creditLimit;

    public CreditAccount(String accountNumber, double balance, Customer owner, double creditLimit) {
        super(accountNumber, balance, owner);
        this.creditLimit = creditLimit;
    }

    public CreditAccount(Customer owner, double creditLimit) {
        super(null, 0.0, owner);
        this.creditLimit = creditLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        if (getBalance() - amount > -creditLimit) {
            return super.withdraw(amount);
        }
        return false;
    }
}