package Commands;

import Core.CalendarContext;
import Core.MyCalendar;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class FindSlotCommand implements BaseCommand {
    private final CalendarContext context;

    public FindSlotCommand(CalendarContext context) {
        this.context = context;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: findslot <start date (yyyy-dd-mm)> <end date (yyyy-dd-mm)> <duration in minutes>");
            return;
        }

        try {
            LocalDate from = LocalDate.parse(args[0]);
            LocalDate to = LocalDate.parse(args[1]);
            long minutes = Long.parseLong(args[2]);
            Optional<LocalDateTime[]> slot = context.calendar.findslot(from, to, Duration.ofMinutes(minutes));

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
}
