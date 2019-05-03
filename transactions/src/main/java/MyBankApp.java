import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class MyBankApp {

    private static Map<String, Record> records = null;
    private static DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static String TX_DEBIT = "PAYMENT";

    public static void main(String[] args) {

        if (args.length != 3) {
            throw new IllegalArgumentException("Exactly 3 parameters required !");
        }

        Result result = analyzeData(args);

        System.out.println("Relative balance for the period is: " + result.amount);
        System.out.println("Number of transactions included is: " + result.txCount);
    }

    /**
     * Analyze the data and output the result
     * @param args
     */
    public static Result analyzeData(String[] args) {
        // Process the records
        String accountId = args[0].trim();
        Date fromDate = stringToDate(args[1].trim());
        Date toDate = stringToDate(args[2].trim());

        // Initialize or load the transactions file
        if(records == null) {
            ClassLoader classLoader = MyBankApp.class.getClassLoader();
            URL resource = classLoader.getResource("transactions.csv");
            loadCsv(resource.getFile());
        }

        Map txAmount = new HashMap<String, Double>();
        int txCount = 0;

        for (Record record: records.values()) {
            // Pick the respective Account Tx only
            if (accountId.equals(record.fromAccountId)) {
                // Check if any related tx occurred
                if (record.relatedTransaction == null
                        && fromDate.before(record.createdAt)
                        && toDate.after(record.createdAt)) {
                    txAmount.put(record.transactionId, record.amount);
                    txCount++;
                } else if (record.relatedTransaction != null) {
                    txAmount.remove(record.relatedTransaction);
                    txCount--;
                }
            }
        }

        double relativeBal = (double) txAmount.values().stream().collect(Collectors.summingDouble(Double::doubleValue));

        return new Result(relativeBal, txCount);
    }

    /**
     * Helper function to parse Date in String format (String -> Date)
     * @param dateString
     * @return
     */
    private static Date stringToDate(String dateString) {
        Date date = null;
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * Load the CSV file and initialize as a Map/cache
     * @param file
     */
    private static void loadCsv(String file) {
        String line = "";
        String delimitter = ",";
        records = new HashMap();
        int skipHeader = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                if (skipHeader == 0) {
                    skipHeader++;
                    continue;
                }

                String[] attributes = line.split(delimitter);
                String txId = attributes[0].trim();
                String accountId = attributes[1].trim();
                Date createdAt = stringToDate(attributes[3].trim());
                double amount = Double.parseDouble(attributes[4].trim());
                String txType = attributes[5].trim();
                if (TX_DEBIT.equalsIgnoreCase(txType))
                    amount = 0 - amount;
                String relatedTx = (attributes.length == 6 ? null : attributes[6].trim());

                records.put(txId, new Record(
                        txId,
                        accountId,
                        attributes[2].trim(),
                        createdAt,
                        amount,
                        txType,
                        relatedTx));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * The representation of finanical transaction object
     */
    private static class Record {
        String transactionId;
        String fromAccountId;
        String toAccountId;
        Date createdAt;
        double amount;
        String transactionType;
        String relatedTransaction;

        Record(String transactionId,
               String fromAccountId,
               String toAccountId,
               Date createdAt,
               double amount,
               String transactionType,
               String relatedTransaction) {
            this.transactionId = transactionId;
            this.fromAccountId = fromAccountId;
            this.toAccountId = toAccountId;
            this.createdAt = createdAt;
            this.amount = amount;
            this.transactionType = transactionType;
            this.relatedTransaction = relatedTransaction;

        }
    }

    public static class Result {
        double amount;
        int txCount;

        Result(double amount, int txCount) {
            this.amount = amount;
            this.txCount = txCount;
        }
    }
}
