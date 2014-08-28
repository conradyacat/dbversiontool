package com.cyacat.data.domain;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Conrad Yacat on 8/4/2014.
 */
@Table(name="DATABASE_VERSION")
public class DatabaseVersion {

    private DatabaseVersionPK databaseVersionPK = new DatabaseVersionPK();
    private Timestamp dateExecuted;
    private String scriptHeader;
    private String status;

    @EmbeddedId
    public DatabaseVersionPK getDatabaseVersionPK() {
        return databaseVersionPK;
    }

    public void setDatabaseVersionPK(DatabaseVersionPK databaseVersionPK) {
        this.databaseVersionPK = databaseVersionPK;
    }

    @Column(name="DATE_EXECUTED")
    @Temporal(TemporalType.TIMESTAMP)
    public Timestamp getDateExecuted() {
        return dateExecuted;
    }

    public void setDateExecuted(Timestamp dateExecuted) {
        this.dateExecuted = dateExecuted;
    }

    @Column(name="SCRIPT_HEADER")
    public String getScriptHeader() {
        return scriptHeader;
    }

    public void setScriptHeader(String scriptHeader) {
        this.scriptHeader = scriptHeader;
    }

    @Column(name="STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatabaseVersion that = (DatabaseVersion) o;

        if (!databaseVersionPK.equals(that.databaseVersionPK)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return databaseVersionPK.hashCode();
    }
}
