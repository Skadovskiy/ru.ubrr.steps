package games;

import org.slf4j.Logger;
import java.io.IOException;

public class Choice {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Choice.class);
    static final String LINE_SEPARATOR = System.lineSeparator();

    static char getCharacterFromUser() throws IOException {
        byte[] input = new byte[1 + LINE_SEPARATOR.length()];

        // без единички почему то не работает в консоли IDEA
        // System.in.read(input) возвращает дину сепаратора = 1 тогда как в input длина сепаратора = 2
        // и Ctrl + C не работает :)
        var i = System.in.read(input) + 1;
        if (i != input.length)
            throw new RuntimeException("Пользователь ввёл недостаточное кол-во символов");
        return (char) input[0];
    }

    public static void main(String... __) {
        while (true) {
            log.info("Выберите игру:\n1 - \"однорукий бандит\", 2 - \"пьяница\", 3 - \"очко\"");
            try {
                switch (getCharacterFromUser()) {
                    case '1':
                        Slot.main();
                        break;
                    case '2':
                        Drunkard.main();
                        break;
                    case '3':
                        BlackJack.main();
                        break;
                    default:
                        log.info("Игры с таким номером нет!");
                }
            } catch (java.io.IOException e) {
                log.info(e.getMessage());
            }
        }
    }
}
