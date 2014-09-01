/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.IntegrationConfiguration
import org.springframework.beans.factory.InitializingBean

class LdmService implements InitializingBean {

    def sessionFactory

    //<TO-DO> has to removed after the Composit Service extending this service has fully integrated with secondary level cache
    List<IntegrationConfiguration> rules = []


    private static HashMap ldmFieldToBannerDomainPropertyMap = [
            abbreviation: 'code',
            title       : 'description'
    ]

    //<TO-DO> LdmService should not implement InitializingBean and this method has to be  removed after integrated with secondary level cache,
    void afterPropertiesSet() {
        def ldmObject = this.class.simpleName.substring( 0, this.class.simpleName.indexOf( "CompositeService" ) ).toLowerCase()
        //Get the ldm name from the service name.
        LdmService.log.debug "Getting LDM defaults for ${this.class.simpleName}"
        def rules = IntegrationConfiguration.findAllByProcessCodeAndSettingNameLike( 'LDM', ldmObject + '.%' )
        this.rules = parseRules( rules )
    }

    //<TO-DO> This method has to be removed after the Composit Service extending this service has fully integrated with secondary level cache
    private def parseRules( def rules ) {
        rules.each {rule ->
            this.rules << new LdmConfigurationValue( rule )
        }
    }

    //<TO-DO> This method has to be removed after the Composit Service extending this service has fully integrated with secondary level cache
    static String fetchBannerDomainPropertyForLdmField( String ldmField ) {
        return ldmFieldToBannerDomainPropertyMap[ldmField]
    }

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
    IntegrationConfiguration fetchAllByProcessCodeAndSettingNameAndTranslationValue( String processCode, String settingName, String translationValue ) {
        System.out.println( "After sessionFactory.getCurrentSession().getCacheMode()" + sessionFactory.getCurrentSession().getCacheMode() );
        IntegrationConfiguration integrationConfig = IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndTranslationValue( 'LDM', settingName, translationValue ).get( 0 )
        LdmService.log.debug( "ldmEnumeration MissCount--" + sessionFactory.getStatistics().getSecondLevelCacheStatistics( IntegrationConfiguration.LDM_CACHE_REGION_NAME ).getMissCount() )
        LdmService.log.debug( "ldmEnumeration HitCount --" + sessionFactory.getStatistics().getSecondLevelCacheStatistics( IntegrationConfiguration.LDM_CACHE_REGION_NAME ).getHitCount() )
        LdmService.log.debug( "ldmEnumeration PutCount --" + sessionFactory.getStatistics().getSecondLevelCacheStatistics( IntegrationConfiguration.LDM_CACHE_REGION_NAME ).getPutCount() )
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
        IntegrationConfiguration integrationConfig = IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndValue( 'LDM', settingName, value ).get( 0 )
        LdmService.log.debug( "ldmEnumeration MissCount--" + sessionFactory.getStatistics().getSecondLevelCacheStatistics( IntegrationConfiguration.LDM_CACHE_REGION_NAME ).getMissCount() )
        LdmService.log.debug( "ldmEnumeration HitCount --" + sessionFactory.getStatistics().getSecondLevelCacheStatistics( IntegrationConfiguration.LDM_CACHE_REGION_NAME ).getHitCount() )
        LdmService.log.debug( "ldmEnumeration PutCount --" + sessionFactory.getStatistics().getSecondLevelCacheStatistics( IntegrationConfiguration.LDM_CACHE_REGION_NAME ).getPutCount() )
        return integrationConfig
    }

    /**
     * This method ensures that application exception includes BusinessLogicValidationException message which returns with http status code 400.
     * @param e ApplicationException
     */
    static void throwBusinessLogicValidationException( ApplicationException e ) {
        String wrappedExceptionMsg = e.getWrappedException()
        if (!wrappedExceptionMsg.contains( 'BusinessLogicValidationException' ) || e.wrappedException instanceof NotFoundException) {
            throw new ApplicationException( e.getEntityClassName(),
                                            wrappedExceptionMsg.substring( wrappedExceptionMsg.indexOf( '@@' ), wrappedExceptionMsg.length() - 2 ) + ':BusinessLogicValidationException@@' )
        } else {
            throw e
        }
    }

    /**
     * This method sets the data origin if passed along with metadata in the payload.
     *
     * @param domainModel Domain object
     * @param map json payload
     */
    static void setDataOrigin( domainModel, map ) {
        if (map?.metadata && map?.metadata?.dataOrigin)
            domainModel.dataOrigin = map?.metadata?.dataOrigin
    }

}

