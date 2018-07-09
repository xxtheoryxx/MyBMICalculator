package sg.edu.rp.c346.mybmicalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.PhantomReference;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText etWeight;
    EditText etHeight;
    TextView tvDate;
    TextView tvBMI;
    TextView tvStatus;
    Button btnCalculate;
    Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etHeight = findViewById(R.id.editTextHeight);
        etWeight = findViewById(R.id.editTextWeight);
        tvBMI = findViewById(R.id.textViewBmi);
        tvDate = findViewById(R.id.textViewDate);
        tvStatus = findViewById(R.id.textViewStatus);
        btnCalculate = findViewById(R.id.buttonCalculate);
        btnReset = findViewById(R.id.buttonResetData);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();

                String datetime = now.get(Calendar.DAY_OF_MONTH) + "/" +
                        (now.get(Calendar.MONTH)+1) + "/" +
                        now.get(Calendar.YEAR) + " " +
                        now.get(Calendar.HOUR_OF_DAY) + ":" +
                        now.get(Calendar.MINUTE);
                float mass = Float.valueOf(etWeight.getText().toString());
                float height = Float.valueOf(etHeight.getText().toString());
                float bmi = mass / (height * height);

                tvDate.setText("Last Calculated Date: "+datetime);
                tvBMI.setText("Last Calculated BMI: "+bmi);
                if (bmi <= 18.5){
                    tvStatus.setText("You are underweight");
                }else if (bmi >= 29.9){
                    tvStatus.setText("You are obese");
                }else if (bmi >= 18.5 && bmi <= 24.9){
                    tvStatus.setText("Your BMI is normal");
                }else if (bmi >= 24.9 && bmi <= 29.9)
                    tvStatus.setText("You are overweight");
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etHeight.setText("0");
                etWeight.setText("0.0");
                tvDate.setText("Last Calculated Date:" );
                tvBMI.setText("Last Calculated BMI:");
                tvStatus.setText("");

                SharedPreferences sharedPrefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.clear();
                editor.commit();
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();

        float wgt = Float.valueOf(etWeight.getText().toString());
        float hgt = Float.valueOf(etHeight.getText().toString());
        String date = tvDate.getText().toString();
        String bmi = tvBMI.getText().toString();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEdit = prefs.edit();

        prefEdit.putFloat("weight", wgt);
        prefEdit.putFloat("height",hgt);
        prefEdit.putString("date",date);
        prefEdit.putString("bmi", bmi);
        if (tvStatus.getText().toString() != null){
            String status = tvStatus.getText().toString();
            prefEdit.putString("status", status);
        }

        prefEdit.commit();
    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        float wgt = prefs.getFloat("weight", 0.0f);
        float hgt = prefs.getFloat("height", 0.0f);
        String date = prefs.getString("date", "Last Calculated Date: ");
        String bmi = prefs.getString("bmi", "Last Calculated BMI:");
        String status = prefs.getString("status", "");

        etWeight.setText(String.valueOf(wgt));
        etHeight.setText(String.valueOf(hgt));
        tvDate.setText(date);
        tvBMI.setText(bmi);
        tvStatus.setText(status);


    }
}
