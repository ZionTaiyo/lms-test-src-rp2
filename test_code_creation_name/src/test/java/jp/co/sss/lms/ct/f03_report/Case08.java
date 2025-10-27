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
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// TODO ここに追加

		// 「提出済み」の行を取得
		List<WebElement> submittedRows = webDriver.findElements(
				By.xpath("//tr[td/span[text()='提出済み']]"));

		// 2行目が存在するかチェック
		assertTrue(submittedRows.size() > 1, "提出済みの日報が2件以上ありません");

		// 2行目の「詳細」ボタンをクリック
		WebElement detailButton = submittedRows.get(1).findElement(By.xpath(".//input[@value='詳細']"));
		detailButton.click();

		//URL確認
		visibilityTimeout(By.id("wrap"), 5);
		assertTrue(webDriver.getCurrentUrl().contains("/lms/section/detail"));
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// TODO ここに追加
		//「提出済み週報」ボタンをクリック
		WebElement weeklyReport = webDriver.findElement(By.cssSelector("input[value='提出済み週報【デモ】を確認する'"));
		weeklyReport.click();

		//"週報【デモ】"が表示されるまで待機
		visibilityTimeout(By.id("wrap"), 5);
		assertTrue(webDriver.getCurrentUrl().contains("/lms/report/regist"));
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() throws InterruptedException {
		// TODO ここに追加
		//所感の入力フィールドを取得
		WebElement writeImpression = webDriver.findElement(By.id("content_1"));
		//所感をクリアし情報を入力
		writeImpression.clear();
		writeImpression.sendKeys("最近うれしいことに沖縄も寒くなってきている。");

		//「提出」ボタンをクリック
		Actions actions = new Actions(webDriver);
		WebElement submitBtn = webDriver.findElement(By.cssSelector("button[type='submit']"));
		actions.moveToElement(submitBtn).perform();

		//待機
		Thread.sleep(500);
		getEvidence(new Object() {
		});
		submitBtn.click();

		//"アルゴリズム、フローチャート"が表示されるまで待機
		visibilityTimeout(By.id("wrap"), 5);
		//URL確認
		assertTrue(webDriver.getCurrentUrl().contains("/lms/section/detail"));
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() throws InterruptedException {
		// TODO ここに追加
		//「ようこそ○○さん」リンクをクリック
		Actions actions = new Actions(webDriver);
		WebElement userLink = webDriver.findElement(By.linkText("ようこそ受講生ＡＡ１さん"));
		actions.moveToElement(userLink).perform();

		//待機
		Thread.sleep(500);
		getEvidence(new Object() {
		}, "01");
		userLink.click();

		visibilityTimeout(By.id("wrap"), 5);
		assertTrue(webDriver.getCurrentUrl().contains("/lms/user/detail"));
		getEvidence(new Object() {
		}, "02");

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() throws InterruptedException {
		// TODO ここに追加
		List<WebElement> rows = webDriver.findElements(By.xpath("//table//tr"));

		for (WebElement row : rows) {
			//"週報【デモ】"を含む行を探す
			if (row.getText().contains("週報【デモ】")) {

				//詳細ボタン取得
				WebElement detailButton = row.findElement(By.xpath(".//input[@value='詳細']"));

				//クリック可能になるまで待機
				WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
				wait.until(ExpectedConditions.elementToBeClickable(detailButton));

				//その行の「詳細」ボタンをクリック
				((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", detailButton);
				Thread.sleep(500);
				getEvidence(new Object() {
				}, "01");

				((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", detailButton);
				break;
			}
		}
		visibilityTimeout(By.id("wrap"), 5);
		assertTrue(webDriver.getCurrentUrl().contains("/lms/report/detail"));
		getEvidence(new Object() {
		}, "02");
	}

}
