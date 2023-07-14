package kz.s1lk.pay.account.api.service;

import kz.s1lk.pay.account.api.dto.AccountRequestDTO;
import kz.s1lk.pay.account.api.dto.AccountResponseDTO;
import kz.s1lk.pay.account.api.dto.TransferRequestDTO;
import kz.s1lk.pay.account.api.dto.exception.InsufficientFundsException;
import kz.s1lk.pay.account.api.respository.BankAccountRepository;
import kz.s1lk.pay.account.api.respository.entity.AccountEntity;
import kz.s1lk.pay.account.api.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
class AccountServiceImplTest {

    @Mock
    private BankAccountRepository repository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("ТЕСТ создания банковского счета")
    void createAccountTest() {

        AccountRequestDTO requestDTO = new AccountRequestDTO();
        requestDTO.setBalance(1000.0);
        requestDTO.setOwnerName("Миша");
        requestDTO.setOwnerSecondName("Федотов");

        when(repository.save(any(AccountEntity.class))).thenReturn(getMockAccountEntity());


        AccountResponseDTO responseDTO = accountService.createAccount(requestDTO);


        assertNotNull(responseDTO);
        assertEquals(1000.0, responseDTO.getBalance());
        assertNotNull(responseDTO.getNumber());
        assertEquals("Миша", responseDTO.getOwnerName());
        assertEquals("Федотов", responseDTO.getOwnerSecondName());
        verify(repository, times(1)).save(any(AccountEntity.class));
    }

    @Test
    @DisplayName("ТЕСТ Получить банковский счет по номеру счета")
    void getAccount_existingAccountNumber_shouldReturnAccount() {

        String accountNumber = "KZ-12345";
        AccountEntity mockAccount = getMockAccountEntity();

        when(repository.findByNumber(accountNumber)).thenReturn(Optional.of(mockAccount));

        AccountResponseDTO responseDTO = accountService.getAccount(accountNumber);

        assertNotNull(responseDTO);
        assertEquals(mockAccount.getBalance(), responseDTO.getBalance());
        assertEquals(mockAccount.getNumber(), responseDTO.getNumber());
        assertEquals(mockAccount.getOwnerName(), responseDTO.getOwnerName());
        assertEquals(mockAccount.getOwnerSecondName(), responseDTO.getOwnerSecondName());
        verify(repository, times(1)).findByNumber(accountNumber);
    }

    @Test
    @DisplayName("ТЕСТ Проверка exception")
    void getAccountAndThrowException() {

        String accountNumber = "KZ-12345";

        when(repository.findByNumber(accountNumber)).thenReturn(Optional.empty());


        assertThrows(Exception.class, () -> accountService.getAccount(accountNumber));
        verify(repository, times(1)).findByNumber(accountNumber);
    }

    @Test
    @DisplayName("ТЕСТ Перевод с банковского счета на другой")
    void transferMoneyAndValid() {
        // Создаем запрос
        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.setSenderAccountNumber("KZ-12345");
        transferRequestDTO.setReceiverAccountNumber("KZ-54321");
        transferRequestDTO.setAmount(500.0);

        AccountEntity senderAccount = getMockAccountEntity();
        AccountEntity receiverAccount = getMockAccountEntity();
        receiverAccount.setNumber("KZ-54321");

        when(repository.findByNumber("KZ-12345")).thenReturn(Optional.of(senderAccount));
        when(repository.findByNumber("KZ-54321")).thenReturn(Optional.of(receiverAccount));

        // Выполняем перевод
        AccountResponseDTO responseDTO = accountService.transferMoney(transferRequestDTO);

        // Проверки условий
        assertNotNull(responseDTO);
        assertEquals(500.0, responseDTO.getBalance());
        assertEquals("KZ-12345", responseDTO.getNumber());
        assertEquals("Миша", responseDTO.getOwnerName());
        assertEquals("Федотов", responseDTO.getOwnerSecondName());
        verify(repository, times(1)).findByNumber("KZ-12345");
        verify(repository, times(1)).findByNumber("KZ-54321");
        verify(repository, times(1)).save(senderAccount);
        verify(repository, times(1)).save(receiverAccount);
    }

    @Test
    @DisplayName("ТЕСТ Перевод с банковского счета на другой с выводом ошибки")
    void transferMoneyAndThrowException() {

        TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
        transferRequestDTO.setSenderAccountNumber("KZ-12345");
        transferRequestDTO.setReceiverAccountNumber("KZ-54321");
        transferRequestDTO.setAmount(1500.0);

        AccountEntity senderAccount = getMockAccountEntity();
        AccountEntity receiverAccount = getMockAccountEntity();
        receiverAccount.setNumber("KZ-54321");

        when(repository.findByNumber("KZ-12345")).thenReturn(Optional.of(senderAccount));
        when(repository.findByNumber("KZ-54321")).thenReturn(Optional.of(receiverAccount));


        assertThrows(InsufficientFundsException.class, () -> accountService.transferMoney(transferRequestDTO));
        verify(repository, times(1)).findByNumber("KZ-12345");
        verify(repository, times(1)).findByNumber("KZ-54321");
        verify(repository, never()).save(any(AccountEntity.class));
    }

    //Подготовительные данные
    private AccountEntity getMockAccountEntity() {
        AccountEntity account = new AccountEntity();
        account.setId(1L);
        account.setNumber("KZ-12345");
        account.setOwnerName("Миша");
        account.setOwnerSecondName("Федотов");
        account.setBalance(1000.0);
        account.setActive(true);
        return account;
    }
}
