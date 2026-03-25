package pa165.project.memberservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Member data transfer object")
public class MemberDto {

    @Schema(description = "Unique identifier of the member")
    private Long id;

    @Schema(description = "Username of the member")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "Email address of the member")
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(description = "Date when the member registered")
    private Date registrationDate;
}
