package kz.s1lk.pay.account.api.service.impl;

import kz.s1lk.pay.account.api.dto.AccountRequestDTO;
import kz.s1lk.pay.account.api.dto.AccountResponseDTO;
import kz.s1lk.pay.account.api.dto.TransferRequestDTO;
import kz.s1lk.pay.account.api.dto.exception.*;
import kz.s1lk.pay.account.api.respository.BankAccountRepository;
import kz.s1lk.pay.account.api.respository.entity.AccountEntity;
import kz.s1lk.pay.account.api.service.IAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final BankAccountRepository repository;

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO requestDTO) {
        AccountEntity entity = new AccountEntity();

        entity.setActive(true);
        entity.setBalance(requestDTO.getBalance());
        entity.setNumber("KZ-" + UUID.randomUUID());
        entity.setOwnerName(requestDTO.getOwnerName());
        entity.setOwnerSecondName(requestDTO.getOwnerSecondName());

        AccountEntity saved = repository.save(entity);


        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setBalance(saved.getBalance());
        accountResponseDTO.setNumber(saved.getNumber());
        accountResponseDTO.setOwnerName(saved.getOwnerName());
        accountResponseDTO.setOwnerSecondName(saved.getOwnerSecondName());

        return accountResponseDTO;
    }

    @Override
    public AccountResponseDTO getAccount(String number) {

        Optional<AccountEntity> byNumber = repository.findByNumber(number);
        if (byNumber.isEmpty()) {
            throw new AccountNotFoundException("Номер банковского счёта: " + number + " не найден!");
        }

        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setBalance(byNumber.get().getBalance());
        accountResponseDTO.setNumber(byNumber.get().getNumber());
        accountResponseDTO.setOwnerName(byNumber.get().getOwnerName());
        accountResponseDTO.setOwnerSecondName(byNumber.get().getOwnerSecondName());
        return accountResponseDTO;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AccountResponseDTO transferMoney(TransferRequestDTO transferRequestDTO) {

        String senderAccountNumber = transferRequestDTO.getSenderAccountNumber();
        String receiverAccountNumber = transferRequestDTO.getReceiverAccountNumber();
        double amount = transferRequestDTO.getAmount();

        Optional<AccountEntity> senderAccount = repository.findByNumber(senderAccountNumber);
        Optional<AccountEntity> receiverAccount = repository.findByNumber(receiverAccountNumber);

        if (senderAccount.isEmpty() || receiverAccount.isEmpty()) {
            throw new AccountNotFoundException("Номер банковского счёта не найден!");
        }
        if (senderAccountNumber.equals(receiverAccountNumber)) {
            throw new HimselfTransferException("Невозможно отправить средства на свой же счёт!");
        }

        if (!senderAccount.get().isActive()) {
            throw new SenderAccountDeactivatedException("Ваш счёт деактивирован!");
        }

        if (!receiverAccount.get().isActive()) {
            throw new ReceiverAccountDeactivatedException("Счёт получателя деактивирован!");
        }


        if (senderAccount.get().getBalance() < amount) {
            throw new InsufficientFundsException("Не хватает средств на счёте!");
        }

        senderAccount.get().setBalance(senderAccount.get().getBalance() - amount);
        receiverAccount.get().setBalance(receiverAccount.get().getBalance() + amount);


        repository.save(senderAccount.get());
        repository.save(receiverAccount.get());

        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setBalance(senderAccount.get().getBalance());
        accountResponseDTO.setNumber(senderAccount.get().getNumber());
        accountResponseDTO.setOwnerName(senderAccount.get().getOwnerName());
        accountResponseDTO.setOwnerSecondName(senderAccount.get().getOwnerSecondName());

        return accountResponseDTO;

    }
}
