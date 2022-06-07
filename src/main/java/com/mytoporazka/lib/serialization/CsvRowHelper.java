package com.mytoporazka.lib.serialization;

import com.mytoporazka.lib.Const;

import java.util.List;

public class CsvRowHelper {
    public static String RowToCsv(List<String> row) {
        return String.join(Const.CSV_DELIM, row);
    }
}
