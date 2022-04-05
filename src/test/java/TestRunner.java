import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features/Tutoiral/Tutoiral.feature"
        },
        glue = {"stepdefs", "utils", "pages", "core", "base"},
        plugin = {"pretty" , "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" ,
                "html:target/cucumber-reports" },
        monochrome = true ,
        publish = true
)
public class TestRunner {

    @BeforeClass
    public static void setup() {

    }

}