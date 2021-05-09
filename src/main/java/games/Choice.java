package games;

public class Choice {
    public static void main(String... __)  {
        System.out.println("Выберите игру:\n1 - \"однорукий бандит\", 2 - \"пьяница\"");
        try {
            switch (System.in.read()) {
                case '1': Slot.main(); break;
                case '2': Drunkard.main(); break;
                default: System.out.println("Игры с таким номером нет!");
            }
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
