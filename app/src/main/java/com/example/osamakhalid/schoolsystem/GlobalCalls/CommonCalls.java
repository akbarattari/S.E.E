package com.example.osamakhalid.schoolsystem.GlobalCalls;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;

import com.example.osamakhalid.schoolsystem.Model.LoginResponse;
import com.example.osamakhalid.schoolsystem.Model.ParentLoginResponse;
import com.example.osamakhalid.schoolsystem.Model.ParentModels.ParentStudentData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by HAMI on 19/02/2018.
 */

public class CommonCalls extends AppCompatActivity {

    public static void saveUserData(LoginResponse loginResponse,Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("UserData", 0);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginResponse);
        prefsEditor.putString("UserObject", json);
        prefsEditor.commit();
    }
    public static void saveChildrenOfParentList(Context context, List<ParentStudentData> list){
        SharedPreferences mPrefs = context.getSharedPreferences("ParentChildren", 0);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        prefsEditor.putString("ParentChildrenList", json);
        prefsEditor.commit();
    }

    public static List<ParentStudentData> getChildrenOfParentList(Context context){
        SharedPreferences mPrefs = context.getSharedPreferences("ParentChildren", 0);
        Gson gson = new Gson();
        String json = mPrefs.getString("ParentChildrenList", "");
        Type type = new TypeToken<List<ParentStudentData>>(){}.getType();
        List<ParentStudentData> children = gson.fromJson(json, type);
        return children;
    }
    public static void saveParentData(ParentLoginResponse parentLoginResponse, Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("ParentData", 0);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(parentLoginResponse);
        prefsEditor.putString("ParentObject", json);
        prefsEditor.commit();
    }

    public static LoginResponse getUserData(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("UserData", 0);
        Gson gson = new Gson();
        String json = mPrefs.getString("UserObject", "");
        LoginResponse userData = gson.fromJson(json, LoginResponse.class);
        return userData;
    }

    public static ParentLoginResponse getParentData(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("ParentData", 0);
        Gson gson = new Gson();
        String json = mPrefs.getString("ParentObject", "");
        ParentLoginResponse parentData = gson.fromJson(json, ParentLoginResponse.class);
        return parentData;
    }

    public static void saveUserType(String type,Context context ){
        SharedPreferences mPrefs = context.getSharedPreferences("UserTypeData", 0);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("UserType", type);
        prefsEditor.commit();
    }

    public static String getUserType(Context context){
        SharedPreferences mPrefs = context.getSharedPreferences("UserTypeData", 0);
        String type = mPrefs.getString("UserType", "");
        return type;
    }

    public static String getCurrentDate(){
        Date now = new Date();
        Date alsoNow = Calendar.getInstance().getTime();
        String nowAsString = new SimpleDateFormat("yyyy-MM-dd").format(now);

    return nowAsString;

    }


    public static ProgressDialog createDialouge(Context context,String title,String msg){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(msg);
        progressDialog.show();
        return progressDialog;
    }

    public static String getMonth(String month) {

        switch (month) {

            case "January":
                return "01";
            case "Feburary":
                return "02";
            case "March":
                return "03";
            case "April":
                return "04";
            case "May":
                return "05";
            case "June":
                return "06";
            case "July":
                return "07";
            case "August":
                return "08";
            case "September":
                return "09";
            case "October":
                return "10";
            case "November":
                return "11";
            case "December":
                return "12";
            default:
                return "null";
        }
    }

    public static Bitmap getImage(String urlString){
        URL url = null;
        Bitmap bmp = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
             bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }




}
