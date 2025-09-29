package com.example.restaurantapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.concurrent.Executors;

public class EditProfileFragment extends Fragment {

    private EditText editName, editEmail;
    private Button saveButton;
    private AppDatabase db;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        editName = view.findViewById(R.id.editName);
        editEmail = view.findViewById(R.id.editEmail);
        saveButton = view.findViewById(R.id.saveProfileButton);

        db = AppDatabase.getInstance(requireContext());

        SharedPreferences prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        // تحميل بيانات المستخدم
        Executors.newSingleThreadExecutor().execute(() -> {
            currentUser = db.userDao().getUserById(userId);
            if (currentUser != null) {
                requireActivity().runOnUiThread(() -> {
                    editName.setText(currentUser.name);
                    editEmail.setText(currentUser.email);
                });
            }
        });

        // حفظ التعديلات
        saveButton.setOnClickListener(v -> {
            String newName = editName.getText().toString().trim();
            String newEmail = editEmail.getText().toString().trim();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            currentUser.name = newName;
            currentUser.email = newEmail;

            Executors.newSingleThreadExecutor().execute(() -> {
                db.userDao().updateUser(currentUser);
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                );
            });
        });

        return view;
    }
}
