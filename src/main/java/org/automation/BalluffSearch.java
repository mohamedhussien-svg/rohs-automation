package org.automation;

import org.openqa.selenium.*;
import utilis.ChromeUtility;
import utilis.FileUtility;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BalluffSearch {

    public static final String URL = "https://www.balluff.com/en-de/products";
    public static final String SUPPLIER = "Balluff";


    public static final String cookiesXpath = "/html/body/div[1]/footer/div/div/div[3]/ul/li[6]/div/div/div/div/form/div[3]/button[2]";
    public static final String openSearchBoxXpath = "/html/body/div[2]/header/div[1]/div[1]/div/div[2]/div[1]/div/button";
    public static final String searchBoxXpath = "/html/body/div[2]/header/div[1]/div[1]/div/div[2]/div[1]/div/div/div/input";
    public static final String partLinkXpath = "/html/body/div[2]/header/div[3]/div/div[2]/div/div[2]/div/div/div[2]/div/div[2]/div[3]/div/a";
    public static final String productDocXpath = "/html/body/div[2]/div[1]/div[5]/div/div/div[4]/div/div[2]/button[1]";
    public static final String listDocXpath = "/html/body/div[2]/div[1]/div[5]/div/div/div[4]/div/div[2]";

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

        WebDriver driver = null;

        List<String> parts = FileUtility.readFile(inputFilePath);

        for (String part : parts) {
            try {

                if (driver != null)
                    driver.quit();
                driver = chromeUtility.getFireFoxDriver();

                driver.get(URL);
                chromeUtility.wait(driver, 5);


                WebElement cookies = chromeUtility.getElementByXpath(driver, cookiesXpath);
                if (Objects.nonNull(cookies)) {
                    cookies.click();
                }

                WebElement openSearchBox = chromeUtility.getElementByXpath(driver, openSearchBoxXpath);
                if (Objects.isNull(openSearchBox)) {
                    System.out.println("PartNumber Not Found " + part + " Open Search Box not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Open Search Box not found"});
                    continue;
                }
                chromeUtility.wait(driver, 5);
                openSearchBox.click();
                WebElement searchBox = chromeUtility.getElementByXpath(driver, searchBoxXpath);
                if (Objects.isNull(searchBox)) {
                    System.out.println("PartNumber Not Found " + part + " Search Box not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Search Box not found"});
                    continue;
                }
                searchBox.sendKeys(part + Keys.ENTER);
                chromeUtility.wait(driver, 5);
                Thread.sleep(5000);

                WebElement element = chromeUtility.getElementByCss(driver, ".mt-2.text-xs.leading-tight.text-gray-400");


                if (Objects.isNull(element)) {
                    System.out.println("PartNumber Not Found " + part + " Product Tab Not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Product Tab Not found"});
                    continue;
                }
                WebElement products = element.findElement(By.tagName("a"));

//                WebElement products = chromeUtility.getElementByXpath(driver, new String[]{partLinkXpath, "/html/body/div[2]/header/div[3]/div/div[2]/div/div[4]/div[1]/div/div/div[2]/div/div/div[1]/div[2]/div[4]/div/div/a"});
                if (Objects.isNull(products)) {
                    System.out.println("PartNumber Not Found " + part + " Product Tab Not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Product Tab Not found"});

                    continue;
                }

                chromeUtility.wait(driver, 5);
                Thread.sleep(5000);
                products.click();


                WebElement eleByXpath = chromeUtility.getElementByXpath(driver, "/html/body/div[2]/div[1]/article/div[1]/div[2]");
                if (Objects.isNull(eleByXpath)) {
                    System.out.println("PartNumber Not Found " + part + " Product Tab Not found");
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Product Tab Not found"});
                    continue;
                }
                List<WebElement> buttons = eleByXpath.findElements(By.tagName("button"));


                Optional<WebElement> resultPage = buttons.stream().filter(x -> x.getText().equalsIgnoreCase("Downloads")).findFirst();
                if (!resultPage.isPresent()) {
                    System.out.println("PartNumber Not Found " + part);
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Downloads Not Found"});
                    continue;
                }

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", resultPage.get());

                Thread.sleep(5000);
                resultPage.get().click();
                chromeUtility.wait(driver, 5);

                WebElement docPage = chromeUtility.getElementByXpath(driver, productDocXpath);
                if (Objects.isNull(docPage)) {
                    System.out.println("PartNumber Not Found " + part);
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Product Docs Not Found"});
                    continue;
                }

                chromeUtility.wait(driver, 5);
                docPage.click();
                Thread.sleep(2000);


                WebElement listDiv = chromeUtility.getElementByXpath(driver, listDocXpath);
                if (Objects.isNull(listDiv)) {
                    System.out.println("PartNumber Not Found " + part);
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "PartNumber Not Found"});
                    continue;
                }
                chromeUtility.wait(driver, 5);
                List<WebElement> docs = listDiv.findElements(By.tagName("div"));
                chromeUtility.wait(driver, 5);


                Optional<WebElement> first = docs.stream().filter(x -> x.getText().equalsIgnoreCase("Material Compliance")).findFirst();
                if (!first.isPresent()) {
                    System.out.println("PartNumber Not Found " + part);
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "Material Compliance Not Found"});
                    continue;
                }
                WebElement webElement = first.get();

                WebElement parent = webElement.findElement(By.xpath(".."));
                List<WebElement> material = parent.findElements(By.tagName("div"));

                Optional<WebElement> download = material.stream().filter(x -> x.getText().equalsIgnoreCase("download")).findFirst();
                if (!download.isPresent()) {
                    System.out.println("PartNumber Not Found " + part);
                    FileUtility.writeFileRow(partsStatusFile, new String[]{part, "NotFound", "", "PartNumber Not Found"});
                    continue;
                }
                download.get().click();

                Thread.sleep(2000);

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

