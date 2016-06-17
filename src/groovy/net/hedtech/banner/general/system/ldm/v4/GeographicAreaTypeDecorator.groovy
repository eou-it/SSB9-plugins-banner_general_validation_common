/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v4

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Geographic Area type Decorator
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class GeographicAreaTypeDecorator {

    String id
    String code
    String title
    String description


    GeographicAreaTypeDecorator(String id,String code=null,String title=null,String description=null) {
        this.id = id
        this.code = code
        this.title = title
        this.description = description
    }

    String getCategory() {
      return  GeographicAreaTypeCategory.INSTITUTIONAL.value
    }

    Detail getDetail() {
        return new Detail(id)
    }

    class Detail {
        String id

        Detail(String id) {
            this.id = id
        }
    }


}
