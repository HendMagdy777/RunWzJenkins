package core;

import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;
import static utils.PropertiesLoader.properties;
import static utils.PropertiesLoader.readPropertyFile;

public class Config {
    private final String platform;
    private final Map<String, Object> capabilities = new HashMap<>();
    private boolean isAndroid;
    private boolean isIos;
    private boolean isMobile;
    public static final String WORKSPACE = getProperty("user.dir");
    private String url;
    Properties iosProp = readPropertyFile("config/ios.properties");
    private String accesskey = System.getProperty("PerfectoUsername", iosProp.getProperty("PerfectoUsername"));
    //  private String accesskey = System.getenv("PerfectoUsername");
    public Config() {
        Logger.getLogger("org.openqa.core.remote").setLevel(Level.OFF);
        Properties androidProp = readPropertyFile("config/platform.properties");
        platform = System.getProperty("PLATFORM", androidProp.getProperty("PLATFORM"));
        setCapabilitiesForPlatform(platform);
    }

    private void setCapabilitiesForPlatform(String platform) {
        isAndroid = platform.equalsIgnoreCase("Android");
        isIos = platform.equalsIgnoreCase("iOS");
        if (isAndroid || isIos) isMobile = true;
        if (isAndroid) setAndroidCapabilities();
        if (isIos) setIosCapabilities();
    }

    private void setIosCapabilities()  {
        Properties iosProp = readPropertyFile("config/ios.properties");
        if(iosProp.getProperty("CLOUD").equalsIgnoreCase("yes"))
        {
            capabilities.put("newCommandTimeout", 90000);
            capabilities.put("adbExecTimeout", 200000);
            capabilities.put("autoAcceptAlerts", true);
            capabilities.put("fullReset", false);
            capabilities.put("noReset", true);
            capabilities.put("autoGrantPermissions", "true");
            }

        }
    private void setAndroidCapabilities() {
        Properties androidProp = readPropertyFile("config/Android.properties");
        url = System.getProperty("seleniumGrid", "http://0.0.0.0:4723/wd/hub");
        capabilities.put("deviceName", System.getProperty("DEVICE_NAME", androidProp.getProperty("DEVICE_NAME")));
        capabilities.put("systemPort", parseInt(getProperty("systemPort", "8200")));
        capabilities.put("appiumVersion", "1.21.0");
        capabilities.put("autoGrantPermissions", true);
        capabilities.put("appActivity", "com.google.android.maps.MapsActivity");
        capabilities.put("appPackage", "com.google.android.apps.maps");
        capabilities.put("autoAcceptAlerts", true);
        capabilities.put("newCommandTimeout", 90000);
        capabilities.put("androidInstallTimeout", 90000);
        capabilities.put("uiautomator2ServerInstallTimeout", 120000);
        capabilities.put("adbExecTimeout", 200000);
        capabilities.put("platformVersion", System.getProperty("PLATFORM_VERSION", androidProp.getProperty("PLATFORM_VERSION")));
        capabilities.put("platformName", "Android");
        capabilities.put("fullReset", false);
        capabilities.put("noReset", true);
        capabilities.put("automatorName", "UiAutomator2");
        capabilities.put("noSign", "true");
    }


    //////////////////
    // Get and Sets //
    //////////////////
    String getPlatform() {
        return platform;
    }

    public Map<String, Object> getCapabilities() {
        return capabilities;
    }

    public String getUrl() {
        return url;
    }

    public boolean isAndroid() {
        return isAndroid;
    }

    public boolean isIos() {
        return isIos;
    }

    public boolean isMobile() {
        return isMobile;
    }

}