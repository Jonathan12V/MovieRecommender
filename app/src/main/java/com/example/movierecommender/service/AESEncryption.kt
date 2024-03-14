import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class AESEncryption {

    private val key: String = "ClaveSecreta12345ClaveSecreta12345".substring(0, 16)
    //substring para que sea del tamaño adecuado

    fun encrypt(plaintext: String): String {
        try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            val secretKey = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val encryptedBytes = cipher.doFinal(plaintext.toByteArray())
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            throw RuntimeException("Error al encriptar: ${e.message}")
        }
    }

    fun decrypt(ciphertext: String): String {
        try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            val secretKey = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            val decryptedBytes = cipher.doFinal(Base64.decode(ciphertext, Base64.DEFAULT))
            return String(decryptedBytes)
        } catch (e: Exception) {
            throw RuntimeException("Error al desencriptar: ${e.message}")
        }
    }
}
