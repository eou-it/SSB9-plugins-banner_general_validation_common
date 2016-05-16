/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import org.hibernate.annotations.Type
import javax.persistence.SequenceGenerator
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.GenerationType

/**
 * Person Hold Type Validation Table
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
@Entity
@Table(name = "STVHLDD")
class HoldType implements Serializable {

    /**
     * Surrogate ID for STVHLDD
     */
    @Id
    @Column(name = "STVHLDD_SURROGATE_ID")
    @SequenceGenerator(name = "STVHLDD_SEQ_GEN", allocationSize = 1, sequenceName = "STVHLDD_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVHLDD_SEQ_GEN")
    Integer id

    /**
     * Optimistic lock token for STVHLDD
     */
    @Version
    @Column(name = "STVHLDD_VERSION")
    Integer version

    /**
     * This field identifies the hold type code referenced in the Academic History Module and cross-referenced by the Hold Form (SOAHOLD) and Hold Query-Only Form (SOQHOLD).
     */
    @Column(name = "STVHLDD_CODE")
    String code

    /**
     * This field indicates whether the hold type prevents registration.
     */
    @Type(type = "yes_no")
    @Column(name = "STVHLDD_REG_HOLD_IND")
    Boolean registrationHoldIndicator

    /**
     * This field indicates whether the hold type prevents transcript generation.
     */
    @Type(type = "yes_no")
    @Column(name = "STVHLDD_TRANS_HOLD_IND")
    Boolean transcriptHoldIndicator

    /**
     * This field indicates whether the hold type prevents graduation.
     */
    @Type(type = "yes_no")
    @Column(name = "STVHLDD_GRAD_HOLD_IND")
    Boolean graduationHoldIndicator

    /**
     * This field indicates whether the hold type prevents grade report generation.
     */
    @Type(type = "yes_no")
    @Column(name = "STVHLDD_GRADE_HOLD_IND")
    Boolean gradeHoldIndicator

    /**
     * This field specifies the hold type (e.g. registrars hold, library fine, financial hold, etc.) associated with the hold type code.
     */ 
    @Column(name = "STVHLDD_DESC")
    String description

    /**
     * Accounts Receivable Hold Indicator.
     */
    @Type(type = "yes_no")
    @Column(name = "STVHLDD_AR_HOLD_IND")
    Boolean accountsReceivableHoldIndicator

    /**
     * Enrollment verification hold indicator
     */
    @Type(type = "yes_no")
    @Column(name = "STVHLDD_ENV_HOLD_IND")
    Boolean enrollmentVerificationHoldIndicator

    /**
     * The Voice Response message number assigned to the recorded message that describes the hold type code.
     */
    @Column(name = "STVHLDD_VR_MSG_NO")
    BigDecimal voiceResponseMessageNumber

    /**
     * Web display indicator
     */
    @Type(type = "yes_no")
    @Column(name = "STVHLDD_DISP_WEB_IND")
    Boolean displayWebIndicator = false

    /**
     * Indicates application hold which will prevent creation of application.
     */
    @Type(type = "yes_no")
    @Column(name = "STVHLDD_APPLICATION_HOLD_IND")
    Boolean applicationHoldIndicator = false

    /**
     * This field indicates whether the hold type prevents compliance.
     */
    @Type(type = "yes_no")
    @Column(name = "STVHLDD_COMPLIANCE_HOLD_IND")
    Boolean complianceHoldIndicator = false

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVHLDD_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for STVHLDD
     */
    @Column(name = "STVHLDD_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVHLDD
     */
    @Column(name = "STVHLDD_DATA_ORIGIN")
    String dataOrigin


    static constraints = {
        code(nullable: false, maxSize: 2)
        registrationHoldIndicator(nullable: true )
        transcriptHoldIndicator(nullable: true )
        graduationHoldIndicator(nullable: true )
        gradeHoldIndicator(nullable: true )
        description(nullable: true, maxSize: 30)
        accountsReceivableHoldIndicator(nullable: true, )
        enrollmentVerificationHoldIndicator(nullable: true )
        voiceResponseMessageNumber(nullable: true, scale: 0)
        displayWebIndicator(nullable: false)
        applicationHoldIndicator(nullable: false)
        complianceHoldIndicator(nullable: false)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }


    public static readonlyProperties = ['code',]

}
