package de.tudarmstadt.informatik.tk.assistance.sdk.util;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModule;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModuleCapability;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.module.ModuleCapabilityResponseDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.module.ModuleResponseDto;

/**
 * Converter between various models
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.09.2015
 */
public class ConverterUtils {

    private final static Gson gson = new Gson();

    private ConverterUtils() {
    }

    /**
     * Converts from db module object to available module response object
     *
     * @param dbModule
     * @return
     */
    @Nullable
    public static ModuleResponseDto convertModule(DbModule dbModule) {

        if (dbModule == null) {
            return null;
        }

        ModuleResponseDto availableModuleResponse = new ModuleResponseDto();

        availableModuleResponse.setTitle(dbModule.getTitle());
        availableModuleResponse.setLogo(dbModule.getLogoUrl());
        availableModuleResponse.setCopyright(dbModule.getCopyright());
        availableModuleResponse.setDescriptionShort(dbModule.getDescriptionShort());
        availableModuleResponse.setDescriptionFull(dbModule.getDescriptionFull());
        availableModuleResponse.setModulePackage(dbModule.getPackageName());
        availableModuleResponse.setSupportEmail(dbModule.getSupportEmail());

        List<DbModuleCapability> dbModuleCaps = dbModule.getDbModuleCapabilityList();

        if (dbModuleCaps != null) {

            List<ModuleCapabilityResponseDto> reqCapsDto = new ArrayList<>();
            List<ModuleCapabilityResponseDto> optCapsDto = new ArrayList<>();

            for (DbModuleCapability dbCap : dbModuleCaps) {

                ModuleCapabilityResponseDto capabilityResponseDto = convertModuleCapability(dbCap);

                if (dbCap.getRequired()) {
                    reqCapsDto.add(capabilityResponseDto);
                } else {
                    optCapsDto.add(capabilityResponseDto);
                }
            }

            availableModuleResponse.setSensorsRequired(reqCapsDto);
            availableModuleResponse.setSensorsOptional(optCapsDto);
        }

        return availableModuleResponse;
    }

    /**
     * Converts from available module response object to db module object
     *
     * @param availableModuleResponse
     * @return
     */
    @Nullable
    public static DbModule convertModule(ModuleResponseDto availableModuleResponse) {

        if (availableModuleResponse == null) {
            return null;
        }

        DbModule dbModule = new DbModule();

        dbModule.setTitle(availableModuleResponse.getTitle() == null ? "" : availableModuleResponse.getTitle());
        dbModule.setLogoUrl(availableModuleResponse.getLogo() == null ? "" : availableModuleResponse.getLogo());
        dbModule.setCopyright(availableModuleResponse.getCopyright() == null ? "" : availableModuleResponse.getCopyright());
        dbModule.setDescriptionShort(availableModuleResponse.getDescriptionShort() == null ? "" : availableModuleResponse.getDescriptionShort());
        dbModule.setDescriptionFull(availableModuleResponse.getDescriptionFull() == null ? "" : availableModuleResponse.getDescriptionFull());
        dbModule.setPackageName(availableModuleResponse.getPackageName() == null ? "" : availableModuleResponse.getPackageName());
        dbModule.setSupportEmail(availableModuleResponse.getSupportEmail() == null ? "" : availableModuleResponse.getSupportEmail());
        dbModule.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        return dbModule;
    }

    /**
     * Converts from db module capability object to module capability response object
     *
     * @param moduleCapability
     * @return
     */
    @Nullable
    public static ModuleCapabilityResponseDto convertModuleCapability(DbModuleCapability moduleCapability) {

        if (moduleCapability == null) {
            return null;
        }

        ModuleCapabilityResponseDto moduleCapabilityResponse = new ModuleCapabilityResponseDto();

        moduleCapabilityResponse.setType(moduleCapability.getType());
        moduleCapabilityResponse.setCollectionInterval(moduleCapability.getCollectionInterval());
        moduleCapabilityResponse.setUpdateInterval(moduleCapability.getUpdateInterval());
        moduleCapabilityResponse.setAccuracy(moduleCapability.getAccuracy());

        String permissions = moduleCapability.getPermissions();

        if (permissions != null) {
            moduleCapabilityResponse.setPermissions(
                    gson.fromJson(
                            permissions,
                            new TypeToken<List<String>>() {
                            }.getType()));
        }

        return moduleCapabilityResponse;
    }

    /**
     * Converts from module capability response object to db module capability object
     *
     * @param moduleCapabilityResponse
     * @return
     */
    @Nullable
    public static DbModuleCapability convertModuleCapability(ModuleCapabilityResponseDto moduleCapabilityResponse) {

        if (moduleCapabilityResponse == null) {
            return null;
        }

        DbModuleCapability moduleCapability = new DbModuleCapability();

        moduleCapability.setType(moduleCapabilityResponse.getType());
        moduleCapability.setCollectionInterval(moduleCapabilityResponse.getCollectionInterval());
        moduleCapability.setUpdateInterval(moduleCapabilityResponse.getUpdateInterval());
        moduleCapability.setAccuracy(moduleCapabilityResponse.getAccuracy());

        List<String> permissions = moduleCapabilityResponse.getPermissions();

        if (permissions != null && !permissions.isEmpty()) {
            moduleCapability.setPermissions(gson.toJson(permissions));
        }

        moduleCapability.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        return moduleCapability;
    }

    /**
     * Converts a list of module caps response
     *
     * @param moduleCapabilityResponses
     * @return
     */
    public static List<DbModuleCapability> convertModuleCapability(List<ModuleCapabilityResponseDto> moduleCapabilityResponses) {

        if (moduleCapabilityResponses == null) {
            return Collections.emptyList();
        }

        List<DbModuleCapability> result = new ArrayList<>(moduleCapabilityResponses.size());

        for (ModuleCapabilityResponseDto resp : moduleCapabilityResponses) {
            result.add(convertModuleCapability(resp));
        }

        return result;
    }

    /**
     * Converts a list of module caps response
     *
     * @param reqCapabilityResponses
     * @return
     */
    public static List<DbModuleCapability> convertModuleCapability(List<ModuleCapabilityResponseDto> reqCapabilityResponses,
                                                                   List<ModuleCapabilityResponseDto> optCapabilityResponses) {

        if (reqCapabilityResponses == null && optCapabilityResponses == null) {
            return Collections.emptyList();
        }

        List<DbModuleCapability> result = new ArrayList<>();

        if (reqCapabilityResponses != null) {
            for (ModuleCapabilityResponseDto resp : reqCapabilityResponses) {

                DbModuleCapability converted = convertModuleCapability(resp);

                if (converted == null) {
                    continue;
                }

                converted.setRequired(true);

                result.add(converted);
            }
        }

        if (optCapabilityResponses != null) {
            for (ModuleCapabilityResponseDto resp : optCapabilityResponses) {

                DbModuleCapability converted = convertModuleCapability(resp);

                if (converted == null) {
                    continue;
                }

                converted.setRequired(false);

                result.add(converted);
            }
        }

        return result;
    }
}
