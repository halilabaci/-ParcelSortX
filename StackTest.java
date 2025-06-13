public class StackTest {
    public static void main(String[] args) {
        ReturnStack stack = new ReturnStack();

        Parcel p1 = new Parcel("P1", "Ankara", 2, "Medium", 1);
        Parcel p2 = new Parcel("P2", "Izmir", 1, "Large", 2);

        assert stack.isEmpty();
        stack.push(p1);
        stack.push(p2);

        assert stack.size() == 2;
        assert stack.peek().equals(p2);
        assert stack.pop().equals(p2);
        assert stack.pop().equals(p1);
        assert stack.isEmpty();

        System.out.println("StackTest passed.");
    }
}
