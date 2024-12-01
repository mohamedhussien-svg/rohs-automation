package org.automation;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilis.ChromeUtility;
import utilis.FileUtility;

import java.io.File;
import java.util.List;
import java.util.Objects;

@Slf4j
public class DelockSearch {

    private static final Logger log = LoggerFactory.getLogger(DelockSearch.class);

    public static final String URL = "https://www.delock.com/";
    public static final String SUPPLIER = "delock";

    public static final String searchBoxXpath = "/html/body/header/section/div[2]/form/input[1]";
    public static final String searchButtonXpath = "/html/body/header/section/div[2]/form/input[2]";
    public static final String productsTabXpath = "/html/body/div[1]/section/article/div[2]/div[2]/div[2]/a[3]";
    public static final String resultPageXpath = "/html/body/div[1]/section/article/div[2]/div[2]/div[5]/a[2]";


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
                    log.info("PartNumber Not Found {}, {}", part, "Search Box not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Search Box not found"});
                    continue;
                }
                searchBox.sendKeys(part);

                WebElement searchButton = chromeUtility.getElementByXpath(driver, searchButtonXpath);
                if (Objects.isNull(searchButton)) {
                    log.info("PartNumber Not Found {}, {}", part, "Search Button not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Search Button not found"});
                    continue;
                }
                searchButton.click();
                chromeUtility.wait(driver, 5);

                WebElement products = chromeUtility.getElementByXpath(driver, productsTabXpath);
                if (Objects.isNull(products)) {
                    log.info("PartNumber Not Found {} {}", part, "Product Tab Not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Product Tab Not found"});
                    continue;
                }
                products.click();
                chromeUtility.wait(driver, 5);

                WebElement resultPage = chromeUtility.getElementByXpath(driver, resultPageXpath);
                if (Objects.isNull(resultPage) || !resultPage.getText().toLowerCase().contains("conformity")) {
                    log.info("PartNumber Not Found {}", part);
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "PartNumber Not Found"});
                    continue;
                }
                log.info(resultPage.getText());
                resultPage.click();
                Thread.sleep(5000);
                log.info("PartNumber Found {}", part);
                FileUtility.writeFileRow(partsStatusFile, new String[]{part, "Found", FileUtility.lastFileCreated(downloadDir)});
            } catch (Exception e) {
                e.printStackTrace();
                FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Need Manual Checks "});
            }
        }
        log.info("Process completed for file {}", inputFilePath);
        driver.quit();
    }


}
