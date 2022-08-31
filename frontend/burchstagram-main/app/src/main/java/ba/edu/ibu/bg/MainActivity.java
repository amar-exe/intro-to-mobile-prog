package ba.edu.ibu.bg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    final String url_post = "http://65.109.2.135/posts";
    final String url_user = "http://65.109.2.135/users/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        viewPager = findViewById(R.id.fragment_container);
        setUpAdapter(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public static void hideBottomNav() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    public static void showBottomNav() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    private void setUpAdapter(ViewPager viewPager) {

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPageAdapter.addFragment(new HomeFragment());
        viewPageAdapter.addFragment(new AddPostFragment());
        viewPageAdapter.addFragment(new UserSettingsFragment());

        viewPager.setAdapter(viewPageAdapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.d("DEBUG", "Item Clicked" + item.getItemId());
            switch (item.getItemId()) {
                case R.id.nav_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.add_post:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.nav_user_settings:
                    viewPager.setCurrentItem(2);
                    return true;
                default:
                    return false;


            }
        }
    };

    public void logout(View view) {
        SharedPreferences preferences = getSharedPreferences("Mypref", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("JWT_Token");
        editor.commit();
        editor.apply();
        showNotification(this, "Burchstagram", "You've successfully logged out", new Intent(getApplicationContext(), MainActivity.class), 1);
        Intent i = new Intent(getApplicationContext(),
                LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void showNotification(Context context, String title, String message, Intent intent, int reqCode) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id

        Log.d("showNotification", "showNotification: " + reqCode);
    }
    public void updateUser(View view){
        SharedPreferences shared = getSharedPreferences("MyPref", MODE_PRIVATE);
        String token = (shared.getString("JWT_Token", "invalid"));
        String username = (shared.getString("USERNAME", "invalid"));
        String firstname = (shared.getString("FIRSTNAME", "invalid"));
        String surname = (shared.getString("SURNAME","invalid"));
        String userid = (shared.getString("USERID","invalid"));
        UserSettingsFragment frag = UserSettingsFragment.GetInstance();
        if(frag.isValid()){
            EditText text=frag.getTextInput();
            String text_data = text.getText().toString();
            new EditUser().execute(username,token,firstname,surname,text_data,userid);

        }
        else{
            showToast("Passwords do not match!");
        }

    }
    public void addPost(View view) {
        SharedPreferences shared = getSharedPreferences("MyPref", MODE_PRIVATE);
        String token = (shared.getString("JWT_Token", "invalid"));
        String username = (shared.getString("USERNAME", "invalid"));
        AddPostFragment frag = AddPostFragment.GetInstance();
        EditText fragment = frag.getTextInput();
        String inputUrl = fragment.getText().toString();
        if (!(inputUrl.contains(".jpg") || inputUrl.contains(".png"))) {
            showToast("Error it's not an image!");
        } else {
            new AddNewPost().execute(username,token,inputUrl);
        }

    }

    public void showToast(final String Text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,
                        Text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public class AddNewPost extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String Username = strings[0];
            String Token = strings[1];
            String URLLink = strings[2];

            OkHttpClient okHttpClient = new OkHttpClient();
            String json = new StringBuilder()
                    .append("{")
                    .append("\"username\":\"" + Username + "\",")
                    .append("\"imageurl\":\"" + URLLink + "\"")
                    .append("}").toString();
            // json request body
            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(url_post)
                    .addHeader("User-Agent", "OkHttp Bot")
                    .addHeader("Authorization", "Bearer " + Token)
                    .post(body)
                    .build();


            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                String result = response.body().string();
                Log.d("test", result);
                if (response.isSuccessful()) {
                    if (result.contains("succesfully")) {
                        showToast("Post added succesfully!");


                    } else {
                        showToast("Unknown Error!");
                    }
                } else {
                    showToast("Unknown Error2!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class EditUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String Username = strings[0];
            String Token = strings[1];
            String Firstname = strings[2];
            String Lastname = strings[3];
            String newPassword = strings[4];
            String id = strings[5];
            OkHttpClient okHttpClient = new OkHttpClient();
            String json = new StringBuilder()
                    .append("{")
                    .append("\"username\":\"" + Username + "\",")
                    .append("\"firstName\":\"" + Firstname + "\",")
                    .append("\"lastName\":\"" + Lastname + "\",")
                    .append("\"password\":\"" + newPassword + "\"")
                    .append("}").toString();
            // json request body
            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(url_user+id)
                    .addHeader("User-Agent", "OkHttp Bot")
                    .addHeader("Authorization", "Bearer " + Token)
                    .put(body)
                    .build();


            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                String result = response.body().string();
                Log.d("test", result);
                if (response.isSuccessful()) {
                    if (result.contains("updated")) {
                        showToast("User updated succesfully!");

                    } else {
                        showToast("Unknown Error!");
                    }
                } else {
                    showToast("Unknown Error!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}