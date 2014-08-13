/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall.ldm

import net.hedtech.banner.general.overall.IntegrationConfiguration
import org.springframework.beans.factory.InitializingBean

class LdmService implements InitializingBean{

    def sessionFactory

   //<TO-DO> has to removed after the Composit Service extending this service has fully integrated with secondary level cache
    List<IntegrationConfiguration> rules = []


    private static HashMap ldmFieldToBannerDomainPropertyMap = [
            abbreviation: 'code',
            title       : 'description'
    ]

    //<TO-DO> LdmService should not implement InitializingBean and this method has to be  removed after integrated with secondary level cache,
    void afterPropertiesSet() {
        def ldmObject = this.class.simpleName.substring(0, this.class.simpleName.indexOf("CompositeService")).toLowerCase() //Get the ldm name from the service name.
        LdmService.log.debug "Getting LDM defaults for ${ this.class.simpleName }"
        def rules = IntegrationConfiguration.findAllByProcessCodeAndSettingNameLike('LDM', ldmObject + '.%')
        this.rules = parseRules(rules)
    }

    //<TO-DO> This method has to be removed after the Composit Service extending this service has fully integrated with secondary level cache
    private def parseRules(def rules) {
        rules.each { rule ->
            this.rules << new LdmConfigurationValue(rule)
        }
    }

    //<TO-DO> This method has to be removed after the Composit Service extending this service has fully integrated with secondary level cache
    static String fetchBannerDomainPropertyForLdmField(String ldmField) {
        return ldmFieldToBannerDomainPropertyMap[ldmField]
    }

    /**
     * Added for supporting secondary level cache on GV_GORICCR View
     * The cached is specific to the query, data will be fetched based  the query
     * @param processCode
     * @param ldmType
     * @param translationValue
     * @return
     */
    IntegrationConfiguration fetchAllByProcessCodeAndSettingNameAndTranslationValue(String processCode, String ldmType, String translationValue ){
        System.out.println("After sessionFactory.getCurrentSession().getCacheMode()" + sessionFactory.getCurrentSession().getCacheMode());
        IntegrationConfiguration integrationConfig = IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndTranslationValue('LDM',ldmType,translationValue).get(0)
        LdmService.log.debug ("ldmEnumeration MissCount--"+sessionFactory.getStatistics().getSecondLevelCacheStatistics(IntegrationConfiguration.LDM_CACHE_REGION_NAME).getMissCount())
        LdmService.log.debug ("ldmEnumeration HitCount --"+sessionFactory.getStatistics().getSecondLevelCacheStatistics(IntegrationConfiguration.LDM_CACHE_REGION_NAME).getHitCount())
        LdmService.log.debug ("ldmEnumeration PutCount --"+sessionFactory.getStatistics().getSecondLevelCacheStatistics(IntegrationConfiguration.LDM_CACHE_REGION_NAME).getPutCount())
        return integrationConfig
    }

    /**
     * Added for supporting secondary level cache on GV_GORICCR View
     * The cached is specific to the query, data will be fetched based  the query
     * @param processCode
     * @param ldmType
     * @param translationValue
     * @return
     */
    IntegrationConfiguration findAllByProcessCodeAndSettingNameAndValue(String processCode, String ldmType, String translationValue ){
        IntegrationConfiguration integrationConfig = IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndValue('LDM',ldmType,translationValue).get(0)
        LdmService.log.debug ("ldmEnumeration MissCount--"+sessionFactory.getStatistics().getSecondLevelCacheStatistics(IntegrationConfiguration.LDM_CACHE_REGION_NAME).getMissCount())
        LdmService.log.debug ("ldmEnumeration HitCount --"+sessionFactory.getStatistics().getSecondLevelCacheStatistics(IntegrationConfiguration.LDM_CACHE_REGION_NAME).getHitCount())
        LdmService.log.debug ("ldmEnumeration PutCount --"+sessionFactory.getStatistics().getSecondLevelCacheStatistics(IntegrationConfiguration.LDM_CACHE_REGION_NAME).getPutCount())
        return integrationConfig
    }

}

