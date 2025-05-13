import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.*;
import java.util.*;

public class CalendarCommands {

    // Календарът, с който работим (предаден отвън)
    private MyCalendar calendar;
    private final Scanner scanner = new Scanner(System.in);

    public CalendarCommands(MyCalendar calendar) {
        this.calendar = calendar;
    }

    // Добавя ново събитие към календара
    public void book(String args) {
        // Очаква: book <date> <start> <end> <name> <note>
        String[] parts = args.split("\\s+", 5);
        if (parts.length < 5) {
            System.out.println("Usage: book <date> <start> <end> <name> <note>");
            return;
        }
        try {
            LocalDate date = LocalDate.parse(parts[0]);
            LocalTime start = LocalTime.parse(parts[1]);
            LocalTime end = LocalTime.parse(parts[2]);
            String name = parts[3];
            String note = parts[4];
            calendar.book(date, start, end, name, note);
            System.out.println("Successfully booked event.");
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    // Изтрива събитие от календара по дата и час
    public void unbook(String args) {
        // Очаква: unbook <date> <start> <end>
        String[] parts = args.split("\\s+", 3);
        if (parts.length < 3) {
            System.out.println("Usage: unbook <date> <start> <end>");
            return;
        }
        try {
            LocalDate date = LocalDate.parse(parts[0]);
            LocalTime start = LocalTime.parse(parts[1]);
            LocalTime end = LocalTime.parse(parts[2]);
            calendar.unbook(date, start, end);
            System.out.println("Successfully unbooked event.");
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    // Показва всички събития за конкретна дата
    public void agenda(String args) {
        try {
            LocalDate date = LocalDate.parse(args.trim());
            List<Event> events = calendar.agenda(date);
            if (events.isEmpty()) {
                System.out.println("No events for " + date);
            } else {
                for (Event e : events) {
                    System.out.println(e);
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid date format.");
        }
    }

    // Търси събития по ключова дума (в име или бележка)
    public void find(String args) {
        List<Event> found = calendar.find(args.trim());
        if (found.isEmpty()) {
            System.out.println("No events found.");
        } else {
            for (Event e : found) {
                System.out.println(e);
            }
        }
    }

    // Променя поле на събитие
    public void change(String args) {
        // Очаква: change <date> <start> <end> <field> <new value>
        String[] parts = args.split("\\s+", 5);
        if (parts.length < 5) {
            System.out.println("Usage: change <date> <start> <end> <field> <new value>");
            return;
        }
        try {
            LocalDate date = LocalDate.parse(parts[0]);
            LocalTime start = LocalTime.parse(parts[1]);
            LocalTime end = LocalTime.parse(parts[2]);
            String field = parts[3];
            String newValue = parts[4];
            calendar.change(date, start, end, field, newValue);
            System.out.println("Successfully changed event.");
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    // Отбелязва ден като почивен (holiday)
    public void holiday(String args) {
        try {
            LocalDate date = LocalDate.parse(args.trim());
            calendar.holiday(date);
            System.out.println("Marked " + date + " as a holiday.");
        } catch (Exception e) {
            System.out.println("Invalid date format.");
        }
    }

    // Показва за кои дни между два дни имаме събития и колко часа заетост има
    public void busydays(String args) {
        // Очаква: busydays <start date> <end date>
        String[] parts = args.split("\\s+", 2);
        if (parts.length < 2) {
            System.out.println("Usage: busydays <start date> <end date>");
            return;
        }
        try {
            LocalDate from = LocalDate.parse(parts[0]);
            LocalDate to = LocalDate.parse(parts[1]);
            Map<LocalDate, Long> stats = calendar.busydays(from, to);
            for (Map.Entry<LocalDate, Long> entry : stats.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " часа");
            }
        } catch (Exception e) {
            System.out.println("Invalid date input.");
        }
    }

    // Намира свободен времеви слот между два дни с дадена продължителност
    public void findslot(String args) {
        // Очаква: findslot <start date> <end date> <duration in minutes>
        String[] parts = args.split("\\s+", 3);
        if (parts.length < 3) {
            System.out.println("Usage: findslot <start date> <end date> <duration in minutes>");
            return;
        }
        try {
            LocalDate from = LocalDate.parse(parts[0]);
            LocalDate to = LocalDate.parse(parts[1]);
            long minutes = Long.parseLong(parts[2]);
            Optional<LocalDateTime[]> slot = calendar.findslot(from, to, Duration.ofMinutes(minutes));
            if (slot.isPresent()) {
                LocalDateTime[] times = slot.get();
                System.out.println("Found slot: " + times[0] + " - " + times[1]);
            } else {
                System.out.println("No available slot found.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    public void findslotwith(String args) {
        String[] parts = args.split("\\s+");
        if (parts.length < 4) {
            System.out.println("Usage: findslotwith <file> <start date> <end date> <duration in minutes>");
            return;
        }

        String filePath = parts[0];
        try {
            // Зареждане на втори календар от файл
            MyCalendar other = new MyCalendar();
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File not found: " + filePath);
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] e = line.split("\\|");
                if (e.length == 5) {
                    LocalDate date = LocalDate.parse(e[0]);
                    LocalTime start = LocalTime.parse(e[1]);
                    LocalTime end = LocalTime.parse(e[2]);
                    String name = e[3];
                    String note = e[4];
                    other.book(date, start, end, name, note);
                }
            }

            // Парсиране на параметри
            LocalDate from = LocalDate.parse(parts[1]);
            LocalDate to = LocalDate.parse(parts[2]);
            long minutes = Long.parseLong(parts[3]);

            // Извикване на логиката от MyCalendar
            Optional<LocalDateTime[]> result = calendar.findslotwith(other, from, to, Duration.ofMinutes(minutes));
            if (result.isPresent()) {
                LocalDateTime[] times = result.get();
                System.out.println("Found mutual slot: " + times[0] + " - " + times[1]);
            } else {
                System.out.println("No common free slot found.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void merge(String args) {
        if (args.isBlank()) {
            System.out.println("Usage: merge <file>");
            return;
        }

        String filePath = args.trim();
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            MyCalendar other = new MyCalendar();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] e = line.split("\\|");
                if (e.length == 5) {
                    LocalDate date = LocalDate.parse(e[0]);
                    LocalTime start = LocalTime.parse(e[1]);
                    LocalTime end = LocalTime.parse(e[2]);
                    String name = e[3];
                    String note = e[4];
                    other.book(date, start, end, name, note);
                }
            }

            calendar.merge(other);
            System.out.println("Merge completed.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // за findslotwith и merge ще трябват 2 календара
    // ще ги добавя допълнително ако искаш, защото са малко по-сложни

}
