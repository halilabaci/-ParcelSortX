public class ParcelTest {

    public static void main(String[] args) {
        System.out.println("*** ParcelTest.java ***");

        // Test 1: Parcel nesnesi düzgün oluşuyor mu onu test ediyorum
        Parcel parcel = new Parcel("P001", "Istanbul", 3, "Large", 5); // elimde örnek bir kargo nesnesi var

        // Şimdi bu nesnenin alanlarını kontrol ediyorum, doğru set edilmiş mi?
        assert parcel.getParcelID().equals("P001") : "Parcel ID hatalı";
        assert parcel.getDestinationCity().equals("Istanbul") : "Destination hatalı";
        assert parcel.getPriority() == 3 : "Priority hatalı";
        assert parcel.getSize().equals("Large") : "Size hatalı";
        assert parcel.getArrivalTick() == 5 : "ArrivalTick hatalı";
        assert parcel.getStatus() == Parcel.ParcelStatus.IN_QUEUE : "Başlangıç status hatalı";

        // Test 2: Status güncelleme doğru çalışıyor mu
        parcel.setStatus(Parcel.ParcelStatus.SORTED); // durumunu SORTED yapıyorum
        assert parcel.getStatus() == Parcel.ParcelStatus.SORTED : "Status güncellenemedi"; // değişti mi kontrol ediyorum

        // Test 3: toString() çıktısı doğru geliyor mu
        // Bu testin amacı: konsola bastığımda mantıklı bir çıktı görüyor muyum
        System.out.println("Parcel toString: " + parcel);

        // Test 4: Geçersiz boyut verilirse IllegalArgumentException fırlatmalı
        // Burada "Gigantic" gibi desteklenmeyen bir boyut veriyorum. Bu hataya düşmeli.
        boolean caught = false; // Önce hata yakalandı mı kontrolü için bir flag tanımladım
        try {
            new Parcel("P999", "Ankara", 1, "Gigantic", 10); // geçersiz boyut
        } catch (IllegalArgumentException e) {
            caught = true; // eğer exception yakalanırsa buraya girer
        }
        assert caught : "Geçersiz size kontrolü çalışmadı"; // eğer false kaldıysa exception fırlatılmamış demektir

        // Eğer buraya kadar geldiysem her şey yolunda demektir
        System.out.println(" Tüm testler başarılı.");
    }
}
