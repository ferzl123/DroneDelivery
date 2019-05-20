package com.tools;

import com.delivery.DroneOrder;
import com.delivery.DroneOrderDirection;
import com.delivery.DroneOrderID;
import com.delivery.DroneOrderTime;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;



/**
 * Read Lines from file, and convert one line to DroneOrder Object
 *
 * @author Owen
 *
 */

public class ReadOrdersFromFile {

    /**
     * Read lines from a file, using US-ASCII encoding.
     * @param path
     * @return lines from the file
     * @throws IOException, NoSuchFileException
     */
    public static List<String> readLinesFromFile(String path) throws IOException {
        Path inputFile = Paths.get(path);
        Charset charset = Charset.forName("US-ASCII");
        List<String> linesRead = null;
        linesRead = Files.readAllLines(inputFile, charset);
        return linesRead;
    }

    /**
     * Convert a String Order to a DroneOrder Object
     * @param line -- String Order
     * @return DroneOrder
     */
    public static DroneOrder parseDroneOrder(String line) {
        String[] temp = line.split(" ");
        DroneOrder order =
                new DroneOrder(new DroneOrderID(temp[0]), new DroneOrderDirection(temp[1]), new DroneOrderTime(temp[2]));
        return order;
    }

}