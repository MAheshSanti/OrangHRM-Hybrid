package driverFactory;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunction.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript_OrangeHRM {

	WebDriver driver;
	String inputpath="./FileInput\\ControlerOrangehrm.xlsx";
	String outputpath="./FileOutput/orangehrmHybridresult.xlsx";
	String tcsheet="MasterTestCase";
	ExtentReports report;
	ExtentTest logger;




	public  void starttest() throws Throwable
	{
		String module_status="";
		String module_new="";
		// call excel file util class
		ExcelFileUtil xl = new ExcelFileUtil(inputpath);
		// iterate all the module present in master test case
		for(int i=1;i<=xl.rowcount(tcsheet);i++)
		{
			if(xl.getCellData(tcsheet, i, 2).equalsIgnoreCase("y"))
			{
				String tcmodule=xl.getCellData(tcsheet, i, 1);
				report = new ExtentReports("./target/Reports/"+tcmodule+FunctionLibrary.generateDate()+".html");

				logger = report.startTest(tcmodule);

				// call all the module one by one
				for(int j=1;j<=xl.rowcount(tcmodule);j++)
				{

					String description=xl.getCellData(tcmodule, j, 0);
					String objecttype=xl.getCellData(tcmodule, j, 1);
					String ltype=xl.getCellData(tcmodule, j, 2);
					String lvalue=xl.getCellData(tcmodule, j, 3);
					String testdata=xl.getCellData(tcmodule, j, 4);

					// call all the method from Function Library

					try 
					{
						if(objecttype.equalsIgnoreCase("startBrowser"))
						{
							driver=	FunctionLibrary.startBrowser();
							logger.log(LogStatus.INFO, description);
						}
						if(objecttype.equalsIgnoreCase("openUrl"))
						{
							FunctionLibrary.openUrl();
							logger.log(LogStatus.INFO, description);

						}
						if(objecttype.equalsIgnoreCase("waitForElement"))
						{
							FunctionLibrary.waitForElement(ltype, lvalue, testdata);
							logger.log(LogStatus.INFO, description);

						}
						if(objecttype.equalsIgnoreCase("typeAction"))
						{
							FunctionLibrary.typeAction(ltype, lvalue, testdata);
							logger.log(LogStatus.INFO, description);

						}
						if(objecttype.equalsIgnoreCase("clickAction"))
						{
							FunctionLibrary.clickAction(ltype, lvalue);
							logger.log(LogStatus.INFO, description);

						}
						if(objecttype.equalsIgnoreCase("validateTitle"))
						{
							FunctionLibrary.validateTitle(testdata);
							logger.log(LogStatus.INFO, description);

						}
						if(objecttype.equalsIgnoreCase("closeBrowser"))
						{
							FunctionLibrary.closeBrowser();
							logger.log(LogStatus.INFO, description);

						}
						if(objecttype.equalsIgnoreCase("captureEmpId"))
						{
							FunctionLibrary.captureEmpId(ltype, lvalue);
							logger.log(LogStatus.INFO, description);

						}
						if(objecttype.equalsIgnoreCase("verifyEmpId"))
						{
							FunctionLibrary.verifyEmpId();
							logger.log(LogStatus.INFO, description);

						}

						module_status="true";
						logger.log(LogStatus.PASS, description);
						xl.setCellData(tcmodule, j, 5, "Pass", outputpath);

					} 
					catch (Exception e)
					{
						System.out.println(e.getMessage());
						xl.setCellData(tcmodule, j, 5, "Fail", outputpath);
						logger.log(LogStatus.FAIL, description);

						module_new="false";
					}

					//write pass into mastertestcase status section
					if(module_status.equalsIgnoreCase("true"))
					{
						xl.setCellData(tcsheet, i, 3, "pass", outputpath);
					}
					report.endTest(logger);
					report.flush();


				}
				if(module_new.equalsIgnoreCase("false"))
				{
					xl.setCellData(tcsheet, i, 3, "fail", outputpath);
				}
			}
			
			else
			{
				xl.setCellData(tcsheet, i, 3, "Blocked", outputpath);
			}


		}

	}



}
