package org.automation;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilis.ChromeUtility;
import utilis.FileUtility;

import java.io.File;
import java.util.List;
import java.util.Objects;

@Slf4j
public class EdacSearch {

    private static final Logger log = LoggerFactory.getLogger(EdacSearch.class);

    public static final String URL = "https://edac.net/environmental-compliance";
    public static final String SUPPLIER = "edac";

    public static final String selectDocType = "/html/body/div[1]/div[2]/div/div[2]/div/div[1]/div[2]/div[2]/div[2]/form/div[1]/select";
    public static final String searchBoxXpath = "/html/body/div[1]/div[2]/div/div[2]/div/div[1]/div[2]/div[2]/div[2]/form/div[2]/div[3]/input[2]";
    public static final String addPNButtonXpath = "/html/body/div[1]/div[2]/div/div[2]/div/div[1]/div[2]/div[2]/div[2]/form/div[2]/div[4]/div/div/a";
    public static final String downloadButtonXpath = "/html/body/div[1]/div[2]/div/div[2]/div/div[1]/div[2]/div[2]/div[2]/form/div[2]/a[2]";
    public static final String clearAllXpath = "/html/body/div[1]/div[2]/div/div[2]/div/div[1]/div[2]/div[2]/div[2]/form/div[2]/a[1]";


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
            driver.get(URL);
            try {
                chromeUtility.wait(driver, 5);
                WebElement dropdown = chromeUtility.getElementByXpath(driver, selectDocType);

                if (Objects.isNull(dropdown)) {
                    log.info("PartNumber Not Found {}, {}", part, "Doc Type not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Doc Type not found"});
                    continue;
                }

                Select select = new Select(dropdown);
                select.selectByVisibleText("Certificate of Compliance- EU RoHS Declaration");
                chromeUtility.wait(driver, 5);

                WebElement searchBox = chromeUtility.getElementByXpath(driver, searchBoxXpath);
                if (Objects.isNull(searchBox)) {
                    log.info("PartNumber Not Found {}, {}", part, "Search Box not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Search Box not found"});
                    continue;
                }
                searchBox.sendKeys(part);
                chromeUtility.wait(driver, 10);


                WebElement searchButton = chromeUtility.getElementByXpathJs(driver, addPNButtonXpath);
                if (Objects.isNull(searchButton)) {
                    log.info("PartNumber Not Found {}, {}", part, "Add Button not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Add Button not found"});
                    continue;
                }
                chromeUtility.wait(driver, 10);
                searchButton.click();

                Thread.sleep(5000);
                WebElement downloadButton = chromeUtility.getElementByXpath(driver, downloadButtonXpath);
                if (Objects.isNull(downloadButton)) {
                    log.info("PartNumber Not Found {}", part);
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "DownloadButton Not Found"});
                    continue;
                }
                log.info(downloadButton.getText());
                downloadButton.click();

                Thread.sleep(5000);
                log.info("PartNumber Found {}", part);
                FileUtility.writeFileRow(partsStatusFile, new String[]{part, "Found", FileUtility.lastFileCreated(downloadDir)});

                WebElement clearAll = chromeUtility.getElementByXpath(driver, clearAllXpath);
                if (Objects.nonNull(clearAll)) {
                    clearAll.click();
                }
            } catch (Exception e) {
                e.printStackTrace();
                FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Need Manual Checks "});
            }
        }
        log.info("Process completed for file {}", inputFilePath);
        driver.quit();
    }


}
