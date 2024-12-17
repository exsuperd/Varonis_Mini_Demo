package extensions;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;
import utilities.Base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.fail;

public class ApiActions {
    private final Base base;

    public ApiActions(Base base) {
        this.base = base;
    }

    public void verifyTheEntireResponseContent(String endPoint, int expectedStatusCode,
                                               String expectedEntireResponseContent) {
        Response response = base.getHttpRequest().get(endPoint);
        base.setResponse(response);
        System.out.println(response.getStatusCode());
        assertEquals(response.getStatusCode(), expectedStatusCode);
        response.prettyPrint();
        assertEquals(expectedEntireResponseContent, response.prettyPrint());
    }

    public void verifySpecificRecordProperty(String endPoint, int expectedStatusCode, String path, String expectedPathOutput) {
        Response response = base.getHttpRequest().get(endPoint);
        base.setResponse(response);
        JsonPath jp = response.jsonPath();
        base.setJsonPath(jp);
        System.out.println(response.getStatusCode());
        assertEquals(response.getStatusCode(), expectedStatusCode);
        response.prettyPrint();
        String targetValue = jp.getJsonObject(path).toString();
        System.out.println("Actual target value is: " + targetValue);
        assertEquals(targetValue, expectedPathOutput);
    }

    public void verifyNumbersOfRecords(String endPoint, int expectedStatusCode, String uniquePath, int expectedNumOfRecords) {
        Response response = base.getHttpRequest().get(endPoint);
        base.setResponse(response);
        JsonPath jp = response.jsonPath();
        base.setJsonPath(jp);
        System.out.println(response.getStatusCode());
        assertEquals(response.getStatusCode(), expectedStatusCode);
        response.prettyPrint();
        List<Object> list = jp.getList(uniquePath);
        Assert.assertEquals(list.size(), expectedNumOfRecords);
    }

    public void verifyWhichRecordIDContainsSpecificValue(String endPoint, int expectedStatusCode, String uniquePath,
                                                         String searchedText, int expectedRecordID) {
        boolean isTextFound = false;
        int actualIDWithTargetText = 0;
        Response response = base.getHttpRequest().get(endPoint);
        base.setResponse(response);
        JsonPath jp = response.jsonPath();
        base.setJsonPath(jp);
        System.out.println(response.getStatusCode());
        assertEquals(response.getStatusCode(), expectedStatusCode);
        response.prettyPrint();
        List<Object> list = jp.getList(uniquePath);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).toString().contains(searchedText)) {
                actualIDWithTargetText = i;
                isTextFound = true;
                System.out.println("Actual target record ID is: " + i);
                break;
            }
        }
        if (isTextFound)
            assertEquals(expectedRecordID, actualIDWithTargetText);
        else {
            fail("Target text was not found in the given path in any of the records");
        }
    }

    public void verifySpecificTextNotIncluded(String endPoint, int expectedStatusCode, String uniquePath,
                                              String notIncludedText) {
        boolean testPassed = true;
        Response response = base.getHttpRequest().get(endPoint);
        base.setResponse(response);
        JsonPath jp = response.jsonPath();
        base.setJsonPath(jp);
        System.out.println(response.getStatusCode());
        assertEquals(response.getStatusCode(), expectedStatusCode);
        response.prettyPrint();
        List<Object> list = jp.getList(uniquePath);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).toString().contains(notIncludedText)) {
                testPassed = false;
                fail("Target text was found in record index " + i);
                break;
            }
        }
        if (testPassed)
            System.out.println("Target test was not found in any record - as expected");
    }

    public void post(String endPoint, JSONObject parameters, HashMap<String, Object> dictionary) {
        for (Map.Entry<String, Object> entry : dictionary.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            parameters.put(key, value);
        }
        RequestSpecification request = base.getHttpRequest()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(parameters.toJSONString());
        Response response = request.post(endPoint);
        base.setResponse(response);
        base.setJsonPath(response.jsonPath());
    }

    public void put(String endPoint, JSONObject parameters, HashMap<String, Object> dictionary) {
        for (Map.Entry<String, Object> entry : dictionary.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            parameters.put(key, value);
        }
        RequestSpecification request = base.getHttpRequest()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(parameters.toJSONString());
        Response response = request.put(endPoint);
        base.setResponse(response);
        base.setJsonPath(response.jsonPath());
    }

    public void deleteSpecificRecord(String endPoint, int expectedStatusCode) {
        Response response = base.getHttpRequest().get(endPoint);
        base.setResponse(response);
        response = base.getHttpRequest().delete(endPoint);
        System.out.println("Status code is " + response.getStatusCode());
        assertEquals(response.getStatusCode(), expectedStatusCode);
    }

    //Specific to jsonPlaceholder

    public void verifySeveralProperties(String endPoint, int expectedStatusCode, String[] expectedValues) {
        Response response = base.getHttpRequest().get(endPoint);
        base.setResponse(response);
        JsonPath jp = response.jsonPath();
        base.setJsonPath(jp);
        System.out.println(response.getStatusCode());
        assertEquals(response.getStatusCode(), expectedStatusCode);
        response.prettyPrint();
        List<Object> actualValues = new ArrayList<>();
        actualValues.add(jp.get("userId").toString());  // Changed from getJsonObject to get
        actualValues.add(jp.get("title").toString());   // Changed from getJsonObject to get
        actualValues.add(jp.get("body").toString());
        for (int i = 0; i < actualValues.size(); i++) {
            assertEquals(expectedValues[i], actualValues.get(i));
        }
    }

}
