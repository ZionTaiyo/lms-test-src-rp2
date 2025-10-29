package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト 勤怠管理機能
 * ケース11
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース11 受講生 勤怠直接編集 正常系")
public class Case11 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// TODO ここに追加
		goTo("http://localhost:8080/lms/");
		assertTrue(webDriver.getCurrentUrl().contains("/lms/"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		// TODO ここに追加
		WebElement loginId = webDriver.findElement(By.id("loginId"));
		WebElement password = webDriver.findElement(By.id("password"));

		loginId.clear();
		password.clear();

		String LMS_LOGIN = System.getenv("LMS_LOGIN");
		String LMS_PASSWORD = System.getenv("LMS_PASSWORD");

		loginId.sendKeys(LMS_LOGIN);
		password.sendKeys(LMS_PASSWORD, Keys.ENTER);

		visibilityTimeout(By.id("wrap"), 5);
		assertTrue(webDriver.getCurrentUrl().contains("/lms/course/detail"));

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() throws InterruptedException {
		// TODO ここに追加

		Actions actions = new Actions(webDriver);
		WebElement attendanceLink = webDriver.findElement(By.linkText("勤怠"));
		actions.moveToElement(attendanceLink).perform();

		//待機&クリック
		Thread.sleep(300);
		getEvidence(new Object() {
		}, "01");
		attendanceLink.click();

		//アラートの「OK」をクリック
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		Thread.sleep(500);
		alert.accept();

		//URL確認
		visibilityTimeout(By.id("wrap"), 5);
		assertTrue(webDriver.getCurrentUrl().contains("lms/attendance/detail"));

		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() throws InterruptedException {
		// TODO ここに追加

		Actions actions = new Actions(webDriver);
		WebElement attendanceUpdateLink = webDriver.findElement(By.linkText("勤怠情報を直接編集する"));
		actions.moveToElement(attendanceUpdateLink).perform();

		Thread.sleep(300);
		getEvidence(new Object() {
		}, "01");
		attendanceUpdateLink.click();

		//URL確認
		visibilityTimeout(By.id("wrap"), 5);
		assertTrue(webDriver.getCurrentUrl().contains("lms/attendance/update"));

		getEvidence(new Object() {
		}, "02");

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 すべての研修日程の勤怠情報を正しく更新し勤怠管理画面に遷移")
	void test05() throws InterruptedException {
		// TODO ここに追加
		// テーブル内の「定時」ボタンを全部取得
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

		List<WebElement> workHourButtons = webDriver.findElements(
				By.xpath("//*[@id='main']/div/div/form/table//button[text()='定時']"));

		System.out.println("定時ボタンの数: " + workHourButtons.size());

		// ボタンを1つずつクリック
		for (WebElement button : workHourButtons) {

			// ボタンがクリック可能になるまで待機
			wait.until(ExpectedConditions.elementToBeClickable(button));

			// スクロールして可視範囲内にする
			((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", button);

			// JSクリック（通常clickで反応しない対策）
			((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", button);

			// Ajax処理が終わるのを軽く待つ
			Thread.sleep(300);
		}
		getEvidence(new Object() {
		}, "01");

		//「更新」ボタンを取得、クリック
		WebElement updateButton = webDriver.findElement(By.cssSelector("input[type='submit']"));
		updateButton.click();

		//アラート処理、OK押下
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		Thread.sleep(500);
		alert.accept();

		//URL確認
		visibilityTimeout(By.id("wrap"), 5);
		assertTrue(webDriver.getCurrentUrl().contains("lms/attendance/update"));

		getEvidence(new Object() {
		}, "02");
	}

}
