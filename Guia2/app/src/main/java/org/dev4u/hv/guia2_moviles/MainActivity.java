package org.dev4u.hv.guia2_moviles;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText txtURL,txtNombreArchivo;
    private  RadioGroup rgCambiar;
    private RadioButton rbCambiar,rbNoCambiar;
    private TextView lblEstado;
    private Button btnDescargar;
    private ProgressBar pbDescarga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inicializar
        txtURL       = (EditText) findViewById(R.id.txtURL);
        lblEstado    = (TextView) findViewById(R.id.lblEstado);
        btnDescargar = (Button)   findViewById(R.id.btnDescargar);
        rbCambiar = (RadioButton) findViewById(R.id.rbCambiar);
        rbNoCambiar = (RadioButton) findViewById(R.id.rbNoCambiar);
        txtNombreArchivo = (EditText) findViewById(R.id.txtNombreArchivo);
        rgCambiar = (RadioGroup) findViewById(R.id.rgCambio);
        pbDescarga = (ProgressBar) findViewById(R.id.pbDescarga);

        rbCambiar.setChecked(true);

        //evento onClick
        btnDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rbCambiar.isChecked() && txtNombreArchivo.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Ponga nombre al archivo", Toast.LENGTH_LONG).show();
                    return;
                }
                new Descargar(
                        MainActivity.this,
                        lblEstado,
                        btnDescargar,
                        pbDescarga,
                        txtNombreArchivo.getText().toString()

                ).execute(txtURL.getText().toString());
            }
        });

        rgCambiar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == rbCambiar.getId()){
                    txtNombreArchivo.setEnabled(true);
                }
                if(checkedId == rbNoCambiar.getId()){
                    txtNombreArchivo.setEnabled(false);
                    txtNombreArchivo.setText("");
                }
            }
        });


        verifyStoragePermissions(this);
    }

    //esto es para activar perimiso de escritura y lectura en versiones de android 6 en adelante
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
