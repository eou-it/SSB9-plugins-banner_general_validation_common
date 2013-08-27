/** *****************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * CREDIT CARD TYPE Validation Table
 */

@Entity
@Table(name = "GTVCCRD")
class CreditCardType implements Serializable {

    /**
     * Surrogate ID for GTVCCRD
     */
    @Id
    @Column(name = "GTVCCRD_SURROGATE_ID")
    @SequenceGenerator(name = "GTVCCRD_SEQ_GEN", allocationSize = 1, sequenceName = "GTVCCRD_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVCCRD_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVCCRD
     */
    @Version
    @Column(name = "GTVCCRD_VERSION")
    Long version

    /**
     * CREDIT CARD TYPE CODE: Credit Card Type Code for Credit Card Type.
     */
    @Column(name = "GTVCCRD_CODE")
    String code

    /**
     * DESCRIPTION: Description of the Credit Card Type.
     */
    @Column(name = "GTVCCRD_DESC")
    String description

    /**
     * EXTERNAL MERCHANT ID: The external value for the related internal merchant value.
     */
    @Column(name = "GTVCCRD_EXTERNAL_MERCHANT_ID")
    String externalMerchantId

    /**
     * ACTIVITY DATE: The date that the record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GTVCCRD_ACTIVITY_DATE")
    Date lastModified

    /**
     * USER IDENTIFICATION: The unique identification of the user creating or updating the record.
     */
    @Column(name = "GTVCCRD_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for GTVCCRD
     */
    @Column(name = "GTVCCRD_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """CreditCardType[
					id=$id, 
					version=$version, 
					code=$code, 
					description=$description, 
					externalMerchantId=$externalMerchantId, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof CreditCardType)) return false
        CreditCardType that = (CreditCardType) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (externalMerchantId != that.externalMerchantId) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (externalMerchantId != null ? externalMerchantId.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 10)
        description(nullable: false, maxSize: 60)
        externalMerchantId(nullable: true, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
