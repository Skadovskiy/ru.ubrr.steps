package games;

import org.slf4j.Logger;

import static games.CardUtils.*;

public class Drunkard {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Drunkard.class);
    private static int incrementIndex(int i) {
        return (i + 1) % CARDS_TOTAL_COUNT;
    }

    private static boolean playerCardsIsEmpty() {
        var isWin = false;
        for (var i = 0; i < PLAYERS_COUNT; i++) {
            isWin = playerCardTails[i] == playerCardHeads[i];
            log.info("У игрока №{} {} карт, ", i + 1, playerCardCount[i]);
        }
        log.info(" ");
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
            for (var i = 0; i < CARDS_TOTAL_COUNT; i++)
                playersCards[j][i] = -1;
            for (var i = 0; i < CARDS_IN_HAND; i++)
                playersCards[j][i] = CardDeck[x++];

            playerCardTails[j] = 0;              // Указатель на ячейку в массиве, с которой начинаются карты
            playerCardHeads[j] = CARDS_IN_HAND;  // Указатель на ячейку, в которую мы будем добавлять карты
            playerCardCount[j] = CARDS_IN_HAND;  // Количество карт у каждого игрока
        }
    }

    public static void main(String... __) {
        // раздача карт
        playersCardsInit();

        var winner = -1;
        var x = 0;
        do {
            ++x;
            log.info("Итерация №{}: ", x);

            var distributionCards = new int[PLAYERS_COUNT];
            for (var i = 0; i < PLAYERS_COUNT; i++) {
                distributionCards[i] = playersCards[i][playerCardTails[i]]; // снимаем у каждого по карте
                playersCards[i][playerCardTails[i]] = -1;                   // аннулируем (не обязательно, просто так наглядней в отладке)
                playerCardTails[i] = incrementIndex(playerCardTails[i]);
                playerCardCount[i]--;
                log.info("игрок №{} карта: {}; ", i, CardUtils.toString(distributionCards[i]));
            }
            log.info("\n");

            // определение победителя, работает только на 2х игроках...
            winner = 0;
            if (distributionCards[0] % PARS_TOTAL_COUNT == distributionCards[1] % PARS_TOTAL_COUNT)
                winner = -1;
            if (distributionCards[0] % PARS_TOTAL_COUNT < distributionCards[1] % PARS_TOTAL_COUNT)
                winner = 1;
            if (getPar(distributionCards[0]) == Par.ACE && getPar(distributionCards[1]) == Par.SIX)
                winner = 1;

            if (winner == -1) { // если нет победителя возвращаем карты игрокам (не учитываем спор... )
                for (var playerid = 0; playerid < distributionCards.length; playerid++) {
                    playersCards[playerid][playerCardHeads[playerid]] = distributionCards[playerid];
                    playerCardHeads[playerid] = incrementIndex(playerCardHeads[playerid]);
                    playerCardCount[playerid]++;
                }
                log.info("Победитель не определен!");
            } else {           // раздачу забирает победитель
                for (var playerid = 0; playerid < distributionCards.length; playerid++) {
                    playersCards[winner][playerCardHeads[winner]] = distributionCards[playerid];
                    playerCardHeads[winner] = incrementIndex(playerCardHeads[winner]);
                    playerCardCount[winner]++;
                }
                log.info("Выиграл игрок №{}!", winner);
            }

            if (x == 3000) break;
        } while (!playerCardsIsEmpty());

        log.info("Выиграл игрок №{}! Количество произведённых итераций: {}.", winner, x);
    }
}


































