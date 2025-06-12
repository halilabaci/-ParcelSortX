public class QueueTest {

    public static void main(String[] args) {
        System.out.println("=== QueueTest.java ===");

        int capacity = 3;
        ArrivalBuffer buffer = new ArrivalBuffer(capacity);

        // Test 1: Başlangıç durumu
        assert buffer.isEmpty() : "Kuyruk başlangıçta boş olmalı";
        assert buffer.size() == 0 : "Kuyruk boyutu 0 olmalı";

        // Test 2: Enqueue işlemi
        Parcel p1 = new Parcel("P001", "Ankara", 1, Parcel.Size.SMALL, 1);
        Parcel p2 = new Parcel("P002", "Izmir", 2, Parcel.Size.MEDIUM, 2);
        Parcel p3 = new Parcel("P003", "Bursa", 3, Parcel.Size.LARGE, 3);

        assert buffer.enqueue(p1) : "P001 eklenemedi";
        assert buffer.enqueue(p2) : "P002 eklenemedi";
        assert buffer.enqueue(p3) : "P003 eklenemedi";

        assert buffer.isFull() : "Kuyruk dolu olmalı";
        assert buffer.size() == 3 : "Kuyruk boyutu 3 olmalı";

        // Test 3: Overflow durumu
        Parcel p4 = new Parcel("P004", "Istanbul", 2, Parcel.Size.MEDIUM, 4);
        assert !buffer.enqueue(p4) : "Taşma kontrolü başarısız";

        // Test 4: Peek ve Dequeue işlemleri
        assert buffer.peek().getParcelID().equals("P001") : "Peek yanlış değer döndürüyor";
        assert buffer.dequeue().getParcelID().equals("P001") : "Dequeue P001 döndürmeli";
        assert buffer.size() == 2 : "Kuyruk boyutu 2 olmalı";

        // Test 5: Geri kalan elemanlar
        assert buffer.dequeue().getParcelID().equals("P002") : "P002 eksik";
        assert buffer.dequeue().getParcelID().equals("P003") : "P003 eksik";
        assert buffer.isEmpty() : "Kuyruk boş olmalı";
        assert buffer.dequeue() == null : "Boş kuyruktan null dönmeli";

        System.out.println("✅ ArrivalBuffer için tüm testler başarılı.");
    }
}

