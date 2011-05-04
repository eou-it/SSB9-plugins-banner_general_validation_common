/** *****************************************************************************
 � 2010 SunGard Higher Education.  All Rights Reserved.

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
 * CIP Code Validation Table
 */
@Entity
@Table(name = "STVCIPC")
class CIPCode implements Serializable {

  /**
   * Surrogate ID for STVCIPC
   */
  @Id
  @Column(name = "STVCIPC_SURROGATE_ID")
  @SequenceGenerator(name = "STVCIPC_SEQ_GEN", allocationSize = 1, sequenceName = "STVCIPC_SURROGATE_ID_SEQUENCE")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVCIPC_SEQ_GEN")
  Long id

  /**
   * This field identifies the Classification of Instructional Programs (CIP) code   assigned an area of study as referenced in the Degree Program Code (SDAPROG)    and the Basic Course Info. Form (SCACRSE) and by STVMAJR.
   */
  @Column(name = "STVCIPC_CODE", nullable = false, length = 6)
  String code

  /**
   * This field specifies the area of study associated with the CIP code.  CIP       codes are used in Integrated Postsecondary Education Data System (IPEDS)        reporting.
   */
  @Column(name = "STVCIPC_DESC", nullable = false, length = 30)
  String description

  /**
   * This field identifies the most recent date a record was created or updated.
   */
  @Column(name = "STVCIPC_ACTIVITY_DATE")
  Date lastModified

  /**
   * Column for storing last modified by for STVCIPC
   */
  @Column(name = "STVCIPC_USER_ID", nullable = false, length = 30)
  String lastModifiedBy

  /**
   * Optimistic Lock Token for STVCIPC
   */
  @Version
  @Column(name = "STVCIPC_VERSION", nullable = false, precision = 19)
  Long version

  /**
   * Column for storing data origin for STVCIPC
   */
  @Column(name = "STVCIPC_DATA_ORIGIN", length = 30)
  String dataOrigin

  /**
   * CIPC A indicator. Y causes the IPEDS Completions Report to process this CIPC code on the A Report
   */
  @Type(type = "yes_no")
  @Column(name = "STVCIPC_CIPC_A_IND")
  Boolean cipcAIndicator

  /**
   * CIPC B indicator. Y causes the IPEDS Completion Report to process the CIPC code on the B report.
   */
  @Type(type = "yes_no")
  @Column(name = "STVCIPC_CIPC_B_IND")
  Boolean cipcBIndicator

  /**
   * CIPC C indicator. Y causes the IPEDS Completion Report to process the CIPC code on the C report.
   */
  @Type(type = "yes_no")
  @Column(name = "STVCIPC_CIPC_C_IND")
  Boolean cipcCIndicator

  /**
   * Code for California MIS process for field SP04.
   */
  @Column(name = "STVCIPC_SP04_PROGRAM_CDE", length = 5)
  String sp04Program


  public String toString() {
    "CIPCode[id=$id, code=$code, description=$description, lastModified=$lastModified, cipcAIndicator=$cipcAIndicator, cipcBIndicator=$cipcBIndicator, cipcCIndicator=$cipcCIndicator, sp04Program=$sp04Program, lastModifiedBy=$lastModifiedBy, version=$version, dataOrigin=$dataOrigin]"
  }

  static constraints = {
    code(nullable: false, maxSize: 6)
    description(nullable: false, maxSize: 30)
    cipcAIndicator(nullable: true, maxSize: 1)
    cipcBIndicator(nullable: true, maxSize: 1)
    cipcCIndicator(nullable: true, maxSize: 1)
    sp04Program(nullable: true, maxSize: 5)
    lastModified(nullable: true)
    lastModifiedBy(nullable: true, maxSize: 30)
    dataOrigin(nullable: true, maxSize: 30)
  }


  boolean equals(o) {
    if (this.is(o)) return true

    if (!(o instanceof CIPCode)) return false

    CIPCode cipCode = (CIPCode) o

    if (cipcAIndicator != cipCode.cipcAIndicator) return false
    if (cipcBIndicator != cipCode.cipcBIndicator) return false
    if (cipcCIndicator != cipCode.cipcCIndicator) return false
    if (code != cipCode.code) return false
    if (dataOrigin != cipCode.dataOrigin) return false
    if (description != cipCode.description) return false
    if (id != cipCode.id) return false
    if (lastModified != cipCode.lastModified) return false
    if (lastModifiedBy != cipCode.lastModifiedBy) return false
    if (sp04Program != cipCode.sp04Program) return false
    if (version != cipCode.version) return false

    return true
  }


  int hashCode() {
    int result

    result = (id != null ? id.hashCode() : 0)
    result = 31 * result + (code != null ? code.hashCode() : 0)
    result = 31 * result + (description != null ? description.hashCode() : 0)
    result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
    result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
    result = 31 * result + (version != null ? version.hashCode() : 0)
    result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
    result = 31 * result + (cipcAIndicator != null ? cipcAIndicator.hashCode() : 0)
    result = 31 * result + (cipcBIndicator != null ? cipcBIndicator.hashCode() : 0)
    result = 31 * result + (cipcCIndicator != null ? cipcCIndicator.hashCode() : 0)
    result = 31 * result + (sp04Program != null ? sp04Program.hashCode() : 0)
    return result
  }

  //Read Only fields that should be protected against update
  public static readonlyProperties = ['code']
  /**
   * Please put all the custom methods/code in this protected section to protect the code
   * from being overwritten on re-generation
   */
  /*PROTECTED REGION ID(cip_custom_methods) ENABLED START*/

  /*PROTECTED REGION END*/
}
