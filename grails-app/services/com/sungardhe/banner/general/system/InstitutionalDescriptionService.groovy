
/*******************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.
 CONFIDENTIAL BUSINESS INFORMATION
 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 *******************************************************************************/

package com.sungardhe.banner.general.system

import java.util.List;

/**
 * A transactional service supporting persistence of the InstitutionalDescriptionService model. 
 **/
class InstitutionalDescriptionService {
	
	boolean transactional = true
	
	static defaultCrudMethods = true
	
	def InstitutionalDescription findByKey() {
		return InstitutionalDescription.fetchByKey()
	}
}