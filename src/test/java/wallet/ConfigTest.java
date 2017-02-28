package wallet;

import org.junit.Test;

import java.util.Collections;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class ConfigTest {

    private final Config config = new Config();

    @Test
    public void envOverwrites() throws Exception {

        Properties properties = config.properties(Collections.singletonMap("DATASOURCE_URL", "TEST"));

        assertEquals("TEST", properties.getProperty("datasource.url"));
    }
}