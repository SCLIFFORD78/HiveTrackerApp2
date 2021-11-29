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
import org.wit.hivetrackerapp.databinding.FragmentAccountDetailsBinding
import org.wit.hivetrackerapp.main.MainApp
import org.wit.hivetrackerapp.models.UserModel
import timber.log.Timber
private var user= UserModel()

class AccountDetailsFragment : Fragment() {
    lateinit var app: MainApp
    private var _fragBinding: FragmentAccountDetailsBinding? = null
    private val fragBinding get() = _fragBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container?.removeAllViews()
        user = app.loggedInUser

        _fragBinding = FragmentAccountDetailsBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        fragBinding.updateUsername.setText(user.userName) 
        fragBinding.updateFirstname.setText(user.firstName)
        fragBinding.updateSecondname.setText(user.secondName)
        fragBinding.updateEmail.setText(user.email)
        fragBinding.updatePassword.setText(user.password)
        fragBinding.totalHives.text = app.hives.findByOwner(user.id).size.toString()
        fragBinding.dateJoined.text = app.users.findByEmail(user.email)!!.dateJoined.toString()
        setUpdateButtonListener(fragBinding)
        setDeleteButtonListener(fragBinding)
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    private fun setUpdateButtonListener(layout: FragmentAccountDetailsBinding) {
        layout.buttonUpdate.setOnClickListener {
            val updateUser = UserModel()
            updateUser.userName = layout.updateUsername.text.toString()
            updateUser.firstName = layout.updateFirstname.text.toString()
            updateUser.secondName = layout.updateSecondname.text.toString()
            updateUser.email = layout.updateEmail.text.toString()
            updateUser.password = layout.updatePassword.text.toString()
            updateUser.id = user.id
            if (!isUserNameValid(updateUser.userName)) {
                showUpdateFailed(R.string.invalid_username)
            }else if (app.users.findByUsername(updateUser.userName) != null && app.users.findByUsername(updateUser.userName)!!.id != user.id ) {
                showUpdateFailed(R.string.invalid_username_notUnique)
            }else if (!isPasswordValid(updateUser.password)) {
                showUpdateFailed(R.string.invalid_password)
            }else if (!isEmailValid(updateUser.email)) {
                showUpdateFailed(R.string.invalid_email)
            }else if (!isEmailValid(user.email)) {
                showUpdateFailed(R.string.invalid_email)
            }else if (app.users.findByEmail(updateUser.email)!= null && app.users.findByEmail(updateUser.email)!!.id != updateUser.id){
                showUpdateFailed(R.string.invalid_email_notUnique)
            }else if (!isFirstNameValid(updateUser.firstName)) {
                showUpdateFailed(R.string.invalid_firstname)
            }else if (!isSecondNameValid(updateUser.secondName)) {
                showUpdateFailed(R.string.invalid_secondname)
            }
            else {
                app.users.update(updateUser)
                if (app.users.findByUsername(user.userName) != null){
                    app.loggedInUser = user
                    updateUiWithUser(user.userName)
                }
            }

            Timber.i("Update Button Pressed: ${updateUser.userName}")
            Navigation.findNavController(this.requireView()).navigate(R.id.accountDetailsFragment)
        }
    }

    private fun setDeleteButtonListener(layout: FragmentAccountDetailsBinding) {
        layout.buttonDeleteAccount.setOnClickListener {
            app.users.delete(app.loggedInUser)
            Timber.i("Delete Button Pressed: ${app.loggedInUser.id} Deleted")
            toastMessage("User Deleted!")
            app.loggedInUser = UserModel()
            Navigation.findNavController(this.requireView()).navigate(R.id.loginOrRegisterFragment)
        }
    }


    private fun showUpdateFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    private fun toastMessage( errorString: String) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    // A placeholder email validation check
    private fun isEmailValid(email: String): Boolean {
        return if (!email.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }else {
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
        Navigation.findNavController(this.requireView()).navigate(R.id.accountDetailsFragment)

        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }
}