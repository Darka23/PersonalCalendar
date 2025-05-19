package Commands;

import Core.CalendarContext;
import Core.Event;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Команда за записване на текущия календар във вече отворения файл.
 */
public class SaveCommand implements BaseCommand {
    private final CalendarContext context;

    /**
     * Създава команда за записване на събития във файл.
     *
     * @param context контекстът с календар и информация за файла
     */
    public SaveCommand(CalendarContext context) {
        this.context = context;
    }

    /**
     * Записва съдържанието на календара във вече отворения файл.
     *
     * @param args не се използват
     */
    @Override
    public void execute(String[] args) {
        if (!context.isOpen()) {
            System.out.println("Няма отворен файл.");
            return;
        }

        saveToFile(context.getOpenedFile());
        System.out.println("Successfully saved " + context.getOpenedFile());
    }

    /**
     * Помощен метод за записване на всички събития от календара във файл.
     *
     * @param path пътят към файла, в който се записва
     */
    private void saveToFile(String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Event e : context.getCalendar().getEvents()) {
                writer.write(String.format("%s|%s|%s|%s|%s",
                        e.getDate(), e.getStartTime(), e.getEndTime(), e.getName(), e.getNote()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}
