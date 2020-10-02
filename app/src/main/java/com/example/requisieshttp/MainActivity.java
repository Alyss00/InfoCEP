package com.example.requisieshttp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.GetChars;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.requisieshttp.api.CEPinteface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button botaoRecuperar;
    private TextView textoResultado;
    public EditText EditCEP;
    private ProgressBar progressBar;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        botaoRecuperar = findViewById(R.id.buttonRecuperar);
        textoResultado = findViewById(R.id.textResultado);
        progressBar = findViewById(R.id.progressBarCircular);
        EditCEP = findViewById(R.id.EditCEP);
        progressBar.setVisibility(View.GONE);







        botaoRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String editCEP = EditCEP.getText().toString();

                retrofit = new Retrofit.Builder()
                        .baseUrl("https://viacep.com.br/ws/" +editCEP +"/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RetornarCEP();





            }
        });
    }

    public void RetornarCEP(){
        CEPinteface cepinteface = retrofit.create(CEPinteface.class);
        Call<CEP> call = cepinteface.getCEP();

        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(Call<CEP> call, Response<CEP> response) {
                if(response.isSuccessful()){
                    textoResultado.setText(response.body().getBairro() + response.body().getCep());
                }else {
                    textoResultado.setText(response.message());
                }

            }

            @Override
            public void onFailure(Call<CEP> call, Throwable t) {
                textoResultado.setText(t.getMessage());

            }
        });
    }



}
