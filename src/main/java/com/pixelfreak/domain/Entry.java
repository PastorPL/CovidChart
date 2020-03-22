package com.pixelfreak.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

/**
 * A Entry.
 */
@Document(collection = "entry")
public class Entry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("province")
    private String province;

    @Field("country")
    private String country;

    @Field("last_update")
    private LocalDate lastUpdate;

    @Field("confirmed")
    private Integer confirmed;

    @Field("deaths")
    private Integer deaths;

    @Field("recovered")
    private Integer recovered;

    @Field("lat")
    private Double lat;

    @Field("lon")
    private Double lon;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public Entry province(String province) {
        this.province = province;
        return this;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public Entry country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public Entry lastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Integer getConfirmed() {
        return confirmed;
    }

    public Entry confirmed(Integer confirmed) {
        this.confirmed = confirmed;
        return this;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public Entry deaths(Integer deaths) {
        this.deaths = deaths;
        return this;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getRecovered() {
        return recovered;
    }

    public Entry recovered(Integer recovered) {
        this.recovered = recovered;
        return this;
    }

    public void setRecovered(Integer recovered) {
        this.recovered = recovered;
    }

    public Double getLat() {
        return lat;
    }

    public Entry lat(Double lat) {
        this.lat = lat;
        return this;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public Entry lon(Double lon) {
        this.lon = lon;
        return this;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entry)) {
            return false;
        }
        return id != null && id.equals(((Entry) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Entry{" +
            "id=" + getId() +
            ", province='" + getProvince() + "'" +
            ", country='" + getCountry() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", confirmed=" + getConfirmed() +
            ", deaths=" + getDeaths() +
            ", recovered=" + getRecovered() +
            ", lat=" + getLat() +
            ", lon=" + getLon() +
            "}";
    }
}
