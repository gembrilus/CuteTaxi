package ua.com.cuteteam.cutetaxiproject.helpers

import com.google.i18n.phonenumbers.PhoneNumberUtil

class PhoneNumberHelper {

    private val phoneUtil = PhoneNumberUtil.getInstance()

    fun regionCode(phone: String): String {
        return phoneUtil.getRegionCodeForCountryCode(phoneUtil.parse(phone, "").countryCode)
    }
}
