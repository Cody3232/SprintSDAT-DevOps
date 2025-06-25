package com.cody.sprintcli;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();  // Show the menu each time

            System.out.print("Enter your choice (1–5): ");
            int choice = scanner.nextInt();
// ******* Remember to add in API call for various Ids *******
            switch (choice) {
                case 1:
                    System.out.print("Enter City ID: ");
                    int cityId = scanner.nextInt();
                    System.out.println("Fetching airports for City ID: " + cityId);
                    break;

                case 2:
                    System.out.println("You chose to view aircraft flown by a passenger.");
                    System.out.print("Enter Passenger Id: ");
                    int passengerIdAircraft = scanner.nextInt();
                    System.out.println("Fetching aircraft flown by passenger ID: "+ passengerIdAircraft);
                    break;

                case 3:
                    System.out.println("You chose to view departure/arrival airports for an aircraft.");
                    System.out.print("Enter Aircraft ID: ");
                    int aircraftId = scanner.nextInt();
                    System.out.println("Fetching departure/arrival airports for Aircraft ID: " + aircraftId);
                    break;

                case 4:
                    System.out.println("You chose to view airports used by a passenger.");
                    System.out.print("Enter Passenger ID: ");
                    int passengerIdAirport = scanner.nextInt();
                    System.out.println("Fetching airports used by Passenger ID: " + passengerIdAirport);
                    break;

                case 5:
                    System.out.println("Exiting... Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }

            System.out.println(); // Blank line for spacing
        }

        scanner.close();
    }

    public static void printMenu() {
        System.out.println("===== Flight Info CLI =====");
        System.out.println("1. View airports in a city");
        System.out.println("2. View aircraft flown by a passenger");
        System.out.println("3. View takeoff/landing airports for an aircraft");
        System.out.println("4. View airports used by a passenger");
        System.out.println("5. Exit");
    }
}
