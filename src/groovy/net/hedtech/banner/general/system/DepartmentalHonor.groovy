/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.*
/**
 * <p> Academic History Department Honor Validation Table</p>
 */
@Entity
@Table(name = "STVHOND")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
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
