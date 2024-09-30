import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Random.class)
public class TestsProject {
    public static Process jar;
    public String url = "http://localhost:4567";

    @BeforeEach
    public void setUp() throws Exception {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "runTodoManagerRestAPI-1.5.5.jar");
            jar = processBuilder.start();
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
    // Start of unit test related to /projects
    //

    @Test
    public void getProjects() {
        Response response = RestAssured.given()
                .get(url + "/projects");

        assertEquals(200, response.getStatusCode());

        String initial_projects_size = response.getBody().jsonPath().getString("projects.size()");
        assertEquals("1",initial_projects_size);
    }

    @Test
    public void headProjects() {
        Response response = RestAssured.given()
                .head(url + "/projects");

        assertEquals(200,response.getStatusCode());
    }

    @Test
    public void postProjectsWithValidJSONInputs() {
        JSONObject body = new JSONObject();
        body.put("title", "title1");
        body.put("completed", false);
        body.put("active", false);
        body.put("description", "description1");

        Response response = RestAssured.given()
                .body(body.toString())
                .post("http://localhost:4567/projects");

        assertEquals(201, response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");

        assertEquals("2", actualId);
        assertEquals("title1", actualTitle);
        assertEquals("false", actualCompleted);
        assertEquals("false", actualActive);
        assertEquals("description1", actualDescription);
    }

    @Test
    public void postProjectsNoTitle() {
        JSONObject body = new JSONObject();
        body.put("completed", false);
        body.put("active", false);
        body.put("description", "description1");

        Response response = RestAssured.given()
                .body(body.toString())
                .post("http://localhost:4567/projects");

        assertEquals(201, response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");
        String actualTitle = response.getBody().jsonPath().getString("title");

        assertEquals("2", actualId);
        assertEquals("", actualTitle);
        assertEquals("false", actualCompleted);
        assertEquals("false", actualActive);
        assertEquals("description1", actualDescription);
    }

    @Test
    public void postProjectsNoCompleted() {
        JSONObject body = new JSONObject();
        body.put("title", "title1");
        body.put("active", false);
        body.put("description", "description1");

        Response response = RestAssured.given()
                .body(body.toString())
                .post("http://localhost:4567/projects");

        assertEquals(201, response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");

        assertEquals("2", actualId);
        assertEquals("title1", actualTitle);
        assertEquals("false", actualCompleted);
        assertEquals("false", actualActive);
        assertEquals("description1", actualDescription);
    }

    @Test
    public void postProjectsNoActive() {
        JSONObject body = new JSONObject();
        body.put("title", "title1");
        body.put("completed", false);
        body.put("description", "description1");

        Response response = RestAssured.given()
                .body(body.toString())
                .post("http://localhost:4567/projects");

        assertEquals(201, response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");

        assertEquals("2", actualId);
        assertEquals("title1", actualTitle);
        assertEquals("false", actualCompleted);
        assertEquals("false", actualActive);
        assertEquals("description1", actualDescription);
    }

    @Test
    public void postProjectsNoDescription() {
        JSONObject body = new JSONObject();
        body.put("title", "title1");
        body.put("completed", false);
        body.put("active", false);

        Response response = RestAssured.given()
                .body(body.toString())
                .post("http://localhost:4567/projects");

        assertEquals(201, response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");

        assertEquals("2", actualId);
        assertEquals("title1", actualTitle);
        assertEquals("false", actualCompleted);
        assertEquals("false", actualActive);
        assertEquals("", actualDescription);
    }

    @Test
    public void postProjectsNoFieldInputs() {
        JSONObject body = new JSONObject();

        Response response = RestAssured.given()
                .body(body.toString())
                .post("http://localhost:4567/projects");

        assertEquals(201, response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");

        assertEquals("2", actualId);
        assertEquals("", actualTitle);
        assertEquals("false", actualCompleted);
        assertEquals("false", actualActive);
        assertEquals("", actualDescription);
    }

    @Test
    public void postProjectsIncludingExistentId() {
        JSONObject body = new JSONObject();
        body.put("id", 1);

        Response response = RestAssured.given()
                .body(body.toString())
                .post("http://localhost:4567/projects");

        assertEquals(400, response.getStatusCode());
    }

    @Test
    public void postProjectsIncludingNonExistentId() {
        JSONObject body = new JSONObject();
        body.put("id", 2);

        Response response = RestAssured.given()
                .body(body.toString())
                .post("http://localhost:4567/projects");

        assertEquals(400, response.getStatusCode());
    }

    @Test
    public void postProjectsNonExistentField() {
        JSONObject body = new JSONObject();
        body.put("nonExistentField", "noField");

        Response response = RestAssured.given()
                .body(body.toString())
                .post("http://localhost:4567/projects");

        assertEquals(400, response.getStatusCode());
    }

    ///////////////////////////////////// /projects/id /////////////////////////

    @Test
    public void getProjectsWithValidId() {
        int id = 1;
        Response response = RestAssured.given()
                .get(url + "/projects/" + id);

        assertEquals(200, response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("projects[0].id");
        String actualTitle = response.getBody().jsonPath().getString("projects[0].title");
        String actualCompleted = response.getBody().jsonPath().getString("projects[0].completed");
        String actualActive = response.getBody().jsonPath().getString("projects[0].active");
        String actualDescription = response.getBody().jsonPath().getString("projects[0].description");

        assertEquals("1",actualId);
        assertEquals("Office Work", actualTitle);
        assertEquals("false", actualCompleted);
        assertEquals("false", actualActive);
        assertEquals("",actualDescription );
    }

    @Test
    public void getProjectsWithInvalidId() {
        int id = 10000;
        Response response = RestAssured.given()
                .get(url + "/projects/" + id);

        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void headProjectsWithValidId() {
        int id = 1;
        Response response = RestAssured.given()
                .head(url + "/projects/" + id);

        assertEquals(200,response.getStatusCode());
    }

    @Test
    public void headProjectsWithInvalidId() {
        int id = 1000;
        Response response = RestAssured.given()
                .head(url + "/projects/" + id);

        assertEquals(404,response.getStatusCode());
    }

    @Test
    public void postProjectsWithValidJSONInputsValidId() {
        int id = 1;

        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("completed", false);
        body.put("active", false);
        body.put("description", "description2");

        Response response = RestAssured.given()
                .body(body.toString())
                .post(url + "/projects/" + id);

        assertEquals(200,response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");

        assertEquals("1", actualId);
        assertEquals("title2", actualTitle);
        assertEquals("false", actualCompleted);
        assertEquals("false", actualActive);
        assertEquals("description2", actualDescription);
    }

    @Test
    public void postProjectsWithValidJSONInputsInvalidId() {
        int id = 10000;

        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("completed", false);
        body.put("active", false);
        body.put("description", "description2");

        Response response = RestAssured.given()
                .body(body.toString())
                .post(url + "/projects/" + id);

        assertEquals(404,response.getStatusCode());
    }

    @Test
    public void postProjectsWithOnlyTitleValidId() {
        int id = 1;

        JSONObject body = new JSONObject();
        body.put("title", "title2");

        Response response = RestAssured.given()
                .body(body.toString())
                .post(url + "/projects/" + id);

        assertEquals(200,response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");

        assertEquals("1", actualId);
        assertEquals("title2", actualTitle);
        assertEquals("false", actualCompleted);
        assertEquals("false", actualActive);
        assertEquals("", actualDescription);
    }

    @Test
    public void postProjectsWithOnlyCompletedValidId() {
        int id = 1;

        JSONObject body = new JSONObject();
        body.put("completed", true);

        Response response = RestAssured.given()
                .body(body.toString())
                .post(url + "/projects/" + id);

        assertEquals(200,response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");

        assertEquals("1", actualId);
        assertEquals("Office Work", actualTitle);
        assertEquals("true", actualCompleted);
        assertEquals("false", actualActive);
        assertEquals("", actualDescription);
    }

    @Test
    public void postProjectsWithOnlyActiveValidId() {
        int id = 1;

        JSONObject body = new JSONObject();
        body.put("active", true);

        Response response = RestAssured.given()
                .body(body.toString())
                .post(url + "/projects/" + id);

        assertEquals(200,response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");

        assertEquals("1", actualId);
        assertEquals("Office Work", actualTitle);
        assertEquals("false", actualCompleted);
        assertEquals("true", actualActive);
        assertEquals("", actualDescription);
    }

    @Test
    public void postProjectsWithOnlyDescriptionValidId() {
        int id = 1;

        JSONObject body = new JSONObject();
        body.put("description", "description2");

        Response response = RestAssured.given()
                .body(body.toString())
                .post(url + "/projects/" + id);

        assertEquals(200,response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");

        assertEquals("1", actualId);
        assertEquals("Office Work", actualTitle);
        assertEquals("false", actualCompleted);
        assertEquals("false", actualActive);
        assertEquals("description2", actualDescription);
    }

    @Test
    public void postProjectsWithNoFieldValidId() {
        int id = 1;

        JSONObject body = new JSONObject();

        Response response = RestAssured.given()
                .body(body.toString())
                .post(url + "/projects/" + id);

        assertEquals(200,response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");

        assertEquals("1", actualId);
        assertEquals("Office Work", actualTitle);
        assertEquals("false", actualCompleted);
        assertEquals("false", actualActive);
        assertEquals("", actualDescription);
    }

    @Test
    public void putProjectsWithCorrectJSONValidId() {
        int id = 1;

        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("completed", true);
        body.put("active", true);
        body.put("description", "description2");

        Response response = RestAssured.given()
                .body(body.toString())
                .put(url + "/projects/" + id);

        assertEquals(200,response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");

        assertEquals("1", actualId);
        assertEquals("title2", actualTitle);
        assertEquals("true", actualCompleted);
        assertEquals("true", actualActive);
        assertEquals("description2", actualDescription);
    }

    @Test
    public void putProjectsWithCorrectJSONInvalidId() {
        int id = 10000;

        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("completed", false);
        body.put("active", false);
        body.put("description", "description2");

        Response response = RestAssured.given()
                .body(body.toString())
                .put(url + "/projects/" + id);

        assertEquals(404,response.getStatusCode());
    }

    // Bug
    @Test
    public void putProjectsWithMissingTitleValidId() {
        int id = 1;

        JSONObject body = new JSONObject();
        body.put("completed", false);
        body.put("active", false);
        body.put("description", "description2");

        Response response = RestAssured.given()
                .body(body.toString())
                .put(url + "/projects/" + id);

        assertEquals(200,response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");

        assertEquals("1", actualId);
        assertEquals("", actualTitle);
        assertEquals("false", actualCompleted);
        assertEquals("false", actualActive);
        assertEquals("description2", actualDescription);
    }

    @Test
    public void putProjectsWithMissingCompletedValidId() {
        int id = 1;

        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("active", true);
        body.put("description", "description2");

        Response response = RestAssured.given()
                .body(body.toString())
                .put(url + "/projects/" + id);

        assertEquals(200,response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");

        assertEquals("1", actualId);
        assertEquals("title2", actualTitle);
        assertEquals("false", actualCompleted);
        assertEquals("true", actualActive);
        assertEquals("description2", actualDescription);
    }

    @Test
    public void putProjectsWithMissingActiveValidId() {
        int id = 1;

        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("active", true);
        body.put("description", "description2");

        Response response = RestAssured.given()
                .body(body.toString())
                .put(url + "/projects/" + id);

        assertEquals(200,response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");

        assertEquals("1", actualId);
        assertEquals("title2", actualTitle);
        assertEquals("false", actualCompleted);
        assertEquals("true", actualActive);
        assertEquals("description2", actualDescription);
    }

    @Test
    public void putProjectsWithMissingDescriptionValidId() {
        int id = 1;

        JSONObject body = new JSONObject();
        body.put("title", "title2");
        body.put("completed", true);
        body.put("active", true);

        Response response = RestAssured.given()
                .body(body.toString())
                .put(url + "/projects/" + id);

        assertEquals(200,response.getStatusCode());

        String actualId = response.getBody().jsonPath().getString("id");
        String actualTitle = response.getBody().jsonPath().getString("title");
        String actualCompleted = response.getBody().jsonPath().getString("completed");
        String actualActive = response.getBody().jsonPath().getString("active");
        String actualDescription = response.getBody().jsonPath().getString("description");

        assertEquals("1", actualId);
        assertEquals("title2", actualTitle);
        assertEquals("true", actualCompleted);
        assertEquals("true", actualActive);
        assertEquals("", actualDescription);
    }

    @Test
    public void deleteProjectsWithValidId(){
        int id = 1;
        Response response = RestAssured.given()
                .delete(url + "/projects/" + id);

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void deleteProjectsWithInvalidId(){
        int id = 10000;
        Response response = RestAssured.given()
                .delete(url + "/projects/" + id);

        assertEquals(404, response.getStatusCode());
    }

    ///////////////////////////////////// /projects/id/tasks /////////////////////////

    @Test
    public void getProjectsTasksWithValidId() {
        int id = 1;
        Response response = RestAssured.given()
                .get(url + "/projects/" + id + "/tasks");

        assertEquals(200, response.getStatusCode());

        String actualTodoId1 = response.getBody().jsonPath().getString("todos[0].id");
        String actualTodoTitle1 = response.getBody().jsonPath().getString("todos[0].title");
        String actualTodoDoneStatus1 = response.getBody().jsonPath().getString("todos[0].doneStatus");
        String actualTodoDescription1 = response.getBody().jsonPath().getString("todos[0].description");
        String actualTaskOfId1 = response.getBody().jsonPath().getString("todos[0].tasksof[0].id");

        String actualTodoId2 = response.getBody().jsonPath().getString("todos[1].id");
        String actualTodoTitle2 = response.getBody().jsonPath().getString("todos[1].title");
        String actualTodoDoneStatus2 = response.getBody().jsonPath().getString("todos[1].doneStatus");
        String actualTodoDescription2 = response.getBody().jsonPath().getString("todos[1].description");
        String actualTaskOfId2 = response.getBody().jsonPath().getString("todos[1].tasksof[0].id");

        assertEquals("1",actualTodoId1);
        assertEquals("scan paperwork", actualTodoTitle1);
        assertEquals("false", actualTodoDoneStatus1);
        assertEquals("", actualTodoDescription1);
        assertEquals("1",actualTaskOfId1);

        assertEquals("2",actualTodoId2);
        assertEquals("file paperwork", actualTodoTitle2);
        assertEquals("false", actualTodoDoneStatus2);
        assertEquals("", actualTodoDescription2);
        assertEquals("1",actualTaskOfId2);
    }

    // Bug
    @Test
    public void getProjectsTasksWithInvalidId() {
        int id = 10000;
        Response response = RestAssured.given()
                .get(url + "/projects/" + id + "/tasks");

        assertEquals(200, response.getStatusCode());

        String actualTodoId1 = response.getBody().jsonPath().getString("todos[0].id");
        String actualTodoTitle1 = response.getBody().jsonPath().getString("todos[0].title");
        String actualTodoDoneStatus1 = response.getBody().jsonPath().getString("todos[0].doneStatus");
        String actualTodoDescription1 = response.getBody().jsonPath().getString("todos[0].description");
        String actualTaskOfId1 = response.getBody().jsonPath().getString("todos[0].tasksof[0].id");

        String actualTodoId2 = response.getBody().jsonPath().getString("todos[1].id");
        String actualTodoTitle2 = response.getBody().jsonPath().getString("todos[1].title");
        String actualTodoDoneStatus2 = response.getBody().jsonPath().getString("todos[1].doneStatus");
        String actualTodoDescription2 = response.getBody().jsonPath().getString("todos[1].description");
        String actualTaskOfId2 = response.getBody().jsonPath().getString("todos[1].tasksof[0].id");

        assertEquals("1",actualTodoId1);
        assertEquals("scan paperwork", actualTodoTitle1);
        assertEquals("false", actualTodoDoneStatus1);
        assertEquals("", actualTodoDescription1);
        assertEquals("1",actualTaskOfId1);

        assertEquals("2",actualTodoId2);
        assertEquals("file paperwork", actualTodoTitle2);
        assertEquals("false", actualTodoDoneStatus2);
        assertEquals("", actualTodoDescription2);
        assertEquals("1",actualTaskOfId2);
    }
}
