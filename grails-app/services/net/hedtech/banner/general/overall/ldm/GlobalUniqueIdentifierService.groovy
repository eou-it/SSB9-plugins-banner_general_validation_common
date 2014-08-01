/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall.ldm

import net.hedtech.banner.service.ServiceBase
import org.springframework.transaction.annotation.Transactional

/**
 * Service class for GlobalUniqueIdentifier responsible for
 * interacting with the Entity GlobalUniqueIdentifier, and
 */
@Transactional
class GlobalUniqueIdentifierService extends ServiceBase {

    static final String API ="api"
    static final int MAX_DEFAULT= 10
    static final int MAX_UPPER_LIMIT = 30
}
