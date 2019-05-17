package com.github.jiefenn8.graphloom.io;

import java.util.List;
import java.util.Map;

public interface InputDatabase {
    List<Map<String, String>> getRows(String table);
}
