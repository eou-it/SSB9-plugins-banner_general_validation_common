/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.Relationship
import net.hedtech.banner.general.system.RelationshipService


class RelationshipCompositeService extends LdmService {

    RelationshipService relationshipService

    def getBannerRelationshipTypeToHedmV7RelationshipTypeMap() {
        return getBannerRelationshipTypeToHedmRelationshipTypeMap(GeneralValidationCommonConstants.RELATIONSHIP_TYPE_SETTING_NAME_V7, GeneralValidationCommonConstants.VERSION_V7)
    }


    private def getBannerRelationshipTypeToHedmRelationshipTypeMap(String settingName, String version) {
        Map<String, String> bannerRelationshipTypeToHedmRelationshipTypeMap = [:]
        List<IntegrationConfiguration> intConfs = findAllByProcessCodeAndSettingName(GeneralValidationCommonConstants.PROCESS_CODE, settingName)
        if (intConfs) {
            List<Relationship> entities = relationshipService.fetchAllByCodeInList(intConfs.value)
            intConfs.each {
                HedmPersonalRelationshipType hedmPersonalRelationshipType = HedmPersonalRelationshipType.getByString(it.translationValue, version)
                if (entities.code.contains(it.value) && hedmPersonalRelationshipType) {
                    bannerRelationshipTypeToHedmRelationshipTypeMap.put(it.value, hedmPersonalRelationshipType.versionToEnumMap[version])
                }
            }
        } else {
            throw new ApplicationException(this.class, new BusinessLogicValidationException("goriccr.not.found.message", [settingName]))
        }

        return bannerRelationshipTypeToHedmRelationshipTypeMap
    }

}
