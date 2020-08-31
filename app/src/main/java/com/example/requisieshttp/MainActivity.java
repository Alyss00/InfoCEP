package com.example.requisieshttp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button botaoRecuperar;
    private TextView textoResultado;
    private EditText EditCEP;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        botaoRecuperar = findViewById(R.id.buttonRecuperar);
        textoResultado = findViewById(R.id.textResultado);

        botaoRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    MyTask task = new MyTask();
                    EditCEP = findViewById(R.id.EditCEP);
                    if (EditCEP.length() < 8 ){
                        Toast.makeText(getApplicationContext(),"Preencha o campo de texto!", Toast.LENGTH_SHORT).show();
                    }else {
                        String CEP = EditCEP.getText().toString();
                        String urlCEP = "https://viacep.com.br/ws/" + CEP + "/json/";
                        task.execute(urlCEP);
                    }
            }
        });
    }
    class MyTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                inputStream = conexao.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);

                BufferedReader reader = new BufferedReader(inputStreamReader);
                buffer = new StringBuffer();
                String linha;

                while ((linha = reader.readLine()) != null){
                    buffer.append(linha);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer.toString();
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            String logradouro = null;
            String cep = null;
            String complemento = null;
            String bairro = null;
            String localidade = null;
            String uf = null;
            String ibge = null;
            String gia = null;
            String ddd = null;


            try {
               JSONObject jsonObject = new JSONObject(resultado);
                logradouro = jsonObject.getString("logradouro");
                cep = jsonObject.getString("cep");
                complemento = jsonObject.getString("complemento");
                bairro = jsonObject.getString("bairro");
                localidade = jsonObject.getString("localidade");
                uf = jsonObject.getString("uf");
                ibge = jsonObject.getString("ibge");
                gia = jsonObject.getString("gia");
                ddd = jsonObject.getString("ddd");


            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (cep == null){
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Erro");
                alert.setMessage("CEP Inválido");
                alert.setIcon(R.drawable.ic_baseline_info_24);
                alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     EditCEP.setText(null);
                    }
                });
                alert.create();
                alert.show();


            }else {
                textoResultado.setText("Logadouro: " + logradouro + System.getProperty("line.separator")
                        + "CEP: " + cep + System.getProperty("line.separator")
                        + "Complemento: " + complemento + System.getProperty("line.separator")
                        + "Bairro: " + bairro + System.getProperty("line.separator")
                        + "Localidade: " + localidade + System.getProperty("line.separator")
                        + "UF: " + uf + System.getProperty("line.separator")
                        + "IBGE: " + ibge + System.getProperty("line.separator")
                        + "GIA: " + gia + System.getProperty("line.separator")
                        + "DDD: " + ddd
                );
                Toast.makeText(getApplicationContext(), "Consulta Concluida", Toast.LENGTH_LONG).show();
            }
        }
    }
}