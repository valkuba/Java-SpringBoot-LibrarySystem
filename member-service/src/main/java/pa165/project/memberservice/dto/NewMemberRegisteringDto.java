package pa165.project.memberservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for registering a new member")
public class NewMemberRegisteringDto {

    @Schema(description = "Username of the new member")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "Email address of the new member")
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(description = "Password of the new member")
    @NotBlank(message = "Password is required")
    private String password;
}
