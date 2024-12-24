package org.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utilis.ChromeUtility;
import utilis.FileUtility;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class MurrelektronikSearch {


    public static final String URL = "https://shop.murrelektronik.com/index.php?lang=1&cl=search&searchparam=";
    public static final String SUPPLIER = "murrelektronik";

    public static final String toProductXpath = "/html/body/div[1]/div[5]/div[2]/ul/li/form/div[2]/div[4]/a";
    public static final String downloadXpath = "/html/body/div[1]/div[5]/div/div/div/div[3]/div/div[1]/div[2]/div[2]/div[3]/div[5]/h3/a";
    public static final String download2Xpath = "/html/body/div[1]/div[5]/div/div/div/div[3]/div/div[1]/div[2]/div[2]/div[3]/div[4]/h3/a";

    public static final String downloadLink1Xpath = "/html/body/div[1]/div[5]/div/div/div/div[3]/div/div[1]/div[2]/div[2]/div[3]/div[5]/div/div/div/div/form/div[3]/div/div[2]/ul/li[8]/div/a";
    public static final String downloadLink2Xpath = "/html/body/div[1]/div[5]/div/div/div/div[3]/div/div[1]/div[2]/div[2]/div[3]/div[4]/div/div/div/div/form/div[3]/div/div[2]/ul/li[6]/div/a";
    public static final String downloadUl1Xpath = "/html/body/div[1]/div[5]/div/div/div/div[3]/div/div[1]/div[2]/div[2]/div[3]/div[4]/div/div/div/div/form/div[3]/div/div[2]/ul";
    public static final String downloadUl2Xpath = "/html/body/div[1]/div[5]/div/div/div/div[3]/div/div[1]/div[2]/div[2]/div[3]/div[5]/div/div/div/div/form/div[3]/div/div[2]/ul";

    public static final String acceptConditionsXpath = "//*[@id=\"checkAgbSingleDownload\"]";
    public static final String finalDownloadXpath = "//*[@id=\"mediabasket-form-single-button\"]";
    public static final String acceptPrivacy = "/div/div/div[2]/div/div[2]/div/div[2]/div/div/div/button[2]";


    public void search() throws InterruptedException {


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
                driver.get(URL.concat(part));
                chromeUtility.wait(driver, 5);

                WebElement toProductLink = chromeUtility.getElementByXpath(driver, toProductXpath);
                if (Objects.isNull(toProductLink)) {
                    System.out.println("PartNumber Not Found " + part + " To Product BTN not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "To Product BTN not found"});
                    continue;
                }
                toProductLink.click();

                WebElement downloadButton = chromeUtility.getElementByXpath(driver, new String[]{downloadXpath, download2Xpath});
                if (Objects.isNull(downloadButton)) {
                    System.out.println("PartNumber Not Found " + part + " Download Button not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Download Button not found"});
                    continue;
                }
                downloadButton.click();
                chromeUtility.wait(driver, 5);


                WebElement downloadUl = chromeUtility.getElementByXpath(driver, new String[]{downloadUl1Xpath, downloadUl2Xpath});
                if (Objects.isNull(downloadUl)) {
                    System.out.println("PartNumber Not Found " + part + " Download UL not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Download UL not found"});
                    continue;
                }
                Thread.sleep(5000);
                List<WebElement> elements = chromeUtility.getElementsByXpath(downloadUl, ".//li");


                Optional<WebElement> rohsReach = elements.stream().filter(x -> x.getText().toUpperCase().contains("ROHS REACH")).findFirst();
                if (!rohsReach.isPresent()) {
                    System.out.println("PartNumber Not Found " + part + " downloadLink  Not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Product Tab Not found"});
                    continue;
                }

//                WebElement downloadLink = chromeUtility.getElementByXpath(driver, new String[]{downloadLink1Xpath, downloadLink2Xpath});
//                if (Objects.isNull(downloadLink)) {
//                    System.out.println("PartNumber Not Found " + part + " downloadLink  Not found");
//                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Product Tab Not found"});
//                    continue;
//                }
                Thread.sleep(5000);
                rohsReach.get().findElement(By.xpath(".//div/a")).click();
                chromeUtility.wait(driver, 5);

                WebElement acceptCondition = chromeUtility.getElementByXpath(driver, acceptConditionsXpath);
                if (Objects.isNull(acceptCondition)) {
                    System.out.println("PartNumber Not Found " + part + " acceptCondition  Not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "acceptCondition Not Found"});
                    continue;
                }
                acceptCondition.click();
                Thread.sleep(2000);

                WebElement finalDownload = chromeUtility.getElementByXpath(driver, finalDownloadXpath);
                if (Objects.isNull(finalDownload)) {
                    System.out.println("PartNumber Not Found " + part + " finalDownload  Not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "finalDownload Not Found"});
                    continue;
                }


                finalDownload.click();
                Thread.sleep(1000);

                System.out.println("PartNumber Found " + part);
                FileUtility.writeFileRow(partsStatusFile, new String[]{part, "Found", FileUtility.lastFileCreated(downloadDir), rohsReach.get().getText().replace("\n", "")});
            } catch (Exception e) {
                Thread.sleep(3000);
                e.printStackTrace();
                FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Need Manual Checks "});
            }
        }
        System.out.println("Process completed for file " + inputFilePath);
        driver.quit();
    }

    private void acceptPrivacyClick(ChromeUtility chromeUtility, WebDriver driver) throws InterruptedException {
        WebElement acceptPrivacyButton = chromeUtility.getElementByXpathJs(driver, acceptPrivacy);

        if (Objects.nonNull(acceptPrivacyButton)) {
            System.out.println("Accept peivacy found");
            acceptPrivacyButton.click();

        }

    }


}
