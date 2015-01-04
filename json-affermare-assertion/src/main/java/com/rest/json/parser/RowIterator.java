package com.rest.json.parser;


import java.util.Iterator;
import java.util.List;

public class RowIterator implements Iterator<TableCell> {

    List<List<String>> rows;
    private int index = 0;

    public RowIterator(List<List<String>> tableRows) {
        this.rows = tableRows;
    }

    @Override
    public boolean hasNext() {
        return index < rows.size();
    }

    @Override
    public TableCell next() {
        List<String> row = rows.get(index++);
        return new TableCell(row.get(0), row.get(1));
    }

    @Override
    public void remove() {
        rows.remove(index);
    }
}
