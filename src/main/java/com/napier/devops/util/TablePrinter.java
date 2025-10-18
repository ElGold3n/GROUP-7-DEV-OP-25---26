package com.napier.devops.util;

import java.util.*;

public class TablePrinter {
    private final List<String> headers;
    private final List<List<String>> rows = new ArrayList<>();

    public TablePrinter(String... headers) {
        this.headers = Arrays.asList(headers);
    }

    public void addRow(Object... values) {
        List<String> row = new ArrayList<>();
        for (Object v : values) {
            row.add(v == null ? "" : v.toString());
        }
        rows.add(row);
    }

    public void print(int pageSize) {
        if (rows.isEmpty()) {
            System.out.println("No results found.");
            return;
        }

        int currentPage = 0;
        int totalPages = (int) Math.ceil((double) rows.size() / pageSize);

        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, rows.size());

        // Print table header
        int[] widths = calculateWidths();
        printLine(widths);
        printRow(headers, widths);
        printLine(widths);

        // Print rows for this page
        for (int i = start; i < end; i++) {
            printRow(rows.get(i), widths);
        }
        printLine(widths);

        // Pagination controls
        System.out.println("Page " + (currentPage + 1) + " of " + totalPages);

    }


    public void print(int pageSize, Scanner scanner) {
        if (rows.isEmpty()) {
            System.out.println("No results found.");
            return;
        }

        int currentPage = 0;
        int totalPages = (int) Math.ceil((double) rows.size() / pageSize);

        while (true) {
            int start = currentPage * pageSize;
            int end = Math.min(start + pageSize, rows.size());

            // Print table header
            int[] widths = calculateWidths();
            printLine(widths);
            printRow(headers, widths);
            printLine(widths);

            // Print rows for this page
            for (int i = start; i < end; i++) {
                printRow(rows.get(i), widths);
            }
            printLine(widths);

            // Pagination controls
            System.out.println("Page " + (currentPage + 1) + " of " + totalPages);
                System.out.println("[N]ext | [P]rev | [E]xit");
                System.out.print("Choice: ");
                String choice = scanner.nextLine().trim().toLowerCase();


                switch (choice) {
                    case "n" -> {
                        if (currentPage < totalPages - 1) currentPage++;
                        else System.out.println("Already at last page.");
                    }
                    case "p" -> {
                        if (currentPage > 0) currentPage--;
                        else System.out.println("Already at first page.");
                    }
                    case "e" -> { return; }
                    default -> System.out.println("Invalid option.");
                }
        }
    }

    // --- Internal helpers ---
    private int[] calculateWidths() {
        int[] widths = new int[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            widths[i] = headers.get(i).length();
        }
        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                widths[i] = Math.max(widths[i], row.get(i).length());
            }
        }
        return widths;
    }

    private void printLine(int[] widths) {
        StringBuilder sb = new StringBuilder();
        for (int w : widths) {
            sb.append("+").append("-".repeat(w + 2));
        }
        sb.append("+");
        System.out.println(sb);
    }

    private void printRow(List<String> row, int[] widths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row.size(); i++) {
            sb.append("| ").append(pad(row.get(i), widths[i])).append(" ");
        }
        sb.append("|");
        System.out.println(sb);
    }

    private String pad(String text, int width) {
        return String.format("%-" + width + "s", text);
    }
}
