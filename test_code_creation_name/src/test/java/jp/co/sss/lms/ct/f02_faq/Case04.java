package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * 結合テスト よくある質問機能
 * ケース04
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース04 よくある質問画面への遷移")
public class Case04 {
	private static String originalTab;

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
		visibilityTimeout(By.id("loginId"), 5);
		getEvidence(new Object() {
		});
		assertEquals("http://localhost:8080/lms/", webDriver.getCurrentUrl());
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		// TODO ここに追加
		//ID/PWを取得
		WebElement loginId = webDriver.findElement(By.id("loginId"));
		WebElement password = webDriver.findElement(By.id("password"));

		//クリア
		loginId.clear();
		password.clear();

		//環境変数からログイン情報を取得
		String LMS_LOGIN = System.getenv("LMS_LOGIN");
		String LMS_PASSWORD = System.getenv("LMS_PASSWORD");

		loginId.sendKeys(LMS_LOGIN);
		password.sendKeys(LMS_PASSWORD, Keys.ENTER);

		//"DEMOコース"表示まで待機
		visibilityTimeout(By.id("wrap"), 5);
		getEvidence(new Object() {
		});
		//URL確認（course/detail）
		assertEquals("http://localhost:8080/lms/course/detail", webDriver.getCurrentUrl());

	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() throws InterruptedException {
		// TODO ここに追加

		//画面上部にある「機能」プルダウンをクリック
		webDriver.findElement(By.className("dropdown-toggle")).click();

		Actions actions = new Actions(webDriver);
		WebElement helpLink = webDriver.findElement(By.linkText("ヘルプ"));
		actions.moveToElement(helpLink).perform();

		//待機
		Thread.sleep(500);
		getEvidence(new Object() {
		}, "01");

		//「ヘルプ」をクリック
		helpLink.click();

		//ヘルプ画面の"ヘルプ"が表示されるまで待機
		visibilityTimeout(By.id("wrap"), 5);

		getEvidence(new Object() {
		}, "02");

		//URL確認
		assertEquals("http://localhost:8080/lms/help", webDriver.getCurrentUrl());

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {

		//スクロール
		scrollTo("300");

		//元のタブを保持
		originalTab = webDriver.getWindowHandle();

		webDriver.findElement(By.linkText("よくある質問")).click();
		//別タブに切替
		switchToNewTab();

		getEvidence(new Object() {
		});
		assertTrue(webDriver.getCurrentUrl().contains("lms/faq"));

		//タブを閉じる
		closeCurrentTabAndReturn(originalTab);
	}

}
