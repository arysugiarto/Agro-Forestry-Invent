package com.agro.inventory.ui.invent

//import com.arysugiarto.attendence.ui.main.MainFragment.Companion.parentBottomAppBar
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import com.agro.inventory.R
import com.agro.inventory.data.local.entity.ReinventEntity
import com.agro.inventory.databinding.FragmentReinventBinding
import com.agro.inventory.ui.main.MainFragment.Companion.parentBottomAppBar
import com.agro.inventory.ui.main.MainFragment.Companion.parentNavigation
import com.agro.inventory.ui.main.imagepicker.ImagePickerActivity
import com.agro.inventory.util.*
import com.agro.inventory.viewmodel.HomeViewModel
import com.agro.inventory.viewmodel.LocalViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
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

    private var latitude = emptyDouble
    private var longitude = emptyDouble
    var lat = emptyString
    var long = emptyString

    var uriImage = emptyString

    var plotCodeId = emptyString
    var reinvent = emptyString
    var idComodity = emptyString
    var edit = emptyBoolean
    var id = emptyString

    var jumlahHidup = emptyString
    var jumlahSakit = emptyString
    var jumlahHIdupSakit = emptyInt
    var jumlahTanam = emptyInt

    private var reInventEntity: ReinventEntity = ReinventEntity()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkLocationPermission()
        initOnClick()
        initViewModel()
        getReInvent()
        onInputTextChanged()

        parentBottomAppBar?.isVisible = false
        parentNavigation?.isVisible = false


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (args.kodePlot !== emptyString) {
            plotCodeId = args.idPlot.toString()
            binding.etKodePlot.textOrNull = args.kodePlot
            binding.etPolaTanam.textOrNull = args.polaTanam
            binding.etKomoditas.textOrNull = args.komoditas
        }

         binding.etJmlTanam.textOrNull = "100"
         jumlahTanam = binding.etJmlTanam.text.toString()?.toInt().orEmpty
    }

    private fun initViewModel() {
        viewModels.getLocalReInvent(args.idKomoditas.toString(), args.kodePlot.toString())
    }

    //getInventEdit
    private fun getReInvent() {
        var data = emptyList<ReinventEntity>()
        viewModels.getReInvent.observe(viewLifecycleOwner) { result ->
            data = result.orEmpty()

            if (data.firstOrNull()?.edit == true) {
                edit = data.firstOrNull()?.edit.orEmpty
                id = data.firstOrNull()?.id.toString()
                binding.etKodePlot.textOrNull = data.firstOrNull()?.kodePlot
                binding.etKomoditas.textOrNull = data.firstOrNull()?.comodity
                binding.etKeliling.textOrNull = data.firstOrNull()?.keliling
                binding.etTinggi.textOrNull = data.firstOrNull()?.tinggi
                binding.etJmlTanam.textOrNull = data.firstOrNull()?.jmlTanam
                binding.etJmlHidup.textOrNull = data.firstOrNull()?.jmlHidup
                binding.etJmlSakit.textOrNull = data.firstOrNull()?.jmlSakit
                binding.etPolaTanam.textOrNull = data.firstOrNull()?.polaTanam
                idComodity = data.firstOrNull()?.idComodity.toString()
                binding.tvLattitude.textOrNull = data.firstOrNull()?.lat
                binding.tvLongitude.textOrNull = data.firstOrNull()?.lng
                binding.ivPhoto.loadImage(
                    data.firstOrNull()?.photo,
                    ImageCornerOptions.ROUNDED
                )

                uriImage = data.firstOrNull()?.photo.toString()
                lat = data.firstOrNull()?.lat.toString()
                long = data.firstOrNull()?.lng.toString()


            }


            Timber.e("test%s", data.toString())

        }
    }

    // Lat Long Location

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        if (!locationPermissionsAlreadyGranted()) {
            requestLocationPermission()
        }
        if (isLocationEnabled()) {
            createLocationRequest()
            mFusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        // Get the last known location coordinates
                        latitude = location.latitude
                        longitude = location.longitude

                        Timber.tag(ContentValues.TAG)
                            .d("Latidude : $latitude longitude : $longitude")

                        // Set coordinate to textview
                        binding.tvLattitude.text = latitude.toString()
                        binding.tvLongitude.text = longitude.toString()

                        initMap()

                    } else {
                        Timber.tag(ContentValues.TAG)
                            .d("Location latitude: ${location?.latitude} longitude: ${location?.longitude}")
                    }
                }
                .addOnFailureListener {
                    Timber.tag(ContentValues.TAG).e("Error getting last known location")
                }
        }
    }

    private val permsLocationPermission = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private fun createLocationRequest() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(5000)
            .build()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        client.checkLocationSettings(builder.build())
    }

    private fun initMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fLocation) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.clear()

        val place = LatLng(latitude.toDouble(), longitude.toDouble())

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

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkLocationPermission() {
        if (EasyPermissions.hasPermissions(requireContext(), *permsLocationPermission)) {
            // this already have location permission
        } else {
            requestLocationPermission()
        }
    }

    private fun locationPermissionsAlreadyGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            resources.getString(R.string.permission_access_request_location),
            PERMISSION_LOCATION_REQUEST_CODE,
            *permsLocationPermission
        )
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
                        getLastKnownLocation()
                    }
            }
        }
    }

    private fun onInputTextChanged() {
        binding.boxJmlHidup.editText?.addTextChangedListener {
            if (binding.etJmlHidup.text?.isEmpty().orEmpty){
                binding.etJmlHidup.textOrNull = "0"
            }
            if (binding.etJmlSakit.text?.isEmpty().orEmpty){
                binding.etJmlSakit.textOrNull = "0"
            }

            if ((binding.etJmlHidup.text?.toString()?.toInt().orEmpty + binding.etJmlSakit.text?.toString()?.toInt().orEmpty) >  jumlahTanam) {
                Timber.e(binding.etJmlHidup.text.toString())
                binding.boxJmlHidup.warning(
                    context?.getString(R.string.alert_reinvent_jumlah_hidup)
                )

            } else {
                binding.boxJmlHidup.error = null
            }

            if (latitude.toString().isNotEmpty() && longitude.toString()
                    .isNotEmpty() && binding.etJmlSakit.text?.isNotEmpty().orEmpty && binding.etJmlHidup.text?.isNotEmpty().orEmpty
                && binding.etKeliling.text?.isNotEmpty().orEmpty &&  binding.etTinggi.text?.isNotEmpty().orEmpty
            ){
                binding.btnAdd.isVisible = true
                binding.btnAddFalse.isVisible = false
            }else{
                binding.btnAdd.isVisible = false
                binding.btnAddFalse.isVisible = true
            }
        }

        binding.boxJmlSakit.editText?.addTextChangedListener {
            if (binding.etJmlHidup.text?.isEmpty().orEmpty){
                binding.etJmlHidup.textOrNull = "0"
            }
            if (binding.etJmlSakit.text?.isEmpty().orEmpty){
                binding.etJmlSakit.textOrNull = "0"
            }
            if ((binding.etJmlSakit.text?.toString()?.toInt().orEmpty + binding.etJmlHidup.text?.toString()?.toInt().orEmpty) > jumlahTanam) {
                binding.boxJmlSakit.warning(
                    context?.getString(R.string.alert_reinvent_jumlah_sakit)
                )
                binding.btnAdd.isVisible = false
            } else {
                binding.boxJmlSakit.error = null
                binding.btnAdd.isVisible = true
            }

            if (latitude.toString().isNotEmpty() && longitude.toString()
                    .isNotEmpty() && binding.etJmlSakit.text?.isNotEmpty().orEmpty && binding.etJmlHidup.text?.isNotEmpty().orEmpty
                && binding.etKeliling.text?.isNotEmpty().orEmpty &&  binding.etTinggi.text?.isNotEmpty().orEmpty
            ){
                binding.btnAdd.isVisible = true
                binding.btnAddFalse.isVisible = false
            }else{
                binding.btnAdd.isVisible = false
                binding.btnAddFalse.isVisible = true
            }
        }

        binding.boxJmlMati.editText?.addTextChangedListener {
            if (binding.etJmlMati.text?.isEmpty().orEmpty) {
                binding.etJmlMati.textOrNull = "0"
            }

            if (binding.etJmlMati.text?.toString()?.toInt().orEmpty > jumlahTanam) {
                binding.boxJmlMati.warning(
                    context?.getString(R.string.alert_reinvent_jumlah_hidup)
                )

            } else {
                binding.boxJmlMati.error = null
            }

            if (latitude.toString().isNotEmpty() && longitude.toString()
                    .isNotEmpty() && binding.etJmlSakit.text?.isNotEmpty().orEmpty && binding.etJmlHidup.text?.isNotEmpty().orEmpty
                && binding.etKeliling.text?.isNotEmpty().orEmpty && binding.etTinggi.text?.isNotEmpty().orEmpty
            ) {
                binding.btnAdd.isVisible = true
                binding.btnAddFalse.isVisible = false
            } else {
                binding.btnAdd.isVisible = false
                binding.btnAddFalse.isVisible = true
            }
        }


    binding.boxTinggi.editText?.addTextChangedListener {
            if (latitude.toString().isNotEmpty() && longitude.toString()
                    .isNotEmpty() && binding.etJmlSakit.text?.isNotEmpty().orEmpty && binding.etJmlHidup.text?.isNotEmpty().orEmpty
                && binding.etKeliling.text?.isNotEmpty().orEmpty &&  binding.etTinggi.text?.isNotEmpty().orEmpty
            ){
                binding.btnAdd.isVisible = true
                binding.btnAddFalse.isVisible = false
            }else{
                binding.btnAdd.isVisible = false
                binding.btnAddFalse.isVisible = true
            }
        }

        binding.boxKeliling.editText?.addTextChangedListener {
            if (latitude.toString().isNotEmpty() && longitude.toString()
                    .isNotEmpty() && binding.etJmlSakit.text?.isNotEmpty().orEmpty && binding.etJmlHidup.text?.isNotEmpty().orEmpty
                && binding.etKeliling.text?.isNotEmpty().orEmpty &&  binding.etTinggi.text?.isNotEmpty().orEmpty
            ){
                binding.btnAdd.isVisible = true
                binding.btnAddFalse.isVisible = false
            }else{
                binding.btnAdd.isVisible = false
                binding.btnAddFalse.isVisible = true
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

                if (edit == true) {
                    viewModels.updateReInvent(
                        jmlTanam = binding.etJmlTanam.text.toString(),
                        jmlHidup = binding.etJmlHidup.text.toString(),
                        jmlSakit = binding.etJmlSakit.text.toString(),
                        keliling = binding.etKeliling.text.toString(),
                        tinggi = binding.etTinggi.text.toString(),
                        photo = uriImage,
                        lat = latitude.toString(),
                        lng = longitude.toString(),
                        idComodity = args.idKomoditas?.toInt(),
                        id = id
                    )
                } else {
                    reInventEntity = ReinventEntity(
                        idPlot = args.idPlot?.toInt(),
                        kodePlot = binding.etKodePlot.text.toString(),
                        comodity = binding.etKomoditas.text.toString(),
                        polaTanam = binding.etPolaTanam.text.toString(),
                        reinventPhase = reinvent,
                        jmlTanam = binding.etJmlTanam.text.toString(),
                        jmlHidup = binding.etJmlHidup.text.toString(),
                        jmlSakit = binding.etJmlSakit.text.toString(),
                        keliling = binding.etKeliling.text.toString(),
                        tinggi = binding.etTinggi.text.toString(),
                        edit = true,
                        penyulaman = binding.etPenyulaman.text.toString(),
                        idComodity = args.idKomoditas,
                        photo = uriImage,
                        lat = latitude.toString(),
                        lng = longitude.toString(),
                    )

                    viewModels.insertLocalReinvent(reInventEntity)

                }

                navController.navigateOrNull(
                    ReInventFragmentDirections.actionReinventFragmentToReInventAssigmentFragment()
                )
//                }


            }

            binding.tvTitle -> {
                navController.navigateOrNull(
                    ReInventFragmentDirections.actionReinventFragmentToReInventAssigmentFragment()
                )
            }

            binding.btnAddFalse -> {

            }
        }

    }


    companion object {
        private const val REQUEST_CAMERA_WRITE = 12
        private const val PERMISSION_LOCATION_REQUEST_CODE = 13
    }
}