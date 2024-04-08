package dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
  private String email;
  private String firstName;
  private Integer id;
  private String lastName;
  private String password;
  private String phone;
  private Integer userStatus;
  private String username;
}
