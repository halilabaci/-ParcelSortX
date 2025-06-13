public class RotatorTest {
    public static void main(String[] args) {
        TerminalRotator rotator = new TerminalRotator();
        String[] cities = {"Istanbul", "Ankara", "Izmir"};

        rotator.initializeFromCityList(cities);
        assert rotator.getActiveTerminal().equals("Istanbul");

        rotator.advanceTerminal();
        assert rotator.getActiveTerminal().equals("Ankara");

        rotator.advanceTerminal();
        assert rotator.getActiveTerminal().equals("Izmir");

        rotator.advanceTerminal();
        assert rotator.getActiveTerminal().equals("Istanbul");

        System.out.println("RotatorTest passed.");
    }
}


