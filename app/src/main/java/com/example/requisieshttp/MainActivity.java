package com.example.requisieshttp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.requisieshttp.api.CEPinteface;
import com.example.requisieshttp.model.CEP;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
    public static final String MYPREFERENCES = "nightModePrefs";
    public static final String KEY_ISNIGHTMODE = "isNightMode";

    private Button botaoRecuperar;
    public EditText EditCEP;
    private Retrofit retrofit;
    private TextView textCEP;
    private TextView textLogradouro;
    private TextView textBairro;
    private TextView textLocalidade;
    private TextView textComplemento;
    private TextView textUF;
    private TextView textIBGE;
    private TextView textGIA;
    private TextView textDDD;
    private TextView textSIAFI;
    private ProgressBar ProgressBarCircular;
    private Switch switchMode;
    private FloatingActionButton fab;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botaoRecuperar = findViewById(R.id.buttonRecuperar);
        EditCEP = findViewById(R.id.EditCEP);
        textCEP = findViewById(R.id.textCEP);
        textLogradouro = findViewById(R.id.textLogradouro);
        textComplemento = findViewById(R.id.textComplemento);
        textLocalidade = findViewById(R.id.textLocalidade);
        textBairro = findViewById(R.id.textBairro);
        textUF = findViewById(R.id.textUF);
        textIBGE = findViewById(R.id.textIBGE);
        textGIA = findViewById(R.id.textGIA);
        textDDD = findViewById(R.id.textDDD);
        textSIAFI = findViewById(R.id.textSIAFI);
        ProgressBarCircular = findViewById(R.id.progressBarCircular);
        switchMode = findViewById(R.id.switch1);
        fab = findViewById(R.id.FABsave);
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        checkNightMode();
        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked){
                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                   saveNightModeState(true);
                   recreate();
               }else{
                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                   saveNightModeState(false);
                   recreate();
               }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Ação ao clicar no botão", Snackbar.LENGTH_LONG).setAction("texto", null);
                Intent Activity =  new Intent(MainActivity.this, SaveActivity.class);
                startActivity(Activity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });




        botaoRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String editCEP = EditCEP.getText().toString();

                if(EditCEP.length() < 8) {
                    Toast.makeText(getApplicationContext(),"O endereço CEP precisa ter 8 Digítos",Toast.LENGTH_SHORT).show();
                }else{
                    retrofit = new Retrofit.Builder()
                            .baseUrl("https://viacep.com.br/ws/" + editCEP + "/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    limparTextview();
                    RetornarCEP();
                }
            }
        });
    }

    private void saveNightModeState(boolean nightMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ISNIGHTMODE, nightMode);
        editor.apply();
    }

    public void checkNightMode(){
        if(sharedPreferences.getBoolean(KEY_ISNIGHTMODE, false)){
            switchMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            switchMode.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void RetornarCEP(){
        CEPinteface cepinteface = retrofit.create(CEPinteface.class);
        Call<CEP> call = cepinteface.getCEP();
        ProgressBarCircular.setVisibility(VISIBLE);
        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(Call<CEP> call, Response<CEP> response) {

                if (response.body().getErro() == "true") {
                    ProgressBarCircular.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Endereço CEP não encontrado", Toast.LENGTH_SHORT).show();
                }
                else if(response.isSuccessful()) {
                    textCEP.setText("CEP: " + response.body().getCep());
                    textLogradouro.setText("Logradouro: " + response.body().getLogradouro());
                    textComplemento.setText("Complemento: " + response.body().getComplemento());
                    textBairro.setText("Bairro: " + response.body().getBairro());
                    textLocalidade.setText("Localidade: " + response.body().getLocalidade());
                    textUF.setText("UF: " + response.body().getUf());
                    textIBGE.setText("IBGE: " + response.body().getIbge());
                    textGIA.setText("GIA: " + response.body().getGia());
                    textDDD.setText("DDD: " + response.body().getDdd());
                    textSIAFI.setText("SIAFI: " + response.body().getSiafi());
                    ProgressBarCircular.setVisibility(View.GONE);
                }else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    ProgressBarCircular.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<CEP> call, Throwable t) {
                ProgressBarCircular.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "OnFailure" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void limparTextview(){
        textCEP.setText(null);
        textLogradouro.setText(null);
        textComplemento.setText(null);
        textBairro.setText(null);
        textLocalidade.setText(null);
        textUF.setText(null);
        textIBGE.setText(null);
        textGIA.setText(null);
        textDDD.setText(null);
        textSIAFI.setText(null);
    }
}












