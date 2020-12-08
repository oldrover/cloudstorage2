package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

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
		homePage = new HomePage(driver);
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
		homePage = new HomePage(driver);
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



	}



	public void signupJohnDoe() {
		signupPage = new SignupPage(driver);
		driver.get("http://localhost:" + this.port + "/signup");
		signupPage.setFirstName("John");
		signupPage.setLastName("Doe");
		signupPage.setUsername("johndoe");
		signupPage.setPassword("password");
		signupPage.clickSubmitButton();
		signupPage.clickLoginLink();

	}

	public void loginJohnDoe() {
		loginPage = new LoginPage(driver);
		driver.get("http://localhost:" + this.port + "/login");
		loginPage.setUsername("johndoe");
		loginPage.setPassword("password");
		loginPage.clickLoginButton();

	}

}
