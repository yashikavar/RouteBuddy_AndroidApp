package routebuddy.administrator.example.com.minor_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class rec_profile extends AppCompatActivity implements View.OnClickListener{



    String rec_uname;
    TextView header;

    TextView edit_1;
    TextView edit_2;

    TextView edit_4;
    TextView edit_5;
    TextView edit_6;
    Button chat;

    static String def="n/a";
    String JSON_STRING;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_profile);
        header = (TextView) findViewById(R.id.rec_wlcmheader);

        chat=(Button)findViewById(R.id.btnchat);
        edit_1=(TextView)findViewById(R.id.rec_name);
        edit_2=(TextView)findViewById(R.id.rec_email);

        edit_4=(TextView)findViewById(R.id.rec_prof);
        edit_5=(TextView)findViewById(R.id.rec_qual);
        edit_6=(TextView)findViewById(R.id.rec_home);
        SharedPreferences pre1=getSharedPreferences("bool", Context.MODE_PRIVATE);
        rec_uname=pre1.getString("recname", def);
        Log.d("recname",rec_uname);
        rec_uname=rec_uname.trim();

        header.setText(""+rec_uname);
        chat.setOnClickListener(this);
        Log.d("rec1","rec1");
        new BackgroundTask().execute();
        Log.d("rec2","rec2");
    }


    @Override
    public void onClick(View v) {

        Intent ic= new Intent(rec_profile.this, TabActivity_3.class);
        startActivity(ic);

    }


    class BackgroundTask extends AsyncTask<Void,Void,String> {

        String json_url9;



        @Override
        protected  void onPreExecute()
        {
            //    add_info_url="http://routebuddy.comli.com/add_info.php";
            json_url9= "http://routebuddy.comli.com/rec_display.php?rid="+rec_uname;
            Log.d("recfun1","recfun1");

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url9);
                Log.d("recb1","recb1");
                Log.d("recconn","recconn");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream2 = httpURLConnection.getInputStream();
                BufferedReader bufferedReader2=new BufferedReader(new InputStreamReader(inputStream2));
                StringBuilder stringBuilder2= new StringBuilder();
                //JSON_STRING=new HashMap();
                Log.d("recconn2","recconn2");

                while((JSON_STRING = bufferedReader2.readLine())!=null)
                {
                    stringBuilder2.append(JSON_STRING+"\n");
                    Log.d("rece1","rece1");
                }

                bufferedReader2.close();
                inputStream2.close();
                httpURLConnection.disconnect();
                Log.d("recf1","recf1");
                return stringBuilder2.toString().trim();


            }
            catch(MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }





        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("d1,","d1");
            // boolean b1=false;
            String json_string;
            JSONObject jsonObject;
            JSONArray jsonArray;

            String name_ar;
            String email_ar;

            String profe_rec;
            String home_rec;
            String qual_rec;

            json_string=result;
            Log.d("uu: ",result);
            //get_json gj=new get_json();
            //gj.check_login(username,password);
            try{
                Log.d("x1","x1");
                jsonObject=new JSONObject(json_string);
                jsonArray=jsonObject.getJSONArray("server_res");
                //int count=0;
                // Log.d("c","c");
                //while (count<jsonArray.length())
                //{
                Log.d("c1","c1");
                JSONObject JOO2=jsonArray.getJSONObject(0);
                name_ar=JOO2.getString("name");
                email_ar=JOO2.getString("email");

                profe_rec=JOO2.getString("profession");
               home_rec=JOO2.getString("hometown");
                qual_rec=JOO2.getString("qualification");

                edit_1.setText(name_ar);
                edit_2.setText(email_ar);

                edit_4.setText(profe_rec);
                edit_6.setText(home_rec);
                edit_5.setText(qual_rec);



            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }
}
