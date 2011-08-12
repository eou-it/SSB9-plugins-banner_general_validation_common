/** *****************************************************************************
 Â© 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
/**
 Banner Automator Version: 0.1.1
 Generated: Mon Jan 03 15:56:54 CST 2011
 */
package com.sungardhe.banner.general.system

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Admission Request Code Validation Table
 */
@Entity
@Table(name = "STVADMR")
class AdmissionRequest implements Serializable {

  /**
   * Surrogate ID for STVADMR
   */
  @Id
  @Column(name = "STVADMR_SURROGATE_ID")
  @SequenceGenerator(name = "STVADMR_SEQ_GEN", allocationSize = 1, sequenceName = "STVADMR_SURROGATE_ID_SEQUENCE")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVADMR_SEQ_GEN")
  Long id

  /**
   * This field identifies the request code associated with required admissions      materials referenced in the Admissions Module.
   */
  @Column(name = "STVADMR_CODE", nullable = false, unique = true, length = 4)
  String code

  /**
   * This field specifies the documents/materials (e.g. high school transcripts,   residency papers, etc.) associated with the request code, which are required  to support an application for admission.
   */
  @Column(name = "STVADMR_DESC", nullable = false, length = 30)
  String description

  /**
   * This field identifies the validation table against which the requested        material is validated.
   */
  @Column(name = "STVADMR_TABLE_NAME", length = 7)
  String tableName

  /**
   * This field identifies the most current date a record was created or updated.
   */
  @Column(name = "STVADMR_ACTIVITY_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  Date lastModified

  /**
   * The Voice Response message number assigned to the recorded message that describes the Admission request code.
   */
  @Column(name = "STVADMR_VR_MSG_NO", precision = 6)
  Integer voiceResponseMsgNumber

  /**
   * This field indicates whether the request code is to be spoken to the student via Voice Response. N - Do not speak this request code.
   */
  @Column(name = "STVADMR_VR_ELIG_IND", length = 1)
  String voiceResponseEligIndicator

  /**
   * Display on Web Indicator.
   */
  @Type(type = "yes_no")
  @Column(name = "STVADMR_DISP_WEB_IND", nullable = false)
  Boolean displayWebIndicator

  /**
   * Version column which is used as a optimistic lock token for STVADMR
   */
  @Version
  @Column(name = "STVADMR_VERSION", nullable = false, precision = 19)
  Long version

  /**
   * Last Modified By column for STVADMR
   */
  @Column(name = "STVADMR_USER_ID", length = 30)
  String lastModifiedBy

  /**
   * Data Origin column for STVADMR
   */
  @Column(name = "STVADMR_DATA_ORIGIN", length = 30)
  String dataOrigin



  public String toString() {
    "AdmissionRequest[id=$id, code=$code, description=$description, tableName=$tableName, lastModified=$lastModified, voiceResponseMsgNumber=$voiceResponseMsgNumber, voiceResponseEligIndicator=$voiceResponseEligIndicator, displayWebIndicator=$displayWebIndicator, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"
  }

  static constraints = {
    code(nullable: false, maxSize: 4)
    description(nullable: false, maxSize: 30)
    tableName(nullable: true, maxSize: 7)
    lastModified(nullable: true)
    voiceResponseMsgNumber(nullable: true)
    voiceResponseEligIndicator(nullable: true, maxSize: 1)
    displayWebIndicator(nullable: false)
    lastModifiedBy(nullable: true, maxSize: 30)
    dataOrigin(nullable: true, maxSize: 30)

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(admissionrequest_custom_constraints) ENABLED START*/

    /*PROTECTED REGION END*/
  }



  boolean equals(o) {
    if (this.is(o)) return true

    if (!(o instanceof AdmissionRequest)) return false

    AdmissionRequest that = (AdmissionRequest) o

    if (code != that.code) return false
    if (dataOrigin != that.dataOrigin) return false
    if (description != that.description) return false
    if (displayWebIndicator != that.displayWebIndicator) return false
    if (id != that.id) return false
    if (lastModified != that.lastModified) return false
    if (lastModifiedBy != that.lastModifiedBy) return false
    if (tableName != that.tableName) return false
    if (version != that.version) return false
    if (voiceResponseEligIndicator != that.voiceResponseEligIndicator) return false
    if (voiceResponseMsgNumber != that.voiceResponseMsgNumber) return false

    return true
  }


  int hashCode() {
    int result

    result = (id != null ? id.hashCode() : 0)
    result = 31 * result + (code != null ? code.hashCode() : 0)
    result = 31 * result + (description != null ? description.hashCode() : 0)
    result = 31 * result + (tableName != null ? tableName.hashCode() : 0)
    result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
    result = 31 * result + (voiceResponseMsgNumber != null ? voiceResponseMsgNumber.hashCode() : 0)
    result = 31 * result + (voiceResponseEligIndicator != null ? voiceResponseEligIndicator.hashCode() : 0)
    result = 31 * result + (displayWebIndicator != null ? displayWebIndicator.hashCode() : 0)
    result = 31 * result + (version != null ? version.hashCode() : 0)
    result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
    result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
    return result
  }

  //Read Only fields that should be protected against update
  public static readonlyProperties = ['code']
  /**
   * Please put all the custom methods/code in this protected section to protect the code
   * from being overwritten on re-generation
   */
  /*PROTECTED REGION ID(admissionrequest_custom_methods) ENABLED START*/

  /*PROTECTED REGION END*/
}
