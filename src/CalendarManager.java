import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class CalendarManager {
    private MyCalendar calendar = new MyCalendar();
    private String openedFile = null;
    private boolean isOpen = false;
    private String currentArguments = "";
    private final Scanner scanner = new Scanner(System.in);
    private CalendarCommands calendarCommands;

    private final Map<String, Runnable> commands = new HashMap<>();

    public CalendarManager() {
        commands.put("open", this::open);
        commands.put("close", this::close);
        commands.put("save", this::save);
        commands.put("saveas", this::saveAs);
        commands.put("help", this::help);
        commands.put("exit", this::exit);
    }

    private void callCalendarCommand(String command, String args) {
        try {
            switch (command) {
                case "book" -> calendarCommands.book(args);
                case "unbook" -> calendarCommands.unbook(args);
                case "agenda" -> calendarCommands.agenda(args);
                case "find" -> calendarCommands.find(args);
                case "change" -> calendarCommands.change(args);
                case "holiday" -> calendarCommands.holiday(args);
                case "busydays" -> calendarCommands.busydays(args);
                case "findslot" -> calendarCommands.findslot(args);
                default -> System.out.println("Unknown calendar command.");
            }
        } catch (Exception e) {
            System.out.println("Error executing calendar command: " + e.getMessage());
        }
    }

    public void run() {
        System.out.println("Добре дошли в Личен Календар!");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            String[] tokens = input.split("\\s+", 2);
            String command = tokens[0].toLowerCase();
            currentArguments = tokens.length > 1 ? tokens[1] : "";

            Runnable action = commands.get(command);
            if (action != null) {
                action.run();
            } else if (isOpen && calendarCommands != null) {
                callCalendarCommand(command, currentArguments);
            } else {
                System.out.println("Unknown command. Type 'help' to see available commands.");
            }
        }
    }

    private void open() {
        if (isOpen) {
            System.out.println("Моля, първо затворете текущия файл с командата 'close'.");
            return;
        }
        if (currentArguments.isEmpty()) {
            System.out.println("Моля, укажете файл за отваряне.");
            return;
        }

        File file = new File(currentArguments);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                calendar = new MyCalendar();
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 5) {
                        LocalDate date = LocalDate.parse(parts[0]);
                        LocalTime start = LocalTime.parse(parts[1]);
                        LocalTime end = LocalTime.parse(parts[2]);
                        String name = parts[3];
                        String note = parts[4];
                        calendar.book(date, start, end, name, note);
                    }
                }
                openedFile = currentArguments;
                isOpen = true;
                calendarCommands = new CalendarCommands(calendar); // <-- ВАЖНО!
                System.out.println("Successfully opened " + openedFile);
            } catch (Exception e) {
                System.out.println("Error opening file: " + e.getMessage());
                System.exit(1);
            }
        } else {
            try {
                file.createNewFile();
                openedFile = currentArguments;
                isOpen = true;
                calendar = new MyCalendar(); // празен календар
                calendarCommands = new CalendarCommands(calendar); // <-- ВАЖНО!
                System.out.println("File did not exist. Created new file: " + openedFile);
            } catch (IOException e) {
                System.out.println("Cannot create file: " + e.getMessage());
            }
        }
    }


    private void close() {
        if (!isOpen) {
            System.out.println("Няма отворен файл.");
            return;
        }
        calendar = new MyCalendar();
        openedFile = null;
        isOpen = false;
        System.out.println("Successfully closed.");
    }

    private void save() {
        if (!isOpen) {
            System.out.println("Няма отворен файл.");
            return;
        }
        saveToFile(openedFile);
        System.out.println("Successfully saved " + openedFile);
    }

    private void saveAs() {
        if (!isOpen) {
            System.out.println("Няма отворен файл.");
            return;
        }
        if (currentArguments.isEmpty()) {
            System.out.println("Моля, укажете път за 'saveas'.");
            return;
        }
        saveToFile(currentArguments);
        openedFile = currentArguments;
        System.out.println("Successfully saved " + openedFile);
    }

    private void help() {
        System.out.println("The following commands are supported:");
        System.out.println("open <file>     opens <file>");
        System.out.println("close           closes currently opened file");
        System.out.println("save            saves the currently open file");
        System.out.println("saveas <file>   saves the currently open file in <file>");
        System.out.println("help            prints this information");
        System.out.println("exit            exits the program");
    }

    private void exit() {
        System.out.println("Exiting the program...");
        System.exit(0);
    }

    private void saveToFile(String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Event e : calendar.getEvents()) {
                writer.write(String.format("%s|%s|%s|%s|%s",
                        e.getDate(), e.getStartTime(), e.getEndTime(), e.getName(), e.getNote()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}

