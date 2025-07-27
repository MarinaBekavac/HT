package mb.projects.ht.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mb.projects.ht.validator.ValidActionId;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class GetCountBySourceIdAndAction {
    @NotBlank(message = "Source ID cannot be blank")
    String sourceId;

    @NotNull(message = "Action ID cannot be null")
    @ValidActionId
    Integer actionId;

    @NotNull(message = "Start date cannot be null")
    @PastOrPresent(message = "Start date must be in the past or present")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @NotNull(message = "End date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    @AssertTrue(message = "End date must be after or equal to start date")
    private boolean isDateRangeValid() {
        return dateTo == null || dateFrom == null || !dateTo.isBefore(dateFrom);
    }
}
