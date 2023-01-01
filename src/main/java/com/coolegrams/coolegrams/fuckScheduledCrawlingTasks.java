package com.coolegrams.coolegrams;
/*
package com.coolegrams.coolegrams;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.coolegrams.coolegrams.serviceImpl.ItemSelectApiServiceImpl;


@Component
public class ScheduledCrawlingTasks {
    private FirefoxDriver driver;
    @Autowired
    private final static String url = "https://www.coupang.com/np/search?component=&q=";
	@Autowired
	private ItemSelectApiServiceImpl itemSelectApiService;
	
    @SuppressWarnings("deprecation")
//	@Scheduled(cron="* *///1 * * * *")
/*    public void scheduledProcess() throws IOException {
    	String torPath = "C:\\Users\\a\\Desktop\\Tor Browser\\Browser\\firefox.exe";
        String profilePath = "C:\\Users\\a\\Desktop\\Tor Browser\\Browser\\TorBrowser\\Data\\Browser\\profile.default";
        System.setProperty("webdriver.gecko.driver","C:\\Users\\a\\Desktop\\Tor Browser\\geckodriver.exe");
        
        File torProfileDir = new File(profilePath);
        FirefoxBinary binary = new FirefoxBinary(new File(torPath));
        FirefoxProfile torProfile = new FirefoxProfile(torProfileDir);
        
        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(torProfile);
        options.setBinary(binary);
        
        System.out.println("111111");
        WebDriver driver = new FirefoxDriver(options);
        System.out.println("222222");
        /*
    	System.setProperty("webdriver.gecko.driver", "C:\\Users\\a\\Desktop\\Tor Browser\\geckodriver.exe");
        String torPath = "C:\\Users\\a\\Desktop\\Tor Browser\\Browser\\firefox.exe";
        String profilePath = "C:\\Users\\a\\Desktop\\Tor Browser\\Browser\\TorBrowser\\Data\\Browser\\profile.default";

        File torProfileDir = new File(profilePath);
        FirefoxBinary binary = new FirefoxBinary(new File(torPath));
        FirefoxProfile torProfile = new FirefoxProfile(torProfileDir);

        torProfile.setPreference("network.proxy.type", 1);
        torProfile.setPreference("network.proxy.socks", "127.0.0.1");
        torProfile.setPreference("network.proxy.socks_port", 9150);
        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(torProfile);
        options.setBinary(binary);
        options.setProfile(torProfile);
        options.addArguments("--headless");
        options.setCapability(FirefoxOptions.FIREFOX_OPTIONS,options);
        System.out.println("씨빨111111111111111");
        WebDriver driver = new FirefoxDriver(options);
        System.out.println("씨빨222222222222222");
        driver.get("https://www.google.com");*/
    	/*
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\a\\Downloads\\geckodriver.exe");
        //크롬 드라이버 셋팅 (드라이버 설치한 경로 입력)

        String torBinaryPath = "C:\\Users\\a\\Desktop\\Tor Browser\\Browser\\firefox.exe";
        Runtime runTime = Runtime.getRuntime();
        Process torProcess = runTime.exec(torBinaryPath + " -n");
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("network.proxy.type", 1);
        profile.setPreference("network.proxy.socks", "127.0.0.1");
        profile.setPreference("network.proxy.socks_port", 9150);
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setProfile(profile);
        System.out.println("1");
        
        driver = new FirefoxDriver(firefoxOptions);
        System.out.println("2");
        driver.manage().window().maximize();
        System.out.println("3");
		String[] productIds = itemSelectApiService.getScheduledList();
		System.out.println("4");
		for(String productId : productIds) {
			System.out.println(url+productId);
	        driver.get(url+productId);    //브라우저에서 url로 이동한다.
	        try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} //브라우저 로딩될때까지 잠시 기다린다.
	        
	        List<WebElement> elementsAll = driver.findElements(By.className("search-product"));
	        String pattern = "^[0-9]*$";
	        for(int j =0 ; j<elementsAll.size();j++) {
	        	if(elementsAll.get(j).getText().length()>0 && Pattern.matches(pattern, String.valueOf(elementsAll.get(j).getText().charAt(elementsAll.get(j).getText().length()-1)))) {
	        		System.out.println(elementsAll.get(j).findElement(By.className("name")).getText());
	        		System.out.println(elementsAll.get(j).getAttribute("id"));
	        		System.out.println(elementsAll.get(j).findElement(By.className("price-value")).getText());	
	        	}
	        }
	         System.out.println("----------------------------");
	         try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	
		}
        driver.close();	//탭 닫기
        driver.quit();	//브라우저 닫기
        */
  //  }
//}
