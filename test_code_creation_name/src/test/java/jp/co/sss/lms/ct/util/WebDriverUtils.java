package jp.co.sss.lms.ct.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;

/**
 * Webドライバーユーティリティ
 * @author holy
 */
public class WebDriverUtils {

	/** Webドライバ */
	public static WebDriver webDriver;

	/**
	 * インスタンス取得
	 * @return Webドライバ
	 */
	public static void createDriver() {
		System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");
		webDriver = new ChromeDriver();
	}

	/**
	 * インスタンス終了
	 */
	public static void closeDriver() {
		webDriver.quit();
	}

	/**
	 * 画面遷移
	 * @param url
	 */
	public static void goTo(String url) {
		webDriver.get(url);
		pageLoadTimeout(5);
	}

	/**
	 * ページロードタイムアウト設定
	 * @param second
	 */
	public static void pageLoadTimeout(int second) {
		webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(second));
	}

	/**
	 * 要素の可視性タイムアウト設定
	 * @param locater
	 * @param second
	 */
	public static void visibilityTimeout(By locater, int second) {
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(second));
		wait.until(ExpectedConditions.visibilityOfElementLocated(locater));
	}

	/**
	 * 指定ピクセル分だけスクロール
	 * @param pixel
	 */
	public static void scrollBy(String pixel) {
		((JavascriptExecutor) webDriver).executeScript("window.scrollBy(0," + pixel + ");");
	}

	/**
	 * 指定位置までスクロール
	 * @param pixel
	 */
	public static void scrollTo(String pixel) {
		((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0," + pixel + ");");
	}

	/**
	 * エビデンス取得
	 * @param instance
	 */
	public static void getEvidence(Object instance) {
		File tempFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		try {
			String className = instance.getClass().getEnclosingClass().getSimpleName();
			String methodName = instance.getClass().getEnclosingMethod().getName();
			Files.move(tempFile, new File("evidence\\" + className + "_" + methodName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * エビデンス取得（サフィックスあり）
	 * @param instance
	 * @param suffix
	 */
	public static void getEvidence(Object instance, String suffix) {
		File tempFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		try {
			String className = instance.getClass().getEnclosingClass().getSimpleName();
			String methodName = instance.getClass().getEnclosingMethod().getName();
			Files.move(tempFile, new File("evidence\\" + className + "_" + methodName + "_" + suffix + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 新しいタブへ切り替え
	 */
	public static void switchToNewTab() {
		String originalHandle = webDriver.getWindowHandle();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		wait.until(driver -> driver.getWindowHandles().size() > 1);

		for (String handle : webDriver.getWindowHandles()) {
			if (!handle.equals(originalHandle)) {
				webDriver.switchTo().window(handle);
				return;
			}
		}
		throw new IllegalStateException("新しいタブが見つかりませんでした");
	}

	/**
	 * 元のタブへ帰る
	 * @param originalTab 元のタブハンドル
	 */
	public static void closeCurrentTabAndReturn(String originalTab) {
		webDriver.close();
		webDriver.switchTo().window(originalTab);
	}

	/**
	 * 入力値クリア
	 * @param locator
	 */
	public static void clearText(By locator) {
		WebElement element = waitVisible(locator);
		element.clear();
	}

	/**
	 * クリック
	 * @param locator
	 */
	public static void clickElement(By locator) {
		WebElement button = waitClickable(locator);
		scrollTo("300");
		button.click();
	}

	/**
	 * 表示確認
	 * @param locator
	 */
	public static void assertVisible(By locator) {
		assertTrue(waitVisible(locator).isDisplayed());
	}

	/**
	 * 表示待機
	 * @param locator
	 * @return
	 */
	public static WebElement waitVisible(By locator) {
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	/**
	* クリック待機
	* @param locator
	* @return
	*/
	public static WebElement waitClickable(By locator) {
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	/**
	* 勤怠の出退勤時間をまとめてセット
	* @param row 行番号（0始まり）
	* @param startH 出勤（時）
	* @param startM 出勤（分）
	* @param endH 退勤（時）
	* @param endM 退勤（分）
	*/
	public static void setWorkTime(int row, String startH, String startM, String endH, String endM) {
		selectDropdown(By.id("startHour" + row), startH);
		selectDropdown(By.id("startMinute" + row), startM);
		selectDropdown(By.id("endHour" + row), endH);
		selectDropdown(By.id("endMinute" + row), endM);
	}

	/**
	 * アラートが出たらOKを押す（出なければ無視して続行）
	 */
	public static void acceptAlertIfPresent() {
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(2));
			wait.until(ExpectedConditions.alertIsPresent()).accept();
		} catch (Exception e) {
			// アラートなし → 何もしない
		}
	}

	/**
	 * 入力ボックスに文字入力（クリア付き）
	 */
	public static void inputText(By locator, String text) {
		WebElement element = waitVisible(locator);
		element.clear();
		element.sendKeys(text);
	}

	/**
	 * ドロップダウン選択（既存でもOKだけど安全性UP版）
	 */
	public static void selectDropdown(By locator, String value) {
		WebElement dropdown = waitClickable(locator);
		dropdown.click();
		WebElement option = dropdown.findElement(By.xpath(".//option[@value='" + value + "']"));
		option.click();
	}

}
