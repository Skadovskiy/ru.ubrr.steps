package games;

import org.slf4j.Logger;

import java.io.IOException;

import static games.CardUtils.*;
import static games.Choice.getCharacterFromUser;

public class BlackJack {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BlackJack.class);

    private static int[] cards; // Основная колода
    private static int cursor; // Счётчик карт основной колоды

    private static int[][] playersCards; // карты игроков. Первый индекс - номер игрока
    private static int[] playersCursors; // курсоры карт игроков. Индекс - номер игрока
    private static int[] playersMoney = {100, 100};
    private static int[] roundSum;
    private static final int MAX_VALUE = 21;
    private static final int MAX_CARDS_COUNT = 8;


    private static int value(int card) {
        switch (getPar(card)) {
            case JACK:
                return 2;
            case QUEEN:
                return 3;
            case KING:
                return 4;
            case SIX:
                return 6;
            case SEVEN:
                return 7;
            case EIGHT:
                return 8;
            case NINE:
                return 9;
            case TEN:
                return 10;
            case ACE:
            default:
                return 11;
        }
    }

    private static void initRound() {
        log.info("\nУ Вас " + playersMoney[0] + "$, у компьютера - " + playersMoney[1] + "$. Начинаем новый раунд!");
        cards = CardUtils.getShaffledCards();
        playersCards = new int[2][MAX_CARDS_COUNT];
        playersCursors = new int[]{0, 0};
        playersMoney[0] -= 10;
        playersMoney[1] -= 10;
        roundSum = new int[]{0, 0};
        cursor = 0;
    }

    private static int addCard2Player(int player) {
        return playersCards[player][playersCursors[player]++] = cards[cursor++];
    }

    static int sum(int player) {
        var result = 0;
        for (int i = 0; i < playersCursors[player]; i++) {
            result += value(playersCards[player][i]);
        }
        return result;
    }

    static int getFinalSum(int player) {
        var result = sum(player);
        return (result > 21) ? 0 : result;
    }

    static boolean confirm(String message) throws IOException {
        log.info(message + " \"Y\" - Да, {любой другой символ} - нет (Чтобы выйти из игры, нажмите Ctrl + C)");
        switch (getCharacterFromUser()) {
            case 'Y':
            case 'y':
                return true;
            default:
                return false;
        }
    }

    public static void main(String... __) throws IOException {

        do {
            initRound();

            log.info("Вам выпала карта {}", CardUtils.toString(addCard2Player(0)));
            log.info("Вам выпала карта {}", CardUtils.toString(addCard2Player(0)));

            if (sum(0) < 20)
                while (confirm("Берём ещё?") && sum(0) < 20)
                    log.info("Вам выпала карта {}", CardUtils.toString(addCard2Player(0)));

            log.info("Компьютеру выпала карта {}", CardUtils.toString(addCard2Player(1)));
            log.info("Компьютеру выпала карта {}", CardUtils.toString(addCard2Player(1)));
            if (sum(1) < 17)
                while (sum(1) < 17)
                    log.info("Компьютер решил взять ещё и ему выпала карта {}", CardUtils.toString(addCard2Player(1)));

            roundSum[0] = getFinalSum(0);
            roundSum[1] = getFinalSum(1);
            log.info("Сумма ваших очков - {}, компьютера - {}", roundSum[0], roundSum[1]);

            if (roundSum[0] > roundSum[1]) {
                playersMoney[0] += 20;
                log.info("Вы выиграли раунд! Получаете 10$");
            } else if (roundSum[0] < roundSum[1]) {
                playersMoney[1] += 20;
                log.info("Вы проиграли раунд! Теряете 10$");
            } else {
                log.info("Ничья!");
                playersMoney[0] += 10;
                playersMoney[1] += 10;
            }
        } while (playersMoney[0] > 0 && playersMoney[1] > 0 && cursor < 36);

        if (playersMoney[0] > 0)
            log.info("Вы выиграли! Поздравляем!");
        else
            log.info("Вы проиграли. Соболезнуем...");
    }

}
