public class BSTTest {

    public static void main(String[] args) {
        System.out.println("=== BSTTest.java ===");

        DestinationSorter sorter = new DestinationSorter();

        // Parcel nesneleri
        Parcel p1 = new Parcel("P001", "Izmir", 2, "Medium", 1);
        Parcel p2 = new Parcel("P002", "Ankara", 1, "Small", 2);
        Parcel p3 = new Parcel("P003", "Istanbul", 3, "Large", 3);
        Parcel p4 = new Parcel("P004", "Izmir", 2, "Medium", 4);
        Parcel p5 = new Parcel("P005", "Ankara", 1, "Small", 5);

        // Test 1: insertParcel
        sorter.insertParcel(p1);
        sorter.insertParcel(p2);
        sorter.insertParcel(p3);
        sorter.insertParcel(p4);
        sorter.insertParcel(p5);

        // Test 2: countCityParcels
        assert sorter.countCityParcels("Izmir") == 2 : "Izmir için 2 parcel bekleniyor";
        assert sorter.countCityParcels("Ankara") == 2 : "Ankara için 2 parcel bekleniyor";
        assert sorter.countCityParcels("Istanbul") == 1 : "Istanbul için 1 parcel bekleniyor";

        // Test 3: removeParcel
        boolean removed = sorter.removeParcel("Izmir", "P001");
        assert removed : "P001 kaldırılmalıydı";
        assert sorter.countCityParcels("Izmir") == 1 : "Izmir’de artık 1 parcel olmalı";

        // Test 4: getCityParcels
        var istList = sorter.getCityParcels("Istanbul");
        assert istList != null && istList.get(0).getParcelID().equals("P003") : "Istanbul P003 içermeli";

        // Test 5: inOrderTraversal (çıktıyı gözle kontrol et)
        System.out.println("\n[InOrder Traversal]");
        sorter.inOrderTraversal();

        // Test 6: BST yüksekliği ve yoğun şehir
        int height = sorter.getHeight();
        System.out.println("\nBST Height: " + height);
        String maxCity = sorter.getCityWithMaxLoad();
        System.out.println("Most loaded city: " + maxCity);

        assert height >= 1 : "Ağaç yüksekliği 1 veya daha fazla olmalı";
        assert maxCity != null : "Yoğun şehir null olamaz";

        System.out.println("\n✅ DestinationSorter için tüm testler başarılı.");
    }
}

