package org.wit.hivetrackerapp.helpers

import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
object PasswordHelpers {
    private val random = SecureRandom()


    fun generateSalt(): ByteArray {
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt
    }

    fun isExpectedPassword(password: String, salt: ByteArray, expectedHash: String): Boolean {
        val pwdHash = hash(password, salt).contentToString()
        if (pwdHash != expectedHash) return false
        return pwdHash!!.indices.all { pwdHash[it] == expectedHash[it] }
    }

    fun hash(password: String, salt: ByteArray): ByteArray? {
        val spec = PBEKeySpec(password.toCharArray(), salt, 1000, 256)
        try {
            val skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            return skf.generateSecret(spec).encoded
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError("Error while hashing a password: " + e.message, e)
        } catch (e: InvalidKeySpecException) {
            throw AssertionError("Error while hashing a password: " + e.message, e)
        } finally {
            spec.clearPassword()
        }
    }
}