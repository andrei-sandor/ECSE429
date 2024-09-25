import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.Random.class)
public class testsTodo {
    public static Process process;
    public String url = "http://localhost:4567";

    @BeforeAll
    public static void setUp() throws Exception {
        try {
            process = Runtime.getRuntime().exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
//            Thread.sleep(100);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @AfterAll
    public static void setDown() throws InterruptedException {
        process.destroy();
//        Thread.sleep(100);
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
        String doneStatus = response.getBody().jsonPath().getString("doneStatus");
        assertEquals(201, statusCode);
        assertEquals("title2", actualTitle);
        assertEquals("true", doneStatus);

    }

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
        assertEquals(201, statusCode);
        assertEquals("title2", actualTitle);
        assertEquals("description2", actualDescription);

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

    ///////////////////////////////////// /todos/id /////////////////////////
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
        assertEquals("title2", actualTitle);
        assertEquals("false", actualStatus);
        assertEquals("description2",actualDescription );

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

    @Test
    public void postTodosWithOnlyDoneStatusValidID() {
        int id = 1;
        JSONObject body = new JSONObject();
        body.put("doneStatus", true);


        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .post(url + "/todos/"+ id);


        int statusCode = response.getStatusCode();
        String actualDescription = response.getBody().jsonPath().getString("doneStatus");
        assertEquals(200, statusCode);
        assertEquals("true", actualDescription);

    }

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

    // Bug
    @Test
    public void putTodosWithMissingTitleValidID() {
        int id = 1;
        JSONObject body = new JSONObject();
        body.put("description", "description5");
        body.put("doneStatus", false);


        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .put(url+ "/todos/" + id);


        int statusCode = response.getStatusCode();
        assertEquals(400, statusCode);

    }

    @Test
    public void putTodosWithMissingTitleValidIDValidScenario() {
        int id = 1;
        JSONObject body = new JSONObject();
        body.put("description", "description5");
        body.put("doneStatus", false);


        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .put(url+ "/todos/" + id);


        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

    }

    @Test
    public void putTodosWithOnlyTitleValidID() {
        int id = 1;
        JSONObject body = new JSONObject();
        body.put("title", "title2");


        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .put(url + "/todos/" + id);


        int statusCode = response.getStatusCode();
        String actualTitle = response.getBody().jsonPath().getString("title");
        assertEquals(200, statusCode);
        assertEquals("title2", actualTitle);


    }


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

    @Test
    public void deleteTodosWithGoodID(){
        int id = 2;
        Response response = RestAssured.given()
                                       .delete(url + "/todos/" +id);

        int statusCode = response.getStatusCode();
        assertEquals(200,statusCode);
    }

    @Test
    public void DeleteNoMoreExistingTodo(){
        int id = 10000;
        Response response = RestAssured.given()
                                       .delete(url + "/todos/" + id);

        int statusCode = response.getStatusCode();
        assertEquals(404,statusCode);

    }


//    ///////////////////////////////////// /todos/id/categories /////////////////////////
    @Test
    public void getCategoriesTodosWithValidID() {
        String id = "1";
        Response response = RestAssured.get(url + "/todos/" + id + "/categories");

        assertEquals(200, response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("categories[0].id");
        String actualTitle = response.getBody().jsonPath().getString("categories[0].title");
        String actualDescription = response.getBody().jsonPath().getString("categories[0].description");

        assertEquals(id, actualId);
        assertEquals("Office", actualTitle);
        assertEquals("", actualDescription);
    }

    // Bug
    @Test
    public void getCategoriesTodosWithInalidID() {
        String id = "100";
        Response response = RestAssured.get(url + "/todos/" + id + "/categories");

        assertEquals(200, response.getStatusCode());

    }
    @Test
    public void headCategoriesTodosWithValidID(){
        int id = 1;
        Response response = RestAssured.given()
                                       .head(url + "/todos/" + id + "/categories");

        assertEquals(200,response.getStatusCode());
    }

    @Test
    public void headCategoriesTodosWithInvalidID(){
        int id = 100;
        Response response = RestAssured.given()
                                       .head(url + "/todos/" + id + "/categories");

        assertEquals(200,response.getStatusCode());
    }


    @Test
    public void postCategoriesWithValidJSONInputsValidID() {
        int id = 1;
        JSONObject body = new JSONObject();
        body.put("title", "title2");

        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .post(url + "/todos/" + id + "/categories");


        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);

//        String actualId = response.getBody().jsonPath().getString("categories[0].id");
//        String actualTitle = response.getBody().jsonPath().getString("categories[0].title");
//        String actualDescription = response.getBody().jsonPath().getString("categories[0].description");
//
//        assertEquals(id, actualId);
////        assertEquals("title2", actualTitle);
////        assertEquals("", actualDescription);


    }

    @Test
    public void postCategoriesWithValidJSONInputsInvalidID() {
        int id = 10;
        JSONObject body = new JSONObject();
        body.put("title", "title2");

        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .post(url + "/todos/" + id + "/categories");


        int statusCode = response.getStatusCode();
        assertEquals(404, statusCode);
    }

    @Test
    public void deleteCategoriesWithGoodID(){
        int idTask = 1;
        int idCategories = 2;
        Response response = RestAssured.given()
                                  .delete(url + "/todos/" +idTask + "/categories/" + idCategories);

        int statusCode = response.getStatusCode();
        assertEquals(404,statusCode);
    }

    @Test
    public void deleteCategoriesWithInvalidID(){
        int idTask = 1;
        int idCategories = 20;
        Response response = RestAssured.given()
                                       .delete(url + "/todos/" +idTask + "/categories/" + idCategories);

        int statusCode = response.getStatusCode();
        assertEquals(404,statusCode);
    }

    /////////////////////////////////// /todos/id/tasksof /////////////////////////
    @Test
    public void getTasksOfTodosWithValidID() {
        String id = "1";
        Response response = RestAssured.given()
                                       .get(url + "/todos/" + id +"/tasksof");

        assertEquals(200, response.getStatusCode());

        String actualID = response.getBody().jsonPath().getString("projects[0].id");
        String actualTitle = response.getBody().jsonPath().getString("projects[0].title");
        String actualDescription = response.getBody().jsonPath().getString("projects[0].description");
        String actualActive = response.getBody().jsonPath().getString("projects[0].active");
        assertEquals(id, actualID);
        assertEquals("Office Work", actualTitle);
        assertEquals("", actualDescription);
        assertEquals("false", actualActive);

    }

    @Test
    public void getTasksOfTodosWithVInvalidID() {
        String id = "1";
        Response response = RestAssured.given()
                                       .get(url + "/todos/" + id +"/tasksof");

        assertEquals(200, response.getStatusCode());

        String actualID = response.getBody().jsonPath().getString("projects[0].id");
        String actualTitle = response.getBody().jsonPath().getString("projects[0].title");
        String actualDescription = response.getBody().jsonPath().getString("projects[0].description");
        String actualActive = response.getBody().jsonPath().getString("projects[0].active");
        assertEquals(id, actualID);
        assertEquals("Office Work", actualTitle);
        assertEquals("", actualDescription);
        assertEquals("false", actualActive);

    }

    @Test
    public void headTasksTodosWithValidID(){
        int id = 1;
        Response response = RestAssured.given()
                                       .head(url + "/todos/"+ id + "/categories");

        assertEquals(200,response.getStatusCode());
    }

    @Test
    public void headTasksTodosWithInvalidID(){
        int id = 1000;
        Response response = RestAssured.given()
                                       .head(url + "/todos/"+ id + "/categories");

        assertEquals(200,response.getStatusCode());
    }



    @Test
    public void postTasksOfWithValidJSONInputsValidID() {
        int id = 1;
        JSONObject body = new JSONObject();
        body.put("title", "title2");

        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .post(url + "/todos/" + id + "/tasksof");


        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
    }

    @Test
    public void postTasksOfWithValidJSONInputsInalidID() {
        int id = 100;
        JSONObject body = new JSONObject();
        body.put("title", "title2");

        Response response = RestAssured.given()
                                       .body(body.toString())
                                       .post(url + "/todos/" + id + "/tasksof");


        int statusCode = response.getStatusCode();
        assertEquals(404, statusCode);

    }

    @Test
    public void deleteTasksofWithGoodID(){
        int idTask = 1;
        int idTasks = 2;
        Response response = RestAssured.given()
                                  .delete(url + "/todos/" +idTask + "/tasksof/" + idTasks);

        int statusCode = response.getStatusCode();
        assertEquals(404,statusCode);
    }

    @Test
    public void deleteTasksofWithInvalidID(){
        int idTask = 1;
        int idTasks = 10;
        Response response = RestAssured.given()
                                       .delete(url + "/todos/" +idTask + "/tasksof/" + idTasks);

        int statusCode = response.getStatusCode();
        assertEquals(404,statusCode);
    }

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
