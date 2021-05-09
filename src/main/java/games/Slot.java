package games;

public class Slot {
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
                isWin = (slot == buf & isWin) | x == 0;
                buf = slot;
                screen = screen + Integer.toString(slot) + "|" ;
            }
            System.out.printf("У Вас $%-3d, ставка - $%-3d. Крутим барабаны! Розыгрыш принёс следующие результаты: %s.\n", money, bet, screen);
            if (isWin) break;
        }
        if (isWin) {
            money = money + jackpot;
            System.out.printf("Вы выиграли! Ваш приз - $%d, и ваш капитал теперь составляет: $%d", jackpot, money);
        } else
            System.out.printf("Вы проиграли, соболезнуем...");
    }
}