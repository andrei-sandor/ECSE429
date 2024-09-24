import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;

import javax.xml.crypto.dsig.XMLObject;

import static org.hamcrest.core.IsEqual.equalTo;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.Random.class)
public class testsTodo {
    public Process process;
    public String url = "http://localhost:4567";

    @BeforeEach
    public void setUp() throws Exception {
        try {
            process = Runtime.getRuntime().exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Make sure application is running
        System.out.println("Starting tests in...\n");
        for (int i = 1; i > 0; i--) {
            System.out.println(i);
            Thread.sleep(1000);
        }



    }

    @AfterEach
    public void setDown() {
        process.destroy();
    }

    @Test
    public void getTodos() {
        Response response = RestAssured.given()
                                       .get(url + "/todos");

        assertEquals(200, response.getStatusCode());

    }

    @Test
    public void headTodos(){
        Response response = RestAssured.given()
                                        .head(url + "/todos");

        assertEquals(200,response.getStatusCode());
    }

    @Test
    public void postTodosWithValidJSONInputs(){
        JSONObject body = new JSONObject();
        body.put("title", "title1");
        body.put("description", "description1");
        body.put("doneStatus", false);

        Response res = RestAssured.given()
                                  .body(body.toString())
                                  .post("http://localhost:4567/todos");


        int statusCode = res.getStatusCode();
        String actualTitle = res.getBody().jsonPath().getString("title");
        String description = res.getBody().jsonPath().getString("description");
        String doneStatus = res.getBody().jsonPath().getString("doneStatus");
        assertEquals(201, statusCode);
        assertEquals("title1", actualTitle);
        assertEquals("description1", description);
        assertEquals("false", doneStatus);
    }

    @Test
    public void postTodosNoTitle(){
        JSONObject body = new JSONObject();
        body.put("description", "description1");
        body.put("doneStatus", false);

        Response res = RestAssured.given()
                                  .body(body.toString())
                                  .post("http://localhost:4567/todos");

        int statusCode = res.getStatusCode();
        assertEquals(400, statusCode);

    }

    @Test
    public void postTodosNoDescription(){
        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("doneStatus", true);

        Response res = RestAssured.given()
                .body(body.toString())
                .post("http://localhost:4567/todos");

        int statusCode = res.getStatusCode();
        String actualTitle = res.getBody().jsonPath().getString("title");
        String doneStatus = res.getBody().jsonPath().getString("doneStatus");
        assertEquals(201, statusCode);
        assertEquals("title2", actualTitle);
        assertEquals("true", doneStatus);

    }

    @Test
    public void postTodosNoStatus(){
        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("description", "description2");

        Response res = RestAssured.given()
                .body(body.toString())
                .post("http://localhost:4567/todos");

        int statusCode = res.getStatusCode();
        String actualTitle = res.getBody().jsonPath().getString("title");
        String actualDescription = res.getBody().jsonPath().getString("description");
        assertEquals(201, statusCode);
        assertEquals("title2", actualTitle);
        assertEquals("description2", actualDescription);

    }

    @Test
    public void postTodosNoPresentField(){
        JSONObject body = new JSONObject();
        body.put("notExistantField", "noField");

        Response res = RestAssured.given()
                                  .body(body.toString())
                                  .post("http://localhost:4567/todos");

        int statusCode = res.getStatusCode();
        assertEquals(400, statusCode);

    }

    ///////////////////////////////////// /todos/id /////////////////////////
    @Test
    public void getTodosWithValidID() {
        Response response = RestAssured.given()
                                       .get(url + "/todos/1");

        assertEquals(200, response.getStatusCode());

    }

    @Test
    public void getTodosWithInvalidID() {
        Response response = RestAssured.given()
                                       .get(url + "/todos/10000");

        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void headTodosWithValidID(){
        Response response = RestAssured.given()
                                       .head(url + "/todos/1");

        assertEquals(200,response.getStatusCode());
    }

    @Test
    public void headTodosWithInValidID(){
        Response response = RestAssured.given()
                                       .head(url + "/todos/1000");

        assertEquals(404,response.getStatusCode());
    }



    @Test
    public void postTodosWithValidJSONInputsValidID() {
        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("description", "description2");
        body.put("doneStatus", false);

        Response res = RestAssured.given()
                .body(body.toString())
                .post("http://localhost:4567/todos/1");


        int statusCode = res.getStatusCode();
        String actualTitle = res.getBody().jsonPath().getString("title");
        String description = res.getBody().jsonPath().getString("description");
        String doneStatus = res.getBody().jsonPath().getString("doneStatus");
        assertEquals(200, statusCode);
        assertEquals("title2", actualTitle);
        assertEquals("description2", description);
        assertEquals("false", doneStatus);

    }

    @Test
    public void postTodosWithOnlyTitleValidID() {
        JSONObject body = new JSONObject();
        body.put("title", "title2");

        Response res = RestAssured.given()
                .body(body.toString())
                .post("http://localhost:4567/todos/1");


        int statusCode = res.getStatusCode();
        String actualTitle = res.getBody().jsonPath().getString("title");
        assertEquals(200, statusCode);
        assertEquals("title2", actualTitle);

    }

    @Test
    public void postTodosWithOnlyDescriptionValidID() {
        JSONObject body = new JSONObject();
        body.put("description", "description2");


        Response res = RestAssured.given()
                .body(body.toString())
                .post("http://localhost:4567/todos/1");


        int statusCode = res.getStatusCode();
        String actualDescription = res.getBody().jsonPath().getString("description");
        assertEquals(200, statusCode);
        assertEquals("description2", actualDescription);

    }

    @Test
    public void postTodosWithOnlyDoneStatusValidID() {
        JSONObject body = new JSONObject();
        body.put("doneStatus", true);


        Response res = RestAssured.given()
                .body(body.toString())
                .post("http://localhost:4567/todos/1");


        int statusCode = res.getStatusCode();
        String actualDescription = res.getBody().jsonPath().getString("doneStatus");
        assertEquals(200, statusCode);
        assertEquals("true", actualDescription);

    }

    @Test
    public void putTodosWithCorrectJSONValidID() {
        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("description", "description2");
        body.put("doneStatus", false);


        Response res = RestAssured.given()
                .body(body.toString())
                .put("http://localhost:4567/todos/1");


        int statusCode = res.getStatusCode();
        String actualTitle = res.getBody().jsonPath().getString("title");
        String description = res.getBody().jsonPath().getString("description");
        String doneStatus = res.getBody().jsonPath().getString("doneStatus");
        assertEquals(200, statusCode);
        assertEquals("title2", actualTitle);
        assertEquals("description2", description);
        assertEquals("false", doneStatus);

    }

    @Test
    public void putTodosWithCorrectJSONInvalidID() {
        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("description", "description2");
        body.put("doneStatus", false);


        Response res = RestAssured.given()
                .body(body.toString())
                .put("http://localhost:4567/todos/100000");


        int statusCode = res.getStatusCode();
        assertEquals(404, statusCode);

    }

    @Test
    public void putTodosWithMissingTitleValidID() {
        JSONObject body = new JSONObject();
        body.put("description", "description5");
        body.put("doneStatus", false);


        Response res = RestAssured.given()
                .body(body.toString())
                .put("http://localhost:4567/todos/1");


        int statusCode = res.getStatusCode();
        assertEquals(400, statusCode);

    }

    @Test
    public void putTodosWithOnlyTitleValidID() {
        JSONObject body = new JSONObject();
        body.put("title", "title2");


        Response res = RestAssured.given()
                                  .body(body.toString())
                                  .put("http://localhost:4567/todos/1");


        int statusCode = res.getStatusCode();
        String actualTitle = res.getBody().jsonPath().getString("title");
        assertEquals(200, statusCode);
        assertEquals("title2", actualTitle);


    }


    @Test
    public void postTodosWithValidJSONInputsInvalidID() {
        JSONObject body = new JSONObject();
        body.put("title", "title3");
        body.put("description", "description3");
        body.put("doneStatus", false);

        Response res = RestAssured.given()
                .body(body.toString())
                .post("http://localhost:4567/todos/1000");


        int statusCode = res.getStatusCode();
        assertEquals(404, statusCode);


    }

    @Test
    public void deleteTodosWithGoodID(){
        Response res = RestAssured.given()
                                  .delete(url + "/todos/2");

        int statusCode = res.getStatusCode();
        assertEquals(200,statusCode);
    }

    @Test
    public void DeleteNoMoreExistingTodo(){
        Response res = RestAssured.given()
                                  .delete(url + "/todos/10000");

        int statusCode = res.getStatusCode();
        assertEquals(404,statusCode);

    }

    @Test
    public void malformatedJSON(){
        JSONObject body = new JSONObject();
        body.put("title1", "title2");
        body.put("description1", "description2");
        body.put("doneStatus1", false);

        Response res = RestAssured.given()
                                  .body(body.toString())
                                  .post("http://localhost:4567/todos/1");


        int statusCode = res.getStatusCode();
        assertEquals(400,statusCode);
    }

//    @Test
//    public void malformatedXML(){
//
//    }

    // Check if it can get a XML response
    @Test
    void testTodoGetRequestXMLPayload() {

        Response response = RestAssured.given()
                                       .accept(ContentType.XML) // Request XML response
                                       .when()
                                       .get("http://localhost:4567/todos/1");

        assertEquals(200, response.getStatusCode());
        assertEquals("application/xml", response.getContentType());
    }

}
