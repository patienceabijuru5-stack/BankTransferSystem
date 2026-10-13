public class SafeTransferTask implements Runnable {
    private final BankAccount source;
    private final BankAccount destination;
    private final double amount;

    public SafeTransferTask(
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

        BankAccount first;
        BankAccount second;

        if (source.getId() < destination.getId()) {
            first = source;
            second = destination;
        } else {
            first = destination;
            second = source;
        }

        synchronized (first) {
            synchronized (second) {
                if (source.withdraw(amount)) {
                    destination.deposit(amount);
                }
            }
        }
    }
}
