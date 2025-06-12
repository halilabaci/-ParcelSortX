public class Parcel {

    public enum Size {
        SMALL, MEDIUM, LARGE
    }

    public enum ParcelStatus {
        IN_QUEUE, SORTED, DISPATCHED, RETURNED
    }

    private String parcelID;
    private String destinationCity;
    private int priority;
    private Size size;
    private int arrivalTick;
    private ParcelStatus status;

    // Constructor
    public Parcel(String parcelID, String destinationCity, int priority, Size size, int arrivalTick) {
        this.parcelID = parcelID;
        this.destinationCity = destinationCity;
        this.priority = priority;
        this.size = size;
        this.arrivalTick = arrivalTick;
        this.status = ParcelStatus.IN_QUEUE; // default at creation
    }

    // Getter & Setter Methods
    public String getParcelID() {
        return parcelID;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public int getPriority() {
        return priority;
    }

    public Size getSize() {
        return size;
    }

    public int getArrivalTick() {
        return arrivalTick;
    }

    public ParcelStatus getStatus() {
        return status;
    }

    public void setStatus(ParcelStatus status) {
        this.status = status;
    }

    // Optional: toString() for logging/debugging
    @Override
    public String toString() {
        return String.format("Parcel[%s to %s | Priority: %d | Size: %s | Tick: %d | Status: %s]",
                parcelID, destinationCity, priority, size.name(), arrivalTick, status.name());
    }
}
