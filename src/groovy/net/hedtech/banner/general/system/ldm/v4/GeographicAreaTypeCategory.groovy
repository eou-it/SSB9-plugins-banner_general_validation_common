package net.hedtech.banner.general.system.ldm.v4

/**
 * Created by invthannee on 4/9/2016.
 */
public enum GeographicAreaTypeCategory {

    GOVERNMENTAL("governmental"),
    POSTAL("postal"),
    FUNDRAISING("fundraising"),
    RECRUITMENT("recruitment"),
    INSTITUTIONAL("institutional")

    GeographicAreaTypeCategory(String value) {
        this.value = value
    }

    private final String value


    public String getValue() { return value }


    @Override
    public String toString() {
        getValue()
    }
}