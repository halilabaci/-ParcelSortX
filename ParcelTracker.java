import java.util.LinkedList;

public class ParcelTracker {

    // === Yardımcı İç Veri Yapısı ===
    public static class ParcelRecord {
        public Parcel.ParcelStatus status;
        public int arrivalTick;
        public int dispatchTick = -1;
        public int returnCount = 0;
        public String destinationCity;
        public int priority;
        public Parcel.Size size;

        public ParcelRecord(Parcel parcel) {
            this.status = parcel.getStatus();
            this.arrivalTick = parcel.getArrivalTick();
            this.destinationCity = parcel.getDestinationCity();
            this.priority = parcel.getPriority();
            this.size = parcel.getSize();
        }
    }

    // === Hash Table Alanları ===
    private class Entry {
        String key; // parcelID
        ParcelRecord value;

        Entry(String key, ParcelRecord value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int TABLE_SIZE = 53; // küçük asal sayı (çakışma azaltır)
    private LinkedList<Entry>[] table;

    @SuppressWarnings("unchecked")
    public ParcelTracker() {
        table = new LinkedList[TABLE_SIZE];
        for (int i = 0; i < TABLE_SIZE; i++) {
            table[i] = new LinkedList<>();
        }
    }

    private int hash(String key) {
        int hash = 0;
        for (char c : key.toCharArray()) {
            hash = (31 * hash + c) % TABLE_SIZE;
        }
        return hash;
    }

    public void insert(String parcelID, Parcel parcel) {
        if (exists(parcelID)) return; // duplicate kontrolü

        int index = hash(parcelID);
        ParcelRecord record = new ParcelRecord(parcel);
        table[index].add(new Entry(parcelID, record));
    }

    public boolean exists(String parcelID) {
        int index = hash(parcelID);
        for (Entry e : table[index]) {
            if (e.key.equals(parcelID)) return true;
        }
        return false;
    }

    public ParcelRecord get(String parcelID) {
        int index = hash(parcelID);
        for (Entry e : table[index]) {
            if (e.key.equals(parcelID)) return e.value;
        }
        return null;
    }

    public void updateStatus(String parcelID, Parcel.ParcelStatus newStatus) {
        ParcelRecord record = get(parcelID);
        if (record != null) {
            record.status = newStatus;
        }
    }

    public void incrementReturnCount(String parcelID) {
        ParcelRecord record = get(parcelID);
        if (record != null) {
            record.returnCount++;
        }
    }

    public void setDispatchTick(String parcelID, int tick) {
        ParcelRecord record = get(parcelID);
        if (record != null) {
            record.dispatchTick = tick;
        }
    }
}
