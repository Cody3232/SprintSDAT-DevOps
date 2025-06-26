package com.cody.sprintcli;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();

            System.out.print("Enter your choice (1–5): ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter City ID: ");
                    int cityId = scanner.nextInt();
                    System.out.println("Airports in City ID " + cityId + ":");
                    System.out.println(ApiClient.getAirportsByCity(cityId));
                    break;

                case 2:
                    System.out.println("You chose to view aircraft flown by a passenger.");
                    System.out.print("Enter Passenger ID: ");
                    int passengerIdAircraft = scanner.nextInt();
                    System.out.println("Aircraft flown by Passenger ID " + passengerIdAircraft + ":");
                    System.out.println(ApiClient.getAircraftByPassenger(passengerIdAircraft));
                    break;

                case 3:
                    System.out.println("You chose to view departure/arrival airports for an aircraft.");
                    System.out.print("Enter Aircraft ID: ");
                    int aircraftId = scanner.nextInt();
                    System.out.println("Airports used by Aircraft ID " + aircraftId + ":");
                    System.out.println(ApiClient.getAirportsForAircraft(aircraftId));
                    break;

                case 4:
                    System.out.println("You chose to view airports used by a passenger.");
                    System.out.print("Enter Passenger ID: ");
                    int passengerIdAirport = scanner.nextInt();
                    System.out.println("Airports used by Passenger ID " + passengerIdAirport + ":");
                    System.out.println(ApiClient.getAirportsUsedByPassenger(passengerIdAirport));
                    break;

                case 5:
                    System.out.println("Exiting... Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }

            System.out.println(); // Add space between interactions
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
