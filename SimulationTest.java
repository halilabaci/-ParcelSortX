import java.util.*;

public class SimulationTest {

    public static void main(String[] args) {
        System.out.println("=== SimulationTest.java ===");

        final List<String> CITY_LIST = Arrays.asList("Ankara", "Izmir");
        final int QUEUE_CAPACITY = 5;

        ArrivalBuffer queue = new ArrivalBuffer(QUEUE_CAPACITY);
        ParcelTracker tracker = new ParcelTracker();
        DestinationSorter sorter = new DestinationSorter();
        ReturnStack stack = new ReturnStack(); // dummy versiyonu olabilir
        TerminalRotator rotator = new TerminalRotator(CITY_LIST); // dummy versiyonu olabilir

        Random rand = new Random();
        int tick = 0;
        int totalGenerated = 0;
        int totalDispatched = 0;

        while (tick < 5) { // Mini simülasyon
            tick++;
            System.out.println("\n[Tick " + tick + "]");

            // Parcel üret
            int parcelCount = 1 + rand.nextInt(2); // 1-2 arası
            for (int i = 0; i < parcelCount; i++) {
                String id = "PX" + (totalGenerated + 1);
                String city = CITY_LIST.get(rand.nextInt(CITY_LIST.size()));
                Parcel p = new Parcel(id, city, 1, "Small", tick);
                if (queue.enqueue(p)) {
                    tracker.insert(id, p);
                    totalGenerated++;
                    System.out.println("Generated: " + p);
                }
            }

            // Queue → BST
            while (!queue.isEmpty()) {
                Parcel p = queue.dequeue();
                tracker.updateStatus(p.getParcelID(), Parcel.ParcelStatus.SORTED);
                sorter.insertParcel(p);
                System.out.println("Sorted: " + p.getParcelID() + " → " + p.getDestinationCity());
            }

            // Dispatch işlemi
            String activeCity = rotator.getActiveTerminal();
            LinkedList<Parcel> parcels = sorter.getCityParcels(activeCity);
            if (parcels != null && !parcels.isEmpty()) {
                Parcel dispatch = parcels.peek();
                sorter.removeParcel(activeCity, dispatch.getParcelID());
                tracker.updateStatus(dispatch.getParcelID(), Parcel.ParcelStatus.DISPATCHED);
                System.out.println("Dispatched: " + dispatch.getParcelID() + " to " + activeCity);
                totalDispatched++;
            }

            // Terminal rotasyonu
            if (tick % 2 == 0) {
                rotator.advanceTerminal();
                System.out.println("Terminal rotated to: " + rotator.getActiveTerminal());
            }
        }

        System.out.println("\nToplam Üretilen: " + totalGenerated);
        System.out.println("Toplam Gönderilen: " + totalDispatched);

        System.out.println("✅ SimulationTest başarılı şekilde tamamlandı.");
    }
}
