import java.io.*;
import java.util.*;

public class ConfigReader {
    private Map<String, String> configMap;

    public ConfigReader(String filename) {
        configMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    configMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Config file error: " + e.getMessage());
        }
    }

    public String get(String key) {
        return configMap.get(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(configMap.get(key));
    }

    public double getDouble(String key) {
        return Double.parseDouble(configMap.get(key));
    }

    public List<String> getList(String key) {
        String val = configMap.get(key);
        return Arrays.asList(val.split(",\s*"));
    }
} 
