package ba.edu.ibu.bg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserSettingsFragment extends Fragment {
    private static UserSettingsFragment instance;
    EditText currentPassword,newPassword,confirmPassword;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        instance = this;
        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);
        currentPassword = (EditText) view.findViewById(R.id.current_password);
        newPassword = (EditText) view.findViewById(R.id.change_password);
        confirmPassword = (EditText) view.findViewById(R.id.confirm_change_password);
        return view;
    }

    public static UserSettingsFragment GetInstance()
    {
        return instance;
    }
    public boolean isValid(){
        if(instance.newPassword.getText().toString().equals(instance.confirmPassword.getText().toString())) return true;
        return false;
    }
    public static EditText getTextInput(){
        return instance.newPassword;
    }


}
