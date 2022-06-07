package com.mytoporazka.lib.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TextReader {
    private final String file;

    public TextReader(String file) {
        this.file = file;
    }

    public List<String> readFromFile() {
        var result = new ArrayList<String>();
        try {
            File myObj = new File(file);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                result.add(data);
            }
            myReader.close();
            return result;
        } catch (FileNotFoundException e) { return List.of(); }
    }
}
