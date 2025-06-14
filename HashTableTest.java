public class HashTableTest {

    public static void main(String[] args) {
        System.out.println("*** HashTableTest.java ***");

        // Önce bir ParcelTracker örneği oluşturuyorum (hash tablom burada)
        ParcelTracker tracker = new ParcelTracker();

        // Test 1: Insert işlemi doğru çalışıyor mu
        Parcel p1 = new Parcel("P001", "Izmir", 2, "Medium", 10); // bir örnek kargo oluşturuyorum
        tracker.insert(p1.getParcelID(), p1); // bunu hash tablosuna ekliyorum

        // Eklenmiş mi kontrol ediyorum
        assert tracker.exists("P001") : "P001 eklenmemiş görünüyor";

        // Kargo kaydı doğru alınabiliyor mu
        ParcelTracker.ParcelRecord record = tracker.get("P001");
        assert record != null : "P001 için kayıt bulunamadı";
        assert record.destinationCity.equals("Izmir") : "Destination yanlış";
        assert record.status == Parcel.ParcelStatus.IN_QUEUE : "Status yanlış"; // varsayılan durum bu olmalı
        assert record.returnCount == 0 : "Return count başlangıçta 0 olmalı";
        assert record.size.equals("Medium") : "Size yanlış (String olmalı)";

        // Test 2: Status güncelleyebiliyor muyum
        tracker.updateStatus("P001", Parcel.ParcelStatus.SORTED);
        assert tracker.get("P001").status == Parcel.ParcelStatus.SORTED : "Status güncellenemedi";

        // Test 3: Return sayacı düzgün artıyor mu
        tracker.incrementReturnCount("P001"); // 1 oldu
        tracker.incrementReturnCount("P001"); // 2 oldu
        assert tracker.get("P001").returnCount == 2 : "Return sayacı 2 olmalı";

        // Test 4: Dispatch tick değeri doğru atanıyor mu
        tracker.setDispatchTick("P001", 25);
        assert tracker.get("P001").dispatchTick == 25 : "Dispatch tick hatalı";

        // Test 5: Aynı ID ile tekrar ekleme yapılabiliyor mu
        // Burada amacım şu: Aynı ID tekrar eklenirse veri ezilmemeli
        tracker.insert("P001", p1);  // Bu ekleme yoksayılmalı
        assert tracker.get("P001").returnCount == 2 : "Duplicate insert overwrite yapmamalı";

        // Test 6: Olmayan bir ID ile erişim denemesi 
        assert tracker.get("P999") == null : "Olmayan parcel için null dönmeli";

        // Tüm testler sorunsuz geçtiyse mesaj basıyorum
        System.out.println(" ParcelTracker için tüm testler başarılı.");
    }
}
