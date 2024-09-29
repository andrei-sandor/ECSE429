import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;


import java.io.IOException;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Random.class)
public class testsTodo {
    public static Process jar;
    public String url = "http://localhost:4567";


    @BeforeEach
    public void setUp() throws Exception {
        try {
            jar = Runtime.getRuntime().exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
            sleep(300);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void setDown() throws InterruptedException {
        jar.destroy();
        sleep(300);
    }

    //
    // Start of unit test related to /todos
    //

    // GET call
    @Test
    public void getTodos() {
        Response response = RestAssured.given()
                                       .get(url + "/todos");

        assertEquals(200, response.getStatusCode());
    }

    // HEAD call
    @Test
    public void headTodos(){
        Response response = RestAssured.given()
                                        .head(url + "/todos");

        assertEquals(200,response.getStatusCode());
    }

    //POST call with all JSON inputs there
    @Test
    public void postTodosWithValidJSONInputs(){
        JSONObject body = new JSONObject();
        body.put("title", "title1");
        body.put("description", "description1");
        body.put("doneStatus", false);

        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .post("http://localhost:4567/todos");


        int statusCode = response.getStatusCode();
        String actualTitle = response.getBody().jsonPath().getString("title");
        String description = response.getBody().jsonPath().getString("description");
        String doneStatus = response.getBody().jsonPath().getString("doneStatus");
        assertEquals(201, statusCode);
        assertEquals("title1", actualTitle);
        assertEquals("description1", description);
        assertEquals("false", doneStatus);
    }

    // POST call without title
    @Test
    public void postTodosNoTitle(){
        JSONObject body = new JSONObject();
        body.put("description", "description1");
        body.put("doneStatus", false);

        Response response = RestAssured.given()
                                  .body(body.toString())
                                  .post("http://localhost:4567/todos");

        int statusCode = response.getStatusCode();
        assertEquals(400, statusCode);

    }

    //POST call without description
    @Test
    public void postTodosNoDescription(){
        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("doneStatus", true);

        Response response = RestAssured.given()
                                  .body(body.toString())
                                  .post("http://localhost:4567/todos");

        int statusCode = response.getStatusCode();
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualDoneStatus = response.getBody().jsonPath().getString("doneStatus");
        String actualDescription = response.getBody().jsonPath().getString("description");
        assertEquals(201, statusCode);
        assertEquals("title2", actualTitle);
        assertEquals("true", actualDoneStatus);
        assertEquals("", actualDescription);

    }

    //POST call without doneStatus
    @Test
    public void postTodosNoStatus(){
        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("description", "description2");

        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .post("http://localhost:4567/todos");

        int statusCode = response.getStatusCode();
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualDescription = response.getBody().jsonPath().getString("description");
        String actualStatus = response.getBody().jsonPath().getString("doneStatus");
        assertEquals(201, statusCode);
        assertEquals("title2", actualTitle);
        assertEquals("description2", actualDescription);
        assertEquals("false", actualStatus);


    }

    @Test
    public void postTodosNoPresentField(){
        JSONObject body = new JSONObject();
        body.put("notExistantField", "noField");

        Response response = RestAssured.given()
                                  .body(body.toString())
                                  .post("http://localhost:4567/todos");

        int statusCode = response.getStatusCode();
        assertEquals(400, statusCode);

    }
    //
    // todos/id section
    //


    @Test
    public void getTodosWithValidID() {
        int id = 1;
        Response response = RestAssured.given()
                                       .get(url + "/todos/" + id);

        assertEquals(200, response.getStatusCode());

        String actualID = response.getBody().jsonPath().getString("todos[0].id");
        String actualTitle = response.getBody().jsonPath().getString("todos[0].title");
        String actualStatus = response.getBody().jsonPath().getString("todos[0].doneStatus");
        String actualDescription = response.getBody().jsonPath().getString("todos[0].description");

        assertEquals("1",actualID );
        assertEquals("scan paperwork", actualTitle);
        assertEquals("false", actualStatus);
        assertEquals("",actualDescription );

    }


    @Test
    public void getTodosWithInvalidID() {
        int id = 10000;
        Response response = RestAssured.given()
                                       .get(url + "/todos/" + id);

        assertEquals(404, response.getStatusCode());
    }


    @Test
    public void headTodosWithValidID(){
        int id = 1;
        Response response = RestAssured.given()
                                       .head(url + "/todos/" + id);

        assertEquals(200,response.getStatusCode());
    }
    
    @Test
    public void headTodosWithInValidID(){
        int id = 1000;
        Response response = RestAssured.given()
                                       .head(url + "/todos/" + id);

        assertEquals(404,response.getStatusCode());
    }


    // POST with valid id and all the correct input values.
    @Test
    public void postTodosWithValidJSONInputsValidID() {
        int id = 1;

        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("description", "description2");
        body.put("doneStatus", false);

        Response response = RestAssured.given()
                                  .body(body.toString())
                                  .post(url + "/todos/" + id);


        int statusCode = response.getStatusCode();
        String actualTitle = response.getBody().jsonPath().getString("title");
        String description = response.getBody().jsonPath().getString("description");
        String doneStatus = response.getBody().jsonPath().getString("doneStatus");
        assertEquals(200, statusCode);
        assertEquals("title2", actualTitle);
        assertEquals("description2", description);
        assertEquals("false", doneStatus);

    }

    //POST with invalid id
    @Test
    public void postTodosWithValidJSONInputsInvalidID() {
        int id = 1000;
        JSONObject body = new JSONObject();
        body.put("title", "title3");
        body.put("description", "description3");
        body.put("doneStatus", false);

        Response response = RestAssured.given()
                .body(body.toString())
                .post(url + "/todos/" + id);


        int statusCode = response.getStatusCode();
        assertEquals(404, statusCode);


    }

    // POST without title
    @Test
    public void postTodosWithoutTitleValidID() {
        int id = 1;
        JSONObject body = new JSONObject();
        body.put("description", "description2");
        body.put("doneStatus", false);

        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .post(url  + "/todos/" + id);


        int statusCode = response.getStatusCode();
        String description = response.getBody().jsonPath().getString("description");
        String doneStatus = response.getBody().jsonPath().getString("doneStatus");
        assertEquals(200, statusCode);
        assertEquals("description2", description);
        assertEquals("false", doneStatus);

    }

    // POST without only description
    @Test
    public void postTodosWithoutOnlyDescriptionValidID() {
        int id = 1;
        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("doneStatus", false);


        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .post(url + "/todos/"+ id);


        int statusCode = response.getStatusCode();
        String actualTitle = response.getBody().jsonPath().getString("title");
        String doneStatus = response.getBody().jsonPath().getString("doneStatus");
        assertEquals(200, statusCode);
        assertEquals("title2", actualTitle);
        assertEquals("false", doneStatus);

    }

    // POST without only done status
    @Test
    public void postTodosWithoutOnlyDoneStatusValidID() {
        int id = 1;
        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("description", "description2");


        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .post(url + "/todos/"+ id);


        int statusCode = response.getStatusCode();
        String actualTitle = response.getBody().jsonPath().getString("title");
        String description = response.getBody().jsonPath().getString("description");
        assertEquals(200, statusCode);
        assertEquals("title2", actualTitle);
        assertEquals("description2", description);

    }

    // POST with only title
    @Test
    public void postTodosWithOnlyTitleValidID() {
        int id = 1;
        JSONObject body = new JSONObject();
        body.put("title", "title2");

        Response response = RestAssured.given()
                .body(body.toString())
                .post(url  + "/todos/" + id);


        int statusCode = response.getStatusCode();
        String actualTitle = response.getBody().jsonPath().getString("title");
        assertEquals(200, statusCode);
        assertEquals("title2", actualTitle);

    }

    // POST with only description
    @Test
    public void postTodosWithOnlyDescriptionValidID() {
        int id = 1;
        JSONObject body = new JSONObject();
        body.put("description", "description2");


        Response response = RestAssured.given()
                .body(body.toString())
                .post(url + "/todos/"+ id);


        int statusCode = response.getStatusCode();
        String actualDescription = response.getBody().jsonPath().getString("description");
        assertEquals(200, statusCode);
        assertEquals("description2", actualDescription);

    }

    // POST with only done status
    @Test
    public void postTodosWithOnlyDoneStatusValidID() {
        int id = 1;
        JSONObject body = new JSONObject();
        body.put("doneStatus", true);


        Response response = RestAssured.given()
                .body(body.toString())
                .post(url + "/todos/" + id);


        int statusCode = response.getStatusCode();
        String actualDescription = response.getBody().jsonPath().getString("doneStatus");
        assertEquals(200, statusCode);
        assertEquals("true", actualDescription);

    }
    // PUT with valid id and all the fields
    @Test
    public void putTodosWithCorrectJSONValidID() {
        int id = 1;
        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("description", "description2");
        body.put("doneStatus", false);


        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .put(url + "/todos/" + id);


        int statusCode = response.getStatusCode();
        String actualTitle = response.getBody().jsonPath().getString("title");
        String description = response.getBody().jsonPath().getString("description");
        String doneStatus = response.getBody().jsonPath().getString("doneStatus");
        assertEquals(200, statusCode);
        assertEquals("title2", actualTitle);
        assertEquals("description2", description);
        assertEquals("false", doneStatus);

    }
    // PUT with invalid id
    @Test
    public void putTodosWithCorrectJSONInvalidID() {
        int id = 10000;
        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("description", "description2");
        body.put("doneStatus", false);


        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .put(url +"/todos/" + id);


        int statusCode = response.getStatusCode();
        assertEquals(404, statusCode);

    }

    // Bug, PUT without a title
    @Test
    public void putTodosWithMissingTitleValidID() {
        int id = 1;
        JSONObject body = new JSONObject();
        body.put("description", "description5");
        body.put("doneStatus", true);


        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .put(url+ "/todos/" + id);


        int statusCode = response.getStatusCode();
        assertEquals(400, statusCode);

    }

    // Expected behavior
    @Test
    public void putTodosWithMissingTitleValidIDExpectedResult() {
        int id = 1;
        JSONObject body = new JSONObject();
        body.put("description", "description5");
        body.put("doneStatus", true);


        Response response = RestAssured.given()
                .body(body.toString())
                .put(url+ "/todos/" + id);


        int statusCode = response.getStatusCode();
        String actualTitle = response.getBody().jsonPath().getString("title");
        String description = response.getBody().jsonPath().getString("description");
        String doneStatus = response.getBody().jsonPath().getString("doneStatus");
        assertEquals(200, statusCode);
        assertEquals("title2", actualTitle);
        assertEquals("description5", description);
        assertEquals("true", doneStatus);

    }


    // Bug, here the description and the doneStatus get reset to "" and false after not touching them
    @Test
    public void putTodosWithOnlyTitleValidID() {
        // We add a todo to better compare the results since the initial todos are set to the default values generated
        // by this bug.

        JSONObject body_post = new JSONObject();
        body_post.put("title", "title3");
        body_post.put("description", "description3");
        body_post.put("doneStatus", true);

        Response response_post = RestAssured.given()
                .body(body_post.toString())
                .post(url + "/todos");

        assertEquals(201, response_post.getStatusCode());

        int id_put = 3;
        JSONObject body = new JSONObject();
        body.put("title", "title new");


        Response response_put = RestAssured.given()
                                           .body(body.toString())
                                           .put(url + "/todos/" + id_put);


        int statusCode = response_put.getStatusCode();
        String actualTitle = response_put.getBody().jsonPath().getString("title");
        String description = response_put.getBody().jsonPath().getString("description");
        String doneStatus = response_put.getBody().jsonPath().getString("doneStatus");
        assertEquals(200, statusCode);
        assertEquals("title new", actualTitle);
        assertEquals("", description);
        assertEquals("false", doneStatus);
    }

    // Expected Result
    @Test
    public void putTodosWithOnlyTitleValidIDExpectedResult() throws InterruptedException {

        // We add a todo to better compare the results since the initial todos are set to the default values generated
        // by this bug.

        JSONObject body_post = new JSONObject();
        body_post.put("title", "title3");
        body_post.put("description", "description3");
        body_post.put("doneStatus", true);

        Response response = RestAssured.given()
                .body(body_post.toString())
                .post(url + "/todos");


        int id_put = 3;
        JSONObject body_put = new JSONObject();
        body_put.put("title", "new title");


        Response response_put = RestAssured.given()
                .body(body_put.toString())
                .put(url + "/todos/" + id_put);


        int statusCode = response_put.getStatusCode();
        String actualTitle = response_put.getBody().jsonPath().getString("title");
        String description = response_put.getBody().jsonPath().getString("description");
        String doneStatus = response_put.getBody().jsonPath().getString("doneStatus");
        assertEquals(200, statusCode);
        assertEquals("new title", actualTitle);
        assertEquals("description3", description);
        assertEquals("true", doneStatus);
    }
    // Delete with good id
    @Test
    public void deleteTodosWithGoodID(){
        int id = 2;
        Response response = RestAssured.given()
                                       .delete(url + "/todos/" +id);

        int statusCode = response.getStatusCode();
        assertEquals(200,statusCode);
    }

    // Delete with wrong id
    @Test
    public void DeleteNoMoreExistingTodo(){
        int id = 10000;
        Response response = RestAssured.given()
                                       .delete(url + "/todos/" + id);

        int statusCode = response.getStatusCode();
        assertEquals(404,statusCode);

    }

    // Test with malforamted JSON
    @Test
    public void malformatedJSON(){
        JSONObject body = new JSONObject();
        body.put("title1", "title2");
        body.put("description1", "description2");
        body.put("doneStatus1", false);

        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .post("http://localhost:4567/todos/1");


        int statusCode = response.getStatusCode();
        assertEquals(400,statusCode);
    }

    // Test with malformated XML
    @Test
	public void malformatedXML() throws Exception {

	    String invalidXmlPayload = "<todo><title>New Todo</title><doneStatus>false</description></todo>";


	    Response response = RestAssured.given()
                                       .header("Accept", ContentType.XML)
	                                   .contentType(ContentType.XML)
                                       .body(invalidXmlPayload)
                                       .when()
                                       .post("http://localhost:4567/todos");


	    assertEquals(400, response.getStatusCode(), "API should return 400 Bad Request for invalid XML");
	}

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
