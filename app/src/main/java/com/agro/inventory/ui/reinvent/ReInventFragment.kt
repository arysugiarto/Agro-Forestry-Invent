package com.agro.inventory.ui.invent

//import com.arysugiarto.attendence.ui.main.MainFragment.Companion.parentBottomAppBar
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import com.agro.inventory.ui.invent.InventFragmentArgs
import com.agro.inventory.R
import com.agro.inventory.data.local.entity.ActivitiesEntity
import com.agro.inventory.data.local.entity.InventEntity
import com.agro.inventory.data.local.entity.ReinventEntity
import com.agro.inventory.databinding.FragmentInventBinding
import com.agro.inventory.databinding.FragmentReinventBinding
import com.agro.inventory.ui.main.MainFragment.Companion.parentBottomAppBar
import com.agro.inventory.ui.main.MainFragment.Companion.parentNavigation
import com.agro.inventory.ui.main.imagepicker.ImagePickerActivity
import com.agro.inventory.util.*
import com.agro.inventory.util.livevent.EventObserver
import com.agro.inventory.viewmodel.HomeViewModel
import com.agro.inventory.viewmodel.LocalViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class ReInventFragment : Fragment(R.layout.fragment_reinvent), OnMapReadyCallback {

    private val binding by viewBinding<FragmentReinventBinding>()
    private val viewModel by hiltNavGraphViewModels<HomeViewModel>(R.id.home)
    private val viewModels by viewModels<LocalViewModel>()

    private val args by navArgs<InventFragmentArgs>()

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    private var latitude = emptyString
    private var longitude = emptyString
    var lat = emptyString
    var long = emptyString

    var uriImage = emptyString

    var plotCodeId = emptyString
    var reinvent = emptyString

    private var reInventEntity: ReinventEntity = ReinventEntity()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOnClick()

        parentBottomAppBar?.isVisible = false
        parentNavigation?.isVisible = false


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (args.kodePlot !== emptyString) {
            plotCodeId = args.idPlot.toString()
            binding.etKodePlot.textOrNull = args.kodePlot
            binding.etPolaTanam.textOrNull = args.polaTanam
            binding.etKomoditas.textOrNull = args.komoditas
        }

        spinner()
    }

    private fun spinner() {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.observe,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sBranch.adapter = adapter

        binding.sBranch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                reinvent = binding.sBranch.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }



    // Lat Long Location
    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { task ->
                    val location: Location? = task
                    if (location != null) {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(
                                location.latitude,
                                location.longitude,
                                1
                            ) as List<Address>
                        binding.apply {
                            tvLattitude.text = "${list[0].latitude}"
                            tvLongitude.text = "${list[0].longitude}"
                            lat = "${list[0].latitude}"
                            long = "${list[0].longitude}"

                            latitude = tvLattitude.toString()
                            longitude = tvLongitude.toString()
                            initMap()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please turn on location", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    private fun initMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fLocation) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    //photo
    private fun photoPicker() {
        val params =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        val pickerActivity = ImagePickerActivity()

        if (EasyPermissions.hasPermissions(requireContext(), *params)) {
            pickerActivity.showImagePickerOptions(requireContext(), object :
                ImagePickerActivity.PickerOptionListener {
                override fun onTakeCameraSelected() {
                    launchCameraIntent()
                }

                override fun onChooseGallerySelected() {
                    TODO("Not yet implemented")
                }
            })
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.image_picker_permission_access_camera),
                REQUEST_CAMERA_WRITE,
                *params
            )
        }
    }

    private fun launchCameraIntent() {
        val intent = Intent(context, ImagePickerActivity::class.java).apply {
            putExtra(
                ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
                ImagePickerActivity.REQUEST_IMAGE_CAPTURE
            )

            putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
            putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1)
            putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        }

        startActivityForResult(intent, REQUEST_CAMERA_WRITE)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data.let { result ->
            Timber.e(result.toString())
            if (requestCode == REQUEST_CAMERA_WRITE && resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result?.getParcelableExtra(Const.General.path)
                uri?.copyAndGetPath(requireContext())
                    ?.fileOf()
                    ?.let { file ->
                        binding.ivPhoto.loadImage(
                            uri.toString(),
                            ImageCornerOptions.ROUNDED
                        )
                        uriImage = uri.toString()
                        getLocation()

                    }
            }
        }
    }

    private fun initOnClick() {
        binding.apply {
            etKodePlot.setOnClickListener(onClickCallback)
            btnTakePhoto.setOnClickListener(onClickCallback)
            btnCancel.setOnClickListener(onClickCallback)
            btnAdd.setOnClickListener(onClickCallback)
            tvTitle.setOnClickListener(onClickCallback)
            btnAddFalse.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {

            binding.btnTakePhoto -> {
                photoPicker()
            }
            binding.btnCancel -> {
                navController.navigateUp()
            }

            binding.btnAdd -> {

                reInventEntity = ReinventEntity(
                    idPlot = args.idPlot?.toInt(),
                    kodePlot = "K-PP1",
                    comodity = "kopi",
                    polaTanam = "Monokultur",
                    reinventPhase = reinvent,
                    jmlTanam = binding.etJmlTanam.text.toString(),
                    keliling = binding.etKeliling.text.toString(),
                    tinggi = binding.etTinggi.text.toString(),
                    edit = true,
                    penyulaman = binding.etPenyulaman.text.toString()
                )

                viewModels.insertLocalReinvent(reInventEntity)

                navController.navigateOrNull(
                    ReInventFragmentDirections.actionMonitoringWorkerFragmentToReinventComodityFragment(
                        status = "edit"
                    )
                )

            }
            binding.tvTitle -> {
                navController.navigateOrNull(
                    ReInventFragmentDirections.actionMonitoringWorkerFragmentToReinventComodityFragment()
                )
            }
            binding.btnAddFalse -> {

            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.clear()

        val place = LatLng(lat.toDouble(), long.toDouble())

        googleMap.addMarker(
            MarkerOptions()
                .position(place)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(place))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(16.0f))
        googleMap.uiSettings.apply {
            isScrollGesturesEnabled = false
            isZoomControlsEnabled = true
        }
    }

    companion object {
        private const val REQUEST_CAMERA_WRITE = 12
    }
}