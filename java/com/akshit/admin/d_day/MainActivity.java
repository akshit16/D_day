package com.akshit.admin.d_day;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity {
    private EditText phone;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phone = (EditText) findViewById(R.id.phone);
       // sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


    }

    public void save(View view) {
        String contact = phone.getText().toString();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("contact", contact );
        editor.commit();

        BackGroundTask backGroundTask = new BackGroundTask();
        backGroundTask.execute(contact);
        finish();
}
    class BackGroundTask extends AsyncTask<String, Void, String> {

        String add_url;

        @Override
        protected void onPreExecute() {
            add_url = "http://hackathonvit-com.stackstaging.com/addcontact.php";
        }


        protected String doInBackground(String... args) {
            String contact;
            contact = args[0];
            try {
                URL url = new URL(add_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("contact", "UTF-8") + "=" + URLEncoder.encode(contact, "UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();


                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        protected void onPostExecute(String result) {
            Intent intent = new Intent(MainActivity.this,earth.class);
            startActivity(intent);
        }}}