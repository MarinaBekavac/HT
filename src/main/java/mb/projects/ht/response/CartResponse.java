package mb.projects.ht.response;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long cartId;
    private String userId;
    private LocalDate dateCreated;
    //private List<CartItemResponse> items;
}
