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

public class TransferRequestDTO {
    @NotNull
    @NotEmpty
    @Schema(example = "KZ-01687dcd-97e2-409b-8736-c532bc60371f")
    private String senderAccountNumber;
    @NotNull
    @NotEmpty
    @Schema(example = "KZ-01687dcd-97e2-409b-8736-c532bc60554f")
    private String receiverAccountNumber;
    @NotNull
    @Min(value = 100, message = "Минимальная сумма перевода 100")
    @Schema(example = "500")
    private double amount;

}
