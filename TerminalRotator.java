import java.util.List;

public class TerminalRotator {

    // Bu iç sınıf, bağlı liste düğümünü temsil ediyor.
    // Her düğüm bir şehir ismini tutuyor ve sonraki düğüme bağlanıyor.
    private static class Node {
        String cityName;
        Node next;

        Node(String cityName) {
            this.cityName = cityName;
            this.next = null; // ilk başta sonraki şehir yok
        }
    }

    // Boş constructor – bazen elle initialize etmek isteyebilirim diye ekledim.
    public TerminalRotator() {
        // hiçbir şey yapmıyor, sadece yapılsın diye var
    }

    // Eğer baştan şehir listesini verirsem, doğrudan başlatmak için bu constructor var.
    public TerminalRotator(String[] cities) {
        initializeFromCityList(cities); // şehirleri bağlı listeye çeviriyoruz
    }

    // Bağlı listenin başını (head) ve şu an aktif olan terminali (current) tutuyorum.
    private Node head;
    private Node current;

    // Bu metod verilen şehir listesini dairesel bağlı listeye dönüştürüyor.
    // Çok önemli çünkü rotasyon bu yapıya göre çalışıyor.
    public void initializeFromCityList(String[] cities) {
        if (cities.length == 0) return; // boş liste gelirse hiçbir şey yapmam

        // İlk şehri head olarak belirliyorum
        head = new Node(cities[0]);
        Node prev = head;

        // Listedeki diğer şehirleri sırayla ekliyorum
        for (int i = 1; i < cities.length; i++) {
            Node newNode = new Node(cities[i]);
            prev.next = newNode; // önceki şehrin sonrasına bunu bağla
            prev = newNode;      // ve prev’i güncelle
        }

        // Son düğümün next'ini head'e bağlayarak listeyi dairesel hale getiriyorum
        prev.next = head;

        // Simülasyonda aktif terminal olarak en baştaki şehri seçiyorum
        current = head;
    }

    // List<String> versiyonunu da destekliyorum – kullanım kolaylığı için
    public TerminalRotator(List<String> cities) {
        // List'i array'e çevirip diğer constructor’ı çağırıyorum
        initializeFromCityList(cities.toArray(new String[0]));
    }

    // Terminal rotasyonunu sağlıyorum.
    // Mevcut terminali bir sonraki şehre kaydırıyor ve bunu logluyorum.
    public void advanceTerminal() {
        if (current != null) {
            current = current.next;
            Logger.logTerminalChange(current.cityName); // log.txt'ye şehir geçişini yazıyorum
        }
    }

    // Şu an aktif terminalin (şehir) adını döndürüyor.
    public String getActiveTerminal() {
        return current != null ? current.cityName : null;
    }

    // Bu metod terminal sırasını test etmek için yazılmış – döngüsel sırayı yazdırıyor.
    // cycles kadar şehir yazıyor, sonra ... ile kesiyor.
    public void printTerminalOrder(int cycles) {
        if (head == null) return; // hiç şehir yoksa bir şey yazdırma
        Node temp = head;
        for (int i = 0; i < cycles; i++) {
            System.out.print(temp.cityName + " -> ");
            temp = temp.next;
        }
        System.out.println("...");
    }
}
