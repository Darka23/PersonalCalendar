package Commands;

public class ExitCommand implements BaseCommand {
    @Override
    public void execute(String[] args) {
        System.out.println("Exiting the program...");
        System.exit(0);
    }
}
