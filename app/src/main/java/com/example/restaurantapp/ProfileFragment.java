package com.example.restaurantapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import java.util.Locale;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    private EditText editUserName, editUserEmail;
    private ImageView profileImage;
    private AppDatabase db;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editUserName = view.findViewById(R.id.editUserName);
        editUserEmail = view.findViewById(R.id.editUserEmail);
        profileImage = view.findViewById(R.id.profileImage);
        Button logoutBtn = view.findViewById(R.id.btnLogout);
        Button changeLanguageButton = view.findViewById(R.id.changeLanguageButton);
        Button toggleThemeButton = view.findViewById(R.id.toggleThemeButton);
        Button saveProfileButton = view.findViewById(R.id.saveProfileButton);
        Button changeImageButton = view.findViewById(R.id.changeImageButton);

        db = AppDatabase.getInstance(requireContext());

        SharedPreferences userPrefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        userId = userPrefs.getInt("user_id", -1);

        // تحميل البيانات من Room
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = db.userDao().getUserById(userId);
            if (user != null) {
                requireActivity().runOnUiThread(() -> {
                    editUserName.setText(user.name);
                    editUserEmail.setText(user.email);
                    profileImage.setImageResource(R.drawable.img); // ثابت حالياً
                });
            }
        });

        // حفظ التعديلات
        saveProfileButton.setOnClickListener(v -> {
            String newName = editUserName.getText().toString().trim();
            String newEmail = editUserEmail.getText().toString().trim();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(requireContext(), "يرجى تعبئة جميع الحقول", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                User updatedUser = new User();
                updatedUser.id = userId;
                updatedUser.name = newName;
                updatedUser.email = newEmail;

                db.userDao().updateUser(updatedUser);
            });

            Toast.makeText(requireContext(), "تم حفظ التعديلات", Toast.LENGTH_SHORT).show();
        });

        // تسجيل الخروج
        logoutBtn.setOnClickListener(v -> {
            userPrefs.edit().clear().apply();
            Intent intent = new Intent(requireContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // تغيير اللغة
        changeLanguageButton.setOnClickListener(v -> showLanguageDialog());

        // تبديل الوضع الليلي
        toggleThemeButton.setOnClickListener(v -> {
            SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            boolean isDark = prefs.getBoolean("is_dark_mode", false);

            if (isDark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                prefs.edit().putBoolean("is_dark_mode", false).apply();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                prefs.edit().putBoolean("is_dark_mode", true).apply();
            }

            requireActivity().recreate();
        });

        changeImageButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "ميزة تغيير الصورة قيد التطوير", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void showLanguageDialog() {
        String[] languages = {"English", "العربية"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getString(R.string.choose_language));
        builder.setItems(languages, (dialog, which) -> {
            if (which == 0) {
                setLocale("en");
            } else {
                setLocale("ar");
            }
            requireActivity().recreate();
        });
        builder.show();
    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        requireContext().getResources().updateConfiguration(config, requireContext().getResources().getDisplayMetrics());

        SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        prefs.edit().putString("app_lang", langCode).apply();
    }
}



