public class ArrivalBuffer {

    // Bağlı listenin düğümünü temsil edecek iç sınıf
    private class Node {
        Parcel data;   // Düğümdeki kargo (parcel) verisi
        Node next;     // Bir sonraki düğüme işaretçi

        Node(Parcel data) {
            this.data = data; // Kurucu ile datayı set ediyorum
        }
    }

    // Kuyruğun başı ve sonu
    private Node head;
    private Node tail;

    // Kapasite: kuyruğun alabileceği maksimum parcel sayısı
    // Size: şu an kuyrukta kaç parcel var onu tutuyorum
    private int capacity;
    private int size;

    // Kurucu metod - kuyruk kapasitesini alıyor
    public ArrivalBuffer(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.head = null;
        this.tail = null;  // Başlangıçta kuyruk tamamen boş
    }

    // Kuyruk dolu mu diye kontrol eden metod
    public boolean isFull() {
        return size >= capacity;
    }

    // Kuyruk boş mu diye kontrol eden metod
    public boolean isEmpty() {
        return size == 0;
    }

    // Anlık kuyruk boyutunu döndürür
    public int size() {
        return size;
    }

    // Kuyruğa yeni bir parcel eklemeye çalışıyorum
    public boolean enqueue(Parcel parcel) {
        // Eğer kuyruk doluysa ekleme işlemini yapamıyorum
        if (isFull()) {
            System.out.println("Queue overflow: Parcel " + parcel.getParcelID() + " discarded.");
            return false;
        }

        // Yeni bir node oluşturuyorum
        Node newNode = new Node(parcel);

        // Eğer kuyruk boşsa, hem başı hem sonu bu yeni node yapıyorum
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            // Kuyruk boş değilse, tail’in next’ini yeni node yapıyorum
            tail.next = newNode;
            tail = newNode; // ve tail’i güncelliyorum
        }

        size++; // Kuyruğa eleman eklendiği için boyutu artırıyorum
        return true;
    }

    // Kuyruktan eleman çıkarıyorum (FIFO mantığı)
    public Parcel dequeue() {
        // Eğer kuyruk boşsa çıkaracak bir şey yok
        if (isEmpty()) {
            return null;
        }

        // Head’teki veriyi alıyorum (çıkarılacak parcel)
        Parcel removed = head.data;

        // Başı bir ileriye kaydırıyorum
        head = head.next;

        // Boyutu azaltıyorum
        size--;

        // Eğer çıkarma sonrası kuyruk boş kaldıysa, tail’i de null yapıyorum
        if (isEmpty()) {
            tail = null;
        }

        return removed; // Çıkardığım parcel’ı döndürüyorum
    }

    // Kuyruğun en başındaki elemanı (head) getiriyor ama çıkarmıyor
    public Parcel peek() {
        return isEmpty() ? null : head.data;
    }
}
