package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestRun {

	public static void main(String[] args) {

		// Properties
		/*
		 * * Properties 특징 - Map 계열 컬렉션 (Key+value 세트로 담는 특징) - KEY, VALUE 모두
		 * String(문자열)으로 담기 setProperty(String key, String value) getProperty(String
		 * key) : String(value)
		 * 
		 * - 주로 외부 파일(.properties / .xml)로 입출력할때 사용
		 */

		/*
		//파일로 출력하는 것
		Properties prop = new Properties();

		prop.setProperty("C", "INSERT");
		prop.setProperty("R", "SELECT");
		prop.setProperty("U", "UPDATE");
		prop.setProperty("D", "DELETE");

		try {
			
			prop.store(new FileOutputStream("resources/test.properties"), "properties Test");
			prop.storeToXML(new FileOutputStream("resources/test.xml"), "properties Test");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		//파일로 읽어드리기
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("resources/driver.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(prop.getProperty("driver"));
		System.out.println(prop.getProperty("url"));
		System.out.println(prop.getProperty("username"));
		System.out.println(prop.getProperty("password"));
		
	}

}