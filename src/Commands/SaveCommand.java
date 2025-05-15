package Commands;

import Core.CalendarContext;
import Core.Event;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SaveCommand implements BaseCommand {
    private final CalendarContext context;

    public SaveCommand(CalendarContext context) {
        this.context = context;
    }

    @Override
    public void execute(String[] args) {
        if (!context.isOpen) {
            System.out.println("Няма отворен файл.");
            return;
        }

        saveToFile(context.openedFile);
        System.out.println("Successfully saved " + context.openedFile);
    }

    private void saveToFile(String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Event e : context.calendar.getEvents()) {
                writer.write(String.format("%s|%s|%s|%s|%s",
                        e.getDate(), e.getStartTime(), e.getEndTime(), e.getName(), e.getNote()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}
