public class ParcelTest {

    public static void main(String[] args) {
        System.out.println("=== ParcelTest.java ===");

        // Test 1: Parcel nesnesi oluşturma
        Parcel parcel = new Parcel("P001", "Istanbul", 3, Parcel.Size.LARGE, 5);

        assert parcel.getParcelID().equals("P001") : "Parcel ID hatalı";
        assert parcel.getDestinationCity().equals("Istanbul") : "Destination hatalı";
        assert parcel.getPriority() == 3 : "Priority hatalı";
        assert parcel.getSize() == Parcel.Size.LARGE : "Size hatalı";
        assert parcel.getArrivalTick() == 5 : "ArrivalTick hatalı";
        assert parcel.getStatus() == Parcel.ParcelStatus.IN_QUEUE : "Başlangıç status hatalı";

        // Test 2: Status güncelleme
        parcel.setStatus(Parcel.ParcelStatus.SORTED);
        assert parcel.getStatus() == Parcel.ParcelStatus.SORTED : "Status güncellenemedi";

        // Test 3: toString çıktısı (gözle kontrol)
        System.out.println("Parcel toString: " + parcel);

        System.out.println("✅ Tüm testler başarılı.");
    }
}
//         return String.format("Parcel[%s to %s | Priority: %d | Size: %s | Tick: %d | Status: %s]",
//                 parcelID, destinationCity, priority, size, arrivalTick, status);
//     }
//     }
//
//     }
//
//         return false;
//     }
//
//     public ParcelRecord get(String parcelID) {
//         int index = hash(parcelID);
//         for (Entry e : table[index]) {       