package net.hedtech.banner.general.system.ldm.v4

enum RecurrenceFrequencyV4 {
    DAILY( 'daily' ),
    WEEKLY( 'weekly' ),
    MONTHLY( 'monthly' ),
    YEARLY( 'yearly' )


    RecurrenceFrequencyV4(String value){
        this.value = value
    }

    private final String value


    public String getValue() {return value}


    @Override
    public String toString() {
        getValue()
    }
}
