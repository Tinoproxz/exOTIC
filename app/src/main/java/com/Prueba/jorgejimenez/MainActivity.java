package com.Prueba.jorgejimenez;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText campo1, campo2, campo3;

    TextView txtFechaHora;
    String lat;
    String lon;
    public LocationManager locManager;
    public LocationListener locListener;
    private EditText rut;
    private Button bgrabar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asociar variables con elementos en el diseño XML
        campo1 = findViewById(R.id.nombre);
        campo2 = findViewById(R.id.rut);
        campo3 = findViewById(R.id.descripcion);
        txtFechaHora = findViewById(R.id.txtFecha);

        // Asociar variables con elementos en el diseño XML
        rut = findViewById(R.id.rut);
        bgrabar = findViewById(R.id.bgrabar);


        mostrarFechaHora();

        // Configurar un Listener para el botón
        bgrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Primero, validar el RUT
                if (validarRut()) {
                    // Luego, validar los demás campos
                    if (validar()) {
                        Toast.makeText(MainActivity.this, "Datos ingresados correctamente", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private boolean validarRut() {
        String rutText = rut.getText().toString().trim();

        // Llamada a un método para validar el RUT (Asegúrate de que el método RutValidator.validaRut() exista)
        if (RutValidator.validaRut(rutText)) {
            return true;
        } else {
            Toast.makeText(this, "RUT no válido", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean validar() {
        boolean retorno = true;

        String c1 = campo1.getText().toString();
        String c2 = campo2.getText().toString();
        String c3 = campo3.getText().toString();
        if (c1.isEmpty()) {
            campo1.setError("El Nombre está vacío");
            retorno = false;
        }
        if (c2.isEmpty()) {
            campo2.setError("El Rut está vacío");
            retorno = false;
        }
        if (c3.isEmpty()) {
            campo3.setError("La Descripción está vacía");
            retorno = false;
        }

        return retorno;
    }

    public void refrescaLocalizacion() {
        try {
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            conectaGps(loc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        locListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                conectaGps(location);
            }

            public void onProviderDisabled(String provider) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };
        try {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void conectaGps(Location loc) {
        try {
            if (loc != null) {
                lat = String.valueOf(loc.getLatitude());
                lon = String.valueOf(loc.getLongitude());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSensorChanged(SensorEvent event) {


        // Verificar si el valor de Y está entre 8 y 9
        float valorY = event.values[SensorManager.DATA_Y];
        if (valorY >= 8 && valorY <= 9) {
            Toast.makeText(MainActivity.this, "Datos guardados", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarFechaHora() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        String formattedDate = dateFormat.format(date);
        TextView txtFecha = findViewById(R.id.txtFecha);
        txtFecha.setText("Fecha: " + formattedDate);

        String formattedTime = timeFormat.format(date);
        TextView txtHora = findViewById(R.id.txtHora);
        txtHora.setText("Hora: " + formattedTime);
    }
}