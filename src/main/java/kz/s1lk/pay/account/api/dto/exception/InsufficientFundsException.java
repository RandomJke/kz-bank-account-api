package kz.s1lk.pay.account.api.dto.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InsufficientFundsException extends RuntimeException {
    private static final long serialVersionUID = -7056352146473732573L;

    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);

    }

    public InsufficientFundsException(String message) {
        super(message);
    }
}
