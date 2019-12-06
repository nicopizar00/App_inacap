package com.example.app_inacap;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_inacap.db.DBAdmin;

public class MainActivity extends AppCompatActivity {

    private EditText etUser;
    private EditText etPass;

    private Button btnIngresar;
    private TextView tvRegristrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);

        btnIngresar = findViewById(R.id.btnIngresar);
        tvRegristrate = findViewById(R.id.tvRegristrate);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = etUser.getText().toString().trim();
                String pass = etPass.getText().toString().trim();

                DBAdmin dbAdmin = new DBAdmin(MainActivity.this, "inacap_app_db", null, 1);
                SQLiteDatabase sqLiteDatabase = dbAdmin.getWritableDatabase();
                String[] params = {user, pass};
                Cursor cursor = sqLiteDatabase.rawQuery(
                        "SELECT " +
                                "USUARIO_ID " +
                                "FROM USUARIO " +
                                "WHERE USUARIO_EMAIL = ? " +
                                "AND USUARIO_PASSWORD = ?", params);

                if (cursor.moveToFirst()) {
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    intent.putExtra("userId", cursor.getString(0));
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Usuario o password incorrecto", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
                sqLiteDatabase.close();
            }
        });

        tvRegristrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Registro.class));
            }
        });
    }
}
