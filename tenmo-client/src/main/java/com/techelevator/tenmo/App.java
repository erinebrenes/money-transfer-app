package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.List;

public class App {
    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private UserService userService = new UserService();
    private AccountService accountService = new AccountService();
    private TransferService transferService = new TransferService();

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
            return;
        }
        accountService.setAuthToken(currentUser.getToken());
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        accountService.setAuthToken(currentUser.getToken());
        System.out.println("Your current account balance is: $" + accountService.getAccountBalance(currentUser.getUser().getId()));
    }

	private void viewTransferHistory() {
        // TODO Auto-generated method stub
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {

            List<User> users = userService.getListUsers();

            System.out.println("-------------------------------------------");
            System.out.println("Users");
            System.out.println("ID          Username");
            System.out.println("-------------------------------------------");

            for (User user : users) {
                System.out.println(user.getId() + "        " + user.getUsername());
            }

            System.out.println("-------------------------------------------");
            int recipientId = consoleService.promptForInt("Enter the ID of the user you are sending to (0 to cancel): ");

            if (recipientId == 0) {
                System.out.println("Transaction canceled.");
                return;
            }

            User recipient = userService.selectUserById(recipientId);

            if (recipient == null) {
                System.out.println("Invalid user ID or user does not exist. Please select another user.");
                return;
            }

            if(recipient.getId() == currentUser.getUser().getId()) {
                System.out.println("You cannot send money to yourself. Please choose another recipient.");
                return;
            }

            BigDecimal amount = consoleService.promptForBigDecimal("Enter the amount: ");

            System.out.println("Sending TEbucks to: " + recipient.getUsername());
            System.out.println("-------------------------------------------");
            System.out.println("Amount: $" + amount);
            System.out.println("-------------------------------------------");

        }

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}