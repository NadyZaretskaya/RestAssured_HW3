package user;

import dto.*;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
import services.UserServiceApi;

public class UpdateUserTest {

  UserServiceApi userApi = new UserServiceApi();

  private Integer userId = 32;
  private String userName = "Nady.Z";
  private String firstName = "Nady";
  private String lastName = "Zar";
  private Integer userStatus = 1988;
  private String email = "nady.z@mail.com";
  private String phone = "+375292223344";
  private String password = "123Qwerty";

  @Test
  public void updateUserWithNewFields() {

    //create user
    UserDTO userDTO = UserDTO.builder()
        .id(userId)
        .userStatus(userStatus)
        .username(userName)
        .build();
    userApi.createUser(userDTO);

    //Update user - add email, password and phone
    ValidatableResponse response = userApi.getUserByName(userName);
    GetUserByNameResponseDTO actualData = response.extract().body().as(GetUserByNameResponseDTO.class);

    UpdateUserDTO updateUser = UpdateUserDTO.builder()
        .id(actualData.getId())
        .username(userName)
        .firstName(firstName)
        .lastName(lastName)
        .email(email)
        .password(password)
        .phone(phone)
        .userStatus(actualData.getUserStatus())
        .build();

    //Check that user updated
    ValidatableResponse responseUpdateUser = userApi.putUser(updateUser, userName);
    UpdateUserResponseDTO actualResponse = responseUpdateUser.extract().body().as(UpdateUserResponseDTO.class);
    Assertions.assertAll("Check user updated",
        () -> Assertions.assertEquals(200, actualResponse.getCode(), "Incorrect http code"),
        () -> Assertions.assertEquals("unknown", actualResponse.getType(), "Incorrect message type"),
        () -> Assertions.assertEquals(updateUser.getId().toString(), actualResponse.getMessage(), "Incorrect message type")
    );

    //Validate that fields added (email, password, phone)
    ValidatableResponse getResponse = userApi.getUserByName(userName);
    GetUserByNameResponseDTO getUpdatedData = getResponse.extract().body().as(GetUserByNameResponseDTO.class);
    Assertions.assertAll("Check fields added to user data",
        () -> Assertions.assertEquals(userId, getUpdatedData.getId(), "Incorrect id"),
        () -> Assertions.assertEquals(userName, getUpdatedData.getUsername(), "Incorrect user name"),
        () -> Assertions.assertEquals(firstName, getUpdatedData.getFirstName(), "Incorrect first name"),
        () -> Assertions.assertEquals(lastName, getUpdatedData.getLastName(), "Incorrect last name"),
        () -> Assertions.assertEquals(email, getUpdatedData.getEmail(), "Incorrect user email"),
        () -> Assertions.assertEquals(password, getUpdatedData.getPassword(), "Incorrect user password"),
        () -> Assertions.assertEquals(phone, getUpdatedData.getPhone(), "Incorrect user phone")
    );

    deleteUser(userName);
  }

  @Test
  public void updateUserClearExistedFields() {

    //create user
    UserDTO userDTO = UserDTO.builder()
        .id(userId)
        .userStatus(userStatus)
        .username(userName)
        .firstName(firstName)
        .lastName(lastName)
        .email(email)
        .password(password)
        .phone(phone)
        .build();
    userApi.createUser(userDTO);

    //Update user - clear first name, last name, email, password and phone
    ValidatableResponse response = userApi.getUserByName(userName);
    GetUserByNameResponseDTO actualData = response.extract().body().as(GetUserByNameResponseDTO.class);

    UpdateUserDTO updateUser = UpdateUserDTO.builder()
        .id(actualData.getId())
        .username(userName)
        .firstName("")
        .lastName("")
        .email("")
        .password("")
        .phone("")
        .userStatus(actualData.getUserStatus())
        .build();

    //Check that user updated
    ValidatableResponse responseUpdateUser = userApi.putUser(updateUser, userName);
    UpdateUserResponseDTO actualResponse = responseUpdateUser.extract().body().as(UpdateUserResponseDTO.class);
    Assertions.assertAll("Check user updated",
        () -> Assertions.assertEquals(200, actualResponse.getCode(), "Incorrect http code"),
        () -> Assertions.assertEquals("unknown", actualResponse.getType(), "Incorrect message type"),
        () -> Assertions.assertEquals(updateUser.getId().toString(), actualResponse.getMessage(), "Incorrect message type")
    );

    //Validate that fields added (email, password, phone)
    ValidatableResponse getResponse = userApi.getUserByName(userName);
    GetUserByNameResponseDTO getUpdatedData = getResponse.extract().body().as(GetUserByNameResponseDTO.class);
    Assertions.assertAll("Check fields added to user data",
        () -> Assertions.assertEquals(userId, getUpdatedData.getId(), "Incorrect id"),
        () -> Assertions.assertEquals(userName, getUpdatedData.getUsername(), "Incorrect user name"),
        () -> Assertions.assertEquals("", getUpdatedData.getFirstName(), "Incorrect first name"),
        () -> Assertions.assertEquals("", getUpdatedData.getLastName(), "Incorrect last name"),
        () -> Assertions.assertEquals("", getUpdatedData.getEmail(), "Incorrect user email"),
        () -> Assertions.assertEquals("", getUpdatedData.getPassword(), "Incorrect user password"),
        () -> Assertions.assertEquals("", getUpdatedData.getPhone(), "Incorrect user phone")
    );

    deleteUser(userName);
  }

  public void deleteUser(String userName) {
    userApi.deleteUser(userName);
  }
}
