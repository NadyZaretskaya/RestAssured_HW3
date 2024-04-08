package user;

import dto.*;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
import services.ServiceApi;

import java.io.IOException;

public class CreateUserTest {
  ServiceApi serviceApi = new ServiceApi();
  private int userId = 2603;
  private String firstName = "Nady";
  private String lastName = "Zaretskaya";
  private String userName = "Nady.zar";
  private String password = "qwerty";
  private int userStatus = 1987;
  private String email = "nady@mail.com";
  private String phone = "+375295556677";

  @Test
  public void createFullUser() {
    //create user with all valid fields
    UserDTO userDTO = UserDTO.builder()
        .id(userId)
        .firstName(firstName)
        .lastName(lastName)
        .username(userName)
        .password(password)
        .userStatus(userStatus)
        .email(email)
        .phone(phone)
        .build();
    serviceApi.createUser(userDTO);
    ValidatableResponse response = serviceApi.createUser(userDTO);
    UserResponseDTO actualData = response.extract().body().as(UserResponseDTO.class);
    Assertions.assertAll("Check create user",
        () -> Assertions.assertEquals(200, actualData.getCode(), "Incorrect code"),
        () -> Assertions.assertEquals(userDTO.getId().toString(), actualData.getMessage(), "Incorrect message"),
        () -> Assertions.assertEquals("unknown", actualData.getType(), "Incorrect userStatus")
    );

    //check that user fully created
    ValidatableResponse responseGet = serviceApi.getUserByName(userName);
    GetUserByNameResponseDTO getUserData = responseGet.extract().body().as(GetUserByNameResponseDTO.class);
    Assertions.assertAll("Check get user by name",
        () -> Assertions.assertEquals(userId, getUserData.getId(), "Incorrect id"),
        () -> Assertions.assertEquals(userName, getUserData.getUsername(), "Incorrect user name"),
        () -> Assertions.assertEquals(firstName, getUserData.getFirstName(), "Incorrect first name"),
        () -> Assertions.assertEquals(lastName, getUserData.getLastName(), "Incorrect last name"),
        () -> Assertions.assertEquals(email, getUserData.getEmail(), "Incorrect user email"),
        () -> Assertions.assertEquals(password, getUserData.getPassword(), "Incorrect user password"),
        () -> Assertions.assertEquals(phone, getUserData.getPhone(), "Incorrect user phone"),
        () -> Assertions.assertEquals(userStatus, getUserData.getUserStatus(), "Incorrect userStatus")
    );

    //delete user
    ValidatableResponse responseDelete = serviceApi.deleteUser(userName);
    DeleteUserResponseDTO deleteUserData = responseDelete.extract().body().as(DeleteUserResponseDTO.class);
    Assertions.assertAll("Check delete user",
        () -> Assertions.assertEquals(200, deleteUserData.getCode(), "Incorrect code"),
        () -> Assertions.assertEquals(userDTO.getUsername(), deleteUserData.getMessage(), "Incorrect message"),
        () -> Assertions.assertEquals("unknown", deleteUserData.getType(), "Incorrect userStatus")
    );

    //check user deleted
    ValidatableResponse responseGetDeleted = serviceApi.getUserByName404(userName);
    GetUserByNameResponse404DTO getUserDataDeleted = responseGetDeleted.extract().body().as(GetUserByNameResponse404DTO.class);
    Assertions.assertAll("Check create user",
        () -> Assertions.assertEquals(1, getUserDataDeleted.getCode(), "Incorrect code"),
        () -> Assertions.assertEquals("User not found", getUserDataDeleted.getMessage(), "Incorrect message"),
        () -> Assertions.assertEquals("error", getUserDataDeleted.getType(), "Incorrect userStatus")
    );

  }

  @Test
  public void createPartUser() {
    //create user with all valid fields
    UserDTO userDTO = UserDTO.builder()
        .id(userId)
        .firstName(firstName)
        .lastName(lastName)
        .username(userName)
        .build();
    serviceApi.createUser(userDTO);
    ValidatableResponse response = serviceApi.createUser(userDTO);
    UserResponseDTO actualData = response.extract().body().as(UserResponseDTO.class);
    Assertions.assertAll("Check create user",
        () -> Assertions.assertEquals(200, actualData.getCode(), "Incorrect code"),
        () -> Assertions.assertEquals(userDTO.getId().toString(), actualData.getMessage(), "Incorrect message"),
        () -> Assertions.assertEquals("unknown", actualData.getType(), "Incorrect userStatus")
    );

    //check that user fully created
    ValidatableResponse responseGet = serviceApi.getUserByName(userName);
    GetUserByNameResponseDTO getUserData = responseGet.extract().body().as(GetUserByNameResponseDTO.class);
    Assertions.assertAll("Check get user by name",
        () -> Assertions.assertEquals(userId, getUserData.getId(), "Incorrect id"),
        () -> Assertions.assertEquals(userName, getUserData.getUsername(), "Incorrect user name"),
        () -> Assertions.assertEquals(firstName, getUserData.getFirstName(), "Incorrect first name"),
        () -> Assertions.assertEquals(lastName, getUserData.getLastName(), "Incorrect last name"),
        () -> Assertions.assertEquals(0, getUserData.getUserStatus(), "Incorrect userStatus")
    );

    //delete user
    ValidatableResponse responseDelete = serviceApi.deleteUser(userName);
    DeleteUserResponseDTO deleteUserData = responseDelete.extract().body().as(DeleteUserResponseDTO.class);
    Assertions.assertAll("Check delete user",
        () -> Assertions.assertEquals(200, deleteUserData.getCode(), "Incorrect code"),
        () -> Assertions.assertEquals(userDTO.getUsername(), deleteUserData.getMessage(), "Incorrect message"),
        () -> Assertions.assertEquals("unknown", deleteUserData.getType(), "Incorrect userStatus")
    );

    //check user deleted
    ValidatableResponse responseGetDeleted = serviceApi.getUserByName404(userName);
    GetUserByNameResponse404DTO getUserDataDeleted = responseGetDeleted.extract().body().as(GetUserByNameResponse404DTO.class);
    Assertions.assertAll("Check create user",
        () -> Assertions.assertEquals(1, getUserDataDeleted.getCode(), "Incorrect code"),
        () -> Assertions.assertEquals("User not found", getUserDataDeleted.getMessage(), "Incorrect message"),
        () -> Assertions.assertEquals("error", getUserDataDeleted.getType(), "Incorrect userStatus")
    );

  }

  @Test
  public void createUserWithPut() {

    //check user doesn't exist
    ValidatableResponse responseGetDeleted = serviceApi.getUserByName404(userName);
    GetUserByNameResponse404DTO getUserDataDeleted = responseGetDeleted.extract().body().as(GetUserByNameResponse404DTO.class);
    Assertions.assertAll("Check create user",
        () -> Assertions.assertEquals(1, getUserDataDeleted.getCode(), "Incorrect code"),
        () -> Assertions.assertEquals("User not found", getUserDataDeleted.getMessage(), "Incorrect message"),
        () -> Assertions.assertEquals("error", getUserDataDeleted.getType(), "Incorrect userStatus")
    );

    //Create user with put method
    UpdateUserDTO updateUser = UpdateUserDTO.builder()
        .id(123456)
        .username(userName)
        .firstName("First")
        .lastName("Last")
        .email("")
        .password("")
        .phone("")
        .build();

    //Check that request success
    ValidatableResponse responseUpdateUser = serviceApi.putUser(updateUser, userName);
    UpdateUserResponseDTO actualResponse = responseUpdateUser.extract().body().as(UpdateUserResponseDTO.class);
    Assertions.assertAll("Check user updated",
        () -> Assertions.assertEquals(200, actualResponse.getCode(), "Incorrect http code"),
        () -> Assertions.assertEquals("unknown", actualResponse.getType(), "Incorrect message type"),
        () -> Assertions.assertEquals("123456", actualResponse.getMessage(), "Incorrect message type")
    );

    //Validate that user created with put method
    ValidatableResponse getResponse = serviceApi.getUserByName(userName);
    GetUserByNameResponseDTO getUpdatedData = getResponse.extract().body().as(GetUserByNameResponseDTO.class);
    Assertions.assertAll("Check fields added to user data",
        () -> Assertions.assertEquals(123456, getUpdatedData.getId(), "Incorrect id"),
        () -> Assertions.assertEquals(userName, getUpdatedData.getUsername(), "Incorrect user name"),
        () -> Assertions.assertEquals("First", getUpdatedData.getFirstName(), "Incorrect first name"),
        () -> Assertions.assertEquals("Last", getUpdatedData.getLastName(), "Incorrect last name"),
        () -> Assertions.assertEquals("", getUpdatedData.getEmail(), "Incorrect user email"),
        () -> Assertions.assertEquals("", getUpdatedData.getPassword(), "Incorrect user password"),
        () -> Assertions.assertEquals("", getUpdatedData.getPhone(), "Incorrect user phone"),
        () -> Assertions.assertEquals(0, getUpdatedData.getUserStatus(), "Incorrect user phone")
    );

    //delete user
    serviceApi.deleteUser(userName);

  }

}
