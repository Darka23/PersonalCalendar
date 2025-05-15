package Commands;

import Core.CalendarContext;
import Core.MyCalendar;

import java.time.LocalDate;

public class HolidayCommand implements BaseCommand {
    private final CalendarContext context;

    public HolidayCommand(CalendarContext context) {
        this.context = context;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: holiday <date (yyyy-dd-mm)>");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(args[0]);
            context.calendar.holiday(date);
            System.out.println("Marked " + date + " as a holiday.");
        } catch (Exception e) {
            System.out.println("Invalid date format.");
        }
    }
}
