package de.ppi.sf1.experimental.cdexample.test;

import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class HelloWorldTest {

	private WebDriver driver;
	
	private String host = "localhost";
	private String port = "8082";

	@Before
	public void setUp() throws Exception {
		driver = new HtmlUnitDriver();
		
		try {
			Properties props = new Properties();
			props.load( Thread.currentThread().getContextClassLoader().getResourceAsStream("env.properties"));
			
			if (props.get("host.ip") != null) {
				host = String.valueOf(props.get("host.ip"));
			}
			
			if (props.get("host.port") != null) {
				port = String.valueOf(props.get("host.port"));
			}
		} catch (Exception e) {					
		}
	}

	@After
	public void tearDown() {
		driver.close();
	}

	@Test
	public void shouldShowHelloWord() {
		
		driver.get("http://" + host + ":" +  port + "/continuous-delivery-example-webapp/");		

		WebElement element = driver.findElement(By.tagName("h2"));

		Assert.assertEquals("Hello World!", element.getText());

	}

}
