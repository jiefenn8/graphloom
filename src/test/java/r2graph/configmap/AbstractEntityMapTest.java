package r2graph.configmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractEntityMapTest {

    private final String tableName = "EMP";
    private final String template = "http://data.example.com/employee/{EMPNO}";
    private final String classType = "http://example.com/ns#Employee";
    private EntityMap entityMap;
    @Mock
    private PredicateObjectMap predicateObjectMap;

    public abstract EntityMap createInstance();

    @Before
    public void setUp() throws Exception {
        entityMap = createInstance();
    }

    @Test
    public void WhenTableNameExists_ShouldReturnValue() {
        entityMap.setEntitySource(tableName);
        String result = entityMap.getEntitySource();
        assertThat(result, is(tableName));
    }

    @Test
    public void WhenTemplateExists_ShouldReturnValue() {
        entityMap.setTemplate(template);
        String result = entityMap.getTemplate();
        assertThat(result, is(template));
    }

    @Test
    public void WhenClassTypeExists_ShouldReturnValue() {
        entityMap.setClassType(classType);
        String result = entityMap.getClassType();
        assertThat(result, is(classType));
    }

    @Test
    public void WhenPredicateObjectMapExits_ShouldReturnList() {
        entityMap.addPredicateObjectMap(predicateObjectMap);
        List<PredicateObjectMap> result = entityMap.listPredicateObjectMaps();
        assertThat(result.isEmpty(), is(false));
    }
}