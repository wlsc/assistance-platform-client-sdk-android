package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.module;

import java.util.List;
import java.util.Set;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 02.01.2016
 */
public class ActivatedModulesResponse {

    private List<ModuleResponseDto> availableModules;

    private Set<String> activeModules;

    public ActivatedModulesResponse(List<ModuleResponseDto> availableModules, Set<String> activeModules) {
        this.availableModules = availableModules;
        this.activeModules = activeModules;
    }

    public List<ModuleResponseDto> getAvailableModules() {
        return this.availableModules;
    }

    public Set<String> getActiveModules() {
        return this.activeModules;
    }

    @Override
    public String toString() {
        return "ActivatedModulesResponse{" +
                "availableModules=" + availableModules +
                ", activeModules=" + activeModules +
                '}';
    }
}
