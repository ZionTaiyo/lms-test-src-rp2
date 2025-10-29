package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

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

/**
 * 結合テスト 勤怠管理機能
 * ケース10
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース10 受講生 勤怠登録 正常系")
public class Case10 {

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
		//「勤怠」リンクをクリック
		Actions actions = new Actions(webDriver);
		WebElement attendanceLink = webDriver.findElement(By.linkText("勤怠"));
		actions.moveToElement(attendanceLink).perform();

		//待機&クリック
		Thread.sleep(300);
		getEvidence(new Object() {
		}, "01");
		attendanceLink.click();

		//アラートの「OK」をクリック
		Alert alert = webDriver.switchTo().alert();
		alert.accept();

		//URL確認
		visibilityTimeout(By.id("wrap"), 5);
		assertTrue(webDriver.getCurrentUrl().contains("lms/attendance/detail"));

		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「出勤」ボタンを押下し出勤時間を登録")
	void test04() throws InterruptedException {
		// TODO ここに追加
		Actions actions = new Actions(webDriver);
		WebElement punchIn = webDriver.findElement(By.cssSelector("input[name='punchIn']"));
		actions.moveToElement(punchIn).perform();

		//待機＆クリック
		Thread.sleep(300);
		getEvidence(new Object() {
		}, "01");
		punchIn.click();

		Alert alert = webDriver.switchTo().alert();
		alert.accept();

		// tbody要素を取得（スクロール対象）
		WebElement tableBody = webDriver.findElement(By.cssSelector("tbody[style*='overflow-y']"));

		// JavaScriptでスクロールする
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", tableBody);

		getEvidence(new Object() {
		}, "02");

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「退勤」ボタンを押下し退勤時間を登録")
	void test05() throws InterruptedException {
		// TODO ここに追加
		Actions actions = new Actions(webDriver);
		WebElement punchOut = webDriver.findElement(By.cssSelector("input[name='punchOut']"));
		actions.moveToElement(punchOut).perform();

		//待機＆クリック
		Thread.sleep(300);
		getEvidence(new Object() {
		}, "01");
		punchOut.click();

		Alert alert = webDriver.switchTo().alert();
		alert.accept();

		// tbody要素を取得（スクロール対象）
		WebElement tableBody = webDriver.findElement(By.cssSelector("tbody[style*='overflow-y']"));

		// JavaScriptでスクロールする
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", tableBody);

		getEvidence(new Object() {
		}, "02");
	}

}
