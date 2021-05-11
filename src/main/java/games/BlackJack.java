package games;

import java.io.IOException;

import static games.CardUtils.*;
import static games.Choice.getCharacterFromUser;

public class BlackJack {
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
        System.out.println("\nУ Вас " + playersMoney[0] + "$, у компьютера - " + playersMoney[1] + "$. Начинаем новый раунд!");
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
        System.out.println(message + " \"Y\" - Да, {любой другой символ} - нет (Чтобы выйти из игры, нажмите Ctrl + C)");
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

            System.out.printf("Вам выпала карта %s\n", CardUtils.toString(addCard2Player(0)));
            System.out.printf("Вам выпала карта %s\n", CardUtils.toString(addCard2Player(0)));

            if (sum(0) < 20)
                while (confirm("Берём ещё?") && sum(0) < 20)
                    System.out.printf("Вам выпала карта %s\n", CardUtils.toString(addCard2Player(0)));

            System.out.printf("Компьютеру выпала карта %s\n", CardUtils.toString(addCard2Player(1)));
            System.out.printf("Компьютеру выпала карта %s\n", CardUtils.toString(addCard2Player(1)));
            if (sum(0) < 17)
                while (sum(1) < 17)
                    System.out.printf("Компьютер решил взять ещё и ему выпала карта %s\n", CardUtils.toString(addCard2Player(1)));

            roundSum[0] = getFinalSum(0);
            roundSum[1] = getFinalSum(1);
            System.out.printf("Сумма ваших очков - %d, компьютера - %d\n", roundSum[0], roundSum[1]);

            if (roundSum[0] > roundSum[1]) {
                playersMoney[0] += 20;
                System.out.println("Вы выиграли раунд! Получаете 10$");
            } else if (roundSum[0] < roundSum[1]) {
                playersMoney[1] += 20;
                System.out.println("Вы проиграли раунд! Теряете 10$");
            } else {
                System.out.println("Ничья!");
                playersMoney[0] += 10;
                playersMoney[1] += 10;
            }
        } while (playersMoney[0] > 0 && playersMoney[1] > 0 && cursor < 36);

        if (playersMoney[0] > 0)
            System.out.println("Вы выиграли! Поздравляем!");
        else
            System.out.println("Вы проиграли. Соболезнуем...");
    }

}
