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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText etEmail,etPassword,etConfirmPassword,etName,etSurname;
    Button btnRegister;
    final String url_Register = "http://65.109.2.135/users/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etEmail=(EditText)findViewById(R.id.username_input_register);
        etPassword=(EditText)findViewById(R.id.password_input);
        etConfirmPassword=(EditText)findViewById(R.id.password_repeat_input);
        etName=(EditText) findViewById(R.id.name_input_register);
        etSurname=(EditText) findViewById(R.id.surname_input_register);
        btnRegister=(Button) findViewById(R.id.register_button);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = etEmail.getText().toString();
                String Password = etPassword.getText().toString();
                String ConfirmPassword = etConfirmPassword.getText().toString();
                String Name = etName.getText().toString();
                String Surname = etSurname.getText().toString();

                if(!checkEmail(Email)){
                    showToast("You must use IBU mail!");
                }
                if(!checkPasswords(Password,ConfirmPassword)){
                    showToast("Passwords don't match, or are too short(min length 5)");
                }
                if(!checkNames(Name,Surname)){
                    showToast("Must enter name and surname!");
                }
                if(checkEmail(Email) && checkPasswords(Password,ConfirmPassword) && checkNames(Name,Surname)){
                    new RegisterUser().execute(Email, Password,Name,Surname);
                }
            }
        });
    }

    boolean checkEmail(String email){
        if(email.contains("@stu.ibu.edu.ba") || email.contains("@ibu.edu.ba")){
            return true;
        }
        return false;
    }
    boolean checkPasswords(String pass1, String pass2){
        if(pass1.length()<5) return false;
        if(!pass1.equals(pass2)) return false;
        return true;
    }
    boolean checkNames(String name, String surname){
        if(name.length()>=1 && surname.length()>=1) return true;
        return false;
    }
    public class RegisterUser extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String Email = strings[0];
            String Password = strings[1];
            String Name = strings[2];
            String Surname = strings[3];
            OkHttpClient okHttpClient = new OkHttpClient();
            String json = new StringBuilder()
                    .append("{")
                    .append("\"firstName\":\""+Name+"\",")
                    .append("\"lastName\":\""+Surname+"\",")
                    .append("\"email\":\""+Email+"\",")
                    .append("\"password\":\""+Password+"\"")
                    .append("}").toString();
            // json request body

            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(url_Register)
                    .addHeader("User-Agent", "OkHttp Bot")
                    .post(body)
                    .build();


            Response response = null;



            try {
                response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {

                    String result = response.body().string();
                    if (result.contains("successful")) {
                        showToast("Register successful");
                        Intent i = new Intent(RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        showToast("User already exists!");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void goToLogin(View view) {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
    }
    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }
}