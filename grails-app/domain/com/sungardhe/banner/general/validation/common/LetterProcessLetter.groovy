
/** *****************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.validation.common

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type


/**
 * Letter Process Letter Validation Table
 */
//TODO: NamedQueries that needs to be ported:
 /**
    * Where clause on this entity present in forms:
  * Order by clause on this entity present in forms:
  * Form Name: GTVLETR
  *  gtvletr_code

*/
@Entity
@Table(name="GTVLETR")
class LetterProcessLetter implements Serializable {
	
	/**
	 * Surrogate ID for GTVLETR
	 */
	@Id
	@Column(name="GTVLETR_SURROGATE_ID")
	@GeneratedValue(generator ="triggerAssigned")
	@GenericGenerator(name = "triggerAssigned", strategy = "com.sungardhe.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
	Long id

	/**
	 * This field identifies the letter code referenced on the Recruiting Material Control Form (SRAMATL) and the Letter Process Form (GUALETR).
	 */
	@Column(name="GTVLETR_CODE")
	String code

	/**
	 * This field identifies the most recent date a record was created or updated.
	 */
	@Column(name="GTVLETR_ACTIVITY_DATE")
	Date lastModified

	/**
	 * Y = Duplicates may be printed for this letter code, N = no dups.
	 */
	@Type(type = "yes_no")
	@Column(name="GTVLETR_DUPL_IND")
	Boolean duplIndicator

	/**
	 * This field specifies the recruiting letter/material associated with the letter code.
	 */
	@Column(name="GTVLETR_DESC")
	String description

	/**
	 * This field identifies alternate print command code (e.g.  PL - Print Landscape) for associated letter/material.
	 */
	@Column(name="GTVLETR_PRINT_COMMAND")
	String printCommand

	/**
	 * Alternate Letter code for letter codes that do not allow duplicates.
	 */
	@Column(name="GTVLETR_LETR_CODE_ALT")
	String letterAlt

	/**
	 * Version column which is used as a optimistic lock token for GTVLETR
	 */
	@Version
	@Column(name="GTVLETR_VERSION", length=19, nullable=false)
	Long version

	/**
	 * Last Modified By column for GTVLETR
	 */
	@Column(name="GTVLETR_USER_ID")
	String lastModifiedBy

	/**
	 * Data Origin column for GTVLETR
	 */
	@Column(name="GTVLETR_DATA_ORIGIN")
	String dataOrigin

	
	
	public String toString() {
		"""LetterProcessLetter[
					id=$id, 
					code=$code, 
					lastModified=$lastModified, 
					duplIndicator=$duplIndicator, 
					description=$description, 
					printCommand=$printCommand, 
					letterAlt=$letterAlt, 
					version=$version, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin, ]"""
	}

	
	boolean equals(o) {
	    if (this.is(o)) return true;
	    if (!(o instanceof LetterProcessLetter)) return false;
	    LetterProcessLetter that = (LetterProcessLetter) o;
        if(id != that.id) return false;
        if(code != that.code) return false;
        if(lastModified != that.lastModified) return false;
        if(duplIndicator != that.duplIndicator) return false;
        if(description != that.description) return false;
        if(printCommand != that.printCommand) return false;
        if(letterAlt != that.letterAlt) return false;
        if(version != that.version) return false;
        if(lastModifiedBy != that.lastModifiedBy) return false;
        if(dataOrigin != that.dataOrigin) return false;
        return true;
    }

	
	int hashCode() {
		int result;
	    result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (duplIndicator != null ? duplIndicator.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (printCommand != null ? printCommand.hashCode() : 0);
        result = 31 * result + (letterAlt != null ? letterAlt.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
	}

	static constraints = {
		code(nullable:false, maxSize:15)
		lastModified(nullable:true)
		duplIndicator(nullable:false)
		description(nullable:true, maxSize:30)
		printCommand(nullable:true, maxSize:10)
		letterAlt(nullable:true, maxSize:15)
		lastModifiedBy(nullable:true, maxSize:30)
		dataOrigin(nullable:true, maxSize:30)


		/**
	     * Please put all the custom tests in this protected section to protect the code
	     * from being overwritten on re-generation
	     */
	    /*PROTECTED REGION ID(letterprocessletter_custom_constraints) ENABLED START*/
	    
	    /*PROTECTED REGION END*/
    }
    
    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(letterprocessletter_custom_methods) ENABLED START*/
    
    /*PROTECTED REGION END*/
}
