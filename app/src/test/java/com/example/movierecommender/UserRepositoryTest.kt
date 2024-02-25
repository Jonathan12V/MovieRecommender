import android.content.Context
import com.example.movierecommender.repository.UserRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class UserRepositoryTest {

    private lateinit var context: Context
    private lateinit var userRepository: UserRepository

    // Crear el repositorio de users
    @Before
    fun setUp() {
        context = org.robolectric.RuntimeEnvironment.application
        userRepository = UserRepository(context)
    }

    @Test
    fun testAddUser() {
        val result = userRepository.addUser("testUser", "test@example.com", "testPassword")
        assertNotEquals(-1, result)
    }

    @Test
    fun testCheckUser() {
        userRepository.addUser("testUser", "test@example.com", "testPassword")
        assertEquals(1, userRepository.checkUser("testUser", "testPassword"))
        assertEquals(3, userRepository.checkUser("testUser", "wrongPassword"))
        assertEquals(2, userRepository.checkUser("nonexistentUser", "anyPassword"))
    }

    @Test
    fun testCheckUserAndEmailUsado() {
        userRepository.addUser("existingUser", "existing@example.com", "password123")
        assertEquals(
            3,
            userRepository.checkUserAndEmailUsado("existingUser", "existing@example.com")
        )
        assertEquals(1, userRepository.checkUserAndEmailUsado("existingUser", "new@example.com"))
        assertEquals(2, userRepository.checkUserAndEmailUsado("newUser", "existing@example.com"))
        assertEquals(-1, userRepository.checkUserAndEmailUsado("newUser", "new@example.com"))
    }

    @Test
    fun testCheckPasswordSecurity() {
        assertTrue(userRepository.checkPasswordSecurity("SecurePassword123"))
        assertFalse(userRepository.checkPasswordSecurity("weak"))
    }
}
