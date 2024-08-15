package com.example.usermanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText username;
    EditText password;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Ánh xạ các thành phần từ layout
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        // Kiểm tra null trước khi gắn sự kiện
        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Xử lý sự kiện khi nút đăng nhập được nhấn
                    if (username.getText().toString().equals("user") && password.getText().toString().equals("1234")) {
                        // Chuyển sang MainActivity khi đăng nhập thành công
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();  // Đóng màn hình đăng nhập để không quay lại được
                    } else {
                        Toast.makeText(Login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

