package kz.s1lk.pay.account.api.dto.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HimselfTransferException extends RuntimeException {
    private static final long serialVersionUID = -7056352146473732573L;

    public HimselfTransferException(String message, Throwable cause) {
        super(message, cause);

    }

    public HimselfTransferException(String message) {
        super(message);
    }
}
