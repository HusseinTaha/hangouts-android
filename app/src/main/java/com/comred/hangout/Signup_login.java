package com.comred.hangout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.comred.Controls.Combo;
import com.comred.Controls.ErrorAlert;
import com.comred.Glob.Global;
import com.comred.JsonDATA.JsonContact;
import com.comred.JsonDATA.JsonCountry;
import com.comred.JsonDATA.JsonInit;
import com.comred.JsonDATA.JsonLogin;
import com.comred.JsonDATA.JsonSchedules;
import com.comred.JsonDATA.JsonSignUp;
import com.comred.JsonDATA.JsonWall;
import com.comred.mylib.Connection.HttpRequest;
import com.comred.mylib.Crypto.StringXORer;
import com.comred.mylib.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/*
* the signup-login activity is to make an account in hangout services.
 * or sign in if u are an existing client.
 * */

public class Signup_login extends Activity {

    private RadioGroup rg_signOptions;
    private AQuery ajax;
    private int height, width;
    private boolean signin = false;
    private Combo cbCountries;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_login);

        Global.SetContext(this);
        if (Global.LoggedIn()) {
            Intent i = new Intent(Signup_login.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        width = Global.getwidth();
        height = Global.getheight();
        ajax = new AQuery(this);
        ajax.id(R.id.img_logo).getView().getLayoutParams().width = width * 84 / 100;
        ajax.id(R.id.img_logo).getView().getLayoutParams().height = height * 30 / 100;
        ((RelativeLayout.LayoutParams) ajax.id(R.id.img_logo).getView().getLayoutParams()).setMargins(0, 2 * height / 100, 0, 0);

        int scale = 5;
        ajax.id(R.id.tv_countryCode).getView().getLayoutParams().height = height * scale / 100;
        ajax.id(R.id.tv_phone).getView().getLayoutParams().height = height * scale / 100;
        ajax.id(R.id.tv_name).getView().getLayoutParams().height = height * scale / 100;
        ajax.id(R.id.tv_email).getView().getLayoutParams().height = height * scale / 100;
        ajax.id(R.id.tv_password).getView().getLayoutParams().height = height * scale / 100;

        //friend image
        ajax.id(R.id.iv_friendsImage).getView().getLayoutParams().height = height * 26 / 100;
        ajax.id(R.id.iv_friendsImage).getView().getLayoutParams().width = width * 55 / 100;

        scale = 14;
        ajax.id(R.id.tv_countryCode).getView().getLayoutParams().width = width * scale / 100;
        ajax.id(R.id.tv_phone).getView().getLayoutParams().width = width * scale / 100;
        ajax.id(R.id.tv_name).getView().getLayoutParams().width = width * scale / 100;
        ajax.id(R.id.tv_email).getView().getLayoutParams().width = width * scale / 100;
        ajax.id(R.id.tv_password).getView().getLayoutParams().width = width * scale / 100;

        ajax.id(R.id.tv_countryCode).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, width * 3 / 100);
        ajax.id(R.id.tv_phone).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, width * 3 / 100);
        ajax.id(R.id.tv_name).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, width * 3 / 100);
        ajax.id(R.id.tv_email).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, width * 3 / 100);
        ajax.id(R.id.tv_password).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, width * 3 / 100);

        ajax.id(R.id.tv_fragmentTitle).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, width * 5 / 100);

        rg_signOptions = (RadioGroup) findViewById(R.id.rg_signOptions);
        cbCountries = (Combo) findViewById(R.id.et_countryCode);


        ajax.id(R.id.ll_items).getView().getLayoutParams().width = 70 * width / 100;

        scale = 11;
        ajax.id(R.id.et_countryCode).getView().getLayoutParams().width = scale * width / 100;

        scale = 5;
        ajax.id(R.id.et_countryCode).getView().getLayoutParams().height = height * scale / 100;
        ajax.id(R.id.et_phone).getView().getLayoutParams().height = height * scale / 100;
        ajax.id(R.id.et_email).getView().getLayoutParams().height = height * scale / 100;
        ajax.id(R.id.et_name).getView().getLayoutParams().height = height * scale / 100;
        ajax.id(R.id.et_password).getView().getLayoutParams().height = height * scale / 100;

        /*
        ajax.id(R.id.et_countryCode).height(scale*height/100);
        ajax.id(R.id.et_phone).height(scale*height/100);
        ajax.id(R.id.et_email).height(scale*height/100);
        ajax.id(R.id.et_name).height(scale*height/100);
        ajax.id(R.id.et_password).height(scale * height / 100);*/

//        ajax.id(R.id.et_countryCode).width(8*width/100);

        ajax.id(R.id.rl_parent).getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                try {
                    Utility.hideKeyboard(Signup_login.this, ajax.id(R.id.rl_parent).getView());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        final Button submitBtn = ajax.id(R.id.btn_submit).getButton();

        submitBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        LightingColorFilter lcf = new LightingColorFilter(0x69CDD6, 0);
                        submitBtn.getBackground().setColorFilter(lcf);
                        submitBtn.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        Submit(submitBtn);
                    case MotionEvent.ACTION_CANCEL: {
                        submitBtn.getBackground().clearColorFilter();
                        submitBtn.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

        rg_signOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.rb_signin) {
                    submitBtn.setText("SignIn");
                    signin = true;
                    ajax.id(R.id.ll_email).animate(Global.outToRightAnimation(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ajax.id(R.id.ll_email).visibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    }));

                    ajax.id(R.id.ll_name).animate(Global.outToRightAnimation(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ajax.id(R.id.ll_name).visibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    }));
                } else {
                    submitBtn.setText("SignUp");
                    signin = false;
                    ajax.id(R.id.ll_email).visibility(View.INVISIBLE);
                    ajax.id(R.id.ll_name).visibility(View.INVISIBLE);
                    ajax.id(R.id.ll_email).animate(Global.inFromRightAnimation(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ajax.id(R.id.ll_email).visibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    }));

                    ajax.id(R.id.ll_name).animate(Global.inFromRightAnimation(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ajax.id(R.id.ll_name).visibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    }));
                }
            }

        });
        loadCountries();
    }


    // function to load the countries records and zipcode to the combobox control
    private void loadCountries() {
        if (Global.pref().getBoolean("isCountriesAvailable", false)) {
            String scountries = Global.pref().getString("Countries", "");
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<JsonCountry>>() {
            }.getType();
            // save index and top position
            List<JsonCountry> countries = (List<JsonCountry>) (Object) gson.fromJson(scountries, listType);
            JsonCountry.data = countries;
            cbCountries.DataSource = countries;
            try {
                int cc = Integer.parseInt(Global.GetCountryZipCode());
                int id = JsonCountry.getNumfromCountryCode(cc);
                cbCountries.SetSelectedValue(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // on btn (signup or singin) click
    public void Submit(View v) {

        JsonCountry jc = (JsonCountry) ((Combo) ajax.id(R.id.et_countryCode).getView()).getSelectedValue();
        String phone = ajax.id(R.id.et_phone).getText().toString();
        String pass = ajax.id(R.id.et_password).getText().toString();

        if (jc == null) {
            ((Combo) ajax.id(R.id.et_countryCode).getView()).setError("CountryCode is missing!");
            return;
        }
        if (phone == null || phone.equals("")) {
            ajax.id(R.id.et_phone).getEditText().setError("Phone is required !");
            return;
        }


        // user choose to sign in (Account exist)
        if (signin) {
            //validate country code, phone number , password (not empty)
            if (pass == null || pass.equals("")) {
                ajax.id(R.id.et_password).getEditText().setError("Password is required !");
                return;
            }
            ajax.id(R.id.btn_submit).enabled(false);
            login(jc.ID, jc.Phone_ext, phone, pass);

        }
        //user choose to signup (no account)
        else {
            //validate country code, phone number , password,  email, name  (not empty and well formed)
            String name = ajax.id(R.id.et_name).getText().toString();
            String email = ajax.id(R.id.et_email).getText().toString();

            if (name == null || name.equals("")) {
                ajax.id(R.id.et_name).getEditText().setError("Name is required !");
                return;
            }
            if (email == null || email.equals("")) {
                ajax.id(R.id.et_email).getEditText().setError("Email is required !");
                return;
            }
            if (pass == null || pass.equals("")) {
                ajax.id(R.id.et_password).getEditText().setError("Password is required !");
                return;
            }
            ajax.id(R.id.btn_submit).enabled(false);
            signUp(jc.ID, jc.Phone_ext, phone, name, email, pass);
        }

    }


    public void login(int CountryID, int country, String phone, String pass) {
        progressDialog = new ProgressDialog(Global.getmContext());
        progressDialog.setTitle("SignIn");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Gson gson = new Gson();
        JsonLogin json = new JsonLogin();

        json.Mobile = phone;
        json.Country = CountryID;
        json.Mac_Address = Utility.getMac(this);
        json.Token = Utility.sha1(country + ":" + phone + ":" + pass);
        Global.Token(json.Token);
        Global.saveStringPref("CountryCode", country + "");
        Global.saveStringPref("Mobile", phone);

        // Add your data
        Log.d("json", gson.toJson(json));
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("json", gson.toJson(json)));

        //decrypt url
        StringXORer stringXORer = new StringXORer();
        String x = stringXORer.decode(StringXORer.ReverseS(Global.URL()), this.getPackageName());

        HttpRequest d = new HttpRequest(uiHandler, x + "LoginHandler.ashx", nameValuePairs);
        d.start();
    }

    public void signUp(int CountryID, int country, String phone, String name, String email, String pass) {
        progressDialog = new ProgressDialog(Global.getmContext());
        progressDialog.setTitle("SignUp");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Gson gson = new Gson();
        JsonSignUp json = new JsonSignUp();

        json.Name = name;
        json.Email = email;
        json.Mobile = phone;
        json.Country = CountryID;
        json.Mac_Address = Utility.getMac(this);
        json.Token = Utility.sha1(country + ":" + json.Mobile + ":" + pass);
        Global.Token(json.Token);
        Global.saveStringPref("CountryCode", country + "");
        Global.saveStringPref("Mobile", phone);
        Global.saveStringPref("Name", name);

        // Add your data
        Log.d("json", gson.toJson(json));
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("json", gson.toJson(json)));

        StringXORer stringXORer = new StringXORer();
        String x = stringXORer.decode(StringXORer.ReverseS(Global.URL()), this.getPackageName());


        HttpRequest d = new HttpRequest(uiHandler, x + "SignupHandler.ashx", nameValuePairs);
        d.start();
    }


    Handler uiHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            ajax.id(R.id.btn_submit).enabled(true);
            Bundle b = msg.getData();
            Exception ex = new Exception("Error in Connection...");
            try {
                progressDialog.dismiss();
                boolean hasError = b.getBoolean("hasError");
                String Response = b.getString("Response");

                if (!hasError && Response != null && !Response.equals("")) {


                    if (Response.contains("true") || Response.contains("True")) {    // signup completed.
                        Global.setLoggedIn(true);
                        Global.saveToken();
                        if (!Response.equalsIgnoreCase("true")) {
                            StringTokenizer st = new StringTokenizer(Response, ":");
                            String name = st.nextToken();
                            if (name != null)
                                Global.saveStringPref("Name", name);
                        }

                        Global.readContacts("InitHandler.ashx", contactsHAndler);
                        progressDialog.setTitle("Getting Data");
                        progressDialog.setMessage("Please wait...");
                        progressDialog.show();
                        Global.init(initHandler);
                    } else {
                        //false logout. user deja existant
                        Global.Token("");
                        ErrorAlert ea = new ErrorAlert(Signup_login.this);
                        ea.showErrorDialog("Hangout", "Unsuccessful login/signup\nPlease try again..");

                    }


                } else {
                    ErrorAlert ea = new ErrorAlert(Signup_login.this);
                    ea.showErrorDialog("Hangout", ex.getMessage());
                }

            } catch (Exception e) {

                // TODO: handle exception
                Utility.showtoast(getApplicationContext(), "Error in Connection ...");
                this.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);

            }
        }

        ;
    };


    Handler contactsHAndler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            ajax.id(R.id.btn_submit).enabled(true);
            Bundle b = msg.getData();

            finish();
        }

        ;
    };


    Handler initHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            Bundle b = msg.getData();
            Exception ex = new Exception("Error in Connection...");
            try {

                boolean hasError = b.getBoolean("hasError");
                String Response = b.getString("Response");

                progressDialog.dismiss();
                if (!hasError && Response != null && !Response.equals("") && !Response.equals("False")) {

                    Gson gson = new Gson();
                    JsonInit parser = gson.fromJson(Response, JsonInit.class);
                    if (parser.isContacts)
                        JsonContact.data = parser.contacts;
                    if (parser.isCountries) {
                        JsonCountry.data = parser.countries;
                        SharedPreferences.Editor editor = Global.pref().edit();
                        editor.putString("Countries", gson.toJson(parser.countries));
                        editor.putBoolean("isCountriesAvailable", true);
                        editor.commit();
                    }
                    if (parser.isSchedules)
                        JsonSchedules.data = parser.schedules;
                    if (parser.isWalls) {
                        JsonWall.data = parser.walls;
                        JsonWall.maxID();
                    }
                    Intent i = new Intent(Signup_login.this, MainActivity.class);
                    startActivity(i);

                } else {
                    Utility.showtoast(getApplicationContext(), "Error in Connection ...");
                    this.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000);
                }

            } catch (Exception e) {
                // TODO: handle exception
                Utility.showtoast(getApplicationContext(), "Error in Connection ...");
                this.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);

            }
        }

        ;
    };


}
