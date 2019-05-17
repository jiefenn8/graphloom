package com.github.jiefenn8.graphloom.mapper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.github.jiefenn8.graphloom.configmap.ConfigMap;
import com.github.jiefenn8.graphloom.configmap.EntityMap;
import com.github.jiefenn8.graphloom.configmap.ObjectMap;
import com.github.jiefenn8.graphloom.configmap.PredicateMap;
import com.github.jiefenn8.graphloom.io.InputDatabase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MapperTest {

    private final String baseNs = "http://data.example.org/ns#";
    private Mapper mapper;

    @Mock
    private InputDatabase database;
    @Mock
    private ConfigMap configMap;
    private List<Map<String, String>> empTable;

    @Before
    public void setUp() throws Exception {
        mapper = new Mapper();
        empTable = parseCSVData(getClass().getResourceAsStream("/EMP.csv"));

    }

    public void setUpBasicR2RMLResponse() {
        EntityMap entityMap = mock(EntityMap.class);
        PredicateMap predicateMap = mock(PredicateMap.class);
        ObjectMap objectMap = mock(ObjectMap.class);

        when(configMap.listEntityMaps())
                .thenReturn(Stream.of(new AbstractMap.SimpleImmutableEntry<>("TriplesMap1", entityMap))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        when(entityMap.getEntitySource()).thenReturn("EMP");
        when(database.getRows("EMP")).thenReturn(empTable);
        when(entityMap.getTemplate()).thenReturn("http://data.example.com/employee/{EMPNO}");
        when(entityMap.getClassType()).thenReturn("http://example.com/ns#Employee");

        when(entityMap.listPredicateMaps())
                .thenReturn(Arrays.asList(predicateMap));

        when(predicateMap.getPredicate()).thenReturn("http://example.com/ns#name");
        when(predicateMap.getObjectMap()).thenReturn(objectMap);
        when(objectMap.getObjectSource()).thenReturn("ENAME");
    }

    private List<Map<String, String>> parseCSVData(InputStream in) throws Exception {
        List<Map<String, String>> tableData = new ArrayList<>();

        CSVParser csvParser = new CSVParser(
                new BufferedReader(new InputStreamReader(in)),
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

        for (CSVRecord csvRecord : csvParser) {
            Map<String, String> row_column = new HashMap<>();
            for (Map.Entry<String, Integer> entry : csvParser.getHeaderMap().entrySet()) {
                row_column.put(entry.getKey(), csvRecord.get(entry.getValue()));
            }
            tableData.add(row_column);
        }

        return tableData;
    }

    @Test
    public void mapToGraph_Invoked_ModelNotNull() {
        Model result = mapper.mapToGraph(database, configMap);

        assertThat(result, is(notNullValue()));
    }

    @Test
    public void mapToGraph_Invoked_BasicModelExists() throws Exception {
        InputStream in = getClass().getResourceAsStream("/rdf_basic_output_01.n3");
        Model expectedResult = ModelFactory.createDefaultModel().read(in, baseNs, "N3");
        setUpBasicR2RMLResponse();

        Model result = mapper.mapToGraph(database, configMap);

        //result.write(System.out, "N3");
        assertThat(result.isIsomorphicWith(expectedResult), is(true));
    }
}