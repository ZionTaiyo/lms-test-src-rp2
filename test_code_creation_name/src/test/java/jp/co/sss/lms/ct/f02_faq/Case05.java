package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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

/**
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

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

		goTo("http://localhost:8080/lms/");
		assertEquals("http://localhost:8080/lms/", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {

		WebElement loginId = webDriver.findElement(By.id("loginId"));
		WebElement password = webDriver.findElement(By.id("password"));

		loginId.clear();
		password.clear();

		String LMS_LOGIN = System.getenv("LMS_LOGIN");
		String LMS_PASSWORD = System.getenv("LMS_PASSWORD");

		loginId.sendKeys(LMS_LOGIN);
		password.sendKeys(LMS_PASSWORD, Keys.ENTER);

		//"DEMOコース"表示まで待機
		visibilityTimeout(By.id("wrap"), 5);
		getEvidence(new Object() {
		});
		//URL確認
		assertEquals("http://localhost:8080/lms/course/detail", webDriver.getCurrentUrl());
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		// TODO ここに追加
		//機能プルダウンとリンク一覧「ヘルプ」をクリック
		webDriver.findElement(By.className("dropdown-toggle")).click();
		getEvidence(new Object() {
		}, "01");
		webDriver.findElement(By.linkText("ヘルプ")).click();

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
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {

		//"よくある質問"が表示されるまで待機
		visibilityTimeout(By.id("wrap"), 5);

		//検索欄にキーワードを入力
		WebElement keywordInput = webDriver.findElement(By.id("form"));
		keywordInput.sendKeys("キャンセル", Keys.ENTER);

		//表示結果を待つ
		visibilityTimeout(By.id("question-h[${status.index}]"), 5);

		List<WebElement> rows = webDriver.findElements(By.id("question-h[${status.index}]"));
		assertTrue(rows.size() > 0);

		for (WebElement row : rows) {
			assertTrue(row.getText().contains("キャンセル"));
		}
		// エビデンス
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {

		// キーワード入力
		WebElement keywordInput = webDriver.findElement(By.id("form"));
		keywordInput.clear();
		keywordInput.sendKeys("キャンセル");

		// クリアボタンクリック
		webDriver.findElement(By.cssSelector("input[type='button'][value='クリア']")).click();

		// 入力欄が空になっていることを確認
		assertEquals("", keywordInput.getAttribute("value"));

		// エビデンス
		getEvidence(new Object() {
		});

		//タブを閉じる
		closeCurrentTabAndReturn(originalTab);

	}

}
