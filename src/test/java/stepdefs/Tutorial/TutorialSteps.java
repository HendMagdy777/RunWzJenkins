package stepdefs.Tutorial;
import core.Config;
import io.cucumber.java.en.*;
import org.testng.asserts.SoftAssert;
import pages.TutorialPage.*;

public class TutorialSteps {
    private TutorialPageAbstract page;
    SoftAssert softAssert;
    public  TutorialSteps(Config config) {
        config = new Config();
        if (config.isAndroid()) page = new TutorialPageAndroid();
        if (config.isIos()) page = new TutorialPageIOS();
        softAssert = new SoftAssert();
    }
    @Given("App is launched")
    public void appLaunched() {
        System.out.println("App Is Launched");

    }
    @Then("message displayed")
    public void message() {
        System.out.println("Testing is done");

    }



}
