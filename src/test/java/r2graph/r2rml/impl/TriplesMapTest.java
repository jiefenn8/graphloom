package r2graph.r2rml.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import r2graph.r2rml.PredicateObjectMap;

import java.util.List;
import java.util.function.Predicate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TriplesMapTest {

    private TriplesMapImpl triplesMap;

    @Mock
    private PredicateObjectMap predicateObjectMap;
    private final String tableName = "EMP";
    private final String template = "http://data.example.com/employee/{EMPNO}";
    private final String classType = "http://example.com/ns#Employee";

    @Before
    public void setUp() throws Exception {
        triplesMap = new TriplesMapImpl();
    }

    @Test
    public void WhenTableNameExists_ShouldReturnValue() {
        triplesMap.setTableName(tableName);
        String result = triplesMap.getTableName();
        assertThat(result, is(tableName));
    }

    @Test
    public void WhenTemplateExists_ShouldReturnValue() {
        triplesMap.setTemplate(template);
        String result = triplesMap.getTemplate();
        assertThat(result, is(template));
    }

    @Test
    public void WhenClassTypeExists_ShouldReturnValue() {
        triplesMap.setClassType(classType);
        String result = triplesMap.getClassType();
        assertThat(result, is(classType));
    }

    @Test
    public void WhenPredicateObjectMapExits_ShouldReturnList() {
        triplesMap.addPredicateObjectMap(predicateObjectMap);
        List<PredicateObjectMap> result = triplesMap.getPredicateObjectMaps();
        assertThat(result.isEmpty(), is(false));
        assertThat(result.size(), is(1));
    }
}