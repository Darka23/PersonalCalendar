package Commands;

import java.time.LocalDate;
import java.time.LocalTime;

import Core.CalendarContext;
import Core.MyCalendar;

public class UnbookCommand implements BaseCommand {
    private final CalendarContext context;

    public UnbookCommand(CalendarContext context) {
        this.context = context;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: unbook <date (yyyy-dd-mm)> <start (hh:mm)> <end (hh:mm)>");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(args[0]);
            LocalTime start = LocalTime.parse(args[1]);
            LocalTime end = LocalTime.parse(args[2]);
            context.calendar.unbook(date, start, end);
            System.out.println("Successfully unbooked event.");
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
