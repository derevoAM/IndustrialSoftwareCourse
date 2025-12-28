public class DebitAccount extends Account {

    public DebitAccount(String accountNumber, double balance, Customer owner) {
        super(accountNumber, balance, owner);
    }

    public DebitAccount(Customer owner) {
        super(null, 0.0, owner);
    }


    @Override
    public boolean withdraw(double amount) {
        if (getBalance() >= amount) {
            return super.withdraw(amount);
        }
        return false;
    }
}