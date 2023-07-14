package kz.s1lk.pay.account.api.service;


import kz.s1lk.pay.account.api.dto.AccountRequestDTO;
import kz.s1lk.pay.account.api.dto.AccountResponseDTO;
import kz.s1lk.pay.account.api.dto.TransferRequestDTO;
import kz.s1lk.pay.account.api.dto.exception.AccountNotFoundException;
import kz.s1lk.pay.account.api.dto.exception.InsufficientFundsException;
import kz.s1lk.pay.account.api.dto.exception.ReceiverAccountDeactivatedException;
import kz.s1lk.pay.account.api.dto.exception.SenderAccountDeactivatedException;
import kz.s1lk.pay.account.api.respository.BankAccountRepository;
import kz.s1lk.pay.account.api.respository.entity.AccountEntity;
import kz.s1lk.pay.account.api.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class AccountServiceImplIntegrationTest {

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private BankAccountRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("ТЕСТ создания банковского счета")
    void createNewAccountTest() {

        AccountRequestDTO requestDTO = new AccountRequestDTO();
        requestDTO.setBalance(1000.0);
        requestDTO.setOwnerName("Миша");
        requestDTO.setOwnerSecondName("Федотов");


        AccountResponseDTO responseDTO = accountService.createAccount(requestDTO);


        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getNumber());
        assertEquals("Миша", responseDTO.getOwnerName());
        assertEquals("Федотов", responseDTO.getOwnerSecondName());
        assertEquals(1000.0, responseDTO.getBalance());

        // Проверка, что счет был сохранен в репозитории
        AccountEntity savedAccount = repository.findByNumber(responseDTO.getNumber()).orElse(null);
        assertNotNull(savedAccount);
        assertEquals(responseDTO.getNumber(), savedAccount.getNumber());
        assertEquals(responseDTO.getOwnerName(), savedAccount.getOwnerName());
        assertEquals(responseDTO.getOwnerSecondName(), savedAccount.getOwnerSecondName());
        assertEquals(responseDTO.getBalance(), savedAccount.getBalance());
    }

    @Test
    @DisplayName("ТЕСТ Получение банковского аккаунта")
    void getAccountExistingAccountNumberShouldReturnAccount() {

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setBalance(2000.0);
        accountEntity.setNumber("KZ-" + UUID.randomUUID());
        accountEntity.setOwnerName("Марат");
        accountEntity.setOwnerSecondName("Онгарбаев");
        accountEntity.setActive(true);
        repository.save(accountEntity);


        AccountResponseDTO responseDTO = accountService.getAccount(accountEntity.getNumber());


        assertNotNull(responseDTO);
        assertEquals(accountEntity.getNumber(), responseDTO.getNumber());
        assertEquals(accountEntity.getOwnerName(), responseDTO.getOwnerName());
        assertEquals(accountEntity.getOwnerSecondName(), responseDTO.getOwnerSecondName());
        assertEquals(accountEntity.getBalance(), responseDTO.getBalance());
    }

    @Test
    @DisplayName("ТЕСТ Получение банковского аккаунта с выбросом ошибки")
    void getAccountNonExistingAccountNumberShouldThrowException() {

        String nonExistingNumber = "KZ-123456";


        assertThrows(Exception.class, () -> accountService.getAccount(nonExistingNumber));
    }

    @Test
    @DisplayName("ТЕСТ Перевод с банковского счета на другой")
    void transferMoneyValidTransferRequestShouldTransferMoney() {

        AccountEntity senderAccount = new AccountEntity();
        senderAccount.setBalance(3000.0);
        senderAccount.setNumber("KZ-" + UUID.randomUUID());
        senderAccount.setOwnerName("Миша");
        senderAccount.setOwnerSecondName("Федотов");
        senderAccount.setActive(true);
        repository.save(senderAccount);

        AccountEntity receiverAccount = new AccountEntity();
        receiverAccount.setBalance(1000.0);
        receiverAccount.setNumber("KZ-" + UUID.randomUUID());
        receiverAccount.setOwnerName("Марат");
        receiverAccount.setOwnerSecondName("Онгарбаев");
        receiverAccount.setActive(true);
        repository.save(receiverAccount);

        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.setSenderAccountNumber(senderAccount.getNumber());
        transferRequestDTO.setReceiverAccountNumber(receiverAccount.getNumber());
        transferRequestDTO.setAmount(500.0);

        AccountResponseDTO responseDTO = accountService.transferMoney(transferRequestDTO);

        assertNotNull(responseDTO);
        assertEquals(2500.0, responseDTO.getBalance());

        // Проверка, что баланс счетов был изменен
        senderAccount = repository.findByNumber(senderAccount.getNumber()).orElse(null);
        assertNotNull(senderAccount);
        assertEquals(2500.0, senderAccount.getBalance());

        receiverAccount = repository.findByNumber(receiverAccount.getNumber()).orElse(null);
        assertNotNull(receiverAccount);
        assertEquals(1500.0, receiverAccount.getBalance());
    }

    @Test
    @DisplayName("ТЕСТ Перевод с банковского счета на другой с выбросом ошибки")
    void transferMoneyInsufficientBalanceShouldThrowException() {

        AccountEntity senderAccount = new AccountEntity();
        senderAccount.setBalance(100.0);
        senderAccount.setNumber("KZ-" + UUID.randomUUID());
        senderAccount.setOwnerName("Миша");
        senderAccount.setOwnerSecondName("Федотов");
        senderAccount.setActive(true);
        repository.save(senderAccount);

        AccountEntity receiverAccount = new AccountEntity();
        receiverAccount.setBalance(1000.0);
        receiverAccount.setNumber("KZ-" + UUID.randomUUID());
        receiverAccount.setOwnerName("Марат");
        receiverAccount.setOwnerSecondName("Онгарбаев");
        receiverAccount.setActive(true);
        repository.save(receiverAccount);

        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.setSenderAccountNumber(senderAccount.getNumber());
        transferRequestDTO.setReceiverAccountNumber(receiverAccount.getNumber());
        transferRequestDTO.setAmount(500.0);

        assertThrows(InsufficientFundsException.class, () -> accountService.transferMoney(transferRequestDTO));

        // Проверка, что баланс счетов не изменился
        senderAccount = repository.findByNumber(senderAccount.getNumber()).orElse(null);
        assertNotNull(senderAccount);
        assertEquals(100.0, senderAccount.getBalance());

        receiverAccount = repository.findByNumber(receiverAccount.getNumber()).orElse(null);
        assertNotNull(receiverAccount);
        assertEquals(1000.0, receiverAccount.getBalance());
    }
}

