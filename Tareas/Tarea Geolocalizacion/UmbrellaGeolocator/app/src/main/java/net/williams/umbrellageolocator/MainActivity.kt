package net.williams.umbrellageolocator

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            tvLatitude.text = String.format("%.6f", location.latitude)
            tvLongitude.text = String.format("%.6f", location.longitude)
            getAddress(location)
        }

        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
    }

    private lateinit var btnGetCoordinates: Button
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView
    private lateinit var tvAddress: TextView
    private lateinit var locationManager: LocationManager
    private lateinit var btnSendUbication: Button
    private lateinit var btnShowOnMap: Button

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
        if (isGranted){
            getLastLocation()
        }else{
            Toast.makeText(this, "No se ha Permitido el Acceso a su Ubicacion", Toast.LENGTH_SHORT).show()
        }
    }
    private var trackingLocation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        btnGetCoordinates = findViewById(R.id.btnGetCoordinates)
        tvLatitude = findViewById(R.id.textViewLatitude)
        tvLongitude = findViewById(R.id.textViewLongitude)
        tvAddress = findViewById(R.id.textViewAddress)
        btnShowOnMap = findViewById(R.id.btnShowOnMap)
        btnSendUbication = findViewById(R.id.btnSendUbication)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        tvAddress.text = "Dirección"
        btnGetCoordinates.setOnClickListener {
            checkPermission()
            getLastLocation()
            if(trackingLocation){
                btnGetCoordinates.text = "Obtener Coordenadas"
                stopTrackingLocation()
            }
        }

        btnSendUbication.setOnClickListener {
            val latitude = tvLatitude.text.toString()
            val longitude = tvLongitude.text.toString()
            val address = tvAddress.text.toString()

            val message = "Mi ubicación actual es:\nLatitud: $latitude\nLongitud: $longitude\nDirección: $address"

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
                `package` = "com.whatsapp"
            }

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show()
            }
        }

        btnShowOnMap.setOnClickListener {
            val latitude = tvLatitude.text.toString().toDoubleOrNull()
            val longitude = tvLongitude.text.toString().toDoubleOrNull()

            if (latitude != null && longitude != null) {
                val intent = Intent(this, MapActivity::class.java).apply {
                    putExtra("LATITUDE", latitude)
                    putExtra("LONGITUDE", longitude)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Coordenadas no válidas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermission(){
        when{
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> { getLastLocation() }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Toast.makeText(this, "Se necesita permiso de ubicación para continuar", Toast.LENGTH_SHORT).show()
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else ->{
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permiso de ubicación no concedido", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            if (lastLocation != null) {
                tvLatitude.text = String.format("%.6f", lastLocation.latitude)
                tvLongitude.text = String.format("%.6f", lastLocation.longitude)
                getAddress(lastLocation)
            } else {
                tvLatitude.text = "No disponible"
                tvLongitude.text = "No disponible"
                Toast.makeText(this@MainActivity, "No se encontró ubicación reciente", Toast.LENGTH_SHORT).show()
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
            trackingLocation = true
        } catch (e: SecurityException) {
            tvLatitude.text = "Error"
            tvLongitude.text = "Error"
            Toast.makeText(this@MainActivity, "Error de seguridad: ${e.message}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            tvLatitude.text = "Error"
            tvLongitude.text = "Error"
            Toast.makeText(this@MainActivity, "Error al obtener ubicación: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopTrackingLocation() {
        locationManager.removeUpdates(locationListener)
        trackingLocation = false
    }

    override fun onPause(){
        super.onPause()
        stopTrackingLocation()
    }

    private fun getAddress(location: Location){
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addressList != null && addressList.isNotEmpty()) {
                val address = addressList[0]
                val addressText = with(address) {
                    val addressFragments = (0..maxAddressLineIndex).map { getAddressLine(it) }
                    addressFragments.joinToString(separator = "\n")
                }
                tvAddress.text = addressText
            } else {
                tvAddress.text = "Dirección no encontrada"
            }
        } catch (e: Exception) {
            tvAddress.text = "Error al obtener la dirección"
            Toast.makeText(this, "Error al obtener la dirección: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}