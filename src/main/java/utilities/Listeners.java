package utilities;

import com.microsoft.playwright.Page;
import org.testng.*;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Listeners implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test Failed: " + result.getName());

        try {
            // Get the test class instance that failed
            Object currentClass = result.getInstance();
            // Get the page object from the test class (assuming it extends Base)
            Page page = ((Base) currentClass).getPage();

            if (page != null) {
                // Get timestamp for unique screenshot name
                String timestamp = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

                // Create screenshots directory if it doesn't exist
                Files.createDirectories(Paths.get("test-output", "screenshots"));

                // Take screenshot
                String screenshotPath = String.format("test-output/screenshots/%s_%s.png",
                        result.getName(), timestamp);
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get(screenshotPath)));

                System.out.println("Screenshot saved: " + screenshotPath);
            } else {
                System.out.println("Page object is null - cannot take screenshot");
            }

            // Log the error details
            if (result.getThrowable() != null) {
                result.getThrowable().printStackTrace();
            }

        } catch (Exception e) {
            System.err.println("Error while capturing failure evidence: " + e.getMessage());
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Test Started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Passed: " + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test Skipped: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Test Suite Started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test Suite Finished: " + context.getName());
    }

}
