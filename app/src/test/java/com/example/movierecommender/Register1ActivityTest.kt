import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movierecommender.Register1Activity
import com.example.movierecommender.Register2Activity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.movierecommender.R

@RunWith(AndroidJUnit4::class)
class Register1ActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<Register1Activity> =
        ActivityScenarioRule(Register1Activity::class.java)

    @Test
    fun verifyEditTextValidation() {
        // Caso de prueba para verificar la validación de campos requeridos
        onView(withId(R.id.buttonSiguiente)).perform(click()) // Hacer clic sin ingresar datos

        // Verificar que se muestren los mensajes de error en los EditText
        onView(withId(R.id.etUser)).check(matches(hasErrorText("Campo requerido")))
        onView(withId(R.id.etEmail)).check(matches(hasErrorText("Campo requerido")))
        onView(withId(R.id.etPassword)).check(matches(hasErrorText("Campo requerido")))
    }

    @Test
    fun verifySuccessfulRegistration() {
        // Caso de prueba para verificar un registro exitoso
        onView(withId(R.id.etUser)).perform(typeText("UsuarioTest"))
        onView(withId(R.id.etEmail)).perform(typeText("correo@test.com"))
        onView(withId(R.id.etPassword)).perform(typeText("Password123"))

        onView(withId(R.id.buttonSiguiente)).perform(click())

        // Verificar que no hay mensajes de error
        onView(withText("Campo requerido")).check(doesNotExist())
    }
}

