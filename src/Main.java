
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args)
            throws InterruptedException {

        BankAccount[] accounts = new BankAccount[10];

        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new BankAccount(i, 10000.0);
        }

        double initialTotal = calculateTotal(accounts);
        ExecutorService pool =
                Executors.newFixedThreadPool(10);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100000; i++) {
            int sourceIndex =
                    (int) (Math.random() * accounts.length);
            int destinationIndex =
                    (int) (Math.random() * accounts.length);

            if (sourceIndex == destinationIndex) {
                continue;
            }

            double amount =
                    1 + (int) (Math.random() * 100);

            pool.execute(new SafeTransferTask(accounts[sourceIndex], accounts[destinationIndex], amount));
        }

        pool.shutdown();

        if (!pool.awaitTermination(5, TimeUnit.MINUTES)) {
            pool.shutdownNow();
            throw new IllegalStateException(
                    "Tasks did not finish in time"
            );
        }

        long endTime = System.currentTimeMillis();
        double finalTotal = calculateTotal(accounts);

        System.out.println("Initial total: $" + initialTotal);
        System.out.println("Final total: $" + finalTotal);
        System.out.println("Time: " +
                (endTime - startTime) + " ms");
    }

    private static double calculateTotal(
            BankAccount[] accounts) {
        double total = 0;

        for (BankAccount account : accounts) {
            total += account.getBalance();
        }

        return total;
    }
}
