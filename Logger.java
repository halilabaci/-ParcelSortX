import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    // Loglarımı kaydedeceğim dosya adı – her şey buraya yazılıyor
    private static final String LOG_FILE = "log.txt";

    // Tick başlığını hem ekrana hem log dosyasına yazdırıyorum
    public static void logTickHeader(int tick) {
        log("\n[Tick " + tick + "]"); // boşluk bırakıyorum ki ayırıcı gibi görünsün
    }

    // Yeni oluşturulan parcel bilgilerini logluyorum
    public static void logNewParcel(Parcel p) {
        log("New Parcel: " + p); // parcel’ın toString() çıktısını yazıyor
    }

    // Kuyruğun o anki boyutunu kaydediyorum – doluluk takip için güzel
    public static void logQueueSize(int size) {
        log("Queue Size: " + size);
    }

    // Parcel BST'ye gönderildiğinde bu log düşüyor
    public static void logSorted(Parcel p) {
        log("Sorted to BST: " + p.getParcelID());
    }

    // Parcel doğru adrese gönderildiyse (başarıyla dispatch edildiyse)
    public static void logDispatchSuccess(Parcel p) {
        log("Dispatched: " + p.getParcelID() + " -> " + p.getDestinationCity() + " [SUCCESS]");
    }

    // Parcel yanlış yere yönlendiyse (misrouted), bunu da ayrı logluyorum
    public static void logDispatchFailure(Parcel p) {
        log("Dispatched: " + p.getParcelID() + " -> " + p.getDestinationCity() + " [MISROUTED]");
    }

    // ReturnStack’e gönderilen parcel’ı buradan takip ediyorum
    public static void logReturnEvent(Parcel p) {
        log("Returned: " + p.getParcelID() + " -> Sent to ReturnStack");
    }

    // O anki aktif terminal (yani dağıtım yapılan şehir) loglanıyor
    public static void logActiveTerminal(String city) {
        log("Active Terminal: " + city);
    }

    // ReturnStack’in güncel boyutunu yazıyorum – kapasite takip için
    public static void logStackSize(int size) {
        log("ReturnStack Size: " + size);
    }

    // Terminal rotasyonu gerçekleştiğinde hangi şehre geçildiğini kaydediyorum
    public static void logTerminalChange(String city) {
        log("Rotated to: " + city);
    }

    // Ana loglama işlemi – hem terminale basıyor hem log.txt’ye yazıyor
    private static void log(String message) {
        System.out.println(message); // ekrana bastır
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(message + "\n"); // dosyaya ek olarak yaz (append)
        } catch (IOException e) {
            e.printStackTrace(); // hata olursa detaylı göreyim
        }
    }
}
