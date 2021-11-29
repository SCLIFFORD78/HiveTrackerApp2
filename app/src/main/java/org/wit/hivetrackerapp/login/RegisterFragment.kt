package org.wit.hivetrackerapp.login

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.navigation.Navigation
import org.wit.hivetrackerapp.R
import org.wit.hivetrackerapp.databinding.FragmentRegisterBinding
import org.wit.hivetrackerapp.helpers.PasswordHelpers.generateSalt
import org.wit.hivetrackerapp.helpers.PasswordHelpers.hash
import org.wit.hivetrackerapp.main.MainApp
import org.wit.hivetrackerapp.models.UserModel
import timber.log.Timber

private var user= UserModel()


class RegisterFragment : Fragment() {
    lateinit var app: MainApp
    private var _fragBinding: FragmentRegisterBinding? = null
    private val fragBinding get() = _fragBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.fragment_header_register)
        setRegisterButtonListener(fragBinding)
        return root
    }

    private fun setRegisterButtonListener(layout: FragmentRegisterBinding) {
        layout.btnRegister.setOnClickListener {
            val salt = org.wit.hivetrackerapp.helpers.salt.salt
            user.userName = layout.registerUsername.text.toString().trim()
            user.firstName = layout.registerFirstname.text.toString().trim()
            user.secondName = layout.registerSecondname.text.toString().trim()
            user.email = layout.registerEmail.text.toString().trim()
            val pass = hash(layout.registerPassword.text.toString().trim(),salt)
            user.password = pass.contentToString()
            if (!isUserNameValid(user.userName)) {
                showLoginFailed(R.string.invalid_username)
            }else if (app.users.findByUsername(user.userName) != null ) {
                showLoginFailed(R.string.invalid_username_notUnique)
            }else if (!isPasswordValid(user.password)) {
                showLoginFailed(R.string.invalid_password)
            }else if (!isEmailValid(user.email)) {
                showLoginFailed(R.string.invalid_email)
            }else if (app.users.findByEmail(user.email)!= null){
                showLoginFailed(R.string.invalid_email_notUnique)
            }else if (!isFirstNameValid(user.firstName)) {
                showLoginFailed(R.string.invalid_firstname)
            }else if (!isSecondNameValid(user.secondName)) {
                showLoginFailed(R.string.invalid_secondname)
            }
            else {
                app.users.create(user)
                if (app.users.findByUsername(user.userName) != null){
                    app.loggedInUser = user
                    updateUiWithUser(user.userName)
                }
            }

            Timber.i("add Button Pressed: ${user.userName}")
            //setResult(RESULT_OK)

        }
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    // A placeholder username validation check
    private fun isEmailValid(email: String): Boolean {
        return if (!email.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
        email.isNotBlank()
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.length > 5
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    // A placeholder firstname validation check
    private fun isFirstNameValid(firstName: String): Boolean {
        return firstName.length > 2
    }

    // A placeholder secondname validation check
    private fun isSecondNameValid(secondName: String): Boolean {
        return secondName.length > 2
    }

    private fun updateUiWithUser(username: String) {
        val welcome = getString(R.string.welcome) + username
        // TODO : initiate successful logged in experience
        Navigation.findNavController(this.requireView()).navigate(R.id.listFragment)

        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }
}