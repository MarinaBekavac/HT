package mb.projects.ht.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class GetCartRequest {

    @NotBlank(message = "User ID cannot be blank")
    String userId;

}
