package com.example.solutiontofarming;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.solutiontofarming.data.AgriLand;
import com.example.solutiontofarming.data.Extras;
import com.example.solutiontofarming.getallapicalls.FetchAllLands;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowAvailableAgriLandsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    TextView textViewLandAddress, textViewLandArea, textViewLandRent, textViewLandType;
    ListView listViewAgriLand;
    NewAgriLandAdapter newAgriLandAdapter;
    List<AgriLand> availableLands;

    ShimmerFrameLayout shimmer;
    ProgressDialog progressDialog;

    String TAG = "Fetch_Lands";

    private static final String API_URL = Extras.API_URL +"/find/agri_lands";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_available_agri_lands);
        bindComponents();
        getSupportActionBar().setTitle("All Lands");

        shimmer = findViewById(R.id.shimmer_activity_available_lands);
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmerAnimation();

        progressDialog = new ProgressDialog(ShowAvailableAgriLandsActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


//        availableRides = (List<AgriculturalLand>) getIntent().getSerializableExtra("availableLand");
//
//        listViewAgriLand = findViewById(R.id.list_available_agri_lands);
//        agriculturalLandAdapter = new AgriculturalLandAdapter(this, (ArrayList<AgriculturalLand>) availableRides);
//        listViewAgriLand.setAdapter(agriculturalLandAdapter);
//        listViewAgriLand.setOnItemClickListener(this);

        availableLands = new ArrayList<>();

        fetch_all_lands();

        listViewAgriLand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AgriLand selectedLand = (AgriLand) newAgriLandAdapter.getItem(position);
                Intent showLandDetailsIntent = new Intent(getApplicationContext(),ShowAgriLandDetailsNew.class);
                showLandDetailsIntent.putExtra("EXTRA_SELECTED_LAND",selectedLand);
                startActivity(showLandDetailsIntent);
            }
        });

    }

    public void fetch_all_lands(){

        FetchAllLands fetchAllLands = new FetchAllLands(this);

        fetchAllLands.fetchData(new FetchNews.ApiResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(i);

                        JsonObject jsonObject = new Gson().fromJson(responseObj.toString(), JsonObject.class);
                        AgriLand agriLand = new Gson().fromJson(jsonObject, AgriLand.class);
                        availableLands.add(agriLand);
//                        Log.d("TAG", "onResponse:jsonObject "+responseObj.toString());
//                        Log.d("TAG", "onResponse:mapped Java "+agriLand.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                progressDialog.dismiss();
                shimmer.stopShimmerAnimation();
                shimmer.setVisibility(View.INVISIBLE);

                newAgriLandAdapter = new NewAgriLandAdapter(ShowAvailableAgriLandsActivity.this,(ArrayList<AgriLand>) availableLands);
                listViewAgriLand.setAdapter(newAgriLandAdapter);
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                shimmer.stopShimmerAnimation();
                shimmer.setVisibility(View.INVISIBLE);
                Toast.makeText(ShowAvailableAgriLandsActivity.this, "Unable to Fetch Available Lands. Try Again.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void bindComponents(){
        this.listViewAgriLand = findViewById(R.id.list_available_agri_lands);
        this.textViewLandAddress = findViewById(R.id.text_field_location);
        this.textViewLandArea = findViewById(R.id.text_field_area);
        this.textViewLandRent = findViewById(R.id.text_field_rent);
        this.textViewLandType = findViewById(R.id.text_agri_land_type_row);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

////        AgriculturalLand agriculturalLand = (AgriculturalLand) agriculturalLandAdapter.getItem(position);
//
//        Intent intent = new Intent(getApplicationContext(),AddAgriLandActivity.class);
//        intent.putExtra("display",true);
//        intent.putExtra("displayAgriLand",agriculturalLand);
//        startActivity(intent);
    }
}