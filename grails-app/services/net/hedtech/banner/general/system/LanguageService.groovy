/*******************************************************************************
 Copyright 2013-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase

class LanguageService extends ServiceBase {

    boolean transactional = true


    public Language fetchByCode(String code) {
        Language entity
        if (code) {
            Language.withSession { session ->
                def query = session.getNamedQuery('Language.fetchByCode')
                query.setString('code', code)
                entity = query.uniqueResult()
            }
        }
        return entity
    }


    public def fetchAllByCodeInList(Collection<String> codes) {
        Collection<Language> entities
        if (codes) {
            entities = Language.withSession { session ->
                session.getNamedQuery('Language.fetchAllByCodeInList').setParameterList('codes', codes).list()
            }
        }

        return entities
    }


    public Map fetchIsoCodeToLanguageCodeMap(Collection<String> languageCodeList) {

        Map<String, String> languageToIsoMap = [:]
        List<State> isoCodeList = this.fetchAllByCodeInList(languageCodeList)
        isoCodeList?.each {
            languageToIsoMap.put(it.code, it.isoCode.toLowerCase())
        }
        return languageToIsoMap
    }


    public Collection<Language> fetchAllByIsoCodeInList(Collection<String> isoCodes) {
        Collection<Language> entities
        if (isoCodes) {
            entities = Language.withSession { session ->
                session.getNamedQuery('Language.fetchAllByIsoCodeInList').setParameterList('isoCodes', isoCodes).list()
            }
        }
        return entities
    }

}