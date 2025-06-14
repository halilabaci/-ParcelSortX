import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ParcelTracker {

    // Yardımcı İç Veri Yapısı 
    // Kargoya dair bilgileri tutmak için oluşturduğum iç sınıf
    public static class ParcelRecord {
        public Parcel.ParcelStatus status;
        public int arrivalTick; // kargonun geldiği zaman
        public int dispatchTick = -1; // gönderildiği zaman başta bilinmiyor
        public int returnCount = 0; // kaç kere iade edildi
        public String destinationCity; // varış şehri
        public int priority; // öncelik seviyesi
        public String size; // boyutu  small, medium, large

        // Constructor: Parcel nesnesinden bilgileri alıp kaydediyorum
        public ParcelRecord(Parcel parcel) {
            this.status = parcel.getStatus();
            this.arrivalTick = parcel.getArrivalTick();
            this.destinationCity = parcel.getDestinationCity();
            this.priority = parcel.getPriority();
            this.size = parcel.getSize();
        }
    }

    // Hash Table Alanları 
    // Her bir kargo ID'sini ve onunla ilişkili bilgileri tutacak iç sınıf
    private class Entry {
        String key; // parcelID olacak
        ParcelRecord value;

        Entry(String key, ParcelRecord value) {
            this.key = key;
            this.value = value;
        }
    }

    // Hash tablosunun boyutu - asal sayı seçtim ki çakışmaları azaltayım
    private final int TABLE_SIZE = 53;

    // Tabloyu linked list dizisi olarak tanımladım, separate chaining yöntemi
    private LinkedList<Entry>[] table;

    // Constructor: her bir bucket için boş bir linked list oluşturuyorum
    @SuppressWarnings("unchecked")
    public ParcelTracker() {
        table = new LinkedList[TABLE_SIZE];
        for (int i = 0; i < TABLE_SIZE; i++) {
            table[i] = new LinkedList<>();
        }
    }

    // Hash fonksiyonu: Java'nın klasik 31 tabanlı hash algoritmasını kullandım
    private int hash(String key) {
        int hash = 0;
        for (char c : key.toCharArray()) {
            hash = (31 * hash + c) % TABLE_SIZE;
        }
        return hash;
    }

    // Yeni bir kargo ekleme işlemi
    public void insert(String parcelID, Parcel parcel) {
        // Aynı ID ile önceden kayıt varsa eklemiyorum
        if (exists(parcelID)) return;

        int index = hash(parcelID); // hangi bucket'a gideceğini bul
        ParcelRecord record = new ParcelRecord(parcel); // bilgileri al
        table[index].add(new Entry(parcelID, record)); // listeye ekle
    }

    // Verilen ID ile kayıt var mı diye kontrol ediyorum
    public boolean exists(String parcelID) {
        int index = hash(parcelID);
        for (Entry e : table[index]) {
            if (e.key.equals(parcelID)) return true;
        }
        return false;
    }

    // ID ile kayıtlı kargo bilgilerini getiriyorum
    public ParcelRecord get(String parcelID) {
        int index = hash(parcelID);
        for (Entry e : table[index]) {
            if (e.key.equals(parcelID)) return e.value;
        }
        return null; // bulunamazsa null dön
    }

    // Kargo durumunu güncellemek için metod
    public void updateStatus(String parcelID, Parcel.ParcelStatus newStatus) {
        ParcelRecord record = get(parcelID);
        if (record != null) {
            record.status = newStatus;
        }
    }

    // Eğer iade edildiyse return count'u 1 artırıyorum
    public void incrementReturnCount(String parcelID) {
        ParcelRecord record = get(parcelID);
        if (record != null) {
            record.returnCount++;
        }
    }

    // Gönderildiği zamanı belirlemek için kullanıyorum
    public void setDispatchTick(String parcelID, int tick) {
        ParcelRecord record = get(parcelID);
        if (record != null) {
            record.dispatchTick = tick;
        }
    }

    // Tablodaki tüm kargo ID'lerini bir liste halinde döndürüyorum
    public List<String> getAllIDs() {
        List<String> ids = new ArrayList<>();
        for (LinkedList<Entry> bucket : table) {
            for (Entry e : bucket) {
                ids.add(e.key);
            }
        }
        return ids;
    }

    // Load factor = toplam eleman sayısı / tablo boyutu
    // Dağılımın ne kadar dengeli olduğunu görmek için faydalı
    public double getLoadFactor() {
        int count = 0;
        for (LinkedList<Entry> bucket : table) {
            count += bucket.size();
        }
        return (double) count / TABLE_SIZE;
    }
}
