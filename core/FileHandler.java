package core;

import shared.Constants;
import java.io.*;
import java.util.*;

public class FileHandler {
    //saves a new parking transaction or fine record
    public static void saveRecord(String filename, String data) {
        //appends data to ensure history is not overwritten 
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(data);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    //retrieves all records to allow check for unpaid fines
    public static List<String> loadAllRecords(String filename) {
        List<String> records = new ArrayList<>();
        File file = new File(filename);
        
        if (!file.exists()) return records;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                records.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return records;
    }
}