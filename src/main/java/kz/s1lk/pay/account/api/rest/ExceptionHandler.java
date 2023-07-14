package kz.s1lk.pay.account.api.rest;


import jakarta.servlet.http.HttpServletRequest;
import kz.s1lk.pay.account.api.dto.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice(basePackages = {"kz.s1lk.pay.account.api.rest"})
@Slf4j
@RequiredArgsConstructor
public class ExceptionHandler {

    private final String EXCEPTION_TEXT = "Исключение при вызове метода: ";


    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public String handleUnknown(HttpServletRequest request, Exception ex) {
        log.warn(EXCEPTION_TEXT + request.getRequestURI() + ". " + ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(AccountNotFoundException.class)
    public String handleAccountNotFoundException(HttpServletRequest request, AccountNotFoundException ex) {
        log.warn(EXCEPTION_TEXT + request.getRequestURI() + ". " + ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @org.springframework.web.bind.annotation.ExceptionHandler(SenderAccountDeactivatedException.class)
    public String handleSenderAccountDeactivatedException(HttpServletRequest request, SenderAccountDeactivatedException ex) {
        log.warn(EXCEPTION_TEXT + request.getRequestURI() + ". " + ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @org.springframework.web.bind.annotation.ExceptionHandler(ReceiverAccountDeactivatedException.class)
    public String handleReceiverAccountDeactivatedException(HttpServletRequest request, ReceiverAccountDeactivatedException ex) {
        log.warn(EXCEPTION_TEXT + request.getRequestURI() + ". " + ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @org.springframework.web.bind.annotation.ExceptionHandler(InsufficientFundsException.class)
    public String handleInsufficientFundsException(HttpServletRequest request, InsufficientFundsException ex) {
        log.warn(EXCEPTION_TEXT + request.getRequestURI() + ". " + ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
    @org.springframework.web.bind.annotation.ExceptionHandler(HimselfTransferException.class)
    public String handleHimselfTransferException(HttpServletRequest request, HimselfTransferException ex) {
        log.warn(EXCEPTION_TEXT + request.getRequestURI() + ". " + ex.getMessage(), ex);
        return ex.getMessage();
    }


}



