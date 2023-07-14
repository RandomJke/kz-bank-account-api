package kz.s1lk.pay.account.api.dto.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReceiverAccountDeactivatedException extends RuntimeException {
    private static final long serialVersionUID = -7056352146473732573L;

    public ReceiverAccountDeactivatedException(String message, Throwable cause) {
        super(message, cause);

    }

    public ReceiverAccountDeactivatedException(String message) {
        super(message);
    }
}
