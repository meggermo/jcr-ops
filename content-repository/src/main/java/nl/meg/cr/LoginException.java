package nl.meg.cr;

import java.io.Serializable;

public class LoginException extends Exception implements Serializable {

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
