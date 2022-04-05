package core;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class DriverFactory {
    private final URL url;
    private final DesiredCapabilities capabilities;
    private final Config config = new Config();

    public DriverFactory(String url, Map<String, Object> map) throws MalformedURLException {
        this.url = new URL(url);
        this.capabilities = new DesiredCapabilities(map);
    }

    MobileDriver createDriver() {
        String platform = config.getPlatform().toUpperCase();
        switch (platform) {
            case "ANDROID":
                return new AndroidDriver<MobileElement>(url, capabilities);
            case "IOS":
                return new IOSDriver<MobileElement>(url, capabilities);
            default:
                throw new IllegalArgumentException(
                        String.format("Driver Factory type not implemented: [%s]", platform));
        }
    }
}
