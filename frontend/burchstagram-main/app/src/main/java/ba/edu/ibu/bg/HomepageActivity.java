package ba.edu.ibu.bg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomepageActivity extends AppCompatActivity {

    private static String JSON_URL = "https://run.mocky.io/v3/49a70d11-f02f-427e-b35e-134e44a4b9fc";

    ArrayList<Post> posts = new ArrayList<>();

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        recyclerView = findViewById(R.id.postsRecyclerView);

        GetData getData = new GetData();
        getData.execute();

    }

    public class GetData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();


                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int data = isr.read();

                    while (data != -1) {

                        current += (char) data;
                        data = isr.read();

                    }
                    return current;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return current;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("posts");

                for (int i = 0; i < jsonArray.length(); i++) {


                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                    Post post = new Post();

                    post.setPostID(jsonObject1.getString("id"));
                    post.setUsername(jsonObject1.getString("username"));
                    post.setPostedOn(jsonObject1.getString("postedOn"));
                    post.setImageURL(jsonObject1.getString("imageURL"));

                    posts.add(post);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            PutDataIntoRecyclerView(posts);


        }
    }

    private void PutDataIntoRecyclerView(List<Post> postsList){

        NewPostAdapter adapter = new NewPostAdapter(this, postsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recyclerView.setAdapter(adapter);
    }
}