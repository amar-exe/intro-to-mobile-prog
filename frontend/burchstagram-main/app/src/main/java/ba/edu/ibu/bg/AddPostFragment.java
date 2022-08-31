package ba.edu.ibu.bg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddPostFragment extends Fragment {
    EditText etPictureURL;
    private static AddPostFragment instance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        instance = this;
        View view = inflater.inflate(R.layout.add_post_new, container, false);
        etPictureURL= (EditText) view.findViewById(R.id.url_input);
        return view;
    }
    public static AddPostFragment GetInstance()
    {
        return instance;
    }
    public static EditText getTextInput(){
        return instance.etPictureURL;
    }
}
