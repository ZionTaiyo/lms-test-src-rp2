package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

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
 * 結合テスト レポート機能
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

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
		WebElement loginId = webDriver.findElement(By.id("loginId"));
		WebElement password = webDriver.findElement(By.id("password"));

		loginId.clear();
		password.clear();

		String LMS_LOGIN = System.getenv("LMS_LOGIN");
		String LMS_PASSWORD = System.getenv("LMS_PASSWORD");

		loginId.sendKeys(LMS_LOGIN);
		password.sendKeys(LMS_PASSWORD, Keys.ENTER);

		visibilityTimeout(By.id("wrap"), 5);
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// TODO ここに追加
		//研修一覧テーブルの行を取得
		List<WebElement> rows = webDriver.findElements(By.xpath("//table//tr"));

		for (WebElement row : rows) {
			//"未提出"を含む行を探す
			if (row.getText().contains("未提出")) {

				//その行の「詳細」ボタンをクリック
				WebElement detailButton = row.findElement(By.xpath(".//input[@value='詳細']"));
				detailButton.click();
				break;
			}

		}

		//URL確認
		visibilityTimeout(By.id("wrap"), 5);
		assertTrue(webDriver.getCurrentUrl().contains("/lms/section/detail"));
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// TODO ここに追加
		//「提出」ボタンをクリック
		WebElement report = webDriver.findElement(By.cssSelector("input[value='日報【デモ】を提出する']"));
		report.click();

		//"日報【デモ】が表示されるまで待機
		visibilityTimeout(By.id("wrap"), 5);
		assertTrue(webDriver.getCurrentUrl().contains("/lms/report/regist"));
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {
		// TODO ここに追加
		//報告内容の入力フィールドを取得
		WebElement writeReport = webDriver.findElement(By.id("content_0"));
		//報告内容を入力
		writeReport.sendKeys("外国人が多い映画館は盛り上がりやすいから楽しいぞぉ～");

		// 「提出」ボタンをクリック
		WebElement submitBtn = webDriver.findElement(By.cssSelector("button[type='submit']"));
		submitBtn.click();

		//"Java概要"が表示されるまで待機
		visibilityTimeout(By.id("wrap"), 5);

		//URL確認
		assertTrue(webDriver.getCurrentUrl().contains("/lms/section/detail"));

		//ボタン名の更新確認
		assertTrue(webDriver.findElement(By.cssSelector("input[value='提出済み日報【デモ】を確認する']")).isDisplayed());

		getEvidence(new Object() {
		});

	}

}
