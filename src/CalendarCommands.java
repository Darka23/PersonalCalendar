import java.time.*;
import java.util.*;

public class CalendarCommands {
    private MyCalendar calendar;
    private final Scanner scanner = new Scanner(System.in);

    public CalendarCommands(MyCalendar calendar) {
        this.calendar = calendar;
    }

    public void book(String args) {
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

    public void unbook(String args) {
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

    public void change(String args) {
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

    public void holiday(String args) {
        try {
            LocalDate date = LocalDate.parse(args.trim());
            calendar.holiday(date);
            System.out.println("Marked " + date + " as a holiday.");
        } catch (Exception e) {
            System.out.println("Invalid date format.");
        }
    }

    public void busydays(String args) {
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

    public void findslot(String args) {
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

    // за findslotwith и merge ще трябват 2 календара
    // ще ги добавя допълнително ако искаш, защото са малко по-сложни

}
