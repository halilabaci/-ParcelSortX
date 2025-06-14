import java.util.*;

public class Main {

    public static void main(String[] args) {

        // Config dosyasından değerleri oku
        ConfigReader config = new ConfigReader("config.txt");
        // ConfigReader sınıfı ile config.txt dosyasından değerleri okuyorum
        final int MAX_TICKS = config.getInt("MAX TICKS"); // simülasyon toplam kaç adım sürecek
        final int QUEUE_CAPACITY = config.getInt("QUEUE CAPACITY");// ArrivalBuffer'ın kapasitesi
        final int TERMINAL_ROTATION_INTERVAL = config.getInt("TERMINAL ROTATION INTERVAL"); // kaç adımda bir terminal değişiyor
        final int PARCEL_PER_TICK_MIN = config.getInt("PARCEL PER TICK MIN");// kaç adımda bir terminal değişiyor
        final int PARCEL_PER_TICK_MAX = config.getInt("PARCEL PER TICK MAX");// maksimum parcel
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

            // Ekrana yazdırmak için her tick'in verilerini biriktiriyorum
            List<String> newParcelLogs = new ArrayList<>();
            List<String> sortedParcelIDs = new ArrayList<>();
            String dispatchLog = "None";
            String returnLog = "None";

            // 1. Yeni Parcel üretimi her tick'te rastgele sayıda parcel üretiyorum
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
                     newParcelLogs.add(id + " to " + city + " (Priority " + priority + ")");
                }
            }

            // 2. Kuyruktaki parcel'ları BST'ye aktarıyorum
            int dequeueLimit = 2;
            int dequeued = 0;
            while (!queue.isEmpty() && dequeued < dequeueLimit) {
                Parcel parcel = queue.dequeue();
                tracker.updateStatus(parcel.getParcelID(), Parcel.ParcelStatus.SORTED);
                sorter.insertParcel(parcel);
                sortedParcelIDs.add(parcel.getParcelID());
                dequeued++;
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
                    returnLog = candidate.getParcelID() + " misrouted -> Pushed to ReturnStack";
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

                    dispatchLog = candidate.getParcelID() + " from BST to " + currentCity + " ---> Success";
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
            
            // Hocanın istediği çıktıya uygun formatta ekrana yazdırma
            System.out.println("[Tick " + tick + "]");
            System.out.println("New Parcels: " + (newParcelLogs.isEmpty() ? "None" : String.join(", ", newParcelLogs)));
            System.out.println("Queue Size: " + queue.size());
            System.out.println("Sorted to BST: " + (sortedParcelIDs.isEmpty() ? "None" : String.join(", ", sortedParcelIDs)));
            System.out.println("Dispatched: " + dispatchLog);
            System.out.println("Returned: " + returnLog);
            System.out.println("Active Terminal: " + currentCity);
            System.out.println("ReturnStack Size: " + stack.size());
            System.out.println();

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
