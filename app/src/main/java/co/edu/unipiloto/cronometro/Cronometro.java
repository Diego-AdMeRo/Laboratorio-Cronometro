package co.edu.unipiloto.cronometro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Cronometro extends AppCompatActivity implements View.OnClickListener {

    private static TextView lblCronometro;
    private TextView[] lblVueltas;
    private Button btnIniciar, btnReiniciar, btnVuelta, btnParar, btnTotalVueltas;
    private boolean ejecutando;
    private long segundos, vueltas[];
    private int contadorVueltas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronometro);

        //Inicialización de Atributos
        this.lblCronometro = (TextView) findViewById(R.id.lbl_cronometro);
        this.btnIniciar = (Button) findViewById(R.id.btn_iniciar);
        this.btnReiniciar = (Button) findViewById(R.id.btn_reiniciar);
        this.btnVuelta = (Button) findViewById(R.id.btn_vuelta);
        this.btnParar = (Button) findViewById(R.id.btn_parar);
        this.btnTotalVueltas = (Button) findViewById(R.id.btn_total_vueltas);
        this.lblVueltas = new TextView[]{
                (TextView) findViewById(R.id.lbl_vuelta_1),
                (TextView) findViewById(R.id.lbl_vuelta_2),
                (TextView) findViewById(R.id.lbl_vuelta_3),
                (TextView) findViewById(R.id.lbl_vuelta_4),
                (TextView) findViewById(R.id.lbl_vuelta_5),
                (TextView) findViewById(R.id.lbl_vuelta_6),
                (TextView) findViewById(R.id.lbl_vuelta_7),
                (TextView) findViewById(R.id.lbl_vuelta_8),
                (TextView) findViewById(R.id.lbl_vuelta_9),
                (TextView) findViewById(R.id.lbl_vuelta_10),
        };
        this.vueltas = new long[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        this.contadorVueltas = 0;

        //Acción Botones
        this.btnIniciar.setOnClickListener(this);
        this.btnReiniciar.setOnClickListener(this);
        this.btnVuelta.setOnClickListener(this);
        this.btnParar.setOnClickListener(this);
        this.btnTotalVueltas.setOnClickListener(this);

        if (savedInstanceState != null) {
            this.segundos = savedInstanceState.getLong("SEGUNDOS");
            this.ejecutando = savedInstanceState.getBoolean("EJECUTANDO");
            this.vueltas = savedInstanceState.getLongArray("VUELTAS");
            this.contadorVueltas = savedInstanceState.getInt("CONTADORVUELTAS");
            cargarVueltas(this.vueltas);
        }
        ejecutarCronometro();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnIniciar.getId()) {
            System.out.println("Iniciar");
            this.ejecutando = true;
        } else if (v.getId() == btnReiniciar.getId()) {
            System.out.println("Reiniciar");
            this.ejecutando = false;
            this.segundos = 0;
        } else if (v.getId() == btnVuelta.getId()) {
            System.out.println("Vuelta");
            long tiempo = segundos;
            this.vueltas[this.contadorVueltas] = tiempo;
            this.lblVueltas[this.contadorVueltas].setText(formatoTiempo(tiempo));
            this.contadorVueltas = this.contadorVueltas == 9 ? 0 : this.contadorVueltas + 1;
            this.segundos = 0;
        } else if (v.getId() == btnParar.getId()) {
            System.out.println("Parar");
            this.ejecutando = false;
        } else if (v.getId() == btnTotalVueltas.getId()) {
            System.out.println("Total Vueltas");
            Toast.makeText(Cronometro.this, "El Total de Las Vueltas fue: " + formatoTiempo(totalVueltas()), Toast.LENGTH_LONG).show();
        }
    }

    private void ejecutarCronometro() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                lblCronometro.setText(formatoTiempo(segundos));
                handler.postDelayed(this, 1000);
                segundos = ejecutando ? segundos + 1 : segundos;
            }
        });
    }

    private String formatoTiempo(long segundos) {
        int horas = (int) (segundos / 3600);
        int minutos = (int) (segundos % 3600 / 60);
        int segundo = (int) (segundos % 60);
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", horas, minutos, segundo);
    }

    private void cargarVueltas(long[] vueltas) {
        for (int i = 0; i < this.lblVueltas.length; i++)
            this.lblVueltas[i].setText(formatoTiempo(vueltas[i]) + "");
    }

    private long totalVueltas() {
        long resultado = 0;
        for (int i = 0; i < this.vueltas.length; i++)
            resultado += this.vueltas[i];
        return resultado;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putLong("SEGUNDOS", this.segundos);
        outState.putBoolean("EJECUTANDO", this.ejecutando);
        outState.putLongArray("VUELTAS", this.vueltas);
        outState.putInt("CONTADORVUELTAS", this.contadorVueltas);
    }

}