package com.sid1804492.bottomnavtest.ui.today

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.android.material.tabs.TabLayoutMediator
import com.sid1804492.bottomnavtest.ApiRequests
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.databinding.FragmentTodayBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.net.URL
import kotlin.math.roundToInt

const val BASE_URL = "https://api.openweathermap.org/"
const val PERMISSION_ID = 1000

class TodayFragment : Fragment() {

    private lateinit var todayViewModel: TodayViewModel
    private lateinit var binding: FragmentTodayBinding

    //Locaion Variables
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_today, container, false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val viewModelFactory = TodayViewModelFactory(dataSource, application)

        todayViewModel =
            ViewModelProvider(this, viewModelFactory).get(TodayViewModel::class.java)

        binding.todayViewModel = todayViewModel
        binding.lifecycleOwner = this

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        todayViewModel.locOp.observe(viewLifecycleOwner, Observer { it ->
            if(!checkPermission() && it == null) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                builder.apply {
                    setPositiveButton("Yes, Enable Location",
                        DialogInterface.OnClickListener { dialog, which ->
                            todayViewModel.createLoc(true)
                            getLastLocation()
                        })
                    setNegativeButton("No Thanks",
                        DialogInterface.OnClickListener{ dialog, which ->
                            todayViewModel.createLoc(false)
                        })
                }
                builder.setMessage("This app uses your location to show you weather information. We don't store your location, but we will remember your preference.").setTitle("Enable Location?")
                builder.show()
            } else if (it != null) {
                if (!checkPermission() && it.value) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    builder.apply {
                        setPositiveButton("Yes, Enable Location",
                            DialogInterface.OnClickListener { dialog, which ->
                                getLastLocation()
                            })
                        setNegativeButton("No Thanks",
                            DialogInterface.OnClickListener{ dialog, which ->
                                todayViewModel.onLocUpdate(it, false)
                            })
                    }
                    builder.setMessage("This app uses your location to show you weather information. We don't store your location, but we will remember your preference.").setTitle("Enable Location?")
                    builder.show()
                }
            } else {
                getLastLocation()
            }
        })

        val fragmentAdapter = TodayAdapter(this)
        binding.todayViewPager.adapter = fragmentAdapter

        TabLayoutMediator(binding.todayFragmentTabs, binding.todayViewPager) { tab, position->
            when(position) {
                0 -> {
                    tab.text = "To-Do's"
                }
                1 -> {
                    tab.text = "Homework"
                }
                else -> {
                    tab.text = "Events"
                }
            }
        }.attach()

        todayViewModel.currentTime.observe(viewLifecycleOwner, Observer { it ->
            binding.dateHeader.text = it
        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.today_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.refresh_menu -> {
            if(checkPermission()) {
                getLastLocation()
            }
            true
        }
        R.id.location_on -> {
            if (todayViewModel.locOp.value?.value!! && !checkPermission()) {
                getLastLocation()
            } else if (!todayViewModel.locOp.value?.value!!) {
                todayViewModel.onLocUpdate(todayViewModel.locOp.value!!, true)
                getLastLocation()
            } else if (todayViewModel.locOp.value == null) {
                todayViewModel.createLoc(true)
                getLastLocation()
            }
            true
        }
        else -> {
            false
        }
    }

    private fun getCurrentData(lat: Double, lon: Double) {

        val weather = binding.weatherText
        Log.d("Get Data", "Function Triggered")
        weather.visibility = View.GONE

        val pBar = binding.loadingWeather
        pBar.visibility = View.VISIBLE

        val weatherIcon = binding.weatherIcon
        weatherIcon.visibility = View.GONE

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getWeatherData(lat, lon).awaitResponse()
                if (response.isSuccessful) {
                    val data = response.body()!!
                    Log.d("ResponseText", response.body().toString())

                    withContext(Dispatchers.Main) {
                        weather.text = data.main.temp.roundToInt().toString() + "°"
                        weather.visibility = View.VISIBLE
                        pBar.visibility = View.INVISIBLE
                        weatherIcon.text = weatherIcon(data.weather[0].icon)
                        weatherIcon.visibility = View.VISIBLE
                    }
                } else {
                    Log.d("Response Error", response.raw().request.url.toString())
                }
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Something went wrong...",
                        Toast.LENGTH_SHORT
                    )
                }
            }
        }
    }

    private fun weatherIcon(icon_code: String) : CharSequence? {
        when (icon_code) {
            "01d" -> {
                return resources.getString(R.string.wi_day_sunny)
            }
            "01n" -> {
                return resources.getString(R.string.wi_night_clear)
            }
            "02d" -> {
                return resources.getString(R.string.wi_day_cloudy)
            }
            "02n" -> {
                return resources.getString(R.string.wi_night_alt_cloudy)
            }
            "03d" -> {
                return resources.getString(R.string.wi_cloud)
            }
            "03n" -> {
                return resources.getString(R.string.wi_cloud)
            }
            "04d" -> {
                return resources.getString(R.string.wi_cloudy)
            }
            "04n" -> {
                return resources.getString(R.string.wi_cloudy)
            }
            "09d" -> {
                return resources.getString(R.string.wi_day_showers)
            }
            "09n" -> {
                return resources.getString(R.string.wi_night_alt_showers)
            }
            "10d" -> {
                return resources.getString(R.string.wi_day_rain)
            }
            "10n" -> {
                return resources.getString(R.string.wi_night_alt_rain)
            }
            "11d" -> {
                return resources.getString(R.string.wi_day_thunderstorm)
            }
            "11n" -> {
                return resources.getString(R.string.wi_night_alt_thunderstorm)
            }
            "13d" -> {
                return resources.getString(R.string.wi_day_snow)
            }
            "13n" -> {
                return resources.getString(R.string.wi_night_alt_snow)
            }
            "50d" -> {
                return resources.getString(R.string.wi_dust)
            }
            "50n" -> {
                return resources.getString(R.string.wi_dust)
            }
            else -> {
                return resources.getString(R.string.wi_na)
            }
        }
    }

    override fun onResume() {
        if(checkPermission()) {
            getLastLocation()
        }
        super.onResume()
    }

    private fun checkPermission(): Boolean {

        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false
    }

    private fun requestPermision() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getLastLocation(){
        if(checkPermission()) {
            if(isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location = task.result
                    if (location == null) {
                        getNewLocation()
                    } else {
                        getCurrentData(location.latitude, location.longitude)
                    }
                }
            } else {
                //Enable your location!
            }
        } else {
            requestPermision()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getNewLocation() {
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 2
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,locationCallback,Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation = p0.lastLocation
            getCurrentData(lastLocation.latitude, lastLocation.longitude)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d("Called", "Function Called")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Location Debug", "Permission Granted")
                getNewLocation()
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

            }
        } else {
            Log.d ("Mismatch", "reqCode: " + requestCode)
        }
    }
}