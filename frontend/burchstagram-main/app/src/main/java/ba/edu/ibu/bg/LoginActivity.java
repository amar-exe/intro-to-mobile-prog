package ba.edu.ibu.bg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin;
    final String url_Login = "http://65.109.2.135/users/authenticate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        /*if(pref.getString("JWT_Token", null)!=null){
            Intent i = new Intent(LoginActivity.this,
                    MainActivity.class);
            startActivity(i);
        }*/
        etEmail = (EditText) findViewById(R.id.username_input);
        etPassword = (EditText) findViewById(R.id.pass);
        btnLogin = (Button) findViewById(R.id.login_button);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = etEmail.getText().toString();
                String Password = etPassword.getText().toString();
                new LoginUser().execute(Email, Password);
            }
        });
    }

    public void goToRegister(View view) {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    public class LoginUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String Email = strings[0];
            String Password = strings[1];

            OkHttpClient okHttpClient = new OkHttpClient();
            String json = new StringBuilder()
                    .append("{")
                    .append("\"username\":\""+Email+"\",")
                    .append("\"password\":\""+Password+"\"")
                    .append("}").toString();
            // json request body
            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(url_Login)
                    .addHeader("User-Agent", "OkHttp Bot")
                    .post(body)
                    .build();


            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (result.contains("token")) {
                        String token = result.substring(result.indexOf("token")+8,result.length()-2);
                        String id = result.substring(result.indexOf("id")+4,result.indexOf(","));
                        String firstname = result.substring(result.indexOf("firstName")+12,result.indexOf("lastName")-3);
                        String surname = result.substring(result.indexOf("lastName")+11,result.indexOf("username")-3);
                        String username = result.substring(result.indexOf("username")+11,result.indexOf("token")-3);
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("JWT_Token", token);
                        editor.putString("USERNAME", username);
                        editor.putString("USERID",id);
                        editor.putString("FIRSTNAME", firstname);
                        editor.putString("SURNAME", surname);
                        editor.commit();
                        editor.apply();
                        Intent i = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        showToast("Username or Password mismatched!");
                    }
                } else {
                    showToast("Username or Password mismatched!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void showToast(final String Text) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this,
                            Text, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}