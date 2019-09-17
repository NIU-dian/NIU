package com.yskj.push.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.yskj.crop.constant.Result;
import com.yskj.push.framework.util.HtmlRequestUtil;
import com.yskj.push.handler.Jin10WebSocketHandler;
import com.yskj.push.listener.Jin10Listener;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * @Author kai.tang@yintech.cn
 * @Date 2019/8/30 16:51
 * @Version 1.0.0
 */
@Controller
@RequestMapping("/jin")
public class Jin10Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(Jin10Controller.class);

	private static final String JIN10_URL = "https://datacenter.jin10.com/market";

	private static final String YINGCAI_URL = "https://cn.investing.com/currencies/usd-cnh";

	private static final String YINGWEI_URL = "https://cn.investing.com/currencies/";

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Value("${chrome.driver}")
	private String chromeDriver;

	@ResponseBody
	@RequestMapping("/realTime")
	public Result pushDaily() {
		try {

			threadPoolTaskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
						webClient.getOptions().setJavaScriptEnabled(false);
						webClient.getOptions().setCssEnabled(false);
						webClient.getOptions().setThrowExceptionOnScriptError(false);
						webClient.getOptions().setTimeout(30 * 1000);
						webClient.getOptions().setActiveXNative(false);
						webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
						webClient.setAjaxController(new NicelyResynchronizingAjaxController());
						webClient.openWindow(new URL(JIN10_URL), JIN10_URL);
						// webClient.waitForBackgroundJavaScript(30 * 1000);
						// WebClient webClient =
						// HtmlRequestUtil.getWebClient(JIN10_URL);
						// webClient.getOptions().setWebSocketMaxBinaryMessageBufferSize(1024);
						// webClient.getOptions().setWebSocketMaxTextMessageBufferSize(1024);
						webClient.getInternals().addListener(new Jin10Listener());

					} catch (IOException e) {
						LOGGER.error("jin10 error:{}", e);
					}
				}
			});
		} catch (Exception e) {
			LOGGER.error("pushDaily error:{}", e);
		}
		return Result.getInstance().success();
	}

	@ResponseBody
	@RequestMapping("/chromeSpider")
	public Result chromeSpider() {
		try {
			threadPoolTaskExecutor.execute(() -> {
				try {
					spiderForeignCurrency();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			LOGGER.error("chromeSpider error:{}", e);
		}
		return Result.getInstance().success();
	}

	private void spiderForeignCurrency() throws InterruptedException {
		// System.out.println(webDriver.manage().logs().getAvailableLogTypes());
		System.setProperty("webdriver.chrome.driver", chromeDriver);
		WebDriver webDriver = null;
		String url = "https://datacenter.jin10.com/market";
		// 启动一个 chrome 实例
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--no-sandbox");
		chromeOptions.addArguments("--disable-dev-shm-usage");
		chromeOptions.addArguments("--headless");
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
		((ChromeDriver) webDriver).findElementById("dcVideoClose").click();
		webDriver.navigate().refresh();

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

	public static void main(String[] args) {
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
			LOGGER.error("chromeSpider error:{}", e);
		}
	}

}
