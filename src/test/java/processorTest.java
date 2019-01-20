import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
        import static org.hamcrest.CoreMatchers.notNullValue;
        import static org.hamcrest.CoreMatchers.nullValue;
        import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class processorTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private final String DUMMY_BASE = "http://data.example.org/ns#";

    @Mock
    private MappingDocument r2rmlDoc;
    @InjectMocks
    private Processor processor;
    @Mock
    private InputDatabase database;
    private List<Map<String, String>> empTable;

    @Before
    public void setUp() throws Exception {
        empTable = parseCSVData(getClass().getResourceAsStream("EMP.csv"));
        setUpR2RMLMockResponse();
    }

    public void setUpR2RMLMockResponse() {
        //todo: Simplify this.. or the code design, Law of Demeter
        //todo: last checkpoint
        R2RMLMapping r2rmlMapping = mock(R2RMLMapping.class);
        Map<String, TriplesMap> triplesMaps = new HashMap<>();
        TriplesMap triplesMap = mock(TriplesMap.class);
        triplesMaps.put("TriplesMap1", triplesMap);
        LogicalTable logicalTable = mock(LogicalTable.class);
        SubjectMap subjectMap = mock(SubjectMap.class);
        PredicateObjectMap predicateObjectMap = mock(PredicateObjectMap.class);
        PredicateMap predicateMap = mock(PredicateMap.class);
        ObjectMap objectMap = mock(ObjectMap.class);


        when(database.getRows("EMP")).thenReturn(empTable);
        when(r2rmlDoc.load(any(InputStream.class))).thenReturn(r2rmlMapping);
        when(r2rmlMapping.getTriplesMaps()).thenReturn(triplesMaps);
        when(triplesMap.getLogicalTable()).thenReturn(logicalTable);
        when(triplesMap.getSubjectMap()).thenReturn(subjectMap);
        when(triplesMap.getPredicateObjectMaps()).thenReturn(new ArrayList<>(Arrays.asList(predicateObjectMap)));
        when(logicalTable.getTableName()).thenReturn("EMP");
        when(subjectMap.getTemplate()).thenReturn("http://data.example.com/employee/{EMPNO}");
        when(subjectMap.getClassType()).thenReturn("http://example.com/ns#Employee");
        when(predicateObjectMap.getPredicateMap()).thenReturn(predicateMap);
        when(predicateObjectMap.getObjectMap()).thenReturn(objectMap);
        when(predicateMap.getPredicate()).thenReturn("http://example.com/ns#name");
        when(objectMap.getColumn()).thenReturn("ENAME");
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

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getRDFGraph_Invoked_ReturnNull() {
        Model result = processor.getGraph();
        assertThat(result, is(nullValue()));
    }

    @Test
    public void mapToGraph_Invoked_ModelNotNull() {
        processor.mapToGraph(database, mock(InputStream.class));
        Model result = processor.getGraph();

        assertThat(result, is(notNullValue()));
    }

    @Test
    public void mapToGraph_Invoked_BasicModelExists() throws Exception {
        //Load expected model result to compare to
        InputStream in = getClass().getResourceAsStream("rdf_basic_output_01.n3");
        Model expectedResult = ModelFactory.createDefaultModel().read(in, DUMMY_BASE, "N3");

        //Execute test target
        processor.mapToGraph(database, mock(InputStream.class));
        Model result = processor.getGraph();

        //result.write(System.out, "N3");
        assertThat(result.isIsomorphicWith(expectedResult), is(true));
    }
}