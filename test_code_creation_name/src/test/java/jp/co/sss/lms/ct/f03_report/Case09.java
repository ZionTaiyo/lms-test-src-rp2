package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

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
		goTo("http:/localhost:8080/lms/");
		assertEquals("http://localhost:8080/lms/", webDriver.getCurrentUrl());
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
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() throws InterruptedException {
		// TODO ここに追加
		//「ようこそ○○さん」リンクをクリック
		Actions actions = new Actions(webDriver);
		WebElement userLink = webDriver.findElement(By.linkText("ようこそ受講生ＡＡ１さん"));
		actions.moveToElement(userLink).perform();

		//待機
		Thread.sleep(300);
		getEvidence(new Object() {
		}, "01");

		//クリック
		userLink.click();

		//URL確認
		visibilityTimeout(By.id("wrap"), 5);
		assertTrue(webDriver.getCurrentUrl().contains("lms/user/detail"));

		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() throws InterruptedException {
		// TODO ここに追加
		//レポート一覧を取得
		List<WebElement> rows = webDriver.findElements(By.xpath("//table//tr"));

		for (WebElement row : rows) {
			//"週報【デモ】"を含む行を探す
			if (row.getText().contains("週報【デモ】")) {

				//修正ボタン取得
				WebElement adjustButton = row.findElement(By.xpath(".//input[@value='修正する']"));
				//クリック可能になるまで待機
				WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
				wait.until(ExpectedConditions.elementToBeClickable(adjustButton));

				//対象の「修正する」ボタンまでスクロール
				((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", adjustButton);

				//待機
				Thread.sleep(300);
				getEvidence(new Object() {
				}, "01");

				//クリック
				((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", adjustButton);
				break;
			}
		}
		visibilityTimeout(By.id("wrap"), 5);
		assertTrue(webDriver.getCurrentUrl().contains("/lms/report/regist"));
		getEvidence(new Object() {
		}, "02");

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {
		// TODO ここに追加
		//報告内容フィールドを取得してクリア
		WebElement studyContent = webDriver.findElement(By.id("intFieldName_0"));
		studyContent.clear();

		getEvidence(new Object() {
		}, "01");

		//提出ボタンをクリック
		WebElement submitButton = webDriver.findElement(By.cssSelector("button[type='submit'"));
		submitButton.click();

		//エラーメッセージ確認
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[contains(text(),'理解度を入力した場合は、学習項目は必須です')]")));
		assertTrue(errorMsg.isDisplayed());

		//URL確認
		assertTrue(webDriver.getCurrentUrl().contains("/lms/report/complete"));
		getEvidence(new Object() {
		}, "02");

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() throws InterruptedException {
		// TODO ここに追加
		//報告内容フィールドを取得して再入力
		WebElement studyContent = webDriver.findElement(By.id("intFieldName_0"));
		studyContent.sendKeys("ITリテラシー①");
		getEvidence(new Object() {
		}, "01");

		//「理解度」プルダウンをクリック
		WebElement understanding = webDriver.findElement(By.id("intFieldValue_0"));
		understanding.click();
		//空欄の選択肢をクリック
		WebElement clickedElement = webDriver.findElement(By.cssSelector("#intFieldValue_0 option[value='']"));
		clickedElement.click();
		getEvidence(new Object() {
		}, "02");

		//提出ボタンをクリック
		scrollTo("300");
		WebElement submitButton = webDriver.findElement(By.cssSelector("button[type='submit'"));

		//クリック可能になるまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.elementToBeClickable(submitButton));

		//クリック
		submitButton.click();

		//エラーメッセージ確認
		WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[contains(text(),'学習項目を入力した場合は、理解度は必須です。')]")));
		assertTrue(errorMsg.isDisplayed());
		//URL確認
		assertTrue(webDriver.getCurrentUrl().contains("/lms/report/complete"));
		getEvidence(new Object() {
		}, "03");

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {
		// TODO ここに追加
		//「理解度」プルダウンをクリック
		WebElement understanding = webDriver.findElement(By.id("intFieldValue_0"));
		understanding.click();
		//空欄の選択肢をクリック
		WebElement clickedElement = webDriver.findElement(By.cssSelector("#intFieldValue_0 option[value='1']"));
		clickedElement.click();
		//目標達成度をクリアし、数値以外を入力
		WebElement targetAchivement = webDriver.findElement(By.id("content_0"));
		targetAchivement.clear();
		getEvidence(new Object() {
		}, "01");

		targetAchivement.sendKeys("AAA");
		getEvidence(new Object() {
		}, "02");

		//提出ボタンをクリック
		scrollTo("300");
		WebElement submitButton = webDriver.findElement(By.cssSelector("button[type='submit'"));

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.elementToBeClickable(submitButton));
		submitButton.click();

		//エラーメッセージ確認
		WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[contains(text(),'目標の達成度は半角数字で入力してください。')]")));
		assertTrue(errorMsg.isDisplayed());
		//URL確認
		assertTrue(webDriver.getCurrentUrl().contains("/lms/report/complete"));
		getEvidence(new Object() {
		}, "03");

	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {
		// TODO ここに追加
		//目標の達成度フィールド取得
		WebElement targetAchivement = webDriver.findElement(By.id("content_0"));
		targetAchivement.clear();
		getEvidence(new Object() {
		}, "01");

		targetAchivement.sendKeys("11");
		getEvidence(new Object() {
		}, "02");

		//提出ボタンをクリック
		scrollTo("300");
		WebElement submitButton = webDriver.findElement(By.cssSelector("button[type='submit'"));

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.elementToBeClickable(submitButton));
		submitButton.click();

		//エラーメッセージ確認
		WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[contains(text(),'目標の達成度は、半角数字で、1～10の範囲内で入力してください。')]")));
		assertTrue(errorMsg.isDisplayed());
		//URL確認
		assertTrue(webDriver.getCurrentUrl().contains("/lms/report/complete"));
		getEvidence(new Object() {
		}, "03");
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {
		// TODO ここに追加
		// 目標の達成度と所感をクリア
		clearText(By.id("content_0"));
		clearText(By.id("content_1"));
		getEvidence(new Object() {
		}, "01");

		//提出ボタンをクリック
		clickElement(By.cssSelector("button[type='submit']"));

		//入力チェック＆URL確認
		assertVisible(By.xpath("//*[contains(text(),'目標の達成度は半角数字で入力してください。')]"));
		assertVisible(By.xpath("//*[contains(text(),'所感は必須です。')]"));
		assertTrue(webDriver.getCurrentUrl().contains("/lms/report/complete"));

		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {
		// TODO ここに追加
		//目標の達成度と所感フィールドを取得 
		WebElement targetAchivement = webDriver.findElement(By.id("content_0"));
		targetAchivement.sendKeys("5");

		scrollTo("300");
		WebElement impressions = webDriver.findElement(By.id("content_1"));
		WebElement reflection = webDriver.findElement(By.id("content_2"));
		clearText(By.id("content_2"));

		//環境変数から入力値を取得
		String YAPPING = System.getenv("YAPPING");

		//同じ文字列を5回繰り返して入力
		String longText = YAPPING.repeat(10);
		impressions.sendKeys(longText);
		reflection.sendKeys(longText);

		//提出ボタンをクリック
		clickElement(By.cssSelector("button[type='submit']"));
		scrollTo("300");

		//入力チェック＆URL確認
		assertVisible(By.xpath("//*[contains(text(),' 所感の長さが最大値(2000)を超えています。')]"));
		assertVisible(By.xpath("//*[contains(text(),'週間の振り返りの長さが最大値(2000)を超えています。')]"));
		assertTrue(webDriver.getCurrentUrl().contains("/lms/report/complete"));
		getEvidence(new Object() {
		});
	}

}
