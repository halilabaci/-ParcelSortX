public class ParcelTest {

    public static void main(String[] args) {
        System.out.println("=== ParcelTest.java ===");

        // Test 1: Parcel nesnesi oluşturma
        Parcel parcel = new Parcel("P001", "Istanbul", 3, "Large", 5);

        assert parcel.getParcelID().equals("P001") : "Parcel ID hatalı";
        assert parcel.getDestinationCity().equals("Istanbul") : "Destination hatalı";
        assert parcel.getPriority() == 3 : "Priority hatalı";
        assert parcel.getSize().equals("Large"): "Size hatalı";
        assert parcel.getArrivalTick() == 5 : "ArrivalTick hatalı";
        assert parcel.getStatus() == Parcel.ParcelStatus.IN_QUEUE : "Başlangıç status hatalı";

        // Test 2: Status güncelleme
        parcel.setStatus(Parcel.ParcelStatus.SORTED);
        assert parcel.getStatus() == Parcel.ParcelStatus.SORTED : "Status güncellenemedi";

        // Test 3: toString çıktısı (gözle kontrol)
        System.out.println("Parcel toString: " + parcel);

        // Test 4: Geçersiz boyut verilirse hata atmalı
        // Sadece exception fırlatılıp fırlatılmadığını test et
        boolean caught = false; // Başta false atanır
        try {
            new Parcel("P999", "Ankara", 1, "Gigantic", 10);
        } catch (IllegalArgumentException e) {
            caught = true;
        }
        assert caught : "Geçersiz size kontrolü çalışmadı";

        System.out.println("✅ Tüm testler başarılı.");
    }
}
     