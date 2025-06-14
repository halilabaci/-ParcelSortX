public class Parcel {

    // Kargonun mevcut durumunu tutmak için bir enum tanımladım.
    // Bu sayede sadece sabit durumlar kullanılabilir:
    // Kuyrukta, sıralanmış, gönderilmiş veya iade edilmiş.
    public enum ParcelStatus {
        IN_QUEUE, SORTED, DISPATCHED, RETURNED
    }

    // Parcel  bilgileri
    public String parcelID;           // Her parcel’a ait benzersiz ID
    public String destinationCity;    // Hedef şehir
    public int priority;              // Öncelik derecesi (1–3 arası)
    public String size;               // Boyutu (Small, Medium, Large gibi)
    public int arrivalTick;           // Simülasyon zamanında ne zaman geldi
    public ParcelStatus status;       // Şu anki durumu

    //  Kurucu metod 
    // Parcel oluşturulurken bütün bilgileri alıyorum ve durumunu IN_QUEUE olarak başlatıyorum.
    public Parcel(String parcelID, String destinationCity, int priority, String size, int arrivalTick) {
        this.parcelID = parcelID;
        this.destinationCity = destinationCity;
        this.priority = priority;
        this.size = size;
        this.arrivalTick = arrivalTick;
        this.status = ParcelStatus.IN_QUEUE; // Yeni gelen parcel otomatik olarak kuyruğa girmiş sayılıyor
    }

    //  Getter metodlar 
    // Bu metodlarla parcel nesnesindeki bilgilere dışarıdan erişim sağlıyorum.
    public String getParcelID() {
        return parcelID;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public int getPriority() {
        return priority;
    }

    public String getSize() {
        return size;
    }

    public int getArrivalTick() {
        return arrivalTick;
    }

    public ParcelStatus getStatus() {
        return status;
    }

    // Parcel'ın durumunu dışarıdan değiştirebilmek için setter tanımladım.
    public void setStatus(ParcelStatus status) {
        this.status = status;
    }

    
}
