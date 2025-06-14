import java.util.*;

public class Main {

    public static void main(String[] args) {

        // Config dosyasından değerleri oku
        ConfigReader config = new ConfigReader("config.txt");
        // ConfigReader sınıfı ile config.txt dosyasından değerleri okuyorum
        final int MAX_TICKS = config.getInt("MAX TICKS"); // toplam simülasyon süresi (tick cinsinden)
        final int QUEUE_CAPACITY = config.getInt("QUEUE CAPACITY");
        final int TERMINAL_ROTATION_INTERVAL = config.getInt("TERMINAL ROTATION INTERVAL"); // her 5 tick'te bir terminal değişiyor
        final int PARCEL_PER_TICK_MIN = config.getInt("PARCEL PER TICK MIN");
        final int PARCEL_PER_TICK_MAX = config.getInt("PARCEL PER TICK MAX");
        final double MISROUTING_RATE = config.getDouble("MISROUTING RATE"); // %10 yanlış yönlendirme oranı

        // Hedef şehirler – rotator bu şehirler arasında döner
        final List<String> CITY_LIST = config.getList("CITY LIST");

        // Nesne Oluşturma 
        // Tüm veri yapılarımı burada oluşturuyorum
        ArrivalBuffer queue = new ArrivalBuffer(QUEUE_CAPACITY); // gelen parcel'lar için kuyruk
        ParcelTracker tracker = new ParcelTracker(); // parcel’ların durumunu takip etmek için hash table
        DestinationSorter sorter = new DestinationSorter(); // şehir bazlı BST
        ReturnStack stack = new ReturnStack(); // yanlış yönlendirmeler buraya gider (stack yapısı)
        TerminalRotator rotator = new TerminalRotator(CITY_LIST); // şehirleri döngüsel olarak gezen yapı
        rotator.initializeFromCityList(CITY_LIST.toArray(new String[0])); // şehirleri rotatora yüklüyorum
        ReportGenerator reporter = new ReportGenerator("report.txt"); // sonunda rapor üretecek

        // İstatistik Takibi
        // Tüm simülasyon boyunca toplanacak istatistikleri burada tanımladım
        int tick = 0;
        int totalParcelsGenerated = 0;
        int totalDispatched = 0;
        int totalReturned = 0;
        int maxQueueSize = 0;
        int maxStackSize = 0;
        int totalProcessingTime = 0;
        int maxDelay = 0;
        String maxDelayParcelID = "-";

        Random rand = new Random(); // rastgele üretimler için

        // Simülasyon Döngüsü 
        while (tick < MAX_TICKS) {
            tick++;
            System.out.println("[Tick " + tick + "]");

            // 1. Yeni Parcel üretimi
            int parcelCount = rand.nextInt(PARCEL_PER_TICK_MAX - PARCEL_PER_TICK_MIN + 1) + PARCEL_PER_TICK_MIN;
            for (int i = 0; i < parcelCount; i++) {
                String id = "P" + (1000 + totalParcelsGenerated); // her kargoya eşsiz ID
                String city = CITY_LIST.get(rand.nextInt(CITY_LIST.size())); // rastgele bir hedef şehir
                int priority = rand.nextInt(3) + 1; // öncelik (1–3)

                // boyut da rastgele geliyor
                String[] sizes = {"Small", "Medium", "Large"};
                String size = sizes[rand.nextInt(3)];

                // parcel oluşturuyorum
                Parcel parcel = new Parcel(id, city, priority, size, tick);

                // enqueue başarılıysa takibe alıyorum
                if (queue.enqueue(parcel)) {
                    tracker.insert(id, parcel);
                    totalParcelsGenerated++;
                    maxQueueSize = Math.max(maxQueueSize, queue.size()); // maksimum kuyruk boyutunu güncelle
                }
            }

            // 2. Kuyruktaki parcel'ları BST'ye aktarıyorum
            while (!queue.isEmpty()) {
                Parcel parcel = queue.dequeue();
                tracker.updateStatus(parcel.getParcelID(), Parcel.ParcelStatus.SORTED);
                sorter.insertParcel(parcel); // doğru şehir dalına yerleşiyor
            }

            // 3. Dispatch işlemi
            String currentCity = rotator.getActiveTerminal(); // aktif terminal hangi şehirdeyse onu alıyorum
            LinkedList<Parcel> cityParcels = sorter.getCityParcels(currentCity); // o şehirdeki parcel'lar

            if (cityParcels != null && !cityParcels.isEmpty()) {
                Parcel candidate = cityParcels.peek(); // sıradaki parcel (FIFO)
                boolean misroute = rand.nextDouble() < MISROUTING_RATE; // %10 ihtimalle yanlış yönlendir

                if (misroute) {
                    // kargo yanlış yönlendirildi → ReturnStack'e gider
                    tracker.updateStatus(candidate.getParcelID(), Parcel.ParcelStatus.RETURNED);
                    tracker.incrementReturnCount(candidate.getParcelID());
                    stack.push(candidate);
                    totalReturned++;
                    System.out.println("Parcel " + candidate.getParcelID() + " misrouted → stack");
                } else {
                    // doğru adrese gönderildi
                    tracker.updateStatus(candidate.getParcelID(), Parcel.ParcelStatus.DISPATCHED);
                    tracker.setDispatchTick(candidate.getParcelID(), tick);
                    sorter.removeParcel(currentCity, candidate.getParcelID());
                    totalDispatched++;

                    // gönderilen parcel’ın gecikme süresini hesaplıyorum
                    int delay = tick - candidate.getArrivalTick();
                    totalProcessingTime += delay;
                    if (delay > maxDelay) {
                        maxDelay = delay;
                        maxDelayParcelID = candidate.getParcelID();
                    }

                    System.out.println("Parcel " + candidate.getParcelID() + " dispatched to " + currentCity);
                }
            }

            // 4. ReturnStack’teki parcel’ları yeniden deniyorum
            if (tick % 3 == 0) { // her 3 tick’te bir
                int retry = Math.min(2, stack.size()); // en fazla 2 parcel geri dönsün
                for (int i = 0; i < retry; i++) {
                    Parcel returned = stack.pop();
                    tracker.updateStatus(returned.getParcelID(), Parcel.ParcelStatus.SORTED);
                    sorter.insertParcel(returned); // tekrar BST’ye gönderiyorum
                }
            }

            // 5. Terminal rotasyonu
            if (tick % TERMINAL_ROTATION_INTERVAL == 0) {
                rotator.advanceTerminal(); // sıradaki şehre geçiyoruz
            }

            maxStackSize = Math.max(maxStackSize, stack.size()); // stack’in maksimum doluluk oranı
        }

        //  Rapor için metrikler hesaplamalar yapılıyor 
        Map<String, Integer> parcelsPerCity = new HashMap<>();
        for (String city : CITY_LIST) {
            parcelsPerCity.put(city, sorter.countCityParcels(city));
        }

        // en çok hedeflenen şehir
        String mostTargetedCity = CITY_LIST.get(0);
        for (String city : CITY_LIST) {
            if (parcelsPerCity.get(city) > parcelsPerCity.get(mostTargetedCity)) {
                mostTargetedCity = city;
            }
        }

        // ortalama gecikme süresi
        double avgTime = totalDispatched == 0 ? 0 : (double) totalProcessingTime / totalDispatched;

        // birden fazla kez iade edilmiş parcel sayısı
        int returnedMoreThanOnce = 0;
        for (String id : tracker.getAllIDs()) {
            if (tracker.get(id).returnCount > 1) {
                returnedMoreThanOnce++;
            }
        }

        // hash table load factor hesabı
        double loadFactor = tracker.getLoadFactor();

        // rapor dosyasını oluşturuyorum
        reporter.generateReport(
                tick,
                totalParcelsGenerated,
                totalDispatched,
                totalReturned,
                queue.size(),
                parcelsPerCity.values().stream().mapToInt(i -> i).sum(),
                stack.size(),
                parcelsPerCity,
                mostTargetedCity,
                avgTime,
                maxDelayParcelID,
                maxDelay,
                returnedMoreThanOnce,
                maxQueueSize,
                maxStackSize,
                sorter.getHeight(),
                loadFactor
        );

        System.out.println(" Simülasyon tamamlandı ve rapor üretildi.");
    }
}
