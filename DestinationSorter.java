import java.util.*;

public class DestinationSorter {

    private class ParcelNode {
        String cityName;
        LinkedList<Parcel> parcelList;
        ParcelNode left, right;

        ParcelNode(String cityName) {
            this.cityName = cityName;
            this.parcelList = new LinkedList<>();
            this.left = null;
            this.right = null;
        }
    }

    private ParcelNode root;

    public DestinationSorter() {
        this.root = null;
    }

    // INSERT
    public void insertParcel(Parcel parcel) {
        root = insertRecursive(root, parcel);
    }

    private ParcelNode insertRecursive(ParcelNode node, Parcel parcel) {
        if (node == null) {
            ParcelNode newNode = new ParcelNode(parcel.getDestinationCity());
            newNode.parcelList.add(parcel);
            return newNode;
        }

        int cmp = parcel.getDestinationCity().compareTo(node.cityName);
        if (cmp == 0) {
            node.parcelList.add(parcel);  // FIFO
        } else if (cmp < 0) {
            node.left = insertRecursive(node.left, parcel);
        } else {
            node.right = insertRecursive(node.right, parcel);
        }
        return node;
    }

    // GET ALL PARCELS FOR A CITY
    public LinkedList<Parcel> getCityParcels(String cityName) {
        ParcelNode node = findNode(root, cityName);
        return node == null ? null : node.parcelList;
    }

    private ParcelNode findNode(ParcelNode node, String city) {
        if (node == null) return null;
        int cmp = city.compareTo(node.cityName);
        if (cmp == 0) return node;
        return cmp < 0 ? findNode(node.left, city) : findNode(node.right, city);
    }

    // REMOVE PARCEL BY ID
    public boolean removeParcel(String city, String parcelID) {
        ParcelNode node = findNode(root, city);
        if (node == null) return false;

        Iterator<Parcel> it = node.parcelList.iterator();
        while (it.hasNext()) {
            if (it.next().getParcelID().equals(parcelID)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    // COUNT PARCELS PER CITY
    public int countCityParcels(String city) {
        ParcelNode node = findNode(root, city);
        return node == null ? 0 : node.parcelList.size();
    }

    // IN-ORDER TRAVERSAL (Şehirleri alfabetik sırayla gez)
    public void inOrderTraversal() {
        traverse(root);
    }

    private void traverse(ParcelNode node) {
        if (node == null) return;
        traverse(node.left);
        System.out.println(node.cityName + ": " + node.parcelList.size() + " parcels");
        traverse(node.right);
    }

    // BONUS: BST yüksekliği
    public int getHeight() {
        return heightRecursive(root);
    }

    private int heightRecursive(ParcelNode node) {
        if (node == null) return 0;
        return 1 + Math.max(heightRecursive(node.left), heightRecursive(node.right));
    }

    // BONUS: En çok yük olan şehir
    public String getCityWithMaxLoad() {
        return getCityWithMaxLoadRecursive(root, null, 0);
    }

    private String getCityWithMaxLoadRecursive(ParcelNode node, String maxCity, int maxCount) {
        if (node == null) return maxCity;
        if (node.parcelList.size() > maxCount) {
            maxCity = node.cityName;
            maxCount = node.parcelList.size();
        }
        maxCity = getCityWithMaxLoadRecursive(node.left, maxCity, maxCount);
        return getCityWithMaxLoadRecursive(node.right, maxCity, maxCount);
    }
}

