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

public class RegisterFragment extends Fragment {

    private EditText nameInput, emailInput, passwordInput;
    private Button registerBtn;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        nameInput = view.findViewById(R.id.nameRegister);
        emailInput = view.findViewById(R.id.emailRegister);
        passwordInput = view.findViewById(R.id.passwordRegister);
        registerBtn = view.findViewById(R.id.btnRegister);

        db = AppDatabase.getInstance(requireContext());

        registerBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "يرجى تعبئة جميع الحقول", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "صيغة البريد غير صحيحة", Toast.LENGTH_SHORT).show();
                return;
            }

            registerBtn.setEnabled(false);

            Executors.newSingleThreadExecutor().execute(() -> {
                // تحقق إذا المستخدم موجود
                User existingUser = db.userDao().getUserByEmail(email);
                if (existingUser != null) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "البريد مستخدم من قبل", Toast.LENGTH_SHORT).show();
                        registerBtn.setEnabled(true); // إعادة التفعيل
                    });
                    return;
                }

                // إنشاء مستخدم جديد
                User user = new User();
                user.name = name;
                user.email = email;
                user.password = password;

                db.userDao().insert(user);

                // استرجاع المستخدم الجديد
                User savedUser = db.userDao().login(email, password);
                new SharedPrefManager(requireContext()).saveUserId(savedUser.id);

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), MainActivity.class));
                    requireActivity().finish();
                });
            });
        });

        return view;
    }
}

