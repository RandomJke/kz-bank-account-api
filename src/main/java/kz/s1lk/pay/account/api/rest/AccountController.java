package kz.s1lk.pay.account.api.rest;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.s1lk.pay.account.api.dto.AccountRequestDTO;
import kz.s1lk.pay.account.api.dto.AccountResponseDTO;
import kz.s1lk.pay.account.api.dto.TransferRequestDTO;
import kz.s1lk.pay.account.api.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Tag(name = "Account controller")
public class AccountController {

    private final IAccountService accountService;

    @PostMapping
    @Operation(summary = "Создание банковского аккаунта")
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody @Valid AccountRequestDTO requestDTO) {
        return ResponseEntity.ok(accountService.createAccount(requestDTO));
    }

    @GetMapping
    @Operation(summary = "Получение данных по номеру счета")
    public ResponseEntity<AccountResponseDTO> getAccount(@RequestParam @Parameter(
            description = "Номер счета",
            example = "KZ-01687dcd-97e2-409b-8736-c532bc60371f") String number) throws Exception {
        return ResponseEntity.ok(accountService.getAccount(number));

    }

    @PostMapping("/transfer")
    @Operation(summary = "Перевод средств между счетами")
    public ResponseEntity<AccountResponseDTO> transferMoney(@RequestBody @Valid TransferRequestDTO transferRequestDTO) throws Exception {
        return ResponseEntity.ok(accountService.transferMoney(transferRequestDTO));
    }
}
