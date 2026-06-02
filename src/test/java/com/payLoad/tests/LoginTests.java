package com.payLoad.tests;

import com.payLoad.core.TestBase;
import com.playLoad.dto.login.LoginRequestDto;
import com.playLoad.dto.login.LoginResponseDto;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class LoginTests extends TestBase {
    LoginRequestDto requestDto = LoginRequestDto.of(EMAIL, PASSWORD);

    @Test
    public void loginSuccessTest() {
        LoginResponseDto loginResponseDto = given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .post(LOGIN_PATH)
                .then()
                .statusCode(201)
                .extract().response().as(LoginResponseDto.class);
        System.out.println(loginResponseDto.accessToken());
    }

    @Test
    public void loginWithWrongPasswordTest() {

        given()
                .contentType(ContentType.JSON)
                .body(LoginRequestDto.of(EMAIL, "wrongPassword"))
                .when()
                .post(LOGIN_PATH)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message",
                        containsString("Invalid password"));
    }

    @Test
    public void loginWithNonExistingEmailTest(){

        given()
                .contentType(ContentType.JSON)
                .body(LoginRequestDto.of(
                        "unknown" + System.currentTimeMillis() + "@gmail.com",
                        PASSWORD))
                .when()
                .post(LOGIN_PATH)
                .then()
                .assertThat().statusCode(404)
                .assertThat().body("message",
                        containsString("No user found for email"));
    }

    @Test
    public void loginWithEmptyEmailTest(){

        given()
                .contentType(ContentType.JSON)
                .body(LoginRequestDto.of("", PASSWORD))
                .when()
                .post(LOGIN_PATH)
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",
                        equalTo("Bad Request"));
    }

    @Test
    public void loginWithEmptyPasswordTest(){

        given()
                .contentType(ContentType.JSON)
                .body(LoginRequestDto.of(EMAIL, ""))
                .when()
                .post(LOGIN_PATH)
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",
                        equalTo("Bad Request"));
    }

    @Test
    public void loginWithNullEmailTest(){

        given()
                .contentType(ContentType.JSON)
                .body(LoginRequestDto.of(null, PASSWORD))
                .when()
                .post(LOGIN_PATH)
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",
                        equalTo("Bad Request"));
    }
}