package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.VisaType

/**
 * Decorator for HEDM schema "visa-types"
 */
class VisaTypeDetail {

    @Delegate
    private final VisaType visaType
    String category
    String guid

    VisaTypeDetail(VisaType visaType,String category, String guid) {
        this.visaType = visaType
        this.category = category
        this.guid = guid
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false
        VisaTypeDetail that = (VisaTypeDetail) o
        if (visaType != that.visaType) return false
        if (category != that.category) return false
        if (guid != that.guid) return false
        return true
    }

    public String toString() {
        """VisaTypeDetail[
                    visaType=$visaType,
                    category=$category,
                    guid=$guid]"""
    }

}


