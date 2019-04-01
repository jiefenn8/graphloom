package r2graph.r2rml;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import r2graph.util.MappingDocument;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests for R2RMLParser code. Currently skeletal till further
 * iteration of the project or feature.
 */
public class R2RMLParserTest {

    @Mock
    MappingDocument mappingDocument;
    private R2RMLParser r2rmlParser;
    private R2RMLMap expectedResult;

    @Before
    public void setUp() throws Exception {
        r2rmlParser = new R2RMLParser();
    }

    /**
     * Tests that the R2RMLParser returns a {@code R2RMLMap} after parsing
     * a given {@code MappingDocument}
     */
    @Test
    public void WhenParseMappingDocument_ShouldReturnR2RMLMap() {


        R2RMLMap result = r2rmlParser.parse(mappingDocument);
        assertThat(result, is(notNullValue()));
    }
}