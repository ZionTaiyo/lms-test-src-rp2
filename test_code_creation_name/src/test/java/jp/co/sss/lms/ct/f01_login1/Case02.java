package jp.co.sss.lms.ct.f01_login1;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト ログイン機能①
 * ケース02
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {

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
		assertEquals("http://localhost:8080/lms/", webDriver.getCurrentUrl());

	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() {
		// TODO ここに追加

		//異常ユーザー
		//クリア
		webDriver.findElement(By.id("loginId")).clear();
		webDriver.findElement(By.id("password")).clear();

		webDriver.findElement(By.id("loginId")).sendKeys("Unknown");
		webDriver.findElement(By.id("password")).sendKeys("aaaa");

		//ログインボタン押下
		webDriver.findElement(By.id("password")).sendKeys(Keys.ENTER);

		//エラーメッセージ確認
		visibilityTimeout(By.cssSelector(".help-inline.error"), 5);
		String errorMessage = webDriver.findElement(By.cssSelector(".help-inline.error")).getText();

		//チェック
		assertEquals("* ログインに失敗しました。", errorMessage);
		assertTrue(webDriver.getCurrentUrl().contains("lms/login"));
		//エビデンス取得
		WebDriverUtils.getEvidence(new Object() {
		});

	}
}
