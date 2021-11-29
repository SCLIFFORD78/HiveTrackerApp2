package org.wit.hivetrackerapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.squareup.picasso.Picasso
import org.wit.hivetrackerapp.R
import org.wit.hivetrackerapp.activities.MapsActivity
import org.wit.hivetrackerapp.databinding.FragmentUpdateBinding
import org.wit.hivetrackerapp.helpers.showImagePicker
import org.wit.hivetrackerapp.main.MainApp
import org.wit.hivetrackerapp.models.HiveModel
import org.wit.hivetrackerapp.models.Location
import timber.log.Timber

class UpdateFragment : Fragment() {
    lateinit var app: MainApp
    private var _fragBinding: FragmentUpdateBinding? = null
    private val fragBinding get() = _fragBinding!!
    private var edit = false
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private var location = Location()
    lateinit var data: HiveModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        data = HiveModel()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container?.removeAllViews()
        _fragBinding = FragmentUpdateBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_add)

        edit = false
        var bundle = arguments
        //hive = data

        if (bundle != null) {
            //hive = data
            hive = bundle.get("data") as HiveModel
            edit = true
            fragBinding.hiveTitle.setText("Tag Number :" + hive.tag)
            fragBinding.description.setText(hive.description)
            fragBinding.type.setText("Type: ${hive.type}")
            fragBinding.btnAdd.setText(R.string.save_hive)
            fragBinding.chooseImage.setText(R.string.button_changeImage)
            Picasso.get()
                .load(hive.image)
                .into(fragBinding.hiveImage)
        }
        setAddButtonListener(fragBinding)
        setDeleteButtonListener(fragBinding)
        setChooseImageListener(fragBinding)
        registerImagePickerCallback(fragBinding)
        setChooseMapListener(fragBinding)
        registerMapCallback()
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddFragment().apply {
                arguments = Bundle().apply {}
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    private fun setAddButtonListener(layout: FragmentUpdateBinding) {
        layout.btnAdd.setOnClickListener {
            hive.description = layout.description.text.toString()
            app.hives.update(hive.copy())
            Timber.i("add Button Pressed: ${app.loggedInUser.userName}")
            //setResult(RESULT_OK)
            Navigation.findNavController(this.requireView()).navigate(R.id.listFragment)
        }
    }

    private fun setDeleteButtonListener(layout: FragmentUpdateBinding) {
        layout.btnDelete.setOnClickListener {
            app.hives.delete(hive)
            Timber.i("Delete Button Pressed: ${hive.id} Deleted")
            toastMessage("Hive Deleted!")
            Navigation.findNavController(this.requireView()).navigate(R.id.listFragment)
        }
    }

    private fun setChooseImageListener(layout: FragmentUpdateBinding){
        layout.chooseImage.setOnClickListener{
            showImagePicker(imageIntentLauncher)
        }
    }

    private fun setChooseMapListener(layout: FragmentUpdateBinding){
        layout.hiveLocation.setOnClickListener{
            Timber.i("Set Location Pressed")
            val location = Location(52.0634310, -9.6853542, 15f)
            if (hive.zoom != 0f) {
                location.lat =  hive.lat
                location.lng = hive.lng
                location.zoom = hive.zoom
            }
            val launcherIntent = Intent(activity, MapsActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
    }

    private fun registerImagePickerCallback(layout: FragmentUpdateBinding) {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    -1 -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            hive.image = result.data!!.data!!
                            Picasso.get()
                                .load(hive.image)
                                .into(layout.hiveImage)
                        } // end of if
                    }
                    0 -> {
                    }
                    else -> {
                        Timber.i("Image selection cancelled")
                    }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                Timber.i("Map data ${result.data.toString()}")
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            location = result.data!!.extras?.getParcelable("location")!!
                            hive.lat = location.lat
                            hive.lng = location.lng
                            hive.zoom = location.zoom
                            Timber.i("Location == $location")
                        } // end of if
                    }
                    Activity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun toastMessage( errorString: String) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
}