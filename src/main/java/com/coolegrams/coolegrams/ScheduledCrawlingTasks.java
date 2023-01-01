package com.coolegrams.coolegrams;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.coolegrams.coolegrams.dto.RefreshItemInfoDto;
import com.coolegrams.coolegrams.serviceImpl.ItemSelectApiServiceImpl;

@Component
public class ScheduledCrawlingTasks {
    private WebDriver driver;
    @Autowired
    private final static String url = "https://www.coupang.com/np/search?component=&q=";
    @Autowired
    private final static String urlFooter = "&channel=user";
	@Autowired
	ItemSelectApiServiceImpl itemSelectApiService;
//	@Scheduled(cron="* */5 * * * *")
	@Scheduled(cron="*/10 * * * * *")
	public void searchProcess() {
		String[] productIds = itemSelectApiService.getScheduledList();
		System.out.println(Arrays.toString(productIds));
		for(int i = 0 ; i <productIds.length ; i++ ) {

		System.setProperty("webdriver.chrome.driver", "C:\\Users\\a\\Downloads\\chromedriver.exe");
        //크롬 드라이버 셋팅 (드라이버 설치한 경로 입력)

        ChromeOptions option = new ChromeOptions();
        option.addArguments("-incognito");
        driver = new ChromeDriver(option);
        String currentWindowHandle=driver.getWindowHandle();
        driver.switchTo().window(currentWindowHandle);
        //브라우저 선택
	        driver.get(url+productIds[i]+urlFooter);    //브라우저에서 url로 이동한다.
	        try {
				Thread.sleep(10);//1500
			} catch (InterruptedException e) {
				e.printStackTrace();
			} //브라우저 로딩될때까지 잠시 기다린다.
	        
        List<WebElement> elementsAll = driver.findElements(By.className("search-product"));
        String pattern = "^[0-9]*$";
		for(int j =0 ; j<elementsAll.size();j++) {
       		if(elementsAll.get(j).getText().length()>0 && Pattern.matches(pattern, String.valueOf(elementsAll.get(j).getText().charAt(elementsAll.get(j).getText().length()-1)))) {
       			System.out.println("==================");
       			System.out.println(elementsAll.get(j).findElement(By.className("name")).getText());
       			System.out.println("==================");
       			itemSelectApiService.insertCrawlingResponse(
       					//쿠팡링크수정
       					new RefreshItemInfoDto(elementsAll.get(j).findElement(By.className("name")).getText(), elementsAll.get(j).getAttribute("id"),"",Integer.parseInt(elementsAll.get(j).findElement(By.className("price-value")).getText().replaceAll(",","")))
       					);
       		}
       	}
        System.out.println("----------------------------");    

        driver.quit();	//브라우저 닫기
        }		
	}
}
