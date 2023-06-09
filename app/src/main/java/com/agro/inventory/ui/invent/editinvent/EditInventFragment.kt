package com.agro.inventory.ui.invent.editinvent

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
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import com.agro.inventory.ui.invent.editinvent.EditInventFragmentArgs
import com.agro.inventory.R
import com.agro.inventory.data.remote.model.local.AddNameWorker
import com.agro.inventory.databinding.FragmentEditInventBinding
import com.agro.inventory.ui.invent.adapter.InventAdapter
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
class EditInventFragment : Fragment(R.layout.fragment_edit_invent), OnMapReadyCallback {

    private val binding by viewBinding<FragmentEditInventBinding>()
    private val viewModel by hiltNavGraphViewModels<HomeViewModel>(R.id.home)
    private val viewModels by viewModels<LocalViewModel>()

    private val args by navArgs<EditInventFragmentArgs>()

    private var nameWorkerAdapter = InventAdapter.addNameWorkerAdapter

    private var nameWorkerList = ArrayList<AddNameWorker>()
    private var worker = ArrayList<String>()
    private var workersId = ArrayList<String>()


    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    private var latLong = LatLng(-6.1458491, 106.8918661)
    private var latitude = emptyString
    private var longitude = emptyString
    var lat = emptyString
    var long = emptyString
    var uriImage = emptyString

    var plotCodeId = emptyInt
    var pekerjaan = emptyInt
    var workerId = emptyString
    var splitWorker = emptyString

    var idWorker = emptyString

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentBottomAppBar?.isVisible = false
        parentNavigation?.isVisible = false

        initViewModel()
        initOnClick()
        initSetWorkerCallback()
        initSetNameWorkerCallback()
        onInputTextChanged()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (args.kodePlot !== emptyString){
            binding.etKodePlot.textOrNull = args.kodePlot
            binding.etPolaTanam.textOrNull = args.polaTanam
            binding.etKomoditas.textOrNull = args.komoditas
            binding.etNameWorker.textOrNull = args.namaPekerja
            binding.etWork.textOrNull = args.pekerjaan
            binding.etVolumeWork.textOrNull = args.volume
            binding.etSatuan.textOrNull = args.satuan
            binding.etWorkActivities.textOrNull = args.activities
            lat = args.lat.toString()
            long = args.lng.toString()
            latitude = args.lat.toString()
            longitude = args.lng.toString()
            binding.tvLattitude.text = args.lat.toString()
            binding.tvLongitude.text = args.lng.toString()

            binding.ivPhoto.loadImage(
                args.photo,
                ImageCornerOptions.ROUNDED
            )

        }

        initMap()

    }

    private fun initViewModel() {
        viewModel.setCodePlot.observe(viewLifecycleOwner, EventObserver { result ->
            if (result.state.orEmpty) {

                viewModel.requestDetailsPlot(
                    "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
                    "1550446421",
                    plotId = result.code.toString()
                )

                plotCodeId = result.code!!.toInt()

                viewModel.setCodePlotNothing()
            }
        })

        viewModel.setWorker.observe(viewLifecycleOwner, EventObserver { result ->
            if (result.state.orEmpty) {

                binding.etWork.textOrNull = result.name
                viewModel.requestActivities(
                    "Sobi+Apps:ae7cda7f7b0e6f38638e40ad3ebb78a4",
                    "1550446421",
                    jobId = result.id.toString()
                )

                pekerjaan = result.id!!.toInt()

                binding.btnAdd.isVisible= true
                binding.btnAddFalse.isVisible= false

                binding.etWorkActivities.textOrNull = ""
                binding.etSatuan.textOrNull = ""
                Timber.e("test ieu mah euy")

                viewModel.setWorkerNothing()
            }
        })

        viewModel.setAddActivites.observe(viewLifecycleOwner, EventObserver { result ->
            if (result.state.orEmpty) {

                binding.etWorkActivities.textOrNull = result.name
                binding.etSatuan.textOrNull = result.satuan

                binding.btnAdd.isVisible= true
                binding.btnAddFalse.isVisible= false


                viewModel.setAddActivitesNothing()
            }
        })

    }

    private fun initSetWorkerCallback() {
        viewModel.setWorker.observe(viewLifecycleOwner, EventObserver { result ->
            if (result.state.orEmpty) {
                binding.etWork.textOrNull = result.name

                binding.btnAdd.isVisible= true
                binding.btnAddFalse.isVisible= false


                viewModel.setWorkerNothing()
            }
        })
    }

    private fun initSetNameWorkerCallback() {
        viewModel.setAddNameWorker.observe(viewLifecycleOwner, EventObserver { result ->
            if (result) {
                if (viewModel.nameWorkerSavedState.checkedAll) {
                } else {
                    nameWorkerList.clear()
                    worker.clear()
                    viewModel.nameWorkerSavedState.data.map {
                        nameWorkerList.add(it)

                        worker.add(
                            it.name
                        )
                        workersId.add(
                            it.id
                        )
                    }

                    nameWorkerAdapter.items = nameWorkerList

                    splitWorker = worker.toString()
                    val myString = worker.joinToString(separator = ",")
                    binding.etNameWorker.textOrNull = myString.toString()

                    val idPekerja = workersId.joinToString(separator = ",")
                    Timber.e(idPekerja.toString())
                    idWorker = idPekerja

                    nameWorkerAdapter.notifyDataSetChanged()

                }

                binding.btnAdd.isVisible= true
                binding.btnAddFalse.isVisible= false

                viewModel.setNameWorkerNothing()
            }
        })
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
                            geocoder.getFromLocation(location.latitude, location.longitude, 1) as List<Address>
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
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

                        viewModel.requestImageUploadResult(
                            "Sobi+Apps:11fbbd445c65d9a7f1c2b53ec88ba993",
                            "1550471710",
                            file
                        )

                        binding.btnAdd.isVisible= true
                        binding.btnAddFalse.isVisible= false


//                        val body = ImageBodyRequest(
//                            medias = images,
//                        )
//                        viewModels.requestUpdateProfile(body)
                    }
            }
        }
    }

    private fun onInputTextChanged() {
        binding.boxVolumeWork.editText?.addTextChangedListener {
            if (binding.etVolumeWork.text?.isNotEmpty() == true && latitude.isNotEmpty() && longitude.isNotEmpty()){
                binding.btnAddFalse.isVisible = false
                binding.btnAdd.isVisible = true
            }else{
                binding.btnAddFalse.isVisible = true
                binding.btnAdd.isVisible = false

            }

        }
    }


    private fun initOnClick() {
        binding.apply {
            etNameWorker.setOnClickListener(onClickCallback)
            etWork.setOnClickListener(onClickCallback)
            etWorkActivities.setOnClickListener(onClickCallback)
            etKodePlot.setOnClickListener(onClickCallback)
            btnTakePhoto.setOnClickListener(onClickCallback)
            btnCancel.setOnClickListener(onClickCallback)
            btnAdd.setOnClickListener(onClickCallback)
            tvTitle.setOnClickListener(onClickCallback)
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

                viewModels.updateActivities(
                    pekerja= binding.etNameWorker.text.toString(),
                    namaPekerjaan = binding.etWork.text.toString(),
                    idPekerjaan = pekerjaan.toString(),
                    nameActivity = binding.etWorkActivities.text.toString(),
                    lat = lat,
                    lng = long,
                    volume = binding.etVolumeWork.text.toString(),
                    satuan = binding.etSatuan.text.toString(),
                    photo = uriImage.toString(),
                    workersId = idWorker,
                    id = args.id.toString()
                )


            }
            binding.tvTitle -> {
                navController.navigateUp()
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
        private const val READ_EXTERNAL_STORAGE = 11
        private const val REQUEST_CAMERA_WRITE = 12
    }
}