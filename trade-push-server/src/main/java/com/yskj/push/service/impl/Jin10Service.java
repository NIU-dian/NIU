package com.yskj.push.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yskj.push.handler.Jin10WebSocketHandler;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * @Author kai.tang@yintech.cn
 * @Date 2019/9/10 18:35
 * @Version 1.0.0
 */
@Service
public class Jin10Service {

    private WebDriver webDriver = null;

    private void spiderForeignCurrency() throws InterruptedException {

        //            System.out.println(webDriver.manage().logs().getAvailableLogTypes());
        System.setProperty("webdriver.chrome.driver", "E:/YTX/yintech_git/hzcj/push-new/trade/trade-push/trade-push-server/src/main/resources/driver/chromedriver.exe");
        String url = "https://datacenter.jin10.com/market";
        //启动一个 chrome 实例
        ChromeOptions chromeOptions = new ChromeOptions();
        LoggingPreferences loggingprefs = new LoggingPreferences();
        loggingprefs.enable(LogType.PERFORMANCE, Level.ALL);
        Map<String, Object> perfLogPrefs = new HashMap<String, Object>();
        perfLogPrefs.put("traceCategories", "browser,devtools.timeline,devtools"); // comma-separated trace categories
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
        ((ChromeDriver) webDriver).findElementById("dcVideoClose").click();

        while (true) {
            LogEntries logEntries = webDriver.manage().logs().get(LogType.PERFORMANCE);
            logEntries.forEach(logEntry -> {
                String messageJson = logEntry.getMessage();
                JSONObject messageObj = JSON.parseObject(messageJson);
                String method = messageObj.getJSONObject("message").getString("method");
                if ("Network.webSocketFrameReceived".equalsIgnoreCase(method)) {
                    Jin10WebSocketHandler.sendMessageToAll(new TextMessage(messageJson));
                    System.out.println(messageJson);
                }
            });

        }
    }

    public void refresh(){
        webDriver.navigate().refresh();
    }

}
