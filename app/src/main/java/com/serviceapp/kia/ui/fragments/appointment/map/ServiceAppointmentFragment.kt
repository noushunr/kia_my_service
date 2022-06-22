package com.serviceapp.kia.ui.fragments.appointment.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.ServiceCenterApi
import com.serviceapp.kia.databinding.ServiceAppointmentFragmentBinding
import com.serviceapp.kia.ui.views.SearchableDialog
import com.serviceapp.kia.ui.views.SearchableItem
import com.serviceapp.kia.ui.views.SearchableItemListener
import com.serviceapp.kia.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.map_info_window_custom_layout.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ServiceAppointmentFragment : Fragment(), KodeinAware, NetworkListener, OnMapReadyCallback,
    SearchableItemListener, GoogleMap.OnInfoWindowClickListener {

    companion object {
        fun newInstance() = ServiceAppointmentFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: ServiceAppointmentViewModel
    private lateinit var navController: NavController
    private lateinit var binding: ServiceAppointmentFragmentBinding
    private val factory: ServiceAppointmentViewModelFactory by instance()

    private var mapFragment : SupportMapFragment? = null
    private var map: GoogleMap? = null
    private var poisList: List<LekuPoi>? = null
    private var lekuPoisMarkersMap: MutableMap<String, LekuPoi>? = null
    private val searchItemsList : MutableList<SearchableItem> = mutableListOf()
    private val markerList : MutableList<Marker> = mutableListOf()
    private var selectedServiceCenter : ServiceCenterApi.ServiceCenterDataResponse? = null
    private var serviceCenterListClone : MutableList<ServiceCenterApi.ServiceCenterDataResponse> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(ServiceAppointmentViewModel::class.java)
        binding = ServiceAppointmentFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        mapFragment = childFragmentManager.findFragmentById(R.id.service_appointment_googleMap) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    private fun initViews() {
        CoRoutines.main {
            viewModel.liveServiceCenter.observe(viewLifecycleOwner, Observer { serviceCenterList ->
                if (serviceCenterList.size > 0) {
                    serviceCenterListClone = serviceCenterList
                    poisList = null
                    val plotList = mutableListOf<LekuPoi>()
                    serviceCenterList.forEachIndexed { index, serviceCenter ->
                        val location = Location("")
                        location.latitude = serviceCenter.servicecenter_location_lat.toDouble()
                        location.longitude = serviceCenter.servicecenter_location_long.toDouble()
                        val serviceName = if (viewModel.getLocale()) {
                            serviceCenter.servicecenter_name.toString()
                        } else {
                            serviceCenter.servicecenter_name_ar.toString()
                        }
                        val address = if (serviceCenter.servicecenter_address != null) {
                            serviceCenter.servicecenter_address.toString()
                        } else {
                            ""
                        }
                        plotList.add(
                            LekuPoi(
                                "${index + 1}",
                                serviceName,
                                location,
                                serviceCenter,
                                address
                            )
                        )
                        searchItemsList.add(
                            SearchableItem(
                                serviceCenter.servicecenter_id.toLong(),
                                serviceName
                            )
                        )
                    }
                    poisList = plotList
                    setPois()
                } else {
                    serviceCenterListClone = mutableListOf()
                    selectedServiceCenter = null
                }
            })
        }
        binding.serviceAppointmentSearchImg.setOnClickListener {
            openDialog()
        }
        binding.serviceAppointmentCenterNameTxt.setOnClickListener {
            openDialog()
        }
        binding.serviceAppointmentContinueBtn.setOnClickListener {
            checkServiceCenterAndProceed()
        }
    }

    private fun checkServiceCenterAndProceed() {
        if (selectedServiceCenter != null) {
            val action = ServiceAppointmentFragmentDirections.actionServiceAppointmentFragmentToSlotAppointmentFragment(
                selectedServiceCenter!!.servicecenter_id, selectedServiceCenter!!.servicecenter_name.toString()
            )
            navController.safeNavigate(action)
        }
    }

    private fun openDialog() {
        binding.serviceAppointmentBottomLayout.visibility = View.GONE

        SearchableDialog(
            requireContext(), searchItemsList, this, getString(R.string.cancel)
        ).show()

    }

    private fun setPois() {
        poisList?.let { pois ->
            if (pois.isNotEmpty()) {
                lekuPoisMarkersMap = HashMap()
                for (lekuPoi in pois) {
                    val location = lekuPoi.location
                    val marker = addPoiMarker(
                        LatLng(location.latitude, location.longitude),
                        lekuPoi.title, lekuPoi.address
                    )
                    if (marker != null) {
                        lekuPoisMarkersMap?.let {
                            it[marker.id] = lekuPoi
                        }
//                        marker.showInfoWindow()
                        markerList.add(marker)
                    }
                }

                map?.setOnMarkerClickListener { marker ->
                    lekuPoisMarkersMap?.let { poisMarkersMap ->
                        val lekuPoi = poisMarkersMap[marker.id]
                        lekuPoi?.let {
                            centerToPoi(it)
                        }
                    }
                    marker.showInfoWindow()
                    true
                }

//                centerToPoi(pois[0])
            }
        }
    }

    private fun centerToPoi(lekuPoi: LekuPoi) {
        map?.let {
            val location = lekuPoi.location
            var marker : Marker? = null
            markerList?.forEach {
                if (it.title.equals(lekuPoi.title))
                    marker = it
            }
            marker?.showInfoWindow()
            val cameraPosition = CameraPosition.Builder()
                .target(
                    LatLng(
                        location.latitude,
                        location.longitude
                    )
                ).zoom(14f).build()
            it.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
        selectedServiceCenter = lekuPoi.center
        println(selectedServiceCenter?.servicecenter_id)
        binding.serviceAppointmentCenterNameTxt.setText(lekuPoi.title)
        binding.serviceAppointmentBottomLayout.visibility = View.VISIBLE
    }

    private fun addPoiMarker(latLng: LatLng, title: String, address: String): Marker? {
        return map?.addMarker(
            MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(title)
                .snippet(address)
        )
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    private fun goToMapRoute(latLng: LatLng) {
        try {
            val gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${latLng.latitude},${latLng.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            startActivity(mapIntent)
        } catch (e: Exception) {
            view?.context?.toast(viewModel.appContext.getLocaleStringResource(R.string.something_wrong))
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        if (map == null) {

            googleMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter{
                override fun getInfoWindow(p0: Marker): View? {
                    return null
                }

                override fun getInfoContents(p0: Marker): View? {
                    val view = layoutInflater.inflate(R.layout.map_info_window_custom_layout, null)
                    view?.title?.text = p0.title
                    view?.address?.text = p0.snippet
                    return view
                }

            })
            googleMap.setOnInfoWindowClickListener(this)
            map = googleMap
            initViews()
            viewModel.fetchServiceCenter()
        }
    }

    override fun onSearchableItemClicked(item: SearchableItem, position: Int) {
        poisList?.let { poi ->
            centerToPoi(poi[position])
        }
    }

    override fun onInfoWindowClick(p0: Marker) {
        goToMapRoute(p0.position)
    }

    override fun onDismissDialog() {
        binding.serviceAppointmentBottomLayout.visibility = View.VISIBLE
    }

    override fun onStarted() {
        showProgress()
    }

    override fun onSuccess() {
        hideProgress()
    }

    override fun onFailure() {
        hideProgress()
    }

    override fun onError() {
        hideProgress()
    }

    override fun onNoNetwork() {
        hideProgress()
    }

    override fun onResume() {
        super.onResume()
        showToolBarContent()
        mapFragment?.onResume()
        hideKeyboard()
        binding.serviceAppointmentBottomLayout.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        mapFragment?.onDestroyView()
        super.onDestroyView()
    }

}