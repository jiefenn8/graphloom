package r2graph.configmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractConfigMapTest {

    private final String id = "TriplesMap1";
    private ConfigMap configMap;
    @Mock
    private EntityMap entityMap;

    public abstract ConfigMap createInstance();

    @Before
    public void setUp() throws Exception {
        configMap = createInstance();
    }

    @Test
    public void WhenTriplesMapExists_ShouldReturnMap() {
        configMap.addEntityMap(id, entityMap);
        Map<String, EntityMap> result = configMap.listEntityMaps();
        assertThat(result, is(notNullValue()));
    }
}