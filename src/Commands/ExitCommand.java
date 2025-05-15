package Commands;

/**
 * Команда за прекратяване на програмата.
 */
public class ExitCommand implements BaseCommand {

    /**
     * Извежда съобщение за изход и спира изпълнението на приложението.
     *
     * @param args не се използват
     */
    @Override
    public void execute(String[] args) {
        System.out.println("Exiting the program...");
        System.exit(0);
    }
}
