import java.util.*;

public class DestinationSorter {

    // İç sınıf: Her bir şehir düğümü 
    private class ParcelNode {
        String cityName; // şehir ismi
        LinkedList<Parcel> parcelList; // o şehre ait Parcel listesi (FIFO mantığında)
        ParcelNode left, right; // ikili arama ağacının sol ve sağ çocukları

        ParcelNode(String cityName) {
            this.cityName = cityName;
            this.parcelList = new LinkedList<>();
            this.left = null;
            this.right = null;
        }
    }

    private ParcelNode root; // ağacın kök düğümü

    public DestinationSorter() {
        this.root = null;
    }

    // Parcel ekleme işlemi 
    // Yeni bir Parcel geldiğinde ağaca ekliyorum
    public void insertParcel(Parcel parcel) {
        root = insertRecursive(root, parcel);
    }

    // Rekürsif olarak uygun konumu bulup ekliyorum
    private ParcelNode insertRecursive(ParcelNode node, Parcel parcel) {
        if (node == null) {
            // Şehir daha önce eklenmemişse yeni bir düğüm oluşturuyorum
            ParcelNode newNode = new ParcelNode(parcel.getDestinationCity());
            newNode.parcelList.add(parcel);
            return newNode;
        }

        // Şehir adına göre karşılaştırma yapıyorum (alfabetik sıraya göre)
        int cmp = parcel.getDestinationCity().compareTo(node.cityName);
        if (cmp == 0) {
            // Aynı şehirse, sadece listeye ekliyorum (FIFO mantığı)
            node.parcelList.add(parcel);
        } else if (cmp < 0) {
            // Daha küçükse sola ekle
            node.left = insertRecursive(node.left, parcel);
        } else {
            // Daha büyükse sağa ekle
            node.right = insertRecursive(node.right, parcel);
        }
        return node;
    }

    // Belirli bir şehirdeki tüm parcelleri getir 
    public LinkedList<Parcel> getCityParcels(String cityName) {
        ParcelNode node = findNode(root, cityName);
        return node == null ? null : node.parcelList;
    }

    // Ağacın içinde belirtilen şehir adını arıyorum
    private ParcelNode findNode(ParcelNode node, String city) {
        if (node == null) return null;
        int cmp = city.compareTo(node.cityName);
        if (cmp == 0) return node;
        return cmp < 0 ? findNode(node.left, city) : findNode(node.right, city);
    }

    //  Belirli bir şehirdeki parcellerden birini ID’ye göre sil 
    public boolean removeParcel(String city, String parcelID) {
        ParcelNode node = findNode(root, city);
        if (node == null) return false;

        Iterator<Parcel> it = node.parcelList.iterator();
        while (it.hasNext()) {
            if (it.next().getParcelID().equals(parcelID)) {
                it.remove(); // aradığım parcel i bulup siliyorum
                return true;
            }
        }
        return false; // yoksa false dön
    }

    //  Şehirde kaç tane Parcel var
    public int countCityParcels(String city) {
        ParcelNode node = findNode(root, city);
        return node == null ? 0 : node.parcelList.size();
    }

    //  Şehirleri alfabetik olarak gezip bastırıyorum 
    public void inOrderTraversal() {
        traverse(root);
    }

    private void traverse(ParcelNode node) {
        if (node == null) return;
        traverse(node.left);
        System.out.println(node.cityName + ": " + node.parcelList.size() + " parcels");
        traverse(node.right);
    }

    // Ağacın yüksekliğini döner 
    // En uzun yol kaç seviye? Onu bulmak için yazdım
    public int getHeight() {
        return heightRecursive(root);
    }

    private int heightRecursive(ParcelNode node) {
        if (node == null) return 0;
        return 1 + Math.max(heightRecursive(node.left), heightRecursive(node.right));
    }

    // En çok parcel in olduğu şehri bul ===
    // Hem analiz hem de yük dağılımı için işime yarıyor
    public String getCityWithMaxLoad() {
        return getCityWithMaxLoadRecursive(root, null, 0);
    }

    private String getCityWithMaxLoadRecursive(ParcelNode node, String maxCity, int maxCount) {
        if (node == null) return maxCity;
        
        if (node.parcelList.size() > maxCount) {
            maxCity = node.cityName;
            maxCount = node.parcelList.size();
        }

        // Sola ve sağa giderek en fazla yüke sahip şehri buluyorum
        maxCity = getCityWithMaxLoadRecursive(node.left, maxCity, maxCount);
        return getCityWithMaxLoadRecursive(node.right, maxCity, maxCount);
    }
}
