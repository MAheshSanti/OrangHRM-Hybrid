package commonFunction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {

	public static WebDriver driver;
	public static Properties p;

	// method for returning browser and all Web driver method

	public static WebDriver startBrowser() throws Throwable
	{
		p= new Properties();
		p.load(new FileInputStream("./PropertyFile\\Enviournment.properties"));
		if(p.getProperty("browser").equalsIgnoreCase("chrome"))
		{
			driver= new ChromeDriver();
		}
		else	if(p.getProperty("browser").equalsIgnoreCase("Firefox"))
		{
			driver= new FirefoxDriver();
		}
		else {
			Reporter.log("Browser does not matching");
		}
		return driver;

	}

	// method for open webApp

	public static void openUrl()
	{
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(p.getProperty("url"));
	}

	// method for wait for element
	public static void waitForElement(String locatortype,String locatorvalue,String testdata)
	{
		WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(testdata)));

		if(locatortype.equalsIgnoreCase("xpath"))
		{
			mywait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(locatorvalue)));
		}
		if(locatortype.equalsIgnoreCase("id"))
		{
			mywait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(locatorvalue)));
		}
		if(locatortype.equalsIgnoreCase("name"))
		{
			mywait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.name(locatorvalue)));
		}
	}

	// method for enter data into textbox that method is typeAction
	public static void typeAction(String locatortype,String locatorvalue,String testdata)
	{
		if(locatortype.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(locatorvalue)).sendKeys(testdata);
		}
		if(locatortype.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(locatorvalue)).sendKeys(testdata);
		}
		if(locatortype.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(locatorvalue)).sendKeys(testdata);
		}
	}

	//method for click on element

	public static void clickAction(String locatortype,String locatorvalue)throws Throwable
	{
		if(locatortype.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(locatorvalue)).click();
			Thread.sleep(1000);
		}
		if(locatortype.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(locatorvalue)).click();
			Thread.sleep(1000);

		}
		if(locatortype.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(locatorvalue)).click();
			Thread.sleep(1000);

		}
	}

	// method for validate  page title

	public static void validateTitle(String exptitle)
	{
		String acttitle = driver.getTitle();
		try {
			Assert.assertEquals(acttitle, exptitle,"title does not matching");
		} catch (AssertionError a) {
			System.out.println(a.getMessage());
		}
	}

	// method fir close the browser

	public static void closeBrowser()
	{
		driver.quit();
	}

	// method for date generation
	public static String generateDate()
	{
		java.util.Date date = new java.util.Date();
		DateFormat df=new SimpleDateFormat("YYYY_MM_dd_HH_mm");
		return df.format(date);
	}

	// method for capture employee id

	public static void captureEmpId(String LocatorType,String LocatorValue) throws Throwable
	{
		String empid="";
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			empid=driver.findElement(By.xpath(LocatorValue)).getAttribute("value");	
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			empid=driver.findElement(By.id(LocatorValue)).getAttribute("value");	
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			empid=driver.findElement(By.name(LocatorValue)).getAttribute("value");	
		}
		//create  a file to store empid in Capture Data Folder
		FileWriter fw = new FileWriter("./CaptureData/EmpID.txt");
		BufferedWriter bw= new BufferedWriter(fw);
		bw.write(empid);
		bw.flush();
		bw.close();
	}

	public static void verifyEmpId() throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/EmpID.txt");
		BufferedReader br= new BufferedReader(fr);
		String expdata=br.readLine();
		String actdata= "";
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@id='empsearch_id']")).sendKeys(expdata);
		driver.findElement(By.id("searchBtn")).sendKeys(Keys.ENTER);
		actdata=driver.findElement(By.xpath("//table[@id='resultTable']/tbody/tr/td[2]")).getText();

		try
		{
			Assert.assertEquals(actdata, expdata,"EMPID not matching");	
		} 
		catch (AssertionError e) 
		{
			System.out.println(e.getMessage());	

		}
	}




}
