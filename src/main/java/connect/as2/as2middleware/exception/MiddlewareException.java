package connect.as2.as2middleware.exception;

import lombok.Data;

/**
 * Created by Odinaka Onah on 08 Jul, 2020.
 */
@Data
public class MiddlewareException extends Exception {
    private final Integer httpCode;
    public MiddlewareException(Integer httpCode, String message)
    {
        super(message);
        this.httpCode = httpCode;
    }
}