package org.wit.hivetrackerapp.login

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import org.wit.hivetrackerapp.databinding.FragmentLoginBinding

import org.wit.hivetrackerapp.R
import org.wit.hivetrackerapp.helpers.PasswordHelpers
import org.wit.hivetrackerapp.helpers.PasswordHelpers.generateSalt
import org.wit.hivetrackerapp.helpers.PasswordHelpers.isExpectedPassword
import org.wit.hivetrackerapp.main.MainApp
import org.wit.hivetrackerapp.models.UserModel
import timber.log.Timber
private var user= UserModel()
class LoginFragment : Fragment() {
    lateinit var app: MainApp
    private var _fragBinding: FragmentLoginBinding? = null
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

        _fragBinding = FragmentLoginBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.fragment_header_login)
        setLoginButtonListener(fragBinding)
        return root
    }



    private fun updateUiWithUser(username: String) {
        val welcome = getString(R.string.welcome) + username
        // TODO : initiate successful logged in experience
        Navigation.findNavController(this.requireView()).navigate(R.id.listFragment)

        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.length > 5
        //return if (username.contains("@")) {
            //Patterns.EMAIL_ADDRESS.matcher(username).matches()
        //} else {
           // username.isNotBlank()
        //}
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun setLoginButtonListener(layout: FragmentLoginBinding) {
        layout.login.setOnClickListener {
            val salt = org.wit.hivetrackerapp.helpers.salt.salt
            user.userName = layout.username.text.toString().trim()
            user.password = layout.password.text.toString().trim()
            if (!isUserNameValid(user.userName)) {
                showLoginFailed(R.string.invalid_username)
            } else if (!isPasswordValid(user.password)) {
                showLoginFailed(R.string.invalid_password)
            } else {

                val registeredUser = app.users.findByUsername(user.userName)
                if (registeredUser != null) {
                    when {
                        registeredUser.userName != user.userName -> {
                            showLoginFailed(R.string.invalid_username)

                        }
                        !isExpectedPassword(user.password,salt,registeredUser.password)
                         -> {
                            showLoginFailed(R.string.invalid_password_login)
                        }
                        else -> {
                            updateUiWithUser(user.userName)
                            app.loggedInUser = registeredUser
                            Navigation.findNavController(this.requireView()).navigate(R.id.listFragment)
                        }
                    }
                }else{
                    showLoginFailed(R.string.invalid_username)
                }
            }

            Timber.i("add Button Pressed: $user")
            //setResult(RESULT_OK)

        }
    }


}