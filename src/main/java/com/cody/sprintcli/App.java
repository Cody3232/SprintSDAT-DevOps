package com.cody.sprintcli;

import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ApiClient client = new ApiClient(new ApiConfig());
        QueryService service = new QueryService(client);

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter your choice (1–5): ");
            int choice = readInt(scanner, 1, 5);

            try {
                switch (choice) {
                    case 1 -> {
                        int cityId = readPositiveInt(scanner, "Enter City ID: ");
                        System.out.println(service.airportsInCity(cityId));
                    }
                    case 2 -> {
                        int passengerId = readPositiveInt(scanner, "Enter Passenger ID: ");
                        System.out.println(service.aircraftForPassenger(passengerId));
                    }
                    case 3 -> {
                        int aircraftId = readPositiveInt(scanner, "Enter Aircraft ID: ");
                        System.out.println("Airports used by Aircraft ID " + aircraftId + ":");
                        System.out.println(service.airportsForAircraft(aircraftId));
                    }
                    case 4 -> {
                        int passengerId = readPositiveInt(scanner, "Enter Passenger ID: ");
                        System.out.println("Airports used by Passenger ID " + passengerId + ":");
                        System.out.println(service.airportsForPassenger(passengerId));
                    }
                    case 5 -> {
                        System.out.println("Exiting... Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (RuntimeException e) {
                System.out.println("Request failed: " + e.getMessage());
            }
            System.out.println(); // spacing
        }

        scanner.close();
    }

    private static int readInt(Scanner scanner, int min, int max) {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a number " + min + "–" + max + ": ");
            scanner.next(); // discard bad token
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume newline
        while (value < min || value > max) {
            System.out.print("Please enter a number " + min + "–" + max + ": ");
            while (!scanner.hasNextInt()) {
                System.out.print("Please enter a number " + min + "–" + max + ": ");
                scanner.next();
            }
            value = scanner.nextInt();
            scanner.nextLine();
        }
        return value;
    }

    private static int readPositiveInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a whole number: ");
            scanner.next(); // discard bad token
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume newline
        while (value <= 0) {
            System.out.print("ID must be positive. Try again: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Please enter a whole number: ");
                scanner.next();
            }
            value = scanner.nextInt();
            scanner.nextLine();
        }
        return value;
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
