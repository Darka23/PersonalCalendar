package Commands;

import Core.CalendarContext;
import Core.MyCalendar;

import java.time.LocalDate;
import java.time.LocalTime;

public class ChangeCommand implements BaseCommand {
    private final CalendarContext context;

    public ChangeCommand(CalendarContext context) {
        this.context = context;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 5) {
            System.out.println("Usage: change <date (yyyy-dd-mm)> <start (hh:mm)> <end (hh:mm)> <field> <new value>");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(args[0]);
            LocalTime start = LocalTime.parse(args[1]);
            LocalTime end = LocalTime.parse(args[2]);
            String field = args[3];
            String newValue = args[4];
            context.calendar.change(date, start, end, field, newValue);
            System.out.println("Successfully changed event.");
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
}
