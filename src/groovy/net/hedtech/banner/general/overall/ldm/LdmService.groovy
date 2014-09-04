/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.IntegrationConfiguration
import org.springframework.beans.factory.InitializingBean

class LdmService {

    def sessionFactory

    private static HashMap ldmFieldToBannerDomainPropertyMap = [
            abbreviation: 'code',
            title       : 'description'
    ]

    /**
     * Added for supporting secondary level cache on GV_GORICCR View, the cached is specific to the query and
     * data will be fetched based  the query, this method is used for  fetch the Banner Value by passing
     * the LDM Value i.e. can be used to fetch rule probably  during the CREATE and UPDATE API’s,
     * wherein the request payload containing the LDM Value can be passes to this method to get the corresponding
     * Banner Type before creating/updating the record in Banner
     * @param processCode
     * @param settingName
     * @param translationValue
     * @return
     */
    IntegrationConfiguration fetchAllByProcessCodeAndSettingNameAndTranslationValue(String processCode, String settingName, String translationValue ){
        List<IntegrationConfiguration> integrationConfigs = IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndTranslationValue('LDM',settingName,translationValue)
        IntegrationConfiguration integrationConfig = integrationConfigs.size() > 0 ? integrationConfigs.get(0) : null
        LdmService.log.debug ("ldmEnumeration MissCount--"+sessionFactory.getStatistics().getSecondLevelCacheStatistics(IntegrationConfiguration.LDM_CACHE_REGION_NAME).getMissCount())
        LdmService.log.debug ("ldmEnumeration HitCount --"+sessionFactory.getStatistics().getSecondLevelCacheStatistics(IntegrationConfiguration.LDM_CACHE_REGION_NAME).getHitCount())
        LdmService.log.debug ("ldmEnumeration PutCount --"+sessionFactory.getStatistics().getSecondLevelCacheStatistics(IntegrationConfiguration.LDM_CACHE_REGION_NAME).getPutCount())
        return integrationConfig
    }

    /**
     * Added for supporting secondary level cache on GV_GORICCR View, the cached is specific to the query and
     * data will be fetched based on the query, this method is used for  fetch the LDM Value by passing
     * the Banner Value i.e. can be used to fetch rule probably during the SHOW  and LIST API’ s,
     * wherein before sending the response  to LDM the  Banner Value can be passes to this method to
     * get the LDM Type for including in the response.
     * @param processCode
     * @param settingName
     * @param value
     * @return
     */
    IntegrationConfiguration findAllByProcessCodeAndSettingNameAndValue( String processCode, String settingName, String value ) {
        List<IntegrationConfiguration> integrationConfigs = IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndValue( 'LDM', settingName, value )
        IntegrationConfiguration integrationConfig = integrationConfigs.size() > 0 ? integrationConfigs.get( 0 ) : null
        LdmService.log.debug( "ldmEnumeration MissCount--" + sessionFactory.getStatistics().getSecondLevelCacheStatistics( IntegrationConfiguration.LDM_CACHE_REGION_NAME ).getMissCount() )
        LdmService.log.debug( "ldmEnumeration HitCount --" + sessionFactory.getStatistics().getSecondLevelCacheStatistics( IntegrationConfiguration.LDM_CACHE_REGION_NAME ).getHitCount() )
        LdmService.log.debug( "ldmEnumeration PutCount --" + sessionFactory.getStatistics().getSecondLevelCacheStatistics( IntegrationConfiguration.LDM_CACHE_REGION_NAME ).getPutCount() )
        return integrationConfig
    }

    /**
     * This method ensures that application exception includes BusinessLogicValidationException message which returns with http status code 400.
     * @param e ApplicationException
     */
    static void throwBusinessLogicValidationException(ApplicationException ae) {
        if (ae.wrappedException instanceof NotFoundException) {
            throw new ApplicationException(ae.getEntityClassName(), "@@r1:not.found.message:${ae.getUserFriendlyName()}:${ae.wrappedException.id}:BusinessLogicValidationException@@")
        } else if (ae.getType() == "RuntimeException" && ae.wrappedException.message.startsWith("@@r1")) {
            String wrappedExceptionMsg = ae.wrappedException.message
            throw new ApplicationException(ae.getEntityClassName(), wrappedExceptionMsg.substring(0, wrappedExceptionMsg.length() - 2) + ':BusinessLogicValidationException@@')
        } else {
            throw ae
        }
    }

    /**
     * This method sets the data origin if passed along with metadata in the payload.
     *
     * @param domainModel Domain object
     * @param map json payload
     */
    static void setDataOrigin( domainModel, map ) {
        if (!domainModel) return
        if (map?.metadata && map?.metadata?.dataOrigin) {
            domainModel.dataOrigin = map?.metadata?.dataOrigin
        }
    }

}

