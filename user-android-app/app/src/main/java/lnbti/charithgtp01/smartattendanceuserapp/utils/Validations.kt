package lnbti.charithgtp01.smartattendanceuserapp.utils

import java.util.regex.Pattern

/**
 * A Validation class containing Validation Methods
 */
class Validations {
    companion object {

        fun isMobileNumberValid(mobileNumber: String?): Boolean {
            if (mobileNumber.isNullOrBlank())
                return false

            val mobileNumberPattern = "^[0-9]{10}\$"
            return mobileNumber.matches(Regex(mobileNumberPattern))
        }
        fun isEmailValid(email: String?): Boolean {
            if (email.isNullOrBlank())
                return false

            val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}"
            val pattern = Pattern.compile(emailPattern)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }
        /**
         * Company ID validation
         */
        fun isCompanyIDValid(id: String?): Boolean {

            if (id.isNullOrBlank())
                return false

            val regex = "^[A-Z]{2}\\d{5}$" // Matches "SA00001" pattern

            return id.matches(Regex(regex))
        }

        /**
         * Password validation
         */
        fun isPasswordValid(password: String?): Boolean {

            if (password.isNullOrBlank())
                return false

            return password.length > 5
        }

        /**
         * Username validation
         */
        fun isUserNameValid(userName: String): Boolean {

            if (userName.isNullOrBlank())
                return false

            return true
        }

        /**
         * NIC validation method
         * Old NIC format - 9 digits and last letter should be v or x
         * New NIC format - Only 12 digits
         */
        fun isNICValid(userNIC: String?): Boolean {
            val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
            var valid: Boolean = if (userNIC.isNullOrBlank()) {
                false
            } else {
                if (userNIC.length == 12) {
                    userNIC.matches(regex)
                } else if (userNIC.length == 10) {
                    val first9Characters: String = userNIC.substring(0, 9)
                    val lastCharacter: String = userNIC.substring(userNIC.length - 1)
                    if (first9Characters.matches(regex)) {
                        if (lastCharacter.equals("v", ignoreCase = true)) {
                            true
                        } else lastCharacter.equals("x", ignoreCase = true)
                    } else {
                        false
                    }
                } else {
                    false
                }
            }
            return valid

        }
    }
}