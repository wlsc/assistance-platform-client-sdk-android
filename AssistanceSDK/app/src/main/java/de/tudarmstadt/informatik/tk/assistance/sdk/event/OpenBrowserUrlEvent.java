package de.tudarmstadt.informatik.tk.assistance.sdk.event;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 20.01.2016
 */
public class OpenBrowserUrlEvent {

    private String url;

    public OpenBrowserUrlEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    @Override
    public String toString() {
        return "OpenBrowserUrlEvent{" +
                "url='" + url + '\'' +
                '}';
    }
}