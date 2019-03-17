package r2graph.r2rml.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import r2graph.r2rml.TriplesMap;

import java.util.Map;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class R2RMLMapTest {
    private R2RMLMap r2rmlMap;
    private String key;
    @Mock
    private TriplesMap triplesMap;

    @Before
    public void setUp() throws Exception {
       r2rmlMap = new R2RMLMap();
       key = "TriplesMap1";
       r2rmlMap.addTriplesMap(key, triplesMap);
    }

    @Test
    public void listTriplesMap() {
        Map<String, TriplesMap> result = r2rmlMap.listTriplesMap();

        assertThat(result.containsKey(key), is(true));
    }

    @Test
    public void getTriplesMapById() {
        TriplesMap result = r2rmlMap.getTriplesMapById(key);

        assertThat(result, is(notNullValue()));
    }

    @Test
    public void hasTriplesMapId() {
        Boolean result = r2rmlMap.hasTriplesMapId(key);

        assertThat(result, is(true));
    }
}