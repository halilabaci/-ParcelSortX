public class RotatorTest {
    public static void main(String[] args) {

        // TerminalRotator sınıfından bir nesne oluşturuyorum.
        // Bu yapı, şehirler arasında dairesel bir rotasyon sağlar.
        TerminalRotator rotator = new TerminalRotator();

        // Testte kullanmak üzere 3 şehir belirledim.
        String[] cities = {"Istanbul", "Ankara", "Izmir"};

        // Şehir listesini rotatöre verip başlatıyorum.
        // Bu, dairesel bağlı listeyi oluşturuyor.
        rotator.initializeFromCityList(cities);

        // İlk terminal Istanbul olmalı, çünkü dizinin başında o var.
        assert rotator.getActiveTerminal().equals("Istanbul");

        // Şimdi sıradaki şehre geçiyorum. Beklenen: Ankara
        rotator.advanceTerminal();
        assert rotator.getActiveTerminal().equals("Ankara");

        // Bir adım daha ilerliyorum. Beklenen: Izmir
        rotator.advanceTerminal();
        assert rotator.getActiveTerminal().equals("Izmir");

        // İzmir'den sonra tekrar başa dönmeli çünkü yapımız dairesel.
        // Yani şimdi tekrar Istanbul’a dönmesini bekliyorum.
        rotator.advanceTerminal();
        assert rotator.getActiveTerminal().equals("Istanbul");

        // Eğer buraya kadar assert'ler patlamadıysa test başarıyla geçti demektir.
        System.out.println("RotatorTest passed.");
    }
}
