package com.example.restaurantapp;

import android.content.Intent;
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

public class LoginFragment extends Fragment {

    private EditText emailInput, passwordInput;
    private Button loginBtn;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailInput = view.findViewById(R.id.emailLogin);
        passwordInput = view.findViewById(R.id.passwordLogin);
        loginBtn = view.findViewById(R.id.btnLogin);

        db = AppDatabase.getInstance(requireContext());

        loginBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "يرجى إدخال البريد وكلمة المرور", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "صيغة البريد غير صحيحة", Toast.LENGTH_SHORT).show();
                return;
            }

            loginBtn.setEnabled(false); // تعطيل الزر مؤقتاً

            Executors.newSingleThreadExecutor().execute(() -> {
                User user = db.userDao().login(email, password);

                requireActivity().runOnUiThread(() -> {
                    loginBtn.setEnabled(true); // إعادة التفعيل

                    if (user != null) {
                        new SharedPrefManager(requireContext()).saveUserId(user.id);
                        Toast.makeText(getContext(), "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), MainActivity.class));
                        requireActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "البريد أو كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        return view;
    }
}
