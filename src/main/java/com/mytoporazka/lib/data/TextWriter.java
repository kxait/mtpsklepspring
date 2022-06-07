package com.mytoporazka.lib.data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class TextWriter {
    private final String file;

    public TextWriter(String file) {
        this.file = file;
    }

    public boolean writeRows(List<String> rows) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            writer.write(String.join("\n", rows));

            writer.close();
            return true;
        }catch(Exception e) {
            return false;
        }
    }
}
