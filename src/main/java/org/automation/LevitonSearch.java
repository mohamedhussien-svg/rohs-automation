package org.automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utilis.ChromeUtility;
import utilis.FileUtility;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class LevitonSearch {


    public static final String URL = "https://leviton.com/";
    public static final String SUPPLIER = "leviton";

    public static final String searchBoxXpath = "//div[@class='atomic-focus-detector']//input[@type='search']";
    public static final String searchButtonXpath = "/div/atomic-focus-detector/button/atomic-icon/svg/path";
    public static final String productsTabXpath = "/a/img";
    public static final String resultPageXpath = "/html/body/div[2]/div/div/main/div/div/div/div/div/div/div/div[2]/div/div[2]/div/div[6]/div[3]/a";


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

        WebDriver driver = chromeUtility.getFireFoxDriver();

        List<String> parts = FileUtility.readFile(inputFilePath);

        for (String part : parts) {
            try {
                driver.get(URL);
                chromeUtility.wait(driver, 5);

                WebElement searchBox = chromeUtility.getElementByXpath(driver, searchBoxXpath);
                if (Objects.isNull(searchBox)) {
                    System.out.println("PartNumber Not Found " + part + " Search Box not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Search Box not found"});
                    continue;
                }
                searchBox.sendKeys(part);

                WebElement searchButton = chromeUtility.getElementByXpath(driver, searchButtonXpath);
                if (Objects.isNull(searchButton)) {
                    System.out.println("PartNumber Not Found " + part + " Search Button not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Search Button not found"});
                    continue;
                }
                searchButton.click();
                chromeUtility.wait(driver, 5);

                WebElement products = chromeUtility.getElementByXpath(driver, productsTabXpath);
                if (Objects.isNull(products)) {
                    System.out.println("PartNumber Not Found " + part + " Product Tab Not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Product Tab Not found"});
                    continue;
                }
                products.click();
                chromeUtility.wait(driver, 5);

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

