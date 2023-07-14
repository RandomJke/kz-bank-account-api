package kz.s1lk.pay.account.api.service;

import kz.s1lk.pay.account.api.dto.AccountRequestDTO;
import kz.s1lk.pay.account.api.dto.AccountResponseDTO;
import kz.s1lk.pay.account.api.dto.TransferRequestDTO;

public interface IAccountService {

    AccountResponseDTO createAccount(AccountRequestDTO requestDTO);

    AccountResponseDTO getAccount(String number) ;

    AccountResponseDTO transferMoney(TransferRequestDTO transferRequestDTO);
}
