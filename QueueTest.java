public class QueueTest {

    public static void main(String[] args) {
        System.out.println("*** QueueTest.java ***");

        int capacity = 3; // Maksimum 3 parcel tutabilen bir kuyruk oluşturuyorum
        ArrivalBuffer buffer = new ArrivalBuffer(capacity);

        // Test 1: Kuyruğun başlangıç durumu 
        assert buffer.isEmpty() : "Kuyruk başlangıçta boş olmalı"; // boş mu hemen kontrol ettim
        assert buffer.size() == 0 : "Kuyruk boyutu 0 olmalı"; // boyutu da sıfır olmalı haliyle

        // Test 2: Enqueue işlemi - Kuyruğa eleman ekleme
        Parcel p1 = new Parcel("P001", "Ankara", 1, "Small", 1);
        Parcel p2 = new Parcel("P002", "Izmir", 2, "Medium", 2);
        Parcel p3 = new Parcel("P003", "Bursa", 3, "Large", 3);

        // 3 tane parcel'ı sırayla ekliyorum, FIFO kuralları geçerli
        assert buffer.enqueue(p1) : "P001 eklenemedi";
        assert buffer.enqueue(p2) : "P002 eklenemedi";
        assert buffer.enqueue(p3) : "P003 eklenemedi";

        assert buffer.isFull() : "Kuyruk dolu olmalı"; // Kapasiteye ulaştık mı kontrol
        assert buffer.size() == 3 : "Kuyruk boyutu 3 olmalı";

        // Test 3: Overflow durumu - kapasiteyi aşmaya çalışıyorum 
        Parcel p4 = new Parcel("P004", "Istanbul", 2, "Medium", 4);
        assert !buffer.enqueue(p4) : "Taşma kontrolü başarısız"; // kapasite doluyken eklenmemeli

        //  Test 4: Peek ve Dequeue işlemleri 
        // FIFO mantığıyla önce gelen ilk çıkar: P001
        assert buffer.peek().getParcelID().equals("P001") : "Peek yanlış değer döndürüyor";
        assert buffer.dequeue().getParcelID().equals("P001") : "Dequeue P001 döndürmeli";
        assert buffer.size() == 2 : "Kuyruk boyutu 2 olmalı"; // bir eleman çıktı, iki kaldı

        // Test 5: Kalan elemanları çıkarıyorum
        assert buffer.dequeue().getParcelID().equals("P002") : "P002 eksik";
        assert buffer.dequeue().getParcelID().equals("P003") : "P003 eksik";

        // Şimdi kuyruk tamamen boş olmalı
        assert buffer.isEmpty() : "Kuyruk boş olmalı";
        assert buffer.dequeue() == null : "Boş kuyruktan null dönmeli"; // Boşken dequeue yaparsam null dönmeli

        System.out.println(" ArrivalBuffer için tüm testler başarılı.");
    }
}
