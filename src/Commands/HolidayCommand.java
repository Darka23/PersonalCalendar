package Commands;

import Core.CalendarContext;
import Core.MyCalendar;

import java.time.LocalDate;

/**
 * Команда за отбелязване на ден като почивен в календара.
 */
public class HolidayCommand implements BaseCommand {
    private final CalendarContext context;

    /**
     * Създава команда за задаване на почивен ден.
     *
     * @param context контекстът, съдържащ календара
     */
    public HolidayCommand(CalendarContext context) {
        this.context = context;
    }

    /**
     * Отбелязва посочената дата като почивна.
     *
     * @param args масив с 1 аргумент – дата (yyyy-MM-dd)
     */
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
