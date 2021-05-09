package games;

import org.apache.commons.math3.util.MathArrays;

public class Drunkard {
    private static final int PARS_TOTAL_COUNT = Par.values().length;
    private static final int CARDS_TOTAL_COUNT = PARS_TOTAL_COUNT * Suit.values().length;
    private static final int PLAYERS_COUNT = 2;
    private static final int CARDS_IN_HAND = CARDS_TOTAL_COUNT / PLAYERS_COUNT;

    private static int[] CardDeck = new int[CARDS_TOTAL_COUNT];
    private static int[][] playersCards = new int[PLAYERS_COUNT][CARDS_TOTAL_COUNT];
    //private static int[] players = new int[PLAYERS_COUNT];
    private static int[] playerCardTails = new int[PLAYERS_COUNT];
    private static int[] playerCardHeads = new int[PLAYERS_COUNT];
    private static int[] playerCardCount = new int[PLAYERS_COUNT];

    // Масть
    enum Suit {
        SPADES,   // пики
        HEARTS,   // червы
        CLUBS,    // трефы
        DIAMONDS  // бубны
    }

    // Наминал
    enum Par {
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,   // Валет
        QUEEN,  // Дама
        KING,   // Король
        ACE     // Туз
    }

    private static Suit getSuit(int cardNumber) {
        return Suit.values()[cardNumber / PARS_TOTAL_COUNT];
    }

    private static Par getPar(int cardNumber) {
        return Par.values()[cardNumber % PARS_TOTAL_COUNT];
    }

    private static String toString(int cardNumber) {
        return getPar(cardNumber) + " " + getSuit(cardNumber);
    }

    private static int incrementIndex(int i) {
        return (i + 1) % CARDS_TOTAL_COUNT;
    }

    private static boolean playerCardsIsEmpty() {
        boolean isWin = false;
        for (int i = 0; i < PLAYERS_COUNT; i++) {
            isWin = playerCardTails[i] == playerCardHeads[i];
            System.out.printf("У игрока №%d %d карт, ", i + 1, playerCardCount[i]);
        }
        System.out.println();
        return isWin;
    }

    // распечатали калоду
    public static void cardDeckInit() {
        for (int i = 0; i < CARDS_TOTAL_COUNT; i++) {
            CardDeck[i] = i;
        }
    }

    // раздача карт
    public static void playersCardsInit() {
        for (int j = 0, x = 0; j < PLAYERS_COUNT; j++) {
            for (int i = 0; i < CARDS_TOTAL_COUNT; i++)
                playersCards[j][i] = -1;
            for (int i = 0; i < CARDS_IN_HAND; i++)
                playersCards[j][i] = CardDeck[x++];
            //players[j] = 1;                    // 1 - означает что игрок в игре, 0 - пропускает раздачу пока остальные осуществляют спор
            playerCardTails[j] = 0;              // Указатель на ячейку в массиве, с которой начинаются карты
            playerCardHeads[j] = CARDS_IN_HAND;  // Указатель на ячейку, в которую мы будем добавлять карты
            playerCardCount[j] = CARDS_IN_HAND;  // Количество карт у каждого игрока
        }
    }

    public static void main(String... __) {
        // распечатали калоду
        cardDeckInit();

        // тасуем карты
        MathArrays.shuffle(CardDeck);

        // раздача карт
        playersCardsInit();

        int winner = -1;
        int x = 0;
        while (!playerCardsIsEmpty()) {
            ++x;
            System.out.printf("Итерация №%d: ", x);

            int[] distributionCards = new int[PLAYERS_COUNT];
            for (int j = 0; j < PLAYERS_COUNT; j++) {
                distributionCards[j] = playersCards[j][playerCardTails[j]]; // снимаем у каждого по карте
                playersCards[j][playerCardTails[j]] = -1;                   // аннулируем (не обязательно, просто так наглядней в отладке)
                playerCardTails[j] = incrementIndex(playerCardTails[j]);
                playerCardCount[j]--;
                System.out.printf("игрок №%d карта: %s; ", j, toString(distributionCards[j]));
            }
            System.out.println();

            // определение победителя, работает только на 2х игроках...
            winner = 0;
            if (distributionCards[0] % PARS_TOTAL_COUNT == distributionCards[1] % PARS_TOTAL_COUNT)
                winner = -1;
            if (distributionCards[0] % PARS_TOTAL_COUNT < distributionCards[1] % PARS_TOTAL_COUNT)
                winner = 1;
            if (getPar(distributionCards[0]) == Par.ACE & getPar(distributionCards[1]) == Par.SIX)
                winner = 1;

            if (winner == -1) { // если нет победителя возвращаем карты игрокам (не учитываем спор... )
                for (int j = 0; j < distributionCards.length; j++) {
                    playersCards[j][playerCardHeads[j]] = distributionCards[j];
                    playerCardHeads[j] = incrementIndex(playerCardHeads[j]);
                    playerCardCount[j]++;
                }
                System.out.print("Победитель не определен!\n");
            } else {           // раздачу забирает победитель
                for (int j = 0; j < distributionCards.length; j++) {
                    playersCards[winner][playerCardHeads[winner]] = distributionCards[j];
                    playerCardHeads[winner] = incrementIndex(playerCardHeads[winner]);
                    playerCardCount[winner]++;
                }
                System.out.printf("Выиграл игрок №%d!\n", winner);
            }

            if (x == 3000) break;
        }

        System.out.printf("Выиграл игрок №%d! Количество произведённых итераций: %d.\n", winner, x);
    }
}


































