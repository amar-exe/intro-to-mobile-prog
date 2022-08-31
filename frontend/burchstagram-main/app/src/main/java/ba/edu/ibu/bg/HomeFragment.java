package ba.edu.ibu.bg;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class HomeFragment extends Fragment {

    private static String JSON_URL = "http://65.109.2.135/posts";

    ArrayList<Post> posts = new ArrayList<>();

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       // startActivity(new Intent(getContext(), HomepageActivity.class));
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.postsRecyclerView);

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.swipeRefreshLayout);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetData getData = new GetData();
                getData.execute();

                pullToRefresh.setRefreshing(false);
            }
        });

        GetData getData = new GetData();
        getData.execute();

        return view;
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
                posts.clear();
                s = "{ \"posts\": " + s + "}";

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

        NewPostAdapter adapter = new NewPostAdapter(getContext(), postsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        recyclerView.setAdapter(adapter);
    }

}
