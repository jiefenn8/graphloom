package r2graph.io;

import java.util.List;
import java.util.Map;

public interface InputDatabase {
    List<Map<String, String>> getRows(String table);
}
