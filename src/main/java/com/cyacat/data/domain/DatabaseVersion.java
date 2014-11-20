package com.cyacat.data.domain;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Created by Conrad Yacat on 8/4/2014.
 */
@Entity
@Table(name="database_version")
public class DatabaseVersion implements Comparable<DatabaseVersion> {

    private DatabaseVersionPK databaseVersionPK;// = new DatabaseVersionPK();
    private Calendar dateExecuted;
    private String scriptInfo;
    private String status;
    private String executedBy;

    @EmbeddedId
    public DatabaseVersionPK getDatabaseVersionPK() {
        return databaseVersionPK;
    }

    public void setDatabaseVersionPK(DatabaseVersionPK databaseVersionPK) {
        this.databaseVersionPK = databaseVersionPK;
    }

    @Column(name="date_executed", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getDateExecuted() {
        return dateExecuted;
    }

    public void setDateExecuted(Calendar dateExecuted) {
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

    @Column(name="executed_by")
    public String getExecutedBy() {
        return executedBy;
    }

    public void setExecutedBy(String executedBy) {
        this.executedBy = executedBy;
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

    @Override
    public int compareTo(DatabaseVersion that) {

        DatabaseVersionPK thisPK = this.getDatabaseVersionPK();
        DatabaseVersionPK thatPK = that.getDatabaseVersionPK();

        if (thisPK.getMajorRelease() < thatPK.getMajorRelease())
            return -1;
        else if (thisPK.getMajorRelease() == thatPK.getMajorRelease() && thisPK.getMinorRelease() < thatPK.getMinorRelease())
            return -1;
        else if (thisPK.getMajorRelease() == thatPK.getMajorRelease() && thisPK.getMinorRelease() == thatPK.getMinorRelease() && thisPK.getVersion() < thatPK.getVersion())
            return -1;
        else if (thisPK.getMajorRelease() == thatPK.getMajorRelease() && thisPK.getMinorRelease() == thatPK.getMinorRelease() && thisPK.getVersion() == thatPK.getVersion())
            return 0;
        else
            return 1;
    }
}