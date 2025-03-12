package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.TestNG;
import org.testng.annotations.DataProvider;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "step_definitions",
        tags = "@smoke and not @ignore",
        plugin = {"pretty", "json:target/cucumber.json", "html:target/cucumber-report.html"}
)
public class RunCucumberTest extends AbstractTestNGCucumberTests {
    private static final Logger logger = Logger.getLogger(RunCucumberTest.class.getName());
    private static final Properties properties = new Properties();

    //Static block to load properties file
    static {
        Path configPath = Paths.get(System.getProperty("config.path",
                Paths.get(System.getProperty("user.dir"), "src", "test", "resources",
                        "config.properties").toString()));
        try(InputStream input = Files.newInputStream(configPath)) {
            properties.load(input);
        }catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load properties file!", e);
        }
    }

    public static void main(String[] args) {
        //Create an instance of TestNG
        TestNG testNG = new TestNG();

        //Create a new TestNG Suite
        XmlSuite suite = new XmlSuite();

        //Get the thread count from the properties file
        int threadCount = getThreadCount();
        System.out.println("Configured thread count value: " + threadCount);

        // Set the number of threads for the data provider
        suite.setDataProviderThreadCount(threadCount);

        // Create a new TestNG test and add it to the suite
        XmlTest test = new XmlTest(suite);
        test.setName("Cucumber Tests"); //Setting the name of our test
        test.setXmlClasses(Collections.singletonList(new XmlClass(RunCucumberTest.class))); // Add the test class to the test

        // Disable default listeners (Will disable TestNG reports from being generated)
        testNG.setUseDefaultListeners(false);

        // Add the suite to the TestNG instance
        testNG.setXmlSuites(Collections.singletonList(suite));

        // Run TestNG with the configured suite
        testNG.run();
    }

    //Method to get thread count from properties file
    private static int getThreadCount() {
        return Integer.parseInt(properties.getProperty("thread.count", "1"));
    }

    //DataProvider Method
    //used for parallel execution, allowing multiple tests to run simultaneous
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios(); // Provide data for the tests, enabling parallel execution
    }
}
