
/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard, Banner and Luminis are either 
 registered trademarks or trademarks of SunGard Higher Education in the U.S.A. 
 and/or other regions and/or countries.
 **********************************************************************************/
package com.sungardhe.banner.general.system

/**
 * Import Related Entities if they are external to this package
 */

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Source/Background Inst Validation Table
 */

@Entity
@Table(name="STVSBGI")
class SourceAndBackgroundInstitution implements Serializable {
	
	/**
	 * Surrogate ID for STVSBGI
	 */
	@Id
	@Column(name="STVSBGI_SURROGATE_ID")
    @SequenceGenerator(name = "STVSBGI_SEQ_GEN", allocationSize = 1, sequenceName = "STVSBGI_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVSBGI_SEQ_GEN")
	Long id

	/**
	 * This field identifies the source/background institution code referenced in the  Recruiting, Admissions and Academic History Modules.
	 */
	@Column(name="STVSBGI_CODE", nullable = false, length=6)
	String code

	/**
	 * This field identifies the source/background institution type (e.g. college,     high school, source-only)
	 */
	@Column(name="STVSBGI_TYPE_IND", nullable = false, length=1)
	String typeIndicator

	/**
	 * This field indicates whether source/background institution is a recruiting      source.
	 */
	@Column(name="STVSBGI_SRCE_IND", length=1)
	String srceIndicator

	/**
	 * This field specifies the source or background institution associated with the   source/background institution code.
	 */
	@Column(name="STVSBGI_DESC", length=30)
	String description

	/**
	 * This field identifies the most recent date a record was created or updated.
	 */
	@Column(name="STVSBGI_activity_date")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastModified

	/**
	 * Electronic Capable Institution: P - PESC/XML, E - EDI, N or Null - No
	 */
	@Column(name="STVSBGI_EDI_CAPABLE", length=1)
	String ediCapable

	/**
	 * This field holds the FICE code of the institution, for EDI, unless STVSBGI_CODE contains the FICE code.
	 */
	@Column(name="STVSBGI_FICE", length=6)
	String fice

	/**
	 * The Voice Response message number assigned to the recorded message that describes the source/background institution code.
	 */
	@Column(name="STVSBGI_VR_MSG_NO", length=22)
	Integer voiceResponseMsgNumber

	 

	/**
	 * Version column which is used as a optimistic lock token for STVSBGI
	 */
	@Version
	@Column(name="STVSBGI_VERSION", nullable = false, length=19)
	Long version

	/**
	 * Last Modified By column for STVSBGI
	 */
	@Column(name="STVSBGI_USER_ID", length=30)
	String lastModifiedBy

	/**
	 * Data Origin column for STVSBGI
	 */
	@Column(name="STVSBGI_DATA_ORIGIN", length=30)
	String dataOrigin

	
	/**
	 * Foreign Key : FK1_STVSBGI_INV_STVADMR_CODE
	 */
	@ManyToOne
	@JoinColumns([
		@JoinColumn(name="STVSBGI_ADMR_CODE", referencedColumnName="STVADMR_CODE")
		])
	AdmissionRequest admrCode

	
	public String toString() {
		"SourceAndBackgroundInstitution[id=$id, code=$code, typeIndicator=$typeIndicator, srceIndicator=$srceIndicator, description=$description, lastModified=$lastModified, ediCapable=$ediCapable, fice=$fice, voiceResponseMsgNumber=$voiceResponseMsgNumber, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"
	}

	static constraints = {
		code(nullable:false, maxSize:6)
		typeIndicator(nullable:false, maxSize:1)
		srceIndicator(nullable:true, maxSize:1)
		description(nullable:false, maxSize:30)
		lastModified(nullable:true)
		ediCapable(nullable:true, maxSize:1)
		fice(nullable:true, maxSize:6)
		voiceResponseMsgNumber(nullable:true)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)
        admrCode(nullable:true)
 
		/**
	     * Please put all the custom tests in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(sourceandbackgroundinstitution_custom_constraints) ENABLED START*/
	    
	    /*PROTECTED REGION END*/
    }


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof SourceAndBackgroundInstitution)) return false;

        SourceAndBackgroundInstitution that = (SourceAndBackgroundInstitution) o;

        if (admrCode != that.admrCode) return false;
        if (code != that.code) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (description != that.description) return false;
        if (ediCapable != that.ediCapable) return false;
        if (fice != that.fice) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (srceIndicator != that.srceIndicator) return false;
        if (typeIndicator != that.typeIndicator) return false;
        if (version != that.version) return false;
        if (voiceResponseMsgNumber != that.voiceResponseMsgNumber) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (typeIndicator != null ? typeIndicator.hashCode() : 0);
        result = 31 * result + (srceIndicator != null ? srceIndicator.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (ediCapable != null ? ediCapable.hashCode() : 0);
        result = 31 * result + (fice != null ? fice.hashCode() : 0);
        result = 31 * result + (voiceResponseMsgNumber != null ? voiceResponseMsgNumber.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        result = 31 * result + (admrCode != null ? admrCode.hashCode() : 0);
        return result;
    }
/**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(sourceandbackgroundinstitution_custom_methods) ENABLED START*/
    
    /*PROTECTED REGION END*/
}
