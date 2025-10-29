package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト 勤怠管理機能
 * ケース12
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース12 受講生 勤怠直接編集 入力チェック")
public class Case12 {

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
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(3));
			Alert alert = wait.until(ExpectedConditions.alertIsPresent());
			Thread.sleep(300);
			alert.accept();
		} catch (TimeoutException e) {
		}
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
		assertTrue(webDriver.getCurrentUrl().contains("/lms/attendance/update"));

		getEvidence(new Object() {
		}, "02");

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 不適切な内容で修正してエラー表示：出退勤の（時）と（分）のいずれかが空白")
	void test05() throws InterruptedException {
		// TODO ここに追加
		//一行目の出勤時間の「時」をクリック
		WebElement startHour = webDriver.findElement(By.id("startHour0"));
		startHour.click();
		//空欄をクリック
		WebElement clickedElement = webDriver.findElement(By.cssSelector("#startHour0 option[value='']"));
		clickedElement.click();
		getEvidence(new Object() {
		}, "01");

		//「更新」ボタンをクリック
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		WebElement updateButton = webDriver.findElement(By.cssSelector("input[type='submit'"));
		updateButton.click();

		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		Thread.sleep(500);
		alert.accept();

		//エラーメッセージの確認

		WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[contains(text(),'出勤時間が正しく入力されていません。')]")));
		assertTrue(errorMsg.isDisplayed());

		//URL確認
		assertTrue(webDriver.getCurrentUrl().contains("/lms/attendance/update"));
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正してエラー表示：出勤が空白で退勤に入力あり")
	void test06() throws InterruptedException {
		// TODO ここに追加

		//1行目の出勤時間の「分」をクリック
		WebElement startMinute = webDriver.findElement(By.id("startMinute0"));
		startMinute.click();
		//空欄をクリック
		WebElement clickedMinute = webDriver.findElement(By.cssSelector("#startMinute0 option[value='']"));
		clickedMinute.click();
		getEvidence(new Object() {
		}, "01");

		//「更新」ボタンをクリック
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		WebElement updateButton = webDriver.findElement(By.cssSelector("input[type='submit'"));
		updateButton.click();

		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		Thread.sleep(500);
		alert.accept();

		//エラーメッセージの確認
		WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[contains(text(),'出勤情報がないため退勤情報を入力出来ません。')]")));
		assertTrue(errorMsg.isDisplayed());

		//URL確認
		assertTrue(webDriver.getCurrentUrl().contains("/lms/attendance/update"));
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正してエラー表示：出勤が退勤よりも遅い時間")
	void test07() {
		// TODO ここに追加
		// 出退勤を不正な時間に設定（例：出勤 18:00、退勤 09:00）
		WebDriverUtils.setWorkTime(0, "18", "0", "9", "0");
		getEvidence(new Object() {
		}, "01");

		// 更新ボタン押下
		clickElement(By.cssSelector("input[type='submit'"));
		acceptAlertIfPresent();

		// エラーメッセージの確認（仮でOK、あとで本物に差し替えて）
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[contains(text(),'退勤時刻[0]は出勤時刻[0]より後でなければいけません。')]")));
		assertTrue(errorMsg.isDisplayed());

		//URL確認
		assertTrue(webDriver.getCurrentUrl().contains("/lms/attendance/update"));
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正してエラー表示：出退勤時間を超える中抜け時間")
	void test08() {
		// TODO ここに追加
		// 正常な出退勤時間セット
		WebDriverUtils.setWorkTime(0, "11", "0", "18", "0");

		// 中抜け時間を不正に設定
		WebElement blankTime = webDriver.findElement(By.name("attendanceList[0].blankTime"));
		blankTime.click();
		//不正時間を選択
		WebElement clickedBlankTime = webDriver
				.findElement(By.cssSelector("option[value='465']"));
		clickedBlankTime.click();
		getEvidence(new Object() {
		}, "01");

		// 更新ボタン押下
		clickElement(By.cssSelector("input[type='submit'"));
		acceptAlertIfPresent();

		// エラーメッセージチェック（仮メッセージ）
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[contains(text(),'中抜け時間が勤務時間を超えています。')]")));
		assertTrue(errorMsg.isDisplayed());

		//URL確認
		assertTrue(webDriver.getCurrentUrl().contains("/lms/attendance/update"));
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正してエラー表示：備考が100文字超")
	void test09() {
		// TODO ここに追加
		// 備考に101文字ぶち込む
		String longNote = "あ".repeat(101);
		WebElement note = webDriver.findElement(By.name("attendanceList[0].note"));
		note.clear();
		note.sendKeys(longNote);
		getEvidence(new Object() {
		}, "01");

		// 更新ボタン押下
		clickElement(By.cssSelector("input[type='submit'"));
		acceptAlertIfPresent();

		// エラーメッセージ確認
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[contains(text(),'備考の長さが最大値(100)を超えています。')]")));
		assertTrue(errorMsg.isDisplayed());

		//URL確認
		assertTrue(webDriver.getCurrentUrl().contains("/lms/attendance/update"));
		getEvidence(new Object() {
		}, "02");
	}

}
