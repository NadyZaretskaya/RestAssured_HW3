
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
public class GetUserByNameResponseDTO {
  private Integer id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String phone;
  private Integer userStatus;
}
