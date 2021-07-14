package com.example.project;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Activity2 extends AppCompatActivity {
    private EditText pinCodeEdt;
    private Button getDataBtn;
    private TextView pinCodeDetailsTV;

    String pinCode;

    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        pinCodeEdt = findViewById(R.id.idedtPinCode);
        getDataBtn = findViewById(R.id.idBtnGetCityandState);
        pinCodeDetailsTV = findViewById(R.id.idTVPinCodeDetails);

        mRequestQueue = Volley.newRequestQueue(Activity2.this);

        getDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCode = pinCodeEdt.getText().toString();

                if (TextUtils.isEmpty(pinCode)) {
                    Toast.makeText(Activity2.this, "Please enter valid pin code", Toast.LENGTH_SHORT).show();
                } else {
                    getDataFromPinCode(pinCode);
                }
            }
        });
    }

    private void getDataFromPinCode(String pinCode) {

        mRequestQueue.getCache().clear();
        String url = "http://www.postalpincode.in/api/pincode/" + pinCode;
        RequestQueue queue = Volley.newRequestQueue(Activity2.this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray postOfficeArray = response.getJSONArray("PostOffice");
                    if (response.getString("Status").equals("Error")) {
                        pinCodeDetailsTV.setText("Pin code is not valid.");
                    } else {
                        JSONObject obj = postOfficeArray.getJSONObject(0);
                        String district = obj.getString("District");
                        String state = obj.getString("State");
                        String country = obj.getString("Country");
                        pinCodeDetailsTV.setText("Details of pin code is : \n" + "District is : " + district + "\n" + "State : "
                                + state + "\n" + "Country : " + country);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pinCodeDetailsTV.setText("Pin code is not valid");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Activity2.this, "Pin code is not valid.", Toast.LENGTH_SHORT).show();
                pinCodeDetailsTV.setText("Pin code is not valid");
            }
        });
        queue.add(objectRequest);
    }
}