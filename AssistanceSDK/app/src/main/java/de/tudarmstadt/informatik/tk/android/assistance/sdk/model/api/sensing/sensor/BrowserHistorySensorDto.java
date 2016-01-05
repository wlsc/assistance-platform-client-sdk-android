package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.SensorApiType;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class BrowserHistorySensorDto implements SensorDto {

    private Long id;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("lastVisited")
    @Expose
    private Long lastVisited;

    @SerializedName("visits")
    @Expose
    private Integer visits;

    @SerializedName("bookmark")
    @Expose
    private Boolean bookmark;

    @SerializedName("isNew")
    @Expose
    private Boolean isNew;

    @SerializedName("isUpdated")
    @Expose
    private Boolean isUpdated;

    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public BrowserHistorySensorDto() {
        this.type = SensorApiType.BROWSER_HISTORY;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public BrowserHistorySensorDto(Long id) {
        this.id = id;
        this.type = SensorApiType.BROWSER_HISTORY;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public BrowserHistorySensorDto(Long id, String url, String title, Long lastVisited, Integer visits, Boolean bookmark, Boolean isNew, Boolean isUpdated, Boolean isDeleted, String created) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.lastVisited = lastVisited;
        this.visits = visits;
        this.bookmark = bookmark;
        this.isNew = isNew;
        this.isUpdated = isUpdated;
        this.isDeleted = isDeleted;
        this.created = created;
        this.type = SensorApiType.BROWSER_HISTORY;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(Long lastVisited) {
        this.lastVisited = lastVisited;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public Boolean getBookmark() {
        return bookmark;
    }

    public void setBookmark(Boolean bookmark) {
        this.bookmark = bookmark;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Boolean getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(Boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "BrowserHistorySensorDto{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", lastVisited=" + lastVisited +
                ", visits=" + visits +
                ", bookmark=" + bookmark +
                ", isNew=" + isNew +
                ", isUpdated=" + isUpdated +
                ", isDeleted=" + isDeleted +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
