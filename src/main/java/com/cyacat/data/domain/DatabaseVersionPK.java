package com.cyacat.data.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Conrad Yacat on 8/5/2014.
 */
@Embeddable
public class DatabaseVersionPK implements Serializable {

    private long majorRelease;
    private long minorRelease;
    private long version;
    private String scriptType;

    public DatabaseVersionPK() {
    }

    public DatabaseVersionPK(long majorRelease, long minorRelease, long version, String scriptType) {
        this.majorRelease = majorRelease;
        this.minorRelease = minorRelease;
        this.version = version;
        this.scriptType = scriptType;
    }

    @Column(name="major_release")
    public long getMajorRelease() {
        return majorRelease;
    }

    public void setMajorRelease(long majorRelease) {
        this.majorRelease = majorRelease;
    }

    @Column(name="minor_release")
    public long getMinorRelease() {
        return minorRelease;
    }

    public void setMinorRelease(long minorRelease) {
        this.minorRelease = minorRelease;
    }

    @Column(name="version")
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Column(name="script_type")
    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatabaseVersionPK that = (DatabaseVersionPK) o;

        if (majorRelease != that.majorRelease) return false;
        if (minorRelease != that.minorRelease) return false;
        if (version != that.version) return false;
        if (!scriptType.equals(that.scriptType)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (majorRelease ^ (majorRelease >>> 32));
        result = 31 * result + (int) (minorRelease ^ (minorRelease >>> 32));
        result = 31 * result + (int) (version ^ (version >>> 32));
        result = 31 * result + scriptType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DatabaseVersionPK{" +
                "majorRelease=" + majorRelease +
                ", minorRelease=" + minorRelease +
                ", version=" + version +
                ", scriptType='" + scriptType + '\'' +
                '}';
    }
}
