package Commands;

import Core.CalendarContext;
import Core.MyCalendar;

import java.time.LocalDate;
import java.util.Map;

public class BusyDaysCommand implements BaseCommand {
    private final CalendarContext context;

    public BusyDaysCommand(CalendarContext context) {
        this.context = context;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: busydays <start date (yyyy-dd-mm)> <end date (yyyy-dd-mm)>");
            return;
        }

        try {
            LocalDate from = LocalDate.parse(args[0]);
            LocalDate to = LocalDate.parse(args[1]);
            Map<LocalDate, Long> stats = context.calendar.busydays(from, to);

            for (Map.Entry<LocalDate, Long> entry : stats.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " часа");
            }
        } catch (Exception e) {
            System.out.println("Invalid date input.");
        }
    }
}
