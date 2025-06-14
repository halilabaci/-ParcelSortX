public class StackTest {
    public static void main(String[] args) {
        // Önce stack’imi oluşturuyorum. ReturnStack sınıfımdan bir örnek aldım.
        ReturnStack stack = new ReturnStack();

        // İki tane test parcel i oluşturdum.
        Parcel p1 = new Parcel("P1", "Ankara", 2, "Medium", 1);
        Parcel p2 = new Parcel("P2", "Izmir", 1, "Large", 2);

        // İlk başta stack’in boş olduğunu test ediyorum
        assert stack.isEmpty() : "Stack başlangıçta boş olmalıydı";

        // Şimdi p1 ve p2’yi stack’e sırayla push ediyorum
        stack.push(p1); // p1 önce eklendi
        stack.push(p2); // sonra p2 geldi, yani en üstte o olacak

        // Stack boyutu şu an 2 olmalı
        assert stack.size() == 2 : "Stack boyutu 2 olmalı";

        // En üstte p2 olmalı çünkü son eklenen o (LIFO mantığı)
        assert stack.peek().equals(p2) : "Peek, en son eklenen p2’yi göstermeli";

        // Şimdi pop ile en üstteki p2’yi çıkarıyorum
        assert stack.pop().equals(p2) : "Pop ilk olarak p2’yi döndürmeli";

        // Sonra p1 kaldı, onu da çıkarıyorum
        assert stack.pop().equals(p1) : "İkinci pop, p1’i döndürmeli";

        // Artık stack boş olmalı
        assert stack.isEmpty() : "Tüm elemanlar çıkınca stack boş olmalı";

        System.out.println(" StackTest passed.");
    }
}
