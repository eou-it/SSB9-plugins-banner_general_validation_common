/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/

package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.DurationUnit
import net.hedtech.banner.general.system.DurationUnitService

/**
 * Section Duration Unit GORICCR Mapping service.
 */
class DurationUnitCompositeService extends LdmService{

    DurationUnitService durationUnitService


    def getBannerDurationUnitCodeToHedmV4DurationUnitCode() {
        return getBannerDurationUnitCodeToHedmDurationUnitCode(GeneralValidationCommonConstants.SECTION_DURATION_UNIT_SETTING_NAME_V4, GeneralValidationCommonConstants.VERSION_V4)
    }


    private Map getBannerDurationUnitCodeToHedmDurationUnitCode(String settingName, String version) {
        Map bannerDurationUnitCodeToHedmDurationMap = [:]
        List<IntegrationConfiguration> integrationConfigurationList = findAllByProcessCodeAndSettingName(GeneralValidationCommonConstants.PROCESS_CODE, settingName)
        if (integrationConfigurationList) {
            List<DurationUnit> entities = durationUnitService.fetchAllByDurationUnitCodes(integrationConfigurationList.value)
            integrationConfigurationList.each {
                SectionDurationUnitName sectionDurationName = SectionDurationUnitName.getByString(it.translationValue, version)
                String value = it.value
                DurationUnit durationUnit = entities.find{ durationUnit -> durationUnit.code == value}
                if (durationUnit.code.contains(value) && sectionDurationName) {
                    bannerDurationUnitCodeToHedmDurationMap.put(it.value, [durationName : sectionDurationName.versionToEnumMap[version], durationUnit:durationUnit])
                }
            }
        }
        if(bannerDurationUnitCodeToHedmDurationMap.isEmpty()){
            throw new ApplicationException(this.class, new BusinessLogicValidationException("goriccr.not.found.message", [settingName]))
        }
        return bannerDurationUnitCodeToHedmDurationMap
    }

    def getDurationCodeToDurationUnitMap(Collection<String> durationCodes) {
        Map durationUnitMap = [:]
        if (durationCodes) {
            List<DurationUnit> durationUnitList = durationUnitService.fetchAllByDurationUnitCodes(durationCodes)
            durationUnitList.each {
                durationUnitMap.put(it.code, it)
            }
        }
        return durationUnitMap

    }
}
