import java.util.*;

public class Main {

    public static void main(String[] args) {
        // === Sabit Konfigürasyonlar ===
        final int MAX_TICKS = 300;
        final int QUEUE_CAPACITY = 30;
        final int TERMINAL_ROTATION_INTERVAL = 5;
        final int PARCEL_PER_TICK_MIN = 1;
        final int PARCEL_PER_TICK_MAX = 3;
        final double MISROUTING_RATE = 0.1;
        final List<String> CITY_LIST = Arrays.asList("Istanbul", "Ankara", "Izmir", "Bursa", "Antalya");

        // === Nesne Oluşturma ===
        ArrivalBuffer queue = new ArrivalBuffer(QUEUE_CAPACITY);
        ParcelTracker tracker = new ParcelTracker();
        DestinationSorter sorter = new DestinationSorter();
        ReturnStack stack = new ReturnStack(); // B kişisi tarafından yazılmalı
        TerminalRotator rotator = new TerminalRotator(CITY_LIST); // B kişisi tarafından yazılmalı
        rotator.initializeFromCityList(CITY_LIST.toArray(new String[0]));
        ReportGenerator reporter = new ReportGenerator("report.txt");

        // === İstatistik Takibi ===
        int tick = 0;
        int totalParcelsGenerated = 0;
        int totalDispatched = 0;
        int totalReturned = 0;
        int maxQueueSize = 0;
        int maxStackSize = 0;
        int totalProcessingTime = 0;
        int maxDelay = 0;
        String maxDelayParcelID = "-";

        Random rand = new Random();

        // === Simülasyon Döngüsü ===
        while (tick < MAX_TICKS) {
            tick++;
            System.out.println("[Tick " + tick + "]");

            // 1. Yeni Parcel üret
            int parcelCount = rand.nextInt(PARCEL_PER_TICK_MAX - PARCEL_PER_TICK_MIN + 1) + PARCEL_PER_TICK_MIN;
            for (int i = 0; i < parcelCount; i++) {
                String id = "P" + (1000 + totalParcelsGenerated);
                String city = CITY_LIST.get(rand.nextInt(CITY_LIST.size()));
                int priority = rand.nextInt(3) + 1;
             
                //size seçimi
                String[] sizes = {"Small", "Medium", "Large"};
                String size = sizes[rand.nextInt(3)];

                Parcel parcel = new Parcel(id, city, priority, size, tick);
                if (queue.enqueue(parcel)) {
                    tracker.insert(id, parcel);
                    totalParcelsGenerated++;
                    maxQueueSize = Math.max(maxQueueSize, queue.size());
                }
            }

            // 2. Queue → BST
            while (!queue.isEmpty()) {
                Parcel parcel = queue.dequeue();
                tracker.updateStatus(parcel.getParcelID(), Parcel.ParcelStatus.SORTED);
                sorter.insertParcel(parcel);
            }

            // 3. Dispatch işlemi
            String currentCity = rotator.getActiveTerminal();
            LinkedList<Parcel> cityParcels = sorter.getCityParcels(currentCity);
            if (cityParcels != null && !cityParcels.isEmpty()) {
                Parcel candidate = cityParcels.peek(); // FIFO
                boolean misroute = rand.nextDouble() < MISROUTING_RATE;

                if (misroute) {
                    tracker.updateStatus(candidate.getParcelID(), Parcel.ParcelStatus.RETURNED);
                    tracker.incrementReturnCount(candidate.getParcelID());
                    stack.push(candidate);
                    totalReturned++;
                    System.out.println("Parcel " + candidate.getParcelID() + " misrouted → stack");
                } else {
                    tracker.updateStatus(candidate.getParcelID(), Parcel.ParcelStatus.DISPATCHED);
                    tracker.setDispatchTick(candidate.getParcelID(), tick);
                    sorter.removeParcel(currentCity, candidate.getParcelID());
                    totalDispatched++;

                    int delay = tick - candidate.getArrivalTick();
                    totalProcessingTime += delay;
                    if (delay > maxDelay) {
                        maxDelay = delay;
                        maxDelayParcelID = candidate.getParcelID();
                    }
                    System.out.println("Parcel " + candidate.getParcelID() + " dispatched to " + currentCity);
                }
            }

            // 4. ReturnStack'ten yeniden ekleme
            if (tick % 3 == 0) {
                int retry = Math.min(2, stack.size());
                for (int i = 0; i < retry; i++) {
                    Parcel returned = stack.pop();
                    tracker.updateStatus(returned.getParcelID(), Parcel.ParcelStatus.SORTED);
                    sorter.insertParcel(returned);
                }
            }

            // 5. Terminal rotasyonu
            if (tick % TERMINAL_ROTATION_INTERVAL == 0) {
                rotator.advanceTerminal();
            }

            maxStackSize = Math.max(maxStackSize, stack.size());
        }

        // === Rapor için metrikler hesapla ===
        Map<String, Integer> parcelsPerCity = new HashMap<>();
        for (String city : CITY_LIST) {
            parcelsPerCity.put(city, sorter.countCityParcels(city));
        }

        String mostTargetedCity = CITY_LIST.get(0);
        for (String city : CITY_LIST) {
            if (parcelsPerCity.get(city) > parcelsPerCity.get(mostTargetedCity)) {
                mostTargetedCity = city;
            }
        }

        double avgTime = totalDispatched == 0 ? 0 : (double) totalProcessingTime / totalDispatched;
        int returnedMoreThanOnce = 0;
        for (String id : tracker.getAllIDs()) {
            if (tracker.get(id).returnCount > 1) {
                returnedMoreThanOnce++;
            }
        }

        double loadFactor = tracker.getLoadFactor();

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

        System.out.println("✅ Simülasyon tamamlandı ve rapor üretildi.");
    }
}
