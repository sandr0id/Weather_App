package com.sandeep.weather_app;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {

    String city_id;
    Context context;

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String response);
    }

    public void getcityId(String cityName, final VolleyResponseListener volleyResponseListener) {
        // Instantiate the RequestQueue.

        String url = "https://www.metaweather.com/api/location/search/?query=" + cityName;

// Request a string response from the provided URL.
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        city_id = "";
                        try {
                            JSONObject city_info = response.getJSONObject(0);
                            city_id = city_info.getString("woeid");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Toast.makeText(context,"City Id : "+city_id,Toast.LENGTH_SHORT).show();
                        volleyResponseListener.onResponse(city_id);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context,"Oops Something Went Wrong!!",Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something went wrong");
            }
        });


// Add the request to the RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface ForecastResponseListener {
        void onError(String message);

        void onResponse(List<WeatherModelData> weatherreportmodels);
    }

    public void getweatherbycityid(String city_id,final ForecastResponseListener forecastResponseListener) {
        List<WeatherModelData> weathearreportmodels = new ArrayList<>();

        String url = "https://www.metaweather.com/api/location/" + city_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       // Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show();

                        try {
                            JSONArray consolidated_weather = response.getJSONArray("consolidated_weather");



                            for(int i =0;i<consolidated_weather.length();i++) {

                                WeatherModelData firstday = new WeatherModelData();
                                JSONObject first_day_datafromapi = (JSONObject) consolidated_weather.get(i);

                                firstday.setId(first_day_datafromapi.getInt("id"));
                                firstday.setWeather_state_name(first_day_datafromapi.getString("weather_state_name"));
                                firstday.setWeather_state_abbr(first_day_datafromapi.getString("weather_state_abbr"));
                                firstday.setWind_direction_compass(first_day_datafromapi.getString("wind_direction_compass"));
                                firstday.setCreated(first_day_datafromapi.getString("created"));
                                firstday.setCreated(first_day_datafromapi.getString("applicable_date"));
                                firstday.setMin_temp(first_day_datafromapi.getLong("min_temp"));
                                firstday.setMax_temp(first_day_datafromapi.getLong("max_temp"));
                                firstday.setThe_temp(first_day_datafromapi.getLong("the_temp"));
                                firstday.setWind_speed(first_day_datafromapi.getLong("wind_speed"));
                                firstday.setWind_direction(first_day_datafromapi.getLong("wind_direction"));
                                firstday.setAir_pressure(first_day_datafromapi.getLong("air_pressure"));
                                firstday.setHumidity(first_day_datafromapi.getInt("humidity"));
                                firstday.setVisibility(first_day_datafromapi.getLong("visibility"));
                                firstday.setPredictability(first_day_datafromapi.getInt("predictability"));
                                weathearreportmodels.add(firstday);
                            }

                            forecastResponseListener.onResponse(weathearreportmodels);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    public interface Getweahterforecastbynamecallback{

        void onError(String message);

        void onResponse(List<WeatherModelData> weatherModelData);
    }

    public void getweathercityname(String name,final Getweahterforecastbynamecallback getweahterforecastbyname)
    {
        getcityId(name, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String response) {
                getweatherbycityid(response, new ForecastResponseListener() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherModelData> weatherreportmodels) {
                        getweahterforecastbyname.onResponse(weatherreportmodels);
                    }
                });
            }
        });
    }
}
