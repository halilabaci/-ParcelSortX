import java.util.*;

public class SimulationTest {

    public static void main(String[] args) {
        System.out.println("*** SimulationTest.java ***");

        // Şehir listesini belirliyorum – bu küçük simülasyonda sadece Ankara ve Izmir var.
        final List<String> CITY_LIST = Arrays.asList("Ankara", "Izmir");

        // Kuyruğun kapasitesini 5 olarak sınırladım ki küçük testte taşma ihtimali olsun.
        final int QUEUE_CAPACITY = 5;

        // Simülasyon için gerekli veri yapıları – hepsinin sade versiyonlarını kullanıyorum.
        ArrivalBuffer queue = new ArrivalBuffer(QUEUE_CAPACITY);
        ParcelTracker tracker = new ParcelTracker(); // parcel durumlarını izlemek için
        DestinationSorter sorter = new DestinationSorter(); // BST yapısı
        ReturnStack stack = new ReturnStack(); // misroute’lar buraya geliyor
        TerminalRotator rotator = new TerminalRotator(CITY_LIST); // terminal şehirlerini döndürüyor

        // Random sınıfı sayesinde her testte farklı sonuçlar elde edebiliyorum.
        Random rand = new Random();

        // İstatistikleri tutmak için birkaç sayaç tanımladım.
        int tick = 0;
        int totalGenerated = 0;
        int totalDispatched = 0;
        int totalMisrouted = 0;

        // Simülasyonu toplam 5 tick süresince çalıştıracağım – bu kısa bir test.
        while (tick < 5) {
            tick++;
            System.out.println("\n[Tick " + tick + "]");

            //  1. Yeni parcel üretimi 
            // Her tick’te 1 veya 2 parcel üretiliyor.
            int parcelCount = 1 + rand.nextInt(2); // 1 veya 2 parcel üret
            for (int i = 0; i < parcelCount; i++) {
                // Parcel ID'leri PX1, PX2, ... şeklinde gidiyor
                String id = "PX" + (totalGenerated + 1);
                // Rastgele hedef şehir seçiyorum
                String city = CITY_LIST.get(rand.nextInt(CITY_LIST.size()));
                // Sabit öncelik ve boyut veriyorum (testi basit tutmak için)
                Parcel p = new Parcel(id, city, 1, "Small", tick);

                // Kuyruğa ekliyorum, yer varsa başarıyla ekleniyor
                if (queue.enqueue(p)) {
                    tracker.insert(id, p);
                    totalGenerated++;
                    System.out.println("Generated: " + p);
                }
            }

            //  2. Kuyruktan BST'ye taşıma 
            while (!queue.isEmpty()) {
                Parcel p = queue.dequeue();
                tracker.updateStatus(p.getParcelID(), Parcel.ParcelStatus.SORTED);
                sorter.insertParcel(p);
                System.out.println("Sorted: " + p.getParcelID() + " → " + p.getDestinationCity());
            }

            //  3. ReturnStack’ten yeniden ekleme (retry)
            // Stack’te varsa en fazla 2 parcel tekrar deneniyor.
            int retry = Math.min(2, stack.size());
            for (int i = 0; i < retry; i++) {
                Parcel r = stack.pop();
                if (r != null) {
                    tracker.updateStatus(r.getParcelID(), Parcel.ParcelStatus.SORTED);
                    sorter.insertParcel(r);
                    System.out.println("Retrying from stack: " + r.getParcelID());
                }
            }

            //  4. Dispatch veya misroute işlemi 
            // Aktif terminaldeki şehirde parcel varsa, onu işlemeye çalışıyorum.
            String activeCity = rotator.getActiveTerminal();
            LinkedList<Parcel> parcels = sorter.getCityParcels(activeCity);

            if (parcels != null && !parcels.isEmpty()) {
                Parcel dispatch = parcels.peek(); // sıradaki parcel

                // %50 ihtimalle parcel yanlış yönlendirilecek (misroute)
                boolean misrouted = rand.nextBoolean();

                if (misrouted) {
                    tracker.updateStatus(dispatch.getParcelID(), Parcel.ParcelStatus.RETURNED);
                    tracker.incrementReturnCount(dispatch.getParcelID());
                    stack.push(dispatch);
                    totalMisrouted++;
                    System.out.println("MISROUTED → Stack: " + dispatch.getParcelID());
                } else {
                    tracker.updateStatus(dispatch.getParcelID(), Parcel.ParcelStatus.DISPATCHED);
                    sorter.removeParcel(activeCity, dispatch.getParcelID());
                    System.out.println("Dispatched: " + dispatch.getParcelID() + " to " + activeCity);
                    totalDispatched++;
                }
            }

            // 5. Terminal rotasyonu 
            // Her 2 tick’te bir sıradaki terminale geçiyoruz.
            if (tick % 2 == 0) {
                rotator.advanceTerminal();
                System.out.println("Terminal rotated to: " + rotator.getActiveTerminal());
            }
        }

        //  Final raporu 
        System.out.println("\nToplam Üretilen: " + totalGenerated);
        System.out.println("Toplam Gönderilen: " + totalDispatched);
        System.out.println("Toplam Misrouted (stack’e giden): " + totalMisrouted);
        System.out.println("ReturnStack'te kalan: " + stack.size());
        System.out.println(" SimulationTest başarılı şekilde tamamlandı.");
    }
}
