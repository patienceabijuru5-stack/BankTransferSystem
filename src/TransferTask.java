
public class TransferTask implements Runnable {
    private final BankAccount source;
    private final BankAccount destination;
    private final double amount;

    public TransferTask(
            BankAccount source,
            BankAccount destination,
            double amount) {
        this.source = source;
        this.destination = destination;
        this.amount = amount;
    }

    @Override
    public void run() {
        if (source == destination || amount <= 0) {
            return;
        }

        if (source.withdraw(amount)) {
            destination.deposit(amount);
        }
    }
}
