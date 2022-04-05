package base;

import core.Config;
import core.Hooks;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static utils.PropertiesLoader.readPropertyFile;

public abstract class PageObjectBase {
    public MobileDriver driver;
    WebDriverWait wait;
    private final int leftX;
    private final int rightX;
    private final int topY;
    private final int bottomY;
    static Properties platformProp = readPropertyFile("config/platform.properties");
    private  static  String platform =  System.getProperty("PLATFORM", platformProp.getProperty("PLATFORM"));

    public PageObjectBase() {
        this.driver = Hooks.getDriver();
        setDecoratorBasedOnPlatform();
        wait = new WebDriverWait(driver, 30);
        leftX = (int)(driver.manage().window().getSize().width *0.2);
        rightX= (int)(driver.manage().window().getSize().width *0.7);
        topY = (int)(driver.manage().window().getSize().height *0.2);
        bottomY = (int)(driver.manage().window().getSize().height *0.7);
    }
    private void setDecoratorBasedOnPlatform()
    {
        Config config = new Config();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        if (config.isMobile()) setAppiumDecorator();
    }
    private void setAppiumDecorator()
    {
        AppiumFieldDecorator appiumFieldDecorator =
                new AppiumFieldDecorator(driver, Duration.ofSeconds(3));
        PageFactory.initElements(appiumFieldDecorator, this);
    }

}
