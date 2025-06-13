public class TerminalRotator {
    private static class Node {
        String cityName;
        Node next;

        Node(String cityName) {
            this.cityName = cityName;
            this.next = null;
        }
    }

    private Node head;
    private Node current;

    public void initializeFromCityList(String[] cities) {
        if (cities.length == 0) return;

        head = new Node(cities[0]);
        Node prev = head;

        for (int i = 1; i < cities.length; i++) {
            Node newNode = new Node(cities[i]);
            prev.next = newNode;
            prev = newNode;
        }

        prev.next = head; // circular
        current = head;
    }

    public void advanceTerminal() {
        if (current != null) {
            current = current.next;
            Logger.logTerminalChange(current.cityName);
        }
    }

    public String getActiveTerminal() {
        return current != null ? current.cityName : null;
    }

    public void printTerminalOrder(int cycles) {
        if (head == null) return;
        Node temp = head;
        for (int i = 0; i < cycles; i++) {
            System.out.print(temp.cityName + " -> ");
            temp = temp.next;
        }
        System.out.println("...");
    }
}
