package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private LoginPage loginPage;
	private SignupPage signupPage;
	private HomePage homePage;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.firefoxdriver().setup();

	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new FirefoxDriver();
		this.homePage = new HomePage(driver);
		this.loginPage = new LoginPage(driver);
		this.signupPage = new SignupPage(driver);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/*
		tests if the homepage is not accessible for unauthorized users and posts right error message
		and if login and signup page is accessible
	 */
	@Test
	public void ifNotAccessible() {
		driver.get("http://localhost:" + this.port + "/login");
		loginPage = new LoginPage(driver);
		loginPage.setUsername("wrongusername");
		loginPage.setPassword("wrongpassword");
		loginPage.clickLoginButton();
		Assertions.assertNotEquals("Home", driver.getTitle());
		Assertions.assertEquals("Invalid username or password",loginPage.getErrorMsg());
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
		Assertions.assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());

	}

	/*
		signs up a new user, then logs in, checks if the user is landing on the homepage
		and logs out again and checks if homepage is no more accessible
	 */
	@Test
	public void signupLoginAndLogout() {
		signupJohnDoe();
		loginJohnDoe();
		Assertions.assertEquals("Home", driver.getTitle());
		homePage.clickLogoutButton();
		Assertions.assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());

	}

	/*
		signs up a new user, then logs in, creates a note and verifies it is displayed
	 */
	@Test
	public void createANoteAndDisplay(){
		signupJohnDoe();
		loginJohnDoe();
		homePage.clickNotesTab();
		homePage.clickCreateNote();
		homePage.setNoteTitle("test title");
		homePage.setNoteDescription("test description");
		homePage.clickNoteSubmit();
		driver.get("http://localhost:" + this.port + "/home");
		homePage.clickNotesTab();
		List<WebElement> resultTitle = driver.findElements(By.ById.id("noteTitle"));
		List<WebElement> resultDescription = driver.findElements(By.ById.id("noteDescription"));
		Assertions.assertTrue(resultTitle.stream()
				.map(WebElement::getText)
				.anyMatch(text -> "test title".equals(text)));
		Assertions.assertTrue(resultDescription.stream()
				.map(WebElement::getText)
				.anyMatch(text -> "test description".equals(text)));
	}

	/*
		signs up a new user, then logs in, creates a note and verifies it is displayed
		then edits the note and verifies that the changes are displayed correctly
	 */
	@Test
	public void editNoteAndVerify() {
		createANoteAndDisplay();
		homePage.clickEditNote();
		homePage.clearNoteTitle();
		homePage.clearNoteDescription();
		homePage.setNoteTitle("edited title");
		homePage.setNoteDescription("edited description");
		homePage.clickNoteSubmit();
		driver.get("http://localhost:" + this.port + "/home");
		homePage.clickNotesTab();
		List<WebElement> resultTitle = driver.findElements(By.ById.id("noteTitle"));
		List<WebElement> resultDescription = driver.findElements(By.ById.id("noteDescription"));
		Assertions.assertTrue(resultTitle.stream()
				.map(WebElement::getText)
				.anyMatch(text -> "edited title".equals(text)));
		Assertions.assertTrue(resultDescription.stream()
				.map(WebElement::getText)
				.anyMatch(text -> "edited description".equals(text)));
	}

	/*
		signs up a new user, then logs in, creates a note,
		then deletes the note and verifies it is no longer displayed
	 */
	@Test
	public void deleteNoteAndVerify() {
		signupJohnDoe();
		loginJohnDoe();
		homePage.clickNotesTab();
		homePage.clickCreateNote();
		homePage.setNoteTitle("deletion title");
		homePage.setNoteDescription("deletion description");
		homePage.clickNoteSubmit();
		driver.get("http://localhost:" + this.port + "/home");
		homePage.clickNotesTab();
		driver.findElement(By.name("deletion title")).click();
		driver.get("http://localhost:" + this.port + "/home");
		homePage.clickNotesTab();
		Assertions.assertTrue(driver.findElements(By.name("deletion title")).isEmpty());

	}

	/*
		signs up a new user, then logs in, creates a credential and verifies it is displayed
		and the password is encrypted
	 */
	@Test
	public void createACredentialAndDisplay(){
		signupJohnDoe();
		loginJohnDoe();
		homePage.clickCredentialsTab();
		homePage.clickCreateCredential();
		homePage.setCredentialUrl("http://test.com");
		homePage.setCredentialUsername("testuser");
		homePage.setCredentialPassword("password");
		homePage.clickCredentialSubmit();
		driver.get("http://localhost:" + this.port + "/home");
		homePage.clickCredentialsTab();
		List<WebElement> resultUrl = driver.findElements(By.ById.id("credentialUrl"));
		List<WebElement> resultUsername = driver.findElements(By.ById.id("credentialUsername"));
		List<WebElement> resultPassword = driver.findElements(By.ById.id("credentialPassword"));
		Assertions.assertTrue(resultUrl.stream()
				.map(WebElement::getText)
				.anyMatch(text -> "http://test.com".equals(text)));
		Assertions.assertTrue(resultUsername.stream()
				.map(WebElement::getText)
				.anyMatch(text -> "testuser".equals(text)));
		Assertions.assertFalse(resultPassword.stream()
				.map(WebElement::getText)
				.anyMatch(text -> "password".equals(text)));

	}

	/*
		signs up a new user, then logs in, creates a credential and verifies it is displayed
		then opens the edit modal, checks if the password is correctly decrypted. Then edits the credential
		and checks if it is displayed correctly
	 */

	@Test
	public void editCredentialAndVerify() {
		createACredentialAndDisplay();
		homePage.clickCredentialsTab();
		homePage.clickEditCredential();
		Assertions.assertEquals("password", driver.findElement(By.ById.id("credential-password")).getAttribute("value"));
		homePage.clearCredentialUrl();
		homePage.clearCredentialUsername();
		homePage.clearCredentialPassword();
		homePage.setCredentialUrl("http://editedtest.com");
		homePage.setCredentialUsername("editeduser");
		homePage.setCredentialPassword("editedpassword");
		homePage.clickCredentialSubmit();
		driver.get("http://localhost:" + this.port + "/home");
		homePage.clickCredentialsTab();
		List<WebElement> resultUrl = driver.findElements(By.ById.id("credentialUrl"));
		List<WebElement> resultUsername = driver.findElements(By.ById.id("credentialUsername"));
		Assertions.assertTrue(resultUrl.stream()
				.map(WebElement::getText)
				.anyMatch(text -> "http://editedtest.com".equals(text)));
		Assertions.assertTrue(resultUsername.stream()
				.map(WebElement::getText)
				.anyMatch(text -> "editeduser".equals(text)));

	}

	/*
		signs up a new user, then logs in, creates a credential,
		then deletes the credential and verifies it is no longer displayed
	 */
	@Test
	public void deleteCredentialAndVerify(){
		signupJohnDoe();
		loginJohnDoe();
		homePage.clickCredentialsTab();
		homePage.clickCreateCredential();
		homePage.setCredentialUrl("http://delete.com");
		homePage.setCredentialUsername("deleteuser");
		homePage.setCredentialPassword("deletepassword");
		homePage.clickCredentialSubmit();
		driver.get("http://localhost:" + this.port + "/home");
		homePage.clickCredentialsTab();
		//homePage.clickDeleteCredential();
		driver.findElement(By.name("http://delete.com")).click();
		driver.get("http://localhost:" + this.port + "/home");
		homePage.clickCredentialsTab();
		Assertions.assertTrue(driver.findElements(By.name("http://delete.com")).isEmpty());

	}

	public void signupJohnDoe() {
		driver.get("http://localhost:" + this.port + "/signup");
		signupPage.setFirstName("John");
		signupPage.setLastName("Doe");
		signupPage.setUsername("johndoe");
		signupPage.setPassword("password");
		signupPage.clickSubmitButton();
		if(driver.getTitle() == "Sign Up") {
			signupPage.clickBackToLogin();
		}

	}

	public void loginJohnDoe() {
		driver.get("http://localhost:" + this.port + "/login");
		loginPage.setUsername("johndoe");
		loginPage.setPassword("password");
		loginPage.clickLoginButton();
	}

}
