package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.exception;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 21.08.2015
 */
public class BadRequestException extends Exception {

    private static final long serialVersionUID = 7791364649743639354L;

    public BadRequestException(String message) {
        super(message);
    }
}
