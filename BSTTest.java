public class BSTTest {

    public static void main(String[] args) {
        System.out.println("*** BSTTest.java ***");

        // Öncelikle DestinationSorter sınıfından bir tane oluşturuyorum.
        // Bu sınıf aslında BST yapısında çalışıyor.
        DestinationSorter sorter = new DestinationSorter();

        // Testlerde kullanacağım parcel nesnelerini oluşturdum.
        // Her biri farklı şehir ve ID'ye sahip.
        Parcel p1 = new Parcel("P001", "Izmir", 2, "Medium", 1);
        Parcel p2 = new Parcel("P002", "Ankara", 1, "Small", 2);
        Parcel p3 = new Parcel("P003", "Istanbul", 3, "Large", 3);
        Parcel p4 = new Parcel("P004", "Izmir", 2, "Medium", 4);
        Parcel p5 = new Parcel("P005", "Ankara", 1, "Small", 5);

        // Test 1: insertParcel 
        // Oluşturduğum parcel’ları BST'ye ekliyorum.
        // Aynı şehirden birden fazla parcel olabileceğini test edebilmek için bazı şehirler tekrar kullanıldı.
        sorter.insertParcel(p1);
        sorter.insertParcel(p2);
        sorter.insertParcel(p3);
        sorter.insertParcel(p4);
        sorter.insertParcel(p5);

        // Test 2: countCityParcels 
        // Belirli şehirler için eklenmiş parcel sayısını kontrol ediyorum.
        // Yukarıda Izmir 2 kere, Ankara 2 kere, Istanbul 1 kere eklenmişti.
        assert sorter.countCityParcels("Izmir") == 2 : "Izmir için 2 parcel bekleniyor";
        assert sorter.countCityParcels("Ankara") == 2 : "Ankara için 2 parcel bekleniyor";
        assert sorter.countCityParcels("Istanbul") == 1 : "Istanbul için 1 parcel bekleniyor";

        // Test 3: removeParcel
        // Şimdi bir parcel'ı kaldırmayı deniyorum (P001 - Izmir).
        // Hem kaldırma işleminin başarılı olması, hem de kalan sayının azalması gerekiyor.
        boolean removed = sorter.removeParcel("Izmir", "P001");
        assert removed : "P001 kaldırılmalıydı";
        assert sorter.countCityParcels("Izmir") == 1 : "Izmir’de artık 1 parcel olmalı";

        // Test 4: getCityParcels 
        // Şehir bazında parcel listesini getirme fonksiyonu test ediliyor.
        // Istanbul için sadece P003 vardı, onun dönmesini bekliyorum.
        var istList = sorter.getCityParcels("Istanbul");
        assert istList != null && istList.get(0).getParcelID().equals("P003") : "Istanbul P003 içermeli";

        // Test 5: inOrderTraversal 
        // Ağacı inorder (alfabetik şehir sırasıyla) dolaşıp çıktı verdiriyorum.
        // Gözle kontrol edeceğim: Ankara → Istanbul → Izmir sıralaması
        System.out.println("\n[InOrder Traversal]");
        sorter.inOrderTraversal();

        // Test 6: BST yüksekliği ve en yoğun şehir
        // Ağacın yüksekliğini alıyorum.
        // Ayrıca hangi şehirde en çok parcel var onu da getiriyorum.
        int height = sorter.getHeight();
        System.out.println("\nBST Height: " + height);
        String maxCity = sorter.getCityWithMaxLoad();
        System.out.println("Most loaded city: " + maxCity);

        // Ağaç yüksekliği en az 1 olmalı çünkü en az bir şehir eklendi
        // En yoğun şehrin null olmaması da bekleniyor.
        assert height >= 1 : "Ağaç yüksekliği 1 veya daha fazla olmalı";
        assert maxCity != null : "Yoğun şehir null olamaz";

        System.out.println("\n DestinationSorter için tüm testler başarılı.");
    }
}
