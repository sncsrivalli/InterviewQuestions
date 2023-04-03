package uiTesting;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class UITesting {

	@Test(dataProvider = "urlData")
	public void url_Test(String url, XmlTest xml) throws IOException, InterruptedException {
		WebDriver driver = null;

		switch ("chrome") {
		case "chrome":
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--remote-allow-origins=*");
			options.addArguments("--disable-notifications");
			driver = new ChromeDriver(options);
			break;
		case "firefox":
			driver = new FirefoxDriver();
			break;
		case "edge":
			driver = new EdgeDriver();
			break;
		default:
			System.out.println("Invalid browser data");
			break;
		}

		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();

		Set<String> resolutions = new HashSet<>();
		resolutions.add("1920×1080");
		resolutions.add("1366×768");
		resolutions.add("1536×864");
		int i = 1;
		for (String res : resolutions) {
			String[] str = res.split("×");
			int x = Integer.parseInt(str[0]);
			int y = Integer.parseInt(str[1]);
			Dimension d = new Dimension(x, y);
			driver.manage().window().setSize(d);

			driver.get(url);
			File file = new File( ".//Screenshot//" + cap.getBrowserName() + "//url" + i + "_"
					+ System.getProperty("os.name") + "_" + res + "_" + getCurrentTime() + ".png");
			Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
					.takeScreenshot(driver);
			ImageIO.write(screenshot.getImage(), "PNG", file);
			i++;
			Thread.sleep(2000);
		}

		driver.quit();
	}

	@DataProvider
	public Object[][] urlData() {
		Object[][] obj = new Object[5][1];

		obj[0][0] = "https://www.getcalley.com/blog/";

		obj[1][0] = "https://www.getcalley.com/";

		obj[2][0] = "https://www.getcalley.com/free-trial-calley-teams-plan-v2/";

		obj[3][0] = "https://www.getcalley.com/free-trial-calley-pro-v2/";

		obj[4][0] = "https://www.getcalley.com/why-automatic-call-dialer-software/";

		return obj;
	}

	public String getCurrentTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_hh_mm_sss");
		return sdf.format(date);
	}

}
