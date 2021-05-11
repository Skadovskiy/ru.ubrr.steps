package games;

import org.slf4j.Logger;

public class Slot {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Slot.class);
    public static void main(String... __) {
        int money = 100;       // личные деньги
        int bet = 10;          // ставка
        int jackpot = 1_000;   // приз
        int size = 7;          // диапазон случайных чисел
        int drums = 3;         // количество барабанов в игровом автомате
        boolean isWin = false; // флаг выигрыша


        while (money > 0) {
            int slot = 1;
            int buf = 0;
            String screen = "|";
            money = money - bet;
            isWin = false;
            for (int x = 0; x < drums; x++) {
                slot = (int) Math.round(Math.random() * 100) % (size - 1) + 1;
                isWin = (slot == buf && isWin) || x == 0;
                buf = slot;
                screen += Integer.toString(slot) + "|" ;
            }
            log.info("У Вас ${}, ставка - ${}. Крутим барабаны! Розыгрыш принёс следующие результаты:{}.", money, bet, screen);
            if (isWin) break;
        };

        if (isWin) {
            money += jackpot;
            log.info("Вы выиграли! Ваш приз - ${}, и ваш капитал теперь составляет: ${}", jackpot, money);
        } else
            log.info("Вы проиграли, соболезнуем...");
    }
}