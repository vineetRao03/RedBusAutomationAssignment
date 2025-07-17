package com.redbus;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RedBusAutomationAssignment2 {

	public static void main(String[] args) throws InterruptedException {

		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--start-maximized");
		// Launching The Chrome Browser
		WebDriver wd = new ChromeDriver(chromeOptions);
		WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(10));

		wd.get("https://www.redbus.in");

		By sourceButtonLocator = By.xpath("//div[contains(@class,\"srcDestWrapper\") and @role=\"button\"]");
//		WebElement sourceButtonElement =wd.findElement(sourceButtonLocator); // not synchronized
		WebElement sourceButtonElement = wait.until(ExpectedConditions.visibilityOfElementLocated(sourceButtonLocator)); // synchronized!!
		sourceButtonElement.click();

		By searchSuggestionLocator = By.xpath("//div[contains(@class,\"searchSuggestionWrapper\")]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(searchSuggestionLocator));
		selectLocation(wd, wait, "Mumbai"); // From Location
		selectLocation(wd, wait, "Pune"); // To Location

		By searchButtonLocator = By.xpath("//button[contains(@class,\"searchButtonWrapper\")]");
		WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(searchButtonLocator));
		searchButton.click();

		By primoButtonLocator = By.xpath("//div[contains(text(),\"Primo\")]");
		WebElement primoButton = wait.until(ExpectedConditions.elementToBeClickable(primoButtonLocator));
		primoButton.click();

		Thread.sleep(4000);

		By eveningButtonLocator = By.xpath("//div[contains(text(),\"18:00-24:00\")]");
		WebElement eveningButton = wait.until(ExpectedConditions.elementToBeClickable(eveningButtonLocator));
		eveningButton.click();

		By subTitleLocator = By.xpath("//span[contains(@class,\"subtitle\")]");
		WebElement subTitle = null;

		if (wait.until(ExpectedConditions.textToBePresentInElementLocated(subTitleLocator, "buses"))) {
			subTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(subTitleLocator));
		}

		System.out.println(subTitle.getText());

		By tuppleWrapperLocator = By.xpath("//li[contains(@class,\"tupleWrapper\")]"); // found the row locator
		By busesNameLocator = By.xpath(".//div[contains(@class,\"travelsName\")]"); // bus name locator
		
//		List<WebElement> rowList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tuppleWrapperLocator));
		// Initially there will be a set of rows present -> 10
//		System.out.println("Total number of row "+ rowList.size());
		// Traverse each row ony by one
//		for (WebElement row : rowList) {
//			System.out.println(row.findElement(busesNameLocator).getText()); // chaining of WebElement
//		}

		// How to Scroll in Selenium WebDriver!!
		// next set of buses , we need to scroll down to 3rd last element
		
		JavascriptExecutor js = (JavascriptExecutor) wd;
//		List< WebElement> newRowList = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(tuppleWrapperLocator, rowList.size()));
//		System.out.println("Total number of buses "+ newRowList.size());

		while (true) {
			// get row from the Page
			List<WebElement> rowList = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tuppleWrapperLocator));

			List<WebElement> endOfList = wd.findElements(By.xpath("//span[contains(text(),\"End of list\")]"));

			if (!endOfList.isEmpty()) {
				break; // exit condition from the while loop
			}
			js.executeScript("arguments[0].scrollIntoView({behavior:'smooth'})", rowList.get(rowList.size() - 3));

		}
		List<WebElement> rowList = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tuppleWrapperLocator));

		for (WebElement row : rowList) {
			String busName = row.findElement(busesNameLocator).getText();
			System.out.println(busName);
		}

		System.out.println("Total number of buses loaded with Primo and Evening filter: " + rowList.size());

	}

	public static void selectLocation(WebDriver wd, WebDriverWait wait, String locationData) {
		WebElement searchTextBoxElement = wd.switchTo().activeElement();
		searchTextBoxElement.sendKeys(locationData);

		// Define the suggestion items locator
		By searchCategoryLocator = By.xpath("//div[contains(@class,\"searchCategory\")]");
		List<WebElement> searchList = wait
				.until(ExpectedConditions.numberOfElementsToBeMoreThan(searchCategoryLocator, 2));

		System.out.println(searchList.size());

		WebElement locationSearchResult = searchList.get(0);

		By locationNameLocator = By.xpath(".//div[contains(@class,\"listHeader\")]"); // chaining of webElements
		List<WebElement> locationList = locationSearchResult.findElements(locationNameLocator);
//		System.out.println(locationList.size());

		for (WebElement location : locationList) {
			String placeName = location.getText();
			if (placeName.equalsIgnoreCase(locationData)) {
				location.click();
				break;
			}

		}
	}
}
