package utils.platform;

public class SpecialCharacters {
    private static SpecialCharacters instance = null;

    private SpecialCharacters() { }

    public static SpecialCharacters getInstance() {
        if (SpecialCharacters.instance == null)
            SpecialCharacters.instance = new SpecialCharacters();

        return SpecialCharacters.instance;
    }

    public String newLine() {
        // TODO: Сделать выдачу специальных символов относительно операционной системы
        return "\r\n";
    }
}
