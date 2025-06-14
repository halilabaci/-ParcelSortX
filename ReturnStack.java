import java.util.NoSuchElementException;

public class ReturnStack {

    // İç içe sınıf: Stack’teki her bir düğümü temsil ediyor
    private static class Node {
        Parcel data;   // Bu düğümde tutulacak parcel nesnesi
        Node next;     // Bir sonraki düğüm

        Node(Parcel data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node top; // Stack'in en üstündeki düğümü tutuyorum
    private int size; // Stack’in anlık boyutunu takip ediyorum

    // Yapıcı metod – başlangıçta stack boş, o yüzden top null, size 0
    public ReturnStack() {
        top = null;
        size = 0;
    }

    // Stack’e yeni bir parcel eklemek için push metodu
    public void push(Parcel parcel) {
        // Yeni bir düğüm oluşturuyorum
        Node newNode = new Node(parcel);

        // Yeni düğümün next’i, mevcut top oluyor
        newNode.next = top;

        // top'u artık yeni düğüme çekiyorum (yani en üst o)
        top = newNode;

        // Boyutu da bir artırıyorum
        size++;

        // Bu parcel iade edildiği için log'lama işini de buradan hallediyorum
        Logger.logReturnEvent(parcel);
    }

    // Stack’ten en üstteki parcel i çıkarmak için pop metodu
    public Parcel pop() {
        // Eğer stack boşsa hata fırlatıyorum – kullanıcıya yanlış kullanım yaptığını söylemek önemli
        if (isEmpty()) throw new NoSuchElementException("ReturnStack is empty");

        // En üstteki parcel i alıyorum
        Parcel parcel = top.data;

        // top’u bir alta çekiyorum 
        top = top.next;

        // Boyutu da azaltıyorum
        size--;

        // Parcel i geri döndürüyorum
        return parcel;
    }

    // En üstteki parcel i sadece görmek istiyorsam (çıkarmadan) peek kullanıyorum
    public Parcel peek() {
        if (isEmpty()) throw new NoSuchElementException("ReturnStack is empty");
        return top.data;
    }

    // Stack boş mu değil mi onu kontrol ediyorum
    public boolean isEmpty() {
        return top == null;
    }

    // Stack’in kaç eleman tuttuğunu öğrenmek için size metodu
    public int size() {
        return size;
    }
}
