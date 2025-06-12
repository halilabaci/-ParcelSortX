public class ArrivalBuffer {

    private class Node {
        Parcel data;
        Node next;

        Node(Parcel data) {
            this.data = data;
        }
    }

    private Node head;
    private Node tail;
    private int capacity;
    private int size;

    public ArrivalBuffer(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    public boolean isFull() {
        return size >= capacity;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public boolean enqueue(Parcel parcel) {
        if (isFull()) {
            System.out.println("⚠️ Queue overflow: Parcel " + parcel.getParcelID() + " discarded.");
            // Logger.logWarning("Queue full. Parcel discarded: " + parcel.getParcelID());
            return false;
        }

        Node newNode = new Node(parcel);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
        return true;
    }

    public Parcel dequeue() {
        if (isEmpty()) {
            return null;
        }
        Parcel removed = head.data;
        head = head.next;
        size--;
        if (isEmpty()) {
            tail = null;
        }
        return removed;
    }

    public Parcel peek() {
        return isEmpty() ? null : head.data;
    }
}
