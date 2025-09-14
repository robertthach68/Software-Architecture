import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Comprehensive test for banking microservices with distributed transactions
 * This class demonstrates both successful transfers and error scenarios with
 * rollback
 */
public class TransactionTest {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final String CHECKING_SERVICE = "http://localhost:8082";
    private static final String SAVING_SERVICE = "http://localhost:8081";
    private static final String TRANSFER_SERVICE = "http://localhost:8083";

    public static void main(String[] args) {
        System.out.println("=== Banking Microservices Transaction Test ===");
        System.out.println();

        try {
            // Test 1: Create accounts
            System.out.println("1. Creating accounts...");
            createCheckingAccount();
            createSavingAccount();

            // Test 2: Check initial balances
            System.out.println("\n2. Checking initial balances...");
            checkBalances();

            // Test 3: Successful transfer
            System.out.println("\n3. Testing successful transfer...");
            performSuccessfulTransfer();
            checkBalances();

            // Test 4: Error scenario with rollback
            System.out.println("\n4. Testing error scenario with rollback...");
            performErrorTransfer();
            checkBalances();

            System.out.println("\n=== All tests completed! ===");

        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createCheckingAccount() throws IOException, InterruptedException {
        String json = """
                {
                    "accountNumber": "CHK001",
                    "customerId": "CUST001",
                    "initialBalance": 1000.00
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CHECKING_SERVICE + "/api/checking-accounts"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Checking account created: " + response.statusCode());
    }

    private static void createSavingAccount() throws IOException, InterruptedException {
        String json = """
                {
                    "accountNumber": "SAV001",
                    "customerId": "CUST001",
                    "initialBalance": 500.00
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SAVING_SERVICE + "/api/saving-accounts"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Saving account created: " + response.statusCode());
    }

    private static void checkBalances() throws IOException, InterruptedException {
        // Check checking account balance
        HttpRequest checkingRequest = HttpRequest.newBuilder()
                .uri(URI.create(CHECKING_SERVICE + "/api/checking-accounts/CHK001"))
                .GET()
                .build();

        HttpResponse<String> checkingResponse = httpClient.send(checkingRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Checking account balance: " + checkingResponse.body());

        // Check saving account balance
        HttpRequest savingRequest = HttpRequest.newBuilder()
                .uri(URI.create(SAVING_SERVICE + "/api/saving-accounts/SAV001"))
                .GET()
                .build();

        HttpResponse<String> savingResponse = httpClient.send(savingRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Saving account balance: " + savingResponse.body());
    }

    private static void performSuccessfulTransfer() throws IOException, InterruptedException {
        String json = """
                {
                    "fromAccountNumber": "CHK001",
                    "toAccountNumber": "SAV001",
                    "amount": 200.00,
                    "simulateError": false
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TRANSFER_SERVICE + "/api/transfers/checking-to-saving"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Transfer result: " + response.body());
    }

    private static void performErrorTransfer() throws IOException, InterruptedException {
        String json = """
                {
                    "fromAccountNumber": "CHK001",
                    "toAccountNumber": "SAV001",
                    "amount": 100.00,
                    "simulateError": true
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TRANSFER_SERVICE + "/api/transfers/test-error"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Error transfer result: " + response.body());
    }
}
