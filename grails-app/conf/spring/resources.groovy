/** *****************************************************************************
 Copyright 2009-2017 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */


/**
 * Spring bean configuration using Groovy DSL, versus normal Spring XML.
 */
beans = {

    // Wiring for plugin integration tests only.
    ldmService(LdmService) {
        grailsWebDataBinder = ref('grailsWebDataBinder')
        sessionFactory = ref('sessionFactory')
        globalUniqueIdentifierService = ref('globalUniqueIdentifierService')
    }
    
}

