package com.example.osamakhalid.schoolsystem.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.osamakhalid.schoolsystem.Adapters.GalleryFolder_Adapter;
import com.example.osamakhalid.schoolsystem.BaseConnection.RetrofitInitialize;
import com.example.osamakhalid.schoolsystem.ConnectionInterface.ClientAPIs;
import com.example.osamakhalid.schoolsystem.Consts.Values;
import com.example.osamakhalid.schoolsystem.GlobalCalls.CommonCalls;
import com.example.osamakhalid.schoolsystem.Interfaces.ItemClickListernerPhoto;
import com.example.osamakhalid.schoolsystem.Model.GalleryData_Model;
import com.example.osamakhalid.schoolsystem.Model.GalleryResponse_Model;
import com.example.osamakhalid.schoolsystem.Model.LoginResponse;
import com.example.osamakhalid.schoolsystem.Model.ParentLoginResponse;
import com.example.osamakhalid.schoolsystem.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhotoGallery extends AppCompatActivity implements ItemClickListernerPhoto{

    RecyclerView recyclerView;
    GalleryFolder_Adapter adapter;
    private ProgressDialog progressDilouge;
    String studentID;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        //Setting up Toolbar
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("Gallery");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PhotoGallery.this, DashboardActivity.class));
            }
        });
        progressDilouge = CommonCalls.createDialouge(PhotoGallery.this,null,Values.DIALOGUE_MSG);
        getGalleryData();
    }

    public void getGalleryData(){

        Retrofit retrofit = RetrofitInitialize.getApiClient();
        ClientAPIs clientAPIs = retrofit.create(ClientAPIs.class);
        String base = null;
        if (CommonCalls.getUserType(PhotoGallery.this).equals(Values.TYPE_PARENT)) {
            ParentLoginResponse loginResponse = CommonCalls.getParentData(PhotoGallery.this);
            base = loginResponse.getUsername() + ":" + loginResponse.getPassword();
            studentID = loginResponse.getParentID();
        } else if (CommonCalls.getUserType(PhotoGallery.this).equals(Values.TYPE_STUDENT)) {
            LoginResponse loginResponse = CommonCalls.getUserData(PhotoGallery.this);
            base = loginResponse.getUsername() + ":" + loginResponse.getPassword();
            studentID = loginResponse.getStudentID();

        }
        final String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        Call<GalleryResponse_Model> call = clientAPIs.getPhotoGalleryImages(studentID,authHeader);
        call.enqueue(new Callback<GalleryResponse_Model>() {
            @Override
            public void onResponse(Call<GalleryResponse_Model> call, Response<GalleryResponse_Model> response) {

                if(response.isSuccessful()){

                    GalleryResponse_Model galleryResponse_model = response.body();
                    if(galleryResponse_model != null){
                        progressDilouge.dismiss();
                        recyclerView = findViewById(R.id.photos_recyclerView);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(PhotoGallery.this));
                        adapter = new GalleryFolder_Adapter(galleryResponse_model.getMediaData(),PhotoGallery.this);
                        adapter.setItemClickListener(PhotoGallery.this);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();


                    }else{
                        progressDilouge.dismiss();
                        Toast.makeText(PhotoGallery.this, Values.DATA_ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }

            @Override
            public void onFailure(Call<GalleryResponse_Model> call, Throwable t) {
                progressDilouge.dismiss();
                Toast.makeText(PhotoGallery.this, Values.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view,String name ,List<GalleryData_Model> galleryData_models) {

        Intent mIntent = new Intent(PhotoGallery.this, PhotoGalleryImages.class);
        mIntent.putExtra("foldername",name);
        mIntent.putParcelableArrayListExtra("Data", (ArrayList<? extends Parcelable>) galleryData_models);
        startActivity(mIntent);


    }
}
