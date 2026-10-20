package scc.dormitory.ui;

import scc.dormitory.dao.ScenarioDao;
import scc.dormitory.model.ScenarioEntity;

import java.util.List;
import java.util.Scanner;

public class ScenarioMenu {

    private final ScenarioDao dao;
    private final Scanner scanner = new Scanner(System.in);

    public ScenarioMenu(ScenarioDao dao) {
        this.dao = dao;
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> addAllocation();
                case 2 -> viewAllocations();
                case 3 -> {
                    System.out.println(
                            "\nExiting system... Thank you!");
                    running = false;
                }
                default -> System.out.println(
                        "Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println(
                "==============================================");
        System.out.println(
                "       STUDENT DORMITORY BED ALLOCATION");
        System.out.println(
                "==============================================");
        System.out.println("[1] Add New Allocation");
        System.out.println("[2] View All Allocations");
        System.out.println("[3] Exit System");
        System.out.println(
                "==============================================");
    }

    private void addAllocation() {
        System.out.println("\n--- ADD NEW BED ALLOCATION ---");

        System.out.print("Enter Student Name: ");
        String studentName = scanner.nextLine().trim();

        System.out.print("Enter Building Name: ");
        String building = scanner.nextLine().trim();

        System.out.print("Enter Room Number: ");
        String roomNo = scanner.nextLine().trim();

        if (studentName.isBlank()
                || building.isBlank()
                || roomNo.isBlank()) {
            System.out.println(
                    "ERROR: Fields cannot be empty.");
            return;
        }

        double rent = readRent();

        ScenarioEntity resident = new ScenarioEntity(
                0, studentName, building, roomNo, rent);

        if (dao.addAllocation(resident)) {
            System.out.println(
                    "\nSUCCESS: Dorm bed allocated successfully!");
        } else {
            System.out.println(
                    "\nFAILED: Allocation was not saved.");
        }
    }

    private void viewAllocations() {
        List<ScenarioEntity> residents =
                dao.getAllAllocations();

        System.out.println("\nDORMITORY BED ALLOCATIONS");

        System.out.printf(
                "+------+----------------------+----------------+"
                        + "----------+--------------+%n");

        System.out.printf(
                "| %-4s | %-20s | %-14s | %-8s | %-12s |%n",
                "ID", "STUDENT NAME", "BUILDING",
                "ROOM NO", "MONTHLY RENT");

        System.out.printf(
                "+------+----------------------+----------------+"
                        + "----------+--------------+%n");

        for (ScenarioEntity resident : residents) {
            System.out.printf(
                    "| %-4d | %-20.20s | %-14.14s | %-8.8s "
                            + "| %12.2f |%n",
                    resident.id(),
                    resident.studentName(),
                    resident.building(),
                    resident.roomNo(),
                    resident.monthlyRent());
        }

        System.out.printf(
                "+------+----------------------+----------------+"
                        + "----------+--------------+%n");

        System.out.println(
                "Total Records Displayed: " + residents.size());
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(
                        "Please enter a whole number.");
            }
        }
    }

    private double readRent() {
        while (true) {
            System.out.print("Enter Monthly Rent: ");
            String input = scanner.nextLine().trim();

            try {
                double rent = Double.parseDouble(input);

                if (Double.isFinite(rent) && rent > 0) {
                    return rent;
                }

            } catch (NumberFormatException ignored) {
                // Display the validation message below.
            }

            System.out.println(
                    "Invalid rent. Enter a positive amount, "
                            + "such as 3500.00.");
        }
    }
}
