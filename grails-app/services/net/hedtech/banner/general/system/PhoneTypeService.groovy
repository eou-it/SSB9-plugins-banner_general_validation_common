/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.service.ServiceBase
import org.apache.log4j.Logger

/**
 * A transactional service supporting persistence of the PhoneType model.
 * Providing restriction on create , update and delete on PhoneType model.
 * */
class PhoneTypeService extends ServiceBase{
    boolean transactional = true
    def phoneTypeLog = Logger.getLogger( this.getClass() )

    /**
     * fetching PhoneType data based on guid
     * @param guid
     * @return
     */
    PhoneType fetchByGuid(String guid){
        return PhoneType.fetchByGuid(guid)
    }

    /**
     * Validate method for create
     * @param map
     */
    def preCreate( domainModelOrMap ) {
        phoneTypeLog.debug "${this.class.simpleName}.create invoked with domainModelOrMap = $domainModelOrMap"
        throw new ApplicationException( PhoneType.class.name, GeneralValidationCommonConstants.ERROR_MSG_OPERATION_NOT_SUPPORTED )
    }

    /**
     * Validate method for delete
     * @param map
     */
    def preDelete( domainModelOrMap ) {
        phoneTypeLog.debug "${this.class.simpleName}.create invoked with domainModelOrMap = $domainModelOrMap"
        throw new ApplicationException( PhoneType.class.name, GeneralValidationCommonConstants.ERROR_MSG_OPERATION_NOT_SUPPORTED )
    }

    /**
     * Validate method for update
     * @param map
     */
    def preUpdate( domainModelOrMap ) {
        phoneTypeLog.debug "${this.class.simpleName}.create invoked with domainModelOrMap = $domainModelOrMap"
        throw new ApplicationException( PhoneType.class.name, GeneralValidationCommonConstants.ERROR_MSG_OPERATION_NOT_SUPPORTED )
    }

}
