package com.sandeep.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn_cityid,btn_getweatherbycityid,btn_getweatherbycityname;
    EditText edt;
    ListView list;
    WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assing values to each control on the layout
        btn_cityid = findViewById(R.id.btn_getCityID);
        btn_getweatherbycityid = findViewById(R.id.btn_getweatherbyCityID);
        btn_getweatherbycityname = findViewById(R.id.btn_getweatherbyCityname);
        edt = findViewById(R.id.txt_cityname);
        list = findViewById(R.id.lst);

        //setting click listener for each button

        btn_cityid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                weatherDataService.getcityId(edt.getText().toString(), new WeatherDataService.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,"Returned value is : "+response,Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

        btn_getweatherbycityid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weatherDataService.getweatherbycityid(edt.getText().toString(), new WeatherDataService.ForecastResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this,"Oops Something Went wrong!!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherModelData> weatherModels) {
                        ArrayAdapter arrayadapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,weatherModels);
                        list.setAdapter(arrayadapter);
                    }
                });
            }
        });

        btn_getweatherbycityname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weatherDataService.getweathercityname(edt.getText().toString(), new WeatherDataService.Getweahterforecastbynamecallback() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this,"Oops Something Went wrong!!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherModelData> weatherModels) {
                        ArrayAdapter arrayadapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,weatherModels);
                        list.setAdapter(arrayadapter);
                    }
                });
            }
        });
    }
}