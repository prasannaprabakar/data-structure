package amazon;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.*;

public class verifyLink
{
    WebDriver driver;
    Set uniqueLinkTexts = new HashSet();
    LinkedList<String>duplicateLinkText= new LinkedList<String>();
    @BeforeTest
    public void invoke(){
        WebDriverManager.chromedriver().setup();
        driver=new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("http://www.amazon.in/");
    }
    @Test(priority = 0)
   public void checkHomePageLink() throws IOException
    {
        List<WebElement> links=driver.findElements(By.tagName("a"));
        //System.out.println(links.size());
        List<String> linkTexts = new ArrayList<String>();
        for(WebElement link : links) {

            linkTexts.add(link.getAttribute("href"));
        }
        boolean isUnique;
        for(String link:linkTexts) {
       if(link!=null)
       {
           isUnique = uniqueLinkTexts.add(link);

           if (!isUnique)
           {
               duplicateLinkText.add(link);
               System.out.println("Duplicate found. Link: " + link);
           }
       }

    }

    }
    @Test(priority = 1)
    public void click(){
        for (Object text:uniqueLinkTexts){
            driver.navigate().to((String) text);
            List<WebElement> links=driver.findElements(By.tagName("a"));
            List<String> linkTexts = new ArrayList<String>();
            for(WebElement link : links) {

                linkTexts.add(link.getAttribute("href"));
            }
            boolean isUnique;
            for(String link:linkTexts)
            {
                if (link != null)
                {
                    isUnique = uniqueLinkTexts.add(link);

                    if (!isUnique)
                    {
                        duplicateLinkText.add(link);
                        //System.out.println("Duplicate found. Link: " + link);
                    }
                }
            }
            driver.navigate().back();
           //driver.findElement(By.xpath("//a[@href,''][text()='Re-Call']")).click();
        }
    }

    @AfterTest
    public void close() throws IOException
    {

        File file=new File("D:\\Selenium automation\\links.xlsx");
        FileOutputStream write=new FileOutputStream(file);
        XSSFWorkbook links=new XSSFWorkbook();
        XSSFSheet uniqueLink=links.createSheet("uniqueLinks");
        XSSFSheet duplicateLink=links.createSheet("duplicateLinks");
        int lengthOfUniquelink=uniqueLinkTexts.size();
        int lengthOfDuplicatelink=duplicateLinkText.size();
        int temp=0;
        for (int cellIndex=0;cellIndex<lengthOfDuplicatelink;cellIndex++){
            Row row=duplicateLink.createRow(cellIndex);
            Cell cell=row.createCell(0);
            cell.setCellValue(duplicateLinkText.get(cellIndex));
        }
        for (Object elements:uniqueLinkTexts)
        {
            Row row=uniqueLink.createRow(temp);
            Cell cell=row.createCell(0);
            cell.setCellValue((String) elements);
            temp++;
        }

links.write(write);

        driver.quit();
    }
}
