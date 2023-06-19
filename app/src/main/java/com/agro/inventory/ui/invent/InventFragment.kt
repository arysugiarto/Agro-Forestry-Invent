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
import com.agro.inventory.databinding.FragmentInventBinding
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
class InventFragment : Fragment(R.layout.fragment_invent), OnMapReadyCallback {

    private val binding by viewBinding<FragmentInventBinding>()
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
    var idComodity = emptyString
    var edit = emptyBoolean
    var id = emptyString

    private var inventEntity: InventEntity = InventEntity()
    private lateinit var invent: List<InventEntity>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOnClick()
        initViewModel()
        getInvent()

        parentBottomAppBar?.isVisible = false
        parentNavigation?.isVisible = false


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (args.kodePlot !== emptyString) {
            plotCodeId = args.idPlot.toString()
            binding.etKodePlot.textOrNull = args.kodePlot
            binding.etPolaTanam.textOrNull = args.polaTanam
            binding.etKomoditas.textOrNull = args.komoditas
        }

    }

    private fun initViewModel() {
        viewModels.getLocalInvent(args.idKomoditas.toString())
    }

    //getInventEdit
    private fun getInvent() {
        var data = emptyList<InventEntity>()
        viewModels.getInvent.observe(viewLifecycleOwner) { result ->
            data = result.orEmpty()

            if (data.firstOrNull()?.edit == true) {
                edit = data.firstOrNull()?.edit.orEmpty
                id = data.firstOrNull()?.id.toString()
                binding.etKodePlot.textOrNull = data.firstOrNull()?.kodePlot
                binding.etKomoditas.textOrNull = data.firstOrNull()?.comodity
                binding.etKeliling.textOrNull = data.firstOrNull()?.keliling
                binding.etTinggi.textOrNull = data.firstOrNull()?.tinggi
                binding.etJmlTanaman.textOrNull = data.firstOrNull()?.jmlTanam
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

                if (edit == true) {
                    viewModels.updateInvent(
                        jmlTanam = binding.etJmlTanaman.text.toString(),
                        keliling = binding.etKeliling.text.toString(),
                        tinggi = binding.etTinggi.text.toString(),
                        idComodity = args.idKomoditas?.toInt(),
                        photo = uriImage,
                        lat= lat,
                        lng= long,
                        id= id
                    )
                } else {
                    inventEntity = InventEntity(
                        idPlot = args.idPlot?.toInt(),
                        kodePlot = binding.etKodePlot.text.toString(),
                        comodity = binding.etKomoditas.text.toString(),
                        idComodity = args.idKomoditas,
                        polaTanam = binding.etPolaTanam.text.toString(),
                        jmlTanam = binding.etJmlTanaman.text.toString(),
                        keliling = binding.etKeliling.text.toString(),
                        tinggi = binding.etTinggi.text.toString(),
                        edit = true,
                        lat = lat,
                        lng = long,
                        photo = uriImage
                    )

                    viewModels.insertLocalInvent(inventEntity)
                }


                navController.navigateOrNull(
                    InventFragmentDirections.actionInventFragmentToComodityFragment(
                        status = "edit"
                    )
                )

            }

            binding.tvTitle -> {
                navController.navigateUp()
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