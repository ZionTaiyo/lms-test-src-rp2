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
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

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
		//機能プルダウンとリンク一覧「ヘルプ」をクリック
		webDriver.findElement(By.className("dropdown-toggle")).click();
		webDriver.findElement(By.linkText("ヘルプ")).click();

		visibilityTimeout(By.id("wrap"), 5);
		getEvidence(new Object() {
		});
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
		//URL確認
		assertTrue(webDriver.getCurrentUrl().contains("lms/faq"));

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {

		//"よくある質問"が表示されるまで待機
		visibilityTimeout(By.id("wrap"), 5);

		//カテゴリ検索で該当カテゴリをクリック
		webDriver.findElement(By.linkText("【研修関係】")).click();

		//表示結果を待つ
		visibilityTimeout(By.id("question-h[${status.index}]"), 5);

		//URLにカテゴリIDが含まれているか確認(【研修関係】= カテゴリID 1)
		assertTrue(webDriver.getCurrentUrl().contains("frequentlyAskedQuestionCategoryId=1"));

		//検索結果の質問が表示されていることと結果が一つ以上であることを確認
		List<WebElement> results = webDriver.findElements(By.xpath("//dl[starts-with(@id,'question-h')]"));
		assertTrue(results.size() > 0);

		//質問文が正しい形式か確認
		for (WebElement result : results) {
			assertTrue(result.getText().contains("Q."));
		}

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		// TODO ここに追加
		// 質問一覧の先頭の質問をクリック
		WebElement firstQuestion = webDriver.findElement(By.id("question-h[${status.index}]"));
		firstQuestion.click();

		// 回答が表示されるまで待機
		visibilityTimeout(By.id("answer-h[${status.index}]"), 5);

		// 回答が表示されていることを確認
		WebElement answer = webDriver.findElement(By.id("answer-h[${status.index}]"));
		assertTrue(answer.isDisplayed());

		// エビデンス
		getEvidence(new Object() {
		});

		//タブを閉じる
		closeCurrentTabAndReturn(originalTab);
	}
}
