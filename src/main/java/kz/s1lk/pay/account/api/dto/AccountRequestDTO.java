package kz.s1lk.pay.account.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDTO {
    @NotNull
    @NotEmpty
    @Schema(example = "Миша")
    private String ownerName;
    @NotNull
    @NotEmpty
    @Schema(example = "Федотов")
    private String ownerSecondName;

    @Min(value = 0, message = "Значение должно быть больше или равно 0")
    @Schema(example = "10000")
    private double balance;

}
