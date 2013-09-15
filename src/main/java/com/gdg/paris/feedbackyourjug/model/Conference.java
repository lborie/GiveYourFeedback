package com.gdg.paris.feedbackyourjug.model;


import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Cache
@Entity
public class Conference {

    @Id
    private Long id;

    @Index
    private String ownerUserEmail;

    private String name;

    private String location;

    private String siteWeb;

    private String userToken;

    private String spreadSheetId;

    private String spreadSheetUrl;

    private String baniereUrl;

    private Boolean isValidated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSpreadSheetId() {
        return spreadSheetId;
    }

    public void setSpreadSheetId(String spreadSheetId) {
        this.spreadSheetId = spreadSheetId;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getSpreadSheetUrl() {
        return spreadSheetUrl;
    }

    public void setSpreadSheetUrl(String spreadSheetUrl) {
        this.spreadSheetUrl = spreadSheetUrl;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getBaniereUrl() {
        return baniereUrl;
    }

    public void setBaniereUrl(String baniereUrl) {
        this.baniereUrl = baniereUrl;
    }

    public Boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    public String getOwnerUserEmail() {
        return ownerUserEmail;
    }

    public void setOwnerUserEmail(String ownerUserEmail) {
        this.ownerUserEmail = ownerUserEmail;
    }
}
