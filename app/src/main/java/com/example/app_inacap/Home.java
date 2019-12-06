package com.example.app_inacap;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_inacap.db.DBAdmin;

public class Home extends AppCompatActivity {
    private Toolbar tbMenuCabecera;
    private FloatingActionButton fabNewComment;
    private ImageView imgRamos;
    private ImageView imgProfesores;
    private ImageView imgRestaurantes;

    TextView tvUsuarioPublicacion;
    TextView tvTextoPublicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tbMenuCabecera = findViewById(R.id.tbMenuTopInterior);
        fabNewComment = findViewById(R.id.fabNewComment);
        imgRamos = findViewById(R.id.imgRamos);
        imgProfesores = findViewById(R.id.imgProfesores);
        imgRestaurantes = findViewById(R.id.imgRestaurantes);

        tvUsuarioPublicacion = findViewById(R.id.tvUsuarioPublicacion);
        tvTextoPublicacion = findViewById(R.id.tvTextoPublicacion);

        setSupportActionBar(tbMenuCabecera);


        mostrarUltimaPublicacion();


        imgRamos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Seccion.class));
            }
        });

        imgProfesores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Seccion.class));
            }
        });

        imgRestaurantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Seccion.class));
            }
        });

        Bundle bundle = getIntent().getExtras();
        final String userId = bundle.getString("userId");

        fabNewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Home.this);
                View mView = getLayoutInflater().inflate(R.layout.alert_new_comment, null);
                //EditText etTitle = mView.findViewById(R.id.etTitle);
                //Spinner spSeccion = mView.findViewById(R.id.spSeccion);
                final EditText etComment = mView.findViewById(R.id.etComment);
                Button btnCancelar = mView.findViewById(R.id.btnCancelar);
                Button btnPublicar = mView.findViewById(R.id.btnPublicar);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                btnPublicar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Publicar
                        String textoPublicacion = etComment.getText().toString().trim();

                        DBAdmin dbAdmin = new DBAdmin(Home.this, "inacap_app_db", null, 1);
                        SQLiteDatabase sqLiteDatabase = dbAdmin.getWritableDatabase();
                        String[] params = {userId, textoPublicacion};
                        sqLiteDatabase.execSQL("INSERT INTO PUBLICACION (USUARIO_ID, PUBLICACION_TEXTO) " +
                                "VALUES (?,?);", params);
                        sqLiteDatabase.close();
                        mostrarUltimaPublicacion();
                        dialog.dismiss();
                    }
                });

                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarUltimaPublicacion();
    }

    private void mostrarUltimaPublicacion() {
        DBAdmin dbAdmin = new DBAdmin(Home.this, "inacap_app_db", null, 1);
        SQLiteDatabase sqLiteDatabase = dbAdmin.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT USUARIO_NOMBRE, PUBLICACION_TEXTO " +
                        "FROM USUARIO, PUBLICACION " +
                        "WHERE USUARIO.USUARIO_ID = PUBLICACION.USUARIO_ID " +
                        "ORDER BY PUBLICACION_ID DESC", null);

        if (cursor.moveToFirst()) {
            tvUsuarioPublicacion.setText(cursor.getString(0));
            tvTextoPublicacion.setText(cursor.getString(1));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_interior, menu);
        return true;
    }

}
