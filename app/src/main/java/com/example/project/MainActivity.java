package com.example.project;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private EditText ifscCodeEdt;
    private TextView bankDetailsTV;

    String ifscCode;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ifscCodeEdt = findViewById(R.id.idedtIfscCode);
        Button getBankDetailsBtn = findViewById(R.id.idBtnGetBankDetails);
        bankDetailsTV = findViewById(R.id.idTVBankDetails);
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        getBankDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifscCode = ifscCodeEdt.getText().toString();
                if (TextUtils.isEmpty(ifscCode)) {
                    Toast.makeText(MainActivity.this, "Please enter valid IFSC code", Toast.LENGTH_SHORT).show();
                } else {
                    getDataFromIFSCCode(ifscCode);
                }
            }
        });
    }

    private void getDataFromIFSCCode(String ifscCode) {

        mRequestQueue.getCache().clear();
        String url = "http://api.techm.co.in/api/v1/ifsc/" + ifscCode;
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("failed")) {
                        bankDetailsTV.setText("Invalid IFSC Code");
                    } else {
                        JSONObject dataObj = response.getJSONObject("data");
                        String state = dataObj.optString("STATE");
                        String bankName = dataObj.optString("BANK");
                        String branch = dataObj.optString("BRANCH");
                        String address = dataObj.optString("ADDRESS");
                        String contact = dataObj.optString("CONTACT");
                        String micrcode = dataObj.optString("MICRCODE");
                        String city = dataObj.optString("CITY");
                        bankDetailsTV.setText("Bank Name : " + bankName + "\nBranch : " + branch + "\nAddress : " + address + "\nMICR Code : " + micrcode + "\nCity : " + city + "\nState : " + state + "\nContact : " + contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    bankDetailsTV.setText("Invalid IFSC Code");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                bankDetailsTV.setText("Invalid IFSC Code");
            }
        });
        queue.add(objectRequest);
    }

    public void pincodeActivity(View v)
    {
        Intent i = new Intent(this,Activity2.class);
        startActivity(i);
    }

}
