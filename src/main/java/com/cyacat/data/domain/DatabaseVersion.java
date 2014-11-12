package com.cyacat.data.domain;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Conrad Yacat on 8/4/2014.
 */
@Entity
@Table(name="database_version")
public class DatabaseVersion {

    private DatabaseVersionPK databaseVersionPK;// = new DatabaseVersionPK();
    private Timestamp dateExecuted;
    private String scriptInfo;
    private String status;

    @EmbeddedId
    public DatabaseVersionPK getDatabaseVersionPK() {
        return databaseVersionPK;
    }

    public void setDatabaseVersionPK(DatabaseVersionPK databaseVersionPK) {
        this.databaseVersionPK = databaseVersionPK;
    }

    @Column(name="date_executed")
    //@Temporal(TemporalType.TIMESTAMP)
    public Timestamp getDateExecuted() {
        return dateExecuted;
    }

    public void setDateExecuted(Timestamp dateExecuted) {
        this.dateExecuted = dateExecuted;
    }

    @Column(name="script_info")
    public String getScriptInfo() {
        return scriptInfo;
    }

    public void setScriptInfo(String scriptInfo) {
        this.scriptInfo = scriptInfo;
    }

    @Column(name="status")
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
