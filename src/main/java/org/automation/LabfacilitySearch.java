package org.automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utilis.ChromeUtility;
import utilis.FileUtility;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class LabfacilitySearch {


    public static final String URL = "https://www.labfacility.com/";
    public static final String SUPPLIER = "labfacility";

    public static final String searchBoxXpath = "//*[@id=\"html-body\"]/div[2]/header/div/div/div/div/nav/ul[1]/li[7]/section/div/section[1]/input";
    public static final String searchButtonXpath = "//*[@id=\"html-body\"]/div[2]/header/div/div/div/div/nav/ul[1]/li[7]/section/div/section[2]/section/ul/li/section/a";
    public static final String resultPageXpath = "//*[@id=\"product_addtocart_form\"]/div[2]/div[2]/div[5]/div[2]/a/span";

    public void search() {

        String workingDir = System.getProperty("user.dir");

        String downloadDir = workingDir + "\\output_files\\" + SUPPLIER;

        String statusFilePath = downloadDir + "\\" + "partsStatus.txt";

        String inputFilePath = workingDir + "\\input_files\\" + SUPPLIER + ".txt";

        String[] statusFileHeader = new String[]{"Part", "Status", "PDF", "Comment"};

        FileUtility.clearDir(downloadDir);

        File partsStatusFile = new File(statusFilePath);

        FileUtility.writeFileRow(partsStatusFile, statusFileHeader);

        ChromeUtility chromeUtility = new ChromeUtility(downloadDir);

        WebDriver driver = chromeUtility.getDriver();

        List<String> parts = FileUtility.readFile(inputFilePath);

        for (String part : parts) {
            try {
                driver.get(URL);
                chromeUtility.wait(driver, 30);
                WebElement searchBox = chromeUtility.getElementByXpath(driver, searchBoxXpath);
                if (Objects.isNull(searchBox)) {
                    System.out.println("PartNumber Not Found " + part + " Search Box not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Search Box not found"});
                    continue;
                }
                searchBox.sendKeys(part);
                WebElement searchButton = chromeUtility.getElementByXpathJs(driver, searchButtonXpath);
                if (Objects.isNull(searchButton)) {
                    System.out.println("PartNumber Not Found " + part + " Search Button not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Search Button not found"});
                    continue;
                }
                searchButton.click();
                chromeUtility.wait(driver, 30);


                WebElement resultPage = chromeUtility.getElementByXpath(driver, resultPageXpath);
                if (Objects.isNull(resultPage)) {
                    System.out.println("PartNumber Not Found " + part);
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "PartNumber Not Found"});
                    continue;
                }

                resultPage.click();
                Thread.sleep(5000);
                System.out.println("PartNumber Found " + part);
                FileUtility.writeFileRow(partsStatusFile, new String[]{part, "Found", FileUtility.lastFileCreated(downloadDir)});
            } catch (Exception e) {
                e.printStackTrace();
                FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Need Manual Checks "});
            }
        }
        System.out.println("Process completed for file " + inputFilePath);
        driver.quit();

    }

}
