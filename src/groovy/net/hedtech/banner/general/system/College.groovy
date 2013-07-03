/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

package net.hedtech.banner.general.system

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Model representing College (the Banenr Validation Table).
 */
@Entity
@Table(name = "STVCOLL")
class College implements Serializable {

    /**
     * Surrogate ID for STVCOLL
     */
    @Id
    @Column(name = "STVCOLL_SURROGATE_ID")
    @SequenceGenerator(name = "STVCOLL_SEQ_GEN", allocationSize = 1, sequenceName = "STVCOLL_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVCOLL_SEQ_GEN")
    Long id

    /**
     * This field identifies the college code referenced in the Catalog, Class Schedule, Recruiting, Admissions, General Student, Registration, Academic History and Degree Audit Modules. Reqd value: 00 - No College Designated.
     */
    @Column(name = "STVCOLL_CODE", nullable = false, unique = true, length = 2)
    String code

    /**
     * This field specifies the college or school (i.e. **Banner"s** highest administrative unit) associated with the college code.
     */
    @Column(name = "STVCOLL_DESC", nullable = false, length = 30)
    String description

    /**
     * This field is not currently in use.
     */
    @Column(name = "STVCOLL_ADDR_STREET_LINE1", length = 75)
    String addressStreetLine1

    /**
     * This field is not currently in use.
     */
    @Column(name = "STVCOLL_ADDR_STREET_LINE2", length = 75)
    String addressStreetLine2

    /**
     * This field is not currently in use.
     */
    @Column(name = "STVCOLL_ADDR_STREET_LINE3", length = 75)
    String addressStreetLine3

    /**
     * This field is not currently in use.
     */
    @Column(name = "STVCOLL_ADDR_CITY", length = 50)
    String addressCity

    /**
     * This field is not currently in use.
     */
    @Column(name = "STVCOLL_ADDR_STATE", length = 2)
    String addressState

    /**
     * This field is not currently in use.
     */
    @Column(name = "STVCOLL_ADDR_COUNTRY", length = 28)
    String addressCountry

    /**
     * This field is not currently in use.
     */
    @Column(name = "STVCOLL_ADDR_ZIP_CODE", length = 10)
    String addressZipCode

    /**
     * System Required Indicator
     */
    @Column(name = "STVCOLL_SYSTEM_REQ_IND", nullable = true, length = 1)
    String systemRequiredIndicator

    /**
     * The Voice Response message number assigned to the recorded message that describes the college code.
     */
    @Column(name = "STVCOLL_VR_MSG_NO", precision = 6)
    Integer voiceResponseMessageNumber

    /**
     * Statistics Canadian reporting institution specific code.
     */
    @Column(name = "STVCOLL_STATSCAN_CDE3", length = 6)
    String statisticsCanadianInstitution

    /**
     * MIS DISTRICT/DIVISION CODE: This field indicates equivalent district or division associated with an Institution.
     */
    @Column(name = "STVCOLL_DICD_CODE", length = 3)
    String districtDivision

    /**
     * HOUSE NUMBER: Building or lot number on a street or in an area.
     */
    @Column(name = "STVCOLL_HOUSE_NUMBER", length = 10)
    String houseNumber

    /**
     * This field is not currently in use.
     */
    @Column(name = "STVCOLL_ADDR_STREET_LINE4", length = 75)
    String addressStreetLine4

    /**
     * Optimistic Lock Token for STVCOLL
     */
    @Version
    @Column(name = "STVCOLL_VERSION", nullable = false, precision = 19)
    Long version

    // Note: lastModified, lastModifiedBy, dataOrigin fields are common audit trail fields that will move to a mapped based class when supported in Grails
    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVCOLL_ACTIVITY_DATE", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Column for storing last modified by for STVCOLL
     */
    @Column(name = "STVCOLL_USER_ID", length = 30, nullable = true)
    String lastModifiedBy

    /**
     * Column for storing data origin for STVCOLL
     */
    @Column(name = "STVCOLL_DATA_ORIGIN", length = 30, nullable = true)
    String dataOrigin


    public String toString() {
        "College[id=$id, code=$code, description=$description, addressStreetLine1=$addressStreetLine1, addressStreetLine2=$addressStreetLine2, addressStreetLine3=$addressStreetLine3, addressCity=$addressCity, addressState=$addressState, addressCountry=$addressCountry, addressZipCode=$addressZipCode, lastModified=$lastModified, systemRequiredIndicator=$systemRequiredIndicator, voiceResponseMessageNumber=$voiceResponseMessageNumber, statisticsCanadianInstitution=$statisticsCanadianInstitution, districtDivision=$districtDivision, houseNumber=$houseNumber, addressStreetLine4=$addressStreetLine4, lastModifiedBy=$lastModifiedBy, version=$version, dataOrigin=$dataOrigin]"
    }


    static constraints = {
        code(nullable: false, maxSize: 2)
        description(nullable: false, maxSize: 30)
        addressStreetLine1(nullable: true, maxSize: 75)
        addressStreetLine2(nullable: true, maxSize: 75)
        addressStreetLine3(nullable: true, maxSize: 75)
        addressCity(nullable: true, maxSize: 50)
        addressState(nullable: true, maxSize: 2)
        addressCountry(nullable: true, maxSize: 28)
        addressZipCode(nullable: true, maxSize: 10)
        systemRequiredIndicator(nullable: true, maxSize: 1, inList: ['Y', 'N'])
        voiceResponseMessageNumber(nullable: true)
        statisticsCanadianInstitution(nullable: true, maxSize: 6)
        districtDivision(nullable: true, maxSize: 3)
        houseNumber(nullable: true, maxSize: 10)
        addressStreetLine4(nullable: true, maxSize: 75)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }


    boolean equals(o) {
        if (this.is(o)) return true

        if (!(o instanceof College)) return false

        College college = (College) o

        if (addressCity != college.addressCity) return false
        if (addressCountry != college.addressCountry) return false
        if (addressState != college.addressState) return false
        if (addressStreetLine1 != college.addressStreetLine1) return false
        if (addressStreetLine2 != college.addressStreetLine2) return false
        if (addressStreetLine3 != college.addressStreetLine3) return false
        if (addressStreetLine4 != college.addressStreetLine4) return false
        if (addressZipCode != college.addressZipCode) return false
        if (code != college.code) return false
        if (dataOrigin != college.dataOrigin) return false
        if (description != college.description) return false
        if (districtDivision != college.districtDivision) return false
        if (houseNumber != college.houseNumber) return false
        if (id != college.id) return false
        if (lastModified != college.lastModified) return false
        if (lastModifiedBy != college.lastModifiedBy) return false
        if (statisticsCanadianInstitution != college.statisticsCanadianInstitution) return false
        if (systemRequiredIndicator != college.systemRequiredIndicator) return false
        if (version != college.version) return false
        if (voiceResponseMessageNumber != college.voiceResponseMessageNumber) return false

        return true
    }


    int hashCode() {
        int result

        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (addressStreetLine1 != null ? addressStreetLine1.hashCode() : 0)
        result = 31 * result + (addressStreetLine2 != null ? addressStreetLine2.hashCode() : 0)
        result = 31 * result + (addressStreetLine3 != null ? addressStreetLine3.hashCode() : 0)
        result = 31 * result + (addressCity != null ? addressCity.hashCode() : 0)
        result = 31 * result + (addressState != null ? addressState.hashCode() : 0)
        result = 31 * result + (addressCountry != null ? addressCountry.hashCode() : 0)
        result = 31 * result + (addressZipCode != null ? addressZipCode.hashCode() : 0)
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0)
        result = 31 * result + (voiceResponseMessageNumber != null ? voiceResponseMessageNumber.hashCode() : 0)
        result = 31 * result + (statisticsCanadianInstitution != null ? statisticsCanadianInstitution.hashCode() : 0)
        result = 31 * result + (districtDivision != null ? districtDivision.hashCode() : 0)
        result = 31 * result + (houseNumber != null ? houseNumber.hashCode() : 0)
        result = 31 * result + (addressStreetLine4 != null ? addressStreetLine4.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

    public static Object fetchBySomeAttribute() {
        return [list: College.list(sort: "code", order: "desc")]
    }


    public static Object fetchBySomeAttribute(filter) {
        return [list: College.findAllByCodeIlikeOrDescriptionIlike("%" + filter + "%", "%" + filter + "%", [sort: "code", order: "desc"])]
    }

}
