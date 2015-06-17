/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system
import javax.persistence.*
/**
 * <p> Academic History Department Honor Validation Table</p>
 */
@Entity
@Table(name = "STVHOND")
class DepartmentalHonor implements Serializable{

    /**
     * Surrogate ID for STVHOND
     */
    @Id
    @Column(name = "STVHOND_SURROGATE_ID")
    @SequenceGenerator(name = "STVHONR_SEQ_GEN", allocationSize = 1, sequenceName = "STVHONR_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVHONR_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVHOND
     */
    @Version
    @Column(name = "STVHOND_VERSION")
    Long version

    /**
     * The Departmental honor code
     */
    @Column(name ="STVHOND_CODE")
    String code

    /**
     * The Departmental honor Description
     */
    @Column(name = "STVHOND_DESC")
    String description

    /**
     * The most recent date a record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVHOND_ACTIVITY_DATE")
    Date lastModified

    /**
     * The transcript print indicator. Y indicates honor should print on transcript.
     */
    @Column(name = "STVHOND_TRANSC_PRT_IND")
    String transcPrintIndicator

    /**
     * The commencement print indicator. Y indicates honor should print on commencement report.
     */
    @Column(name = "STVHOND_COMMENCE_PRT_IND")
    String commencePrintIndicator

    /**
     * Last modified by column for STVHONR
     */
    @Column(name = "STVHOND_USER_ID")
    String lastModifiedBy;

    /**
     * Data origin column for STVHONR
     */
    @Column(name = "STVHOND_DATA_ORIGIN")
    String dataOrigin;


    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        DepartmentalHonor that = (DepartmentalHonor) o

        if (code != that.code) return false
        if (commencePrintIndicator != that.commencePrintIndicator) return false
        if (dataOrigin != that.dataOrigin) return false
        if (description != that.description) return false
        if (id != that.id) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (transcPrintIndicator != that.transcPrintIndicator) return false
        if (vdpiCode != that.vdpiCode) return false
        if (version != that.version) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (transcPrintIndicator != null ? transcPrintIndicator.hashCode() : 0)
        result = 31 * result + (commencePrintIndicator != null ? commencePrintIndicator.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (vdpiCode != null ? vdpiCode.hashCode() : 0)
        return result
    }

    @Override
    public String toString() {
        return "DepartmentalHonor{" +
                "id=" + id +
                ", version=" + version +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", lastModified=" + lastModified +
                ", transcriptPrintIndicator='" + transcPrintIndicator + '\'' +
                ", commencementPrintIndicator='" + commencePrintIndicator + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", dataOrigin='" + dataOrigin + '\'' +
                ", vdpiCode='" + vdpiCode + '\'' +
                '}';
    }

    static constraints = {
        code(nullable: false, maxSize: 6)
        description(nullable: true, maxSize: 30)
        transcPrintIndicator(nullable: true, maxSize: 1, inList: ["Y"])
        commencePrintIndicator(nullable: true, maxSize: 1, inList: ["Y"])
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }
}
