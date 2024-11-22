package com.example.weather_app_final

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.weather_app_final.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val apiKey = "97d7622cabe965a48bcd992af4c0d6d5"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("Chennai")
        searchCity()
    }

    private fun searchCity() {
        val searchView = binding.searchView2
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if(p0 != null){
                    fetchWeatherData(p0)
                }else{
                    Toast.makeText(this@MainActivity, "Please enter a city", Toast.LENGTH_LONG).show()
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != null) {
                    Log.d("Qerror", p0)
                }
                return true
            }

        })
    }

    private fun fetchWeatherData(city: String) {
        val retfroFit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://api.weatherstack.com/")
            .build().create(ApiInterface::class.java)

        val response = retfroFit.getWeatherData(apiKey, city)
        response.enqueue(object : Callback<DataModel>{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<DataModel>, response: Response<DataModel>) {
                val body = response.body()
//                Log.d("body", body.toString())
                if(response.isSuccessful){
                    if(body?.success == false){
                        Toast.makeText(this@MainActivity, "Please Enter a valid city name", Toast.LENGTH_LONG).show()
                        return
                    }
                    val temp = body?.current?.temperature.toString()
                    val humidity = body?.current?.humidity.toString()
                    val windSpeed = body?.current?.wind_speed.toString()
                    val uv = body?.current?.uv_index.toString()
                    val pressure = body?.current?.pressure.toString()
                    var conditions = body?.current?.weather_descriptions?.get(0)
//                    if (conditions != null) {
//                        if(conditions.indexOf(" ") > -1){
//                            conditions = conditions.split(" ")[1]
//                        }
//                    }
                    val visibility = body?.current?.visibility.toString()
                    val localTime = body?.current?.observation_time.toString()
                    val country = body?.location?.country.toString()
                    val date = body?.location?.localtime.toString().split(" ")[0]
                    val day = findDayOfWeek(date)
                    binding.temperature.text = "$temp °C"
                    binding.condition.text = conditions
                    binding.humidity.text = "$humidity %"
                    binding.windspeed.text = "$windSpeed mph"
                    binding.uv.text = uv
                    binding.visiblility.text = "$visibility km"
                    binding.maxTemp.text = country
                    binding.minTemp.text = body?.location?.localtime.toString().split(" ")[1]
                    binding.day.text = day
                    binding.date.text = date
                    binding.weather.text = conditions
                    binding.sealevel.text = "$pressure hPa"
                    binding.cityName.text = city
                    //Log.d("Tag", "onResponse $temp")

                    body?.current?.weather_descriptions?.get(0)
                        ?.let { changeImagesAccordingToWeatherConditions(it) }

                }else{
                    Toast.makeText(this@MainActivity, "Please Enter a valid city name", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DataModel>, t: Throwable) {
                Log.d("Error", "${t.message}")
            }

        })

    }

    private fun changeImagesAccordingToWeatherConditions(conditon: String) {
        when (conditon){
            "Hazy", "Partly cloudy", "Mist", "Intervals of clouds and sunshine", "Fog", "Mist", "Freezing Fog" ->{
                binding.root.setBackgroundResource(R.drawable.cloud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Clear ", "Sunny" , "Mostly sunny" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sunny)
            }
            "Snow", "Light snow",
                    "Heavy snow" ,
                    "Ice pellets" ,
                    "Snow shower" ,
                    "Moderate or heavy snow showers" ,
                    "Blizzard" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
                    }
            "Light rain" , "Overcast",
                    "Heavy rain" ,
                    "Patchy rain possible" ,
                    "Light drizzle" ,
                    "Heavy drizzle" ,
                    "Rain shower" ,
                    "Torrential rain shower" ->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
                    }
        }
        binding.lottieAnimationView.playAnimation()
    }
}