package Commands;

import Core.CalendarContext;
import Core.Event;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Команда за записване на календара в нов файл.
 */
public class SaveAsCommand implements BaseCommand {
    private final CalendarContext context;

    /**
     * Създава команда за записване на календара в нов файл.
     *
     * @param context контекстът, съдържащ календара и текущото състояние
     */
    public SaveAsCommand(CalendarContext context) {
        this.context = context;
    }

    /**
     * Записва събитията в нов файл и задава новото име като текущо.
     *
     * @param args масив с 1 аргумент – път до нов файл
     */
    @Override
    public void execute(String[] args) {
        if (!context.isOpen) {
            System.out.println("Няма отворен файл.");
            return;
        }

        if (args.length < 1) {
            System.out.println("Моля, укажете път за 'saveas'.");
            return;
        }

        String newPath = args[0];
        saveToFile(newPath);
        context.openedFile = newPath;
        System.out.println("Successfully saved " + newPath);
    }

    /**
     * Записва събитията от календара във файл.
     *
     * @param path пътят до новия файл
     */
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
