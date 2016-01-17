package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.exception;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 21.08.2015
 */
public class ServiceNotAvailableException extends Exception {

    private static final long serialVersionUID = -8737807391261219986L;

    public ServiceNotAvailableException(String message) {
        super(message);
    }
}
