package com.yskj.niu;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@SpringBootApplication
public class NiuApplication {

    public static void main(String[] args) {
//        SpringApplication.run(NiuApplication.class, args);
        try {
            // System.out.println(webDriver.manage().logs().getAvailableLogTypes());
            System.setProperty("webdriver.chrome.driver", "D:/chromeDriver/chromedriver.exe");
            WebDriver webDriver = null;
            String url = "http://sh.niu.com/Account/LogOn";
            // 启动一个 chrome 实例
            ChromeOptions chromeOptions = new ChromeOptions();
            // chromeOptions.addArguments("--no-sandbox");
            // chromeOptions.addArguments("--disable-dev-shm-usage");
            // chromeOptions.addArguments("--headless");
            LoggingPreferences loggingprefs = new LoggingPreferences();
            loggingprefs.enable(LogType.PERFORMANCE, Level.ALL);
            Map<String, Object> perfLogPrefs = new HashMap<String, Object>();
            perfLogPrefs.put("traceCategories", "browser,devtools.timeline,devtools"); // comma-separated
            // trace
            // categories
            chromeOptions.setExperimentalOption("perfLoggingPrefs", perfLogPrefs);
            chromeOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
            chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

            DesiredCapabilities cap = DesiredCapabilities.chrome();
            cap.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs);
            chromeOptions.merge(cap);
            chromeOptions.setExperimentalOption("w3c", false);
            webDriver = new ChromeDriver(chromeOptions);
            webDriver.get(url);
            Thread.sleep(5 * 1000);
            System.out.println(webDriver.manage().logs().getAvailableLogTypes().toString());
            while (true) {
                try {
                    List<WebElement> userNameInputList = ((ChromeDriver) webDriver).findElementsById("UserName");
                    ((ChromeDriver) webDriver).findElementById("UserName").clear();
                    if (CollectionUtils.isEmpty(userNameInputList)) {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
                ((ChromeDriver) webDriver).findElementById("UserName").sendKeys("HN37401Z02");
                ((ChromeDriver) webDriver).findElementById("Passwordinput").clear();
                ((ChromeDriver) webDriver).findElementById("Passwordinput").sendKeys("fm339900");
                ((ChromeDriver) webDriver).findElementById("iValidCode").clear();
                ((ChromeDriver) webDriver).findElementById("iValidCode").click();
                WebElement codeImg = ((ChromeDriver) webDriver).findElementById("GL_StandardCode");
                String imgSrc = codeImg.getAttribute("src");
                // /Account/GetImg?ValidateCode=CMJF&t=1568616692770
                String[] codeArry = imgSrc.split("=");
                String code = codeArry[1].substring(0, 4);
                System.out.println(code);
                ((ChromeDriver) webDriver).findElementById("iValidCode").sendKeys(code);

                System.out.println("等待输入验证码");
                // 等待输入验证码
                Thread.sleep(3 * 1000);
                System.out.println("登录");
                ((ChromeDriver) webDriver).findElementByClassName("easyui-linkbutton").click();
            }
            // 登錄成功等待5秒防止不能正常獲取元素
            Thread.sleep(5 * 1000);
            WebElement searchBtn = null;
            boolean flag = true;
            while (flag) {
                ((ChromeDriver) webDriver).findElementById("leftmenu_1").click();
//				WebElement menuElemnt = ((ChromeDriver) webDriver).findElement(By.id("menuframe"));
                webDriver.switchTo().frame("menuframe");
                List<WebElement> elementList = ((ChromeDriver) webDriver).findElementsByClassName("lcitem");
                for (WebElement webElement : elementList) {
                    String text = webElement.getText();
                    System.out.println(text);
                    if( "查配件发运".equals(text)){
                        webElement.click();
                        webDriver.switchTo().defaultContent();
                        webDriver.switchTo().frame("winFrame_002_032");
                        searchBtn = ((ChromeDriver) webDriver).findElementByClassName("searchbox-button");
                        flag = false;
                        break;
                    }
                }

            }

            while(true){
                Thread.sleep(60 * 1000);
                searchBtn.click();
            }

            // webDriver.navigate().refresh();

        } catch (Exception e) {
           e.printStackTrace();

    }


    }

}
