package com.rest.json.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class TableIterator implements Iterator<RowIterator> {

    List<RowIterator> contents;
    private int index = 0;

    public TableIterator(List<Map<String, String>> maps) {
        contents = new ArrayList<>();
        for (Map<String, String> map : maps) {
            List<List<String>> row = new ArrayList<>();
            for (String key : map.keySet()) {
                row.add(asList(key, map.get(key)));
            }
            contents.add(new RowIterator(row));
        }
    }

    @Override
    public boolean hasNext() {
        return index < contents.size();
    }

    @Override
    public RowIterator next() {
        return contents.get(index++);
    }

    @Override
    public void remove() {
        contents.remove(index);
    }
}
