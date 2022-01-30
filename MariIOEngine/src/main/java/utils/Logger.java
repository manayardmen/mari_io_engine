package utils;

public class Logger {
    private static Logger instance = null;

    private Logger() { }

    public static Logger getInstance() {
        if (Logger.instance == null)
            Logger.instance = new Logger();

        return Logger.instance;
    }

    public void write(String message) {
        if (DefaultConstants.IS_DEBUG)
            System.out.println(message);
    }

    public void error(String message) {
        System.err.println(message);
    }
}
