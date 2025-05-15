package Commands;

import Core.CalendarContext;
import Core.MyCalendar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.*;
import java.util.Optional;

public class FindSlotWithCommand implements BaseCommand {
    private final CalendarContext context;

    public FindSlotWithCommand(CalendarContext context) {
        this.context = context;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: findslotwith <file> <start date (yyyy-dd-mm)> <end date (yyyy-dd-mm)> <duration in minutes>");
            return;
        }

        String filePath = args[0];
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

            LocalDate from = LocalDate.parse(args[1]);
            LocalDate to = LocalDate.parse(args[2]);
            long minutes = Long.parseLong(args[3]);

            Optional<LocalDateTime[]> result = context.calendar.findslotwith(other, from, to, Duration.ofMinutes(minutes));
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
}
