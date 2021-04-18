package com.fundmanagement.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fundmanagement.R;

import java.util.Calendar;

public class Add_Bill extends AppCompatActivity {

    ImageView cal;
    int mDate=0, mMonth=0, mYear=0;
    String prior_id,collectionId;
    EditText name, rollno, paid_amount;
    TextView request_no,date;
    Button submit_bill;
    Boolean isDataValid = false;
    Spinner spinner;
    SharedPreferences sharedPreferences;
    ImageView backbutton;
    TextView toolbarText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__bill);
        collectionId = getIntent().getStringExtra("collectionId");
        prior_id = getIntent().getStringExtra("id");
        date = findViewById(R.id.date);
        cal = findViewById(R.id.datepicker);
        request_no = findViewById(R.id.request_no);
        name = findViewById(R.id.enter_name);
        rollno = findViewById(R.id.enter_rollno);
        paid_amount = findViewById(R.id.paid_amount);
        request_no.setText(prior_id);
        submit_bill = findViewById(R.id.upload_bill);
        spinner = findViewById(R.id.category_spinner);
        sharedPreferences = getSharedPreferences("deleted", Context.MODE_PRIVATE);
        backbutton  = findViewById(R.id.toolbar_image);
        toolbarText = findViewById(R.id.toolbar_textview);
        toolbarText.setText("Add Bill");

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submit_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData(name);
                validateData(rollno);
                validateData(paid_amount);
                if(spinner.getSelectedItem().toString().equals("Not Selected")){
                    isDataValid = false;
                    TextView errorText = (TextView)spinner.getSelectedView();
                    errorText.setError("anything");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Required Field");
                    return;
                }else{
                    isDataValid = true;
                }
                if(mDate==0 || mMonth==0 || mYear==0){
                    isDataValid = false;
                    date.setError("Required Field");
                    return;
                }else{
                    isDataValid = true;
                }

                if(isDataValid){
                    String str = mDate+"-"+mMonth+"-"+mYear;
                    Intent it = new Intent(Add_Bill.this,UploadImage.class);
                    it.putExtra("date",str);
                    it.putExtra("name",name.getText().toString().trim());
                    it.putExtra("roll_no",rollno.getText().toString().trim());
                    it.putExtra("paid_amount",paid_amount.getText().toString().trim());
                    it.putExtra("prior_id",prior_id);
                    it.putExtra("category",spinner.getSelectedItem().toString());
                    it.putExtra("collectionId",collectionId);
                    startActivity(it);
                }
            }
        });
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar Cal = Calendar.getInstance();
                mDate = Cal.get(Calendar.DATE);
                mMonth = Cal.get(Calendar.MONTH);
                mYear = Cal.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Add_Bill.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth+"-"+month+"-"+year);
                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
                Log.d("date_time", "onCreate: date = "+mDate+" "+mMonth+" "+mYear);
            }
        });

    }

    private void validateData(EditText field) {
        if(field.getText().toString().toString().isEmpty()){
            isDataValid = false;
            field.setError("Required Field");
            return;
        }else
            isDataValid = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String str = sharedPreferences.getString("delete","");
        if(str.equals("yes")){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            finish();
        }
    }
}