import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private static final String LOG_FILE = "log.txt";

    public static void logTickHeader(int tick) {
        log("\n[Tick " + tick + "]");
    }

    public static void logNewParcel(Parcel p) {
        log("New Parcel: " + p);
    }

    public static void logQueueSize(int size) {
        log("Queue Size: " + size);
    }

    public static void logSorted(Parcel p) {
        log("Sorted to BST: " + p.getParcelID());
    }

    public static void logDispatchSuccess(Parcel p) {
        log("Dispatched: " + p.getParcelID() + " -> " + p.getDestinationCity() + " [SUCCESS]");
    }

    public static void logDispatchFailure(Parcel p) {
        log("Dispatched: " + p.getParcelID() + " -> " + p.getDestinationCity() + " [MISROUTED]");
    }

    public static void logReturnEvent(Parcel p) {
        log("Returned: " + p.getParcelID() + " -> Sent to ReturnStack");
    }

    public static void logActiveTerminal(String city) {
        log("Active Terminal: " + city);
    }

    public static void logStackSize(int size) {
        log("ReturnStack Size: " + size);
    }

    public static void logTerminalChange(String city) {
        log("Rotated to: " + city);
    }

    private static void log(String message) {
        System.out.println(message);
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
