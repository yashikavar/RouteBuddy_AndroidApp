package routebuddy.administrator.example.com.minor_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.simbio.encryption.Encryption;

public class registeractivity extends Activity implements View.OnClickListener{


    TextView loginScreen;
    TextView strengthView;

    EditText login1;
    EditText phone1;
    EditText email1;
    EditText pass1;
    EditText pass2;
    EditText name1;
    EditText profe;
    EditText qual;
    EditText home;
    static String login;
    static long phone;
    static String email;
    static String pass;
    static String passr;
    static String name;
    static String profession;
    static String hometown;
    static String qualification;
    String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registeractivity);
        Button button=(Button)findViewById(R.id.btnRegister);
        loginScreen = (TextView) findViewById(R.id.link_to_login);
        login1=(EditText) findViewById(R.id.reg_username);
        phone1=(EditText) findViewById(R.id.reg_phone);
        pass1=(EditText) findViewById(R.id.reg_password);
        pass2=(EditText) findViewById(R.id.reg_password1);
        name1=(EditText) findViewById(R.id.edit_phone);
        email1=(EditText) findViewById(R.id.reg_email);
        profe=(EditText) findViewById(R.id.reg_profe);
        qual=(EditText) findViewById(R.id.reg_qual);
        home=(EditText) findViewById(R.id.reg_home);
        strengthView=(TextView)findViewById(R.id.password_strength);
        loginScreen.setOnClickListener(this);
        button.setOnClickListener(this);

        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null && !networkInfo.isConnected())
        {
            button.setEnabled(false);
            loginScreen.setEnabled(false);
        }

    }

    @Override
    public void onClick(View v) {

            String temp;
        Pattern pattern1 = Pattern.compile( "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");
        login=login1.getText().toString();
        email=email1.getText().toString();
        name=name1.getText().toString();
        pass=pass1.getText().toString();
        passr=pass2.getText().toString();
        temp=phone1.getText().toString();
        profession=profe.getText().toString();
        qualification=qual.getText().toString();
        hometown=home.getText().toString();
        phone=Long.parseLong(phone1.getText().toString());
        switch(v.getId()) {
            case R.id.link_to_login:
                Intent mainIntent = new Intent(registeractivity.this, routebuddy.administrator.example.com.minor_project.loginpage.class);
                registeractivity.this.startActivity(mainIntent);
                break;
            case R.id.btnRegister:
                Matcher matcher1 = pattern1.matcher(email);

                if(!pass.equals(passr) || !matcher1.matches()|| pass.isEmpty() || passr.isEmpty() || email.isEmpty()|| login.isEmpty()||name.isEmpty()||temp.isEmpty())
                {
                    if(pass.isEmpty() || passr.isEmpty())
                    {
                        Toast.makeText(registeractivity.this," enter password, its empty",Toast.LENGTH_LONG).show();
                    }
                    if(name.isEmpty())
                    {
                        Toast.makeText(registeractivity.this," enter name, its empty",Toast.LENGTH_LONG).show();
                    }
                    if(login.isEmpty())
                    {
                        Toast.makeText(registeractivity.this," enter loginid, its empty",Toast.LENGTH_LONG).show();
                    }
                    if(temp.isEmpty())
                    {
                        Toast.makeText(registeractivity.this," enter contact, its empty",Toast.LENGTH_LONG).show();
                    }
                    if(email.isEmpty())
                    {
                        Toast.makeText(registeractivity.this," enter email, its empty",Toast.LENGTH_LONG).show();
                    }
                    if(!pass.equals(passr)) {
                        Toast err = Toast.makeText(registeractivity.this, "sorry! passwords do not match", Toast.LENGTH_SHORT);
                        err.show();
                        // Log.d("pass1","pass2");
                        pass1.setText("");
                        pass2.setText("");
                    }
                    if(!matcher1.matches())
                    {
                        Toast.makeText(registeractivity.this, "oops!! InValid Email!!",Toast.LENGTH_LONG).show();
                        email1.setText("");
                    }
                }
                else {
                    //DBhelper db = new DBhelper(this);
                    PasswordStrength str = PasswordStrength.calculateStrength(passr);
                    strengthView.setText(MessageFormat.format(
                            getText(R.string.password_strength_caption).toString(),
                            str.getText(this)));
                    strengthView.setTextColor(str.getColor());


                    token= FirebaseInstanceId.getInstance().getToken();
                    Log.d("token",token);
                /*Toast.makeText(this, "error1", LENGTH_LONG).show();
                db.login_details(login, pass, email, phone, name);
                Intent i = new Intent(this,main.class);
                startActivity(i);
                Toast.makeText(this, "error", LENGTH_LONG).show();
                finish();*/
                    // call the encrypt function and pass the encrypted password
                    BackgroundTask backgroundTask = new BackgroundTask();
                    backgroundTask.execute(login, pass, name, email, Long.toString(phone),token,profession,qualification,hometown);
                }
                break;
            default:
                break;
        }
    }

    class BackgroundTask extends AsyncTask<String,Void,Void> {
        String add_info_url;
        @Override
        protected  void onPreExecute()
        {
            add_info_url="http://routebuddy.comli.com/add_info.php";


        }

        @Override
        protected Void doInBackground(String... args) {
            String name,loginid,email,password,token,proff,quali, home_town;
            Long phone;
            loginid=args[0];
            password=args[1];
            name=args[2];
            email=args[3];
            phone=Long.parseLong(args[4]);
            token=args[5];
            proff=args[6];
            quali=args[7];
            home_town=args[8];
            Log.d("reg1","reg1");
            String c=null;
            try {
                String key ="Your key";
                String salt="Your salt";
                Log.d("a","a");
                byte[] iv =new byte[16];
                Encryption encryption= Encryption.getDefault(key,salt,iv);

                String encrypted= encryption.encryptOrNull(password);
                Log.d("encrypted",encrypted);


                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string= URLEncoder.encode("loginid","UTF-8")+"="+URLEncoder.encode(loginid,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(encrypted,"UTF-8")+"&"+
                        URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                        URLEncoder.encode("phone","UTF-8")+"="+ URLEncoder.encode(Long.toString(phone),"UTF-8")+"&"+
                        URLEncoder.encode("token_no","UTF-8")+"="+URLEncoder.encode(token,"UTF-8")+"&"+
                        URLEncoder.encode("profession","UTF-8")+"="+URLEncoder.encode(proff,"UTF-8")+"&"+
                        URLEncoder.encode("profession","UTF-8")+"="+URLEncoder.encode(proff,"UTF-8")+"&"+
                        URLEncoder.encode("qualification","UTF-8")+"="+URLEncoder.encode(quali,"UTF-8")+"&"+
                        URLEncoder.encode("hometown","UTF-8")+"="+URLEncoder.encode(home_town,"UTF-8");

                Log.d("reg2","reg2");

                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
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
        protected void onPostExecute(Void aVoid) {
            Intent i=new Intent(registeractivity.this,loginpage.class);
            startActivity(i);
        }
    }
}

