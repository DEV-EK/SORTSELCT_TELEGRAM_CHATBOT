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

import com.coolegrams.coolegrams.dto.CoupangItemDto;
import com.coolegrams.coolegrams.dto.RefreshItemInfoDto;
import com.coolegrams.coolegrams.serviceImpl.CallCoupangApiServiceImpl;
import com.coolegrams.coolegrams.serviceImpl.ItemRefreshServiceImpl;
import com.coolegrams.coolegrams.serviceImpl.ItemSelectApiServiceImpl;

@Component
public class SearchTasks {
    private WebDriver driver;
    @Autowired
    private final static String url = "https://www.coupang.com/np/search?component=&q=";
    @Autowired
    private final static String urlFooter = "&channel=user";
    @Autowired
    ItemRefreshServiceImpl itemRefreshService;
    @Autowired
    CallCoupangApiServiceImpl callCoupangService;
    public CoupangItemDto[] searchProcess(String keyword) {
        int yn=0;
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\a\\Downloads\\chromedriver.exe");
        //크롬 드라이버 셋팅 (드라이버 설치한 경로 입력)

        ChromeOptions option = new ChromeOptions();
        option.addArguments("-incognito");
        driver = new ChromeDriver(option);
        String currentWindowHandle=driver.getWindowHandle();
        driver.switchTo().window(currentWindowHandle);
        //브라우저 선택
	    driver.get(url+keyword+urlFooter);    //브라우저에서 url로 이동한다.
	    try {
			Thread.sleep(10);
	    } catch (InterruptedException e) {
			e.printStackTrace();
		} //브라우저 로딩될때까지 잠시 기다린다.
	        
        List<WebElement> elementsAll = driver.findElements(By.className("search-product"));
        String pattern = "^[0-9]*$";
        int count=0;
        CoupangItemDto[] elementReturnList = new CoupangItemDto[elementsAll.size()];
       	for(int j =0 ; j<elementsAll.size();j++) {
       		if(elementsAll.get(j).getText().length()>0 && Pattern.matches(pattern, String.valueOf(elementsAll.get(j).getText().charAt(elementsAll.get(j).getText().length()-1)))) {
       			System.out.println();
       			System.out.println(elementsAll.get(j).findElement(By.className("name")).getText());
       			System.out.println(elementsAll.get(j).findElement(By.className("price-value")).getText());
       			System.out.println("----------------------------------------------------------------------------------------");
       			String tempLiteralProcess = elementsAll.get(j).findElement(By.className("price-value")).getText().replaceAll(",", "");
       			int price = Integer.parseInt(tempLiteralProcess);
       			String name = elementsAll.get(j).findElement(By.className("name")).getText();
        		String id = elementsAll.get(j).getAttribute("id");
        		yn = itemRefreshService.insertItemInfo(new RefreshItemInfoDto(name,id,"",price));
       			elementReturnList[count]= new CoupangItemDto("","","",callCoupangService.callCoupangItemInfoAPI(id),"","",elementsAll.get(j).findElement(By.className("name")).getText(), elementsAll.get(j).findElement(By.className("price-value")).getText(),"");
       			count++;
       		}
       	}
       	CoupangItemDto[] elementReturnList2 = new CoupangItemDto[count];
       	int count2=0;
       	for(int i = 0 ; i < elementReturnList.length ; i++) {
       		if(null!=elementReturnList[i]) {
       			elementReturnList2[count2]=elementReturnList[i];
       			count2++;
       		}
       	}
       	System.out.println(count+" 카운트 !!");
       	for(int i = 0 ; i < elementReturnList2.length ; i++) {
       		System.out.println(elementReturnList2[i].toString());
       	}
        driver.quit();

        //브라우저 닫기
        return elementReturnList2;
	}
}
