package driverFactory;

import org.testng.annotations.Test;

public class AppTest {
	@Test
	public void kickStart() throws Throwable
	{

		DriverScript_OrangeHRM ds = new DriverScript_OrangeHRM();
		ds.starttest();
	}
}
