package r2graph;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import r2graph.mapper.Mapper;
import r2graph.mapper.impl.MapperCom;
import r2graph.r2rml.PredicateObjectMap;
import r2graph.r2rml.R2RML;
import r2graph.r2rml.TriplesMap;
import r2graph.util.InputDatabase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
        import static org.hamcrest.CoreMatchers.notNullValue;
        import static org.hamcrest.CoreMatchers.nullValue;
        import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MapperTest {
    private final String baseNs = "http://data.example.org/ns#";
    private Mapper mapperCom;
    @Mock
    private InputDatabase database;
    @Mock
    private R2RML r2rml;
    private List<Map<String, String>> empTable;

    @Before
    public void setUp() throws Exception {
        mapperCom = new MapperCom();
        empTable = parseCSVData(getClass().getResourceAsStream("../EMP.csv"));

    }

    public void setUpBasicR2RMLResponse() {
        TriplesMap triplesMap = mock(TriplesMap.class);
        PredicateObjectMap predicateObjectMap = mock(PredicateObjectMap.class);

        when(r2rml.listTriplesMap()).thenReturn(Stream.of(
                new AbstractMap.SimpleImmutableEntry<>("TriplesMap1", triplesMap))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        when(triplesMap.getTableName()).thenReturn("EMP");
        when(database.getRows("EMP")).thenReturn(empTable);
        when(triplesMap.getTemplate()).thenReturn("http://data.example.com/employee/{EMPNO}");
        when(triplesMap.getClassType()).thenReturn("http://example.com/ns#Employee");
        when(triplesMap.getPredicateObjectMaps()).thenReturn(Arrays.asList(predicateObjectMap));
        when(predicateObjectMap.getPredicate()).thenReturn("http://example.com/ns#name");
        when(predicateObjectMap.getColumnName()).thenReturn("ENAME");
    }

    private List<Map<String, String>> parseCSVData(InputStream in) throws Exception {
        List<Map<String, String>> tableData = new ArrayList<>();

        CSVParser csvParser = new CSVParser(
                new BufferedReader(new InputStreamReader(in)),
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

        for(CSVRecord csvRecord : csvParser){
            Map<String, String> row_column = new HashMap<>();
            for(Map.Entry<String, Integer> entry : csvParser.getHeaderMap().entrySet()){
                row_column.put(entry.getKey(), csvRecord.get(entry.getValue()));
            }
            tableData.add(row_column);
        }

        return tableData;
    }

    @Test
    public void mapToGraph_Invoked_ModelNotNull() {
        Model result = mapperCom.mapToGraph(database, r2rml);

        assertThat(result, is(notNullValue()));
    }

    @Test
    public void mapToGraph_Invoked_BasicModelExists() throws Exception {
        InputStream in = getClass().getResourceAsStream("../rdf_basic_output_01.n3");
        Model expectedResult = ModelFactory.createDefaultModel().read(in, baseNs, "N3");
        setUpBasicR2RMLResponse();

        Model result = mapperCom.mapToGraph(database, r2rml);

        //result.write(System.out, "N3");
        assertThat(result.isIsomorphicWith(expectedResult), is(true));
    }
}