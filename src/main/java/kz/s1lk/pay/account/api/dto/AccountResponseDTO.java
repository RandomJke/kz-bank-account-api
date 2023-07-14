package kz.s1lk.pay.account.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {
    @Schema(example = "Миша")
    private String ownerName;
    @Schema(example = "Федотов")
    private String ownerSecondName;
    @Schema(example = "KZ-01687dcd-97e2-409b-8736-c532bc60554f")
    private String number;
    @Schema(example = "10000")
    private double balance;

}
