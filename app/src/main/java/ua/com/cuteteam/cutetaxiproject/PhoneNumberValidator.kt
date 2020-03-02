package ua.com.cuteteam.cutetaxiproject

import com.google.i18n.phonenumbers.PhoneNumberUtil

class PhoneNumberValidator {

    private val phoneUtil = PhoneNumberUtil.getInstance()

    fun regionCode(phone: String): String {
        return phoneUtil.getRegionCodeForCountryCode(phoneUtil.parse(phone, "").countryCode)
    }
}