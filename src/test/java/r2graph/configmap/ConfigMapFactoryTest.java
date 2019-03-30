package r2graph.configmap;

import org.junit.Test;
import r2graph.r2rml.R2RMLMap;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for ConfigMapFactory code. Currently skeletal till further
 * iteration of the project or feature.
 */
public class ConfigMapFactoryTest {

    /**
     * Tests that the ConfigMap factory returns a new instance of {@code R2RMLMap}
     * implementation.
     */
    @Test
    public void WhenCreateR2RMLMap_ShouldReturnR2RMLInstance(){
        ConfigMap result = ConfigMapFactory.createR2RMLMap();
        assertThat(result, instanceOf(R2RMLMap.class));
    }
}
