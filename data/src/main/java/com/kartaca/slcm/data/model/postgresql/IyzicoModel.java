package com.kartaca.slcm.data.model.postgresql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class IyzicoModel {
    @Id @GeneratedValue
    private long id;
    private String apiKey;
    private String secretKey;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
