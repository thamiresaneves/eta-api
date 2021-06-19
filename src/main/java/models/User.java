package models;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import io.restassured.path.json.JsonPath;


import static io.restassured.RestAssured.*;

public class User {

    public String name;
    public String email;
    public String password;
    public String isAdmin;
    public String authToken;
    public String userID;

    public User(String name, String email, String password, String isAdmin) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public void setUserAuthToken(String authToken){
        this.authToken = authToken;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserCredentials() {
        JSONObject userJsonRepresentation = new JSONObject();
        userJsonRepresentation.put("email", this.email);
        userJsonRepresentation.put("password", this.password);

        return userJsonRepresentation.toJSONString();
    }

    public Response authenticateUser() {
        Response loginResponse =
                given().
                        header("accepct", "application/json").
                        header("Content-Type", "application/json").
                and().
                        body(getUserCredentials()).
                when().
                        post("http://localhost:3000/login");

        JsonPath jsonPathEvaluator = loginResponse.jsonPath();
        String authToken = jsonPathEvaluator.get("authorization");
        setUserAuthToken(authToken);

        return loginResponse;
    }

    public Response registerUserRequest() {

        JSONObject userJsonRepresentation = new JSONObject();
        userJsonRepresentation.put("nome", this.name);
        userJsonRepresentation.put("email", this.email);
        userJsonRepresentation.put("password", this.password);
        userJsonRepresentation.put("administrador", this.isAdmin);

        Response registerUserResponse =
                given().
                        header("accept", "application/json").
                        header("Content-Type", "application/json").
                        and().
                        body(userJsonRepresentation.toJSONString()).
                        when().
                        post("http://localhost:3000/usuarios");

        JsonPath jsonPathEvaluator = registerUserResponse.jsonPath();
        setUserID(jsonPathEvaluator.get("_id"));

        return registerUserResponse;
    }

    public Response deleteUserRequest(RequestSpecification spec) {
        Response deleteUserResponse =
                given().
                        spec(spec).
                        header("Content-Type", "application/json").
                when().
                        delete("usuarios/" + this.userID);

        return deleteUserResponse;
    }

}
