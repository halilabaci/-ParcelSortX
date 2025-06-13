import java.util.NoSuchElementException;

public class ReturnStack {
    private static class Node {
        Parcel data;
        Node next;

        Node(Parcel data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node top;
    private int size;

    public ReturnStack() {
        top = null;
        size = 0;
    }

    public void push(Parcel parcel) {
        Node newNode = new Node(parcel);
        newNode.next = top;
        top = newNode;
        size++;
        Logger.logReturnEvent(parcel);
    }

    public Parcel pop() {
        if (isEmpty()) throw new NoSuchElementException("ReturnStack is empty");
        Parcel parcel = top.data;
        top = top.next;
        size--;
        return parcel;
    }

    public Parcel peek() {
        if (isEmpty()) throw new NoSuchElementException("ReturnStack is empty");
        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }
}
