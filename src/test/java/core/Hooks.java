package core;

import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static utils.PropertiesLoader.readPropertyFile;

public class Hooks {
    private static MobileDriver driver;
    private final Config config = new Config();
    private DriverFactory factory;
    private String scenarioname;
    Properties androidProp = readPropertyFile("config/Android.properties");
    Properties iOSProp = readPropertyFile("config/ios.properties");
    static Properties platformProp = readPropertyFile("config/platform.properties");
    private  static  String platform =  System.getProperty("PLATFORM", platformProp.getProperty("PLATFORM"));

    public Hooks() {
        setDriver(driver);
    }

    public static MobileDriver getDriver() {
        return driver;
    }

    private static void setDriver(MobileDriver driver) {
        Hooks.driver = driver;
    }

    @Before(order = 1)
    public void beforeAll() throws MalformedURLException {
        if (driver == null) {
            factory = new DriverFactory(config.getUrl(), config.getCapabilities());
            setDriver(factory.createDriver());
            String logsFilePath = System.getProperty("user.dir"+"config/log4j2.xml");
            //Configure xml file for LOG4J
            DOMConfigurator.configure("config/log4j2.xml");
            BasicConfigurator.configure();
            PropertyConfigurator.configure("config/log4j2.xml");
        }
    }
    @Before(order = 2)
    public void closeAppleIdOverlay(){
        if(platform == "iOs"){
            try{
                WebElement ele =   driver.findElement(By.xpath(" //*[@text='Not Now']"));
                if (ele.isDisplayed())
                    ele.click();
            }
            catch (Exception e)
            {
                System.out.println("element overlay [Apple id] not exist");
            }
        }
    }
    @Before(order = 3 , value = "@Reset")
    public void Before(){
        driver.resetApp();
        if(platform == "Android")
        {
            System.out.println("The value is " +platform);
            driver.resetApp();

        }
    }
    @Before(order = 4)
    public void start_recording()
    {
        if(platform == "Android")
        ((CanRecordScreen)driver).startRecordingScreen(new AndroidStartScreenRecordingOptions());
    }
    @Before(order = 5)
    public void getScenarioName(Scenario scenario){
        String[] tab = scenario.getId().split("/");
        int rawFeatureNameLength = tab.length;
        String featureName = tab[rawFeatureNameLength - 1].split(":")[0];
        scenarioname = featureName.replace("." , "")+scenario.getName().toString().replace(" ", "");

    }

    @After()
    public void stop_recording() throws IOException {
        if (platform == "Android") {
            String base64String = ((CanRecordScreen) driver).stopRecordingScreen();
            byte[] data = Base64.decodeBase64(base64String);
            String destinationPath =
                    String.format((new File(System.getProperty("user.dir")) +
                                    System.getProperty("Records", androidProp.getProperty("Records"))),
                            scenarioname);
            Path path = Paths.get(destinationPath);
            Files.write(path, data);
        }
    }
    @After(order = 1)
    public void takeScreenshotOnFail(Scenario scenario) {
        if(platform == "Android") {
            if (scenario.isFailed()) {
                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                try {
                    String screenshotsPath = String.format((new File(System.getProperty("user.dir")) +
                            System.getProperty("Screenshots", androidProp.getProperty("Screenshots"))), scenarioname);

                    FileUtils.copyFile(scrFile, new File(screenshotsPath));

                    TakesScreenshot ts = (TakesScreenshot) driver;
                    byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
                    scenario.attach(screenshot, "image/jpeg", scenarioname);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @After()
    public void afterFeature()
    {
        //driver.resetApp();
    }

}
