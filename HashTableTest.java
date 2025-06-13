public class HashTableTest {

    public static void main(String[] args) {
        System.out.println("=== HashTableTest.java ===");

        ParcelTracker tracker = new ParcelTracker();

        // Test 1: Insert işlemi
        Parcel p1 = new Parcel("P001", "Izmir", 2, "Medium", 10);
        tracker.insert(p1.getParcelID(), p1);

        assert tracker.exists("P001") : "P001 eklenmemiş görünüyor";

        ParcelTracker.ParcelRecord record = tracker.get("P001");
        assert record != null : "P001 için kayıt bulunamadı";
        assert record.destinationCity.equals("Izmir") : "Destination yanlış";
        assert record.status == Parcel.ParcelStatus.IN_QUEUE : "Status yanlış";
        assert record.returnCount == 0 : "Return count başlangıçta 0 olmalı";
        assert record.size.equals("Medium") : "Size yanlış (String olmalı)";

        // Test 2: Status güncelleme
        tracker.updateStatus("P001", Parcel.ParcelStatus.SORTED);
        assert tracker.get("P001").status == Parcel.ParcelStatus.SORTED : "Status güncellenemedi";

        // Test 3: Return sayacı artırma
        tracker.incrementReturnCount("P001");
        tracker.incrementReturnCount("P001");
        assert tracker.get("P001").returnCount == 2 : "Return sayacı 2 olmalı";

        // Test 4: Dispatch tick atama
        tracker.setDispatchTick("P001", 25);
        assert tracker.get("P001").dispatchTick == 25 : "Dispatch tick hatalı";

        // Test 5: Duplicate insert engeli
        tracker.insert("P001", p1);  // Aynı ID tekrar eklenmemeli
        assert tracker.get("P001").returnCount == 2 : "Duplicate insert overwrite yapmamalı";

        // Test 6: Olmayan parcel için get
        assert tracker.get("P999") == null : "Olmayan parcel için null dönmeli";

        System.out.println("✅ ParcelTracker için tüm testler başarılı.");
    }
}

