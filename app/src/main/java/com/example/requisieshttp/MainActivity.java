package com.example.requisieshttp;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.requisieshttp.api.CEPinteface;
import com.example.requisieshttp.model.CEP;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

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
                if (response.body().getErro() == "true") {
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
                }else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<CEP> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "OnFailure" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
