package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
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

	@Autowired
	private CredentialService credentialService;

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
		Assertions.assertEquals("test title", driver.findElement(By.ById.id("noteTitle")).getText());
		Assertions.assertEquals("test description", driver.findElement(By.ById.id("noteDescription")).getText());

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
		Assertions.assertEquals("edited title", driver.findElement(By.ById.id("noteTitle")).getText());
		Assertions.assertEquals("edited description", driver.findElement(By.ById.id("noteDescription")).getText());
	}

	/*
		signs up a new user, then logs in, creates a note and verifies it is displayed
		then deletes the note and verifies it is no longer displayed
	 */
	@Test
	public void deleteNoteAndVerify() {
		createANoteAndDisplay();
		homePage.clickDeleteNote();
		driver.get("http://localhost:" + this.port + "/home");
		homePage.clickNotesTab();
		Assertions.assertTrue(driver.findElements(By.ById.id("noteTitle")).isEmpty());

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
		Assertions.assertEquals("http://test.com", driver.findElement(By.ById.id("credentialUrl")).getText());
		Assertions.assertEquals("testuser", driver.findElement(By.ById.id("credentialUsername")).getText());

		//password encryption check
		Assertions.assertNotEquals("password", driver.findElement(By.ById.id("credentialPassword")).getText());

	}

	/*
		signs up a new user, then logs in, creates a credential and verifies it is displayed
		then opens the edit modal, checks if the password is decrypted. Then it edits the credential
		and checks if it is displayed correctly
	 */

	@Test
	public void editCredentialAndVerify() throws InterruptedException {
		createACredentialAndDisplay();
		homePage.clickCredentialsTab();
		homePage.clickEditCredential();
		Thread.sleep(5000);
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
		Assertions.assertEquals("http://editedtest.com", driver.findElement(By.ById.id("credentialUrl")).getText());
		Assertions.assertEquals("editeduser", driver.findElement(By.ById.id("credentialUsername")).getText());
		Assertions.assertNotEquals("editedpassword", driver.findElement(By.ById.id("credentialPassword")).getText());
	}

	/*
		signs up a new user, then logs in, creates a credential and verifies it is displayed
		then deletes the credential and verifies it is no longer displayed
	 */
	@Test
	public void deleteCredentialAndVerify(){
		createACredentialAndDisplay();
		homePage.clickDeleteCredential();
		driver.get("http://localhost:" + this.port + "/home");
		homePage.clickCredentialsTab();
		Assertions.assertTrue(driver.findElements(By.ById.id("credentialUrl")).isEmpty());

	}

	public void signupJohnDoe() {
		driver.get("http://localhost:" + this.port + "/signup");
		signupPage.setFirstName("John");
		signupPage.setLastName("Doe");
		signupPage.setUsername("johndoe");
		signupPage.setPassword("password");
		signupPage.clickSubmitButton();
		signupPage.clickLoginLink();
	}

	public void loginJohnDoe() {
		driver.get("http://localhost:" + this.port + "/login");
		loginPage.setUsername("johndoe");
		loginPage.setPassword("password");
		loginPage.clickLoginButton();
	}

}
