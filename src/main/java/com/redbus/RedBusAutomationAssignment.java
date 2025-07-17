package com.redbus;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;

public class RedBusAutomationAssignment {

	public static void main(String[] args) {

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
		WebElement searchTextBoxElement = wd.switchTo().activeElement();
		searchTextBoxElement.sendKeys("Mumbai");

		// Define the suggestion items locator
		By searchCategoryLocator = By.xpath("//div[contains(@class,\"searchCategory\")]");
		List<WebElement> searchList = wait
				.until(ExpectedConditions.numberOfElementsToBeMoreThan(searchCategoryLocator, 1));

		System.out.println(searchList.size());
		
		WebElement locationSearchResult = searchList.get(0);
		
		By locationNameLocator = By.xpath(".//div[contains(@class,\"listHeader\")]"); // chaining of webElements
		List<WebElement> locationList = locationSearchResult.findElements(locationNameLocator);
		System.out.println(locationList.size());

		for (WebElement location : locationList) {
			String placeName = location.getText();
			if (placeName.equalsIgnoreCase("Mumbai")) {
				location.click();
				break;
			}

		}

		// Focus on the TO section

		WebElement toTextBox = wd.switchTo().activeElement();
		toTextBox.sendKeys("Pune");

		By ToSearchCategoryLocator = By.xpath("//div[contains(@class,\"searchCategory\")]");
		List<WebElement> ToSearchList = wait
				.until(ExpectedConditions.numberOfElementsToBeMoreThan(ToSearchCategoryLocator, 2));
		System.out.println(ToSearchList.size());

		WebElement toLocationCategory = ToSearchList.get(0);
		By toLocationNameLocator = By.xpath(".//div[contains(@class,\"listHeader\")]");
		List<WebElement> toLocationList = toLocationCategory.findElements(toLocationNameLocator);

		for (WebElement toLocation : toLocationList) {
			if (toLocation.getText().equalsIgnoreCase("Pune")) {
				toLocation.click();
				break;

			}
		}

	}
}
