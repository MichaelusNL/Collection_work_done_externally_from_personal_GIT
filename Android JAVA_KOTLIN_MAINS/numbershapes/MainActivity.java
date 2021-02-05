package com.example.numbershapes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public void onClick (View view){
        EditText input_number = (EditText) findViewById(R.id.editTextNumber);
        if (input_number.getText().toString().isEmpty()){
            Toast.makeText(this, "Fill in number please!", Toast.LENGTH_SHORT).show();
        }
        else{
            ShapeOfNumber shapenumber = new ShapeOfNumber();
            shapenumber.number = Integer.parseInt(input_number.getText().toString());
            Toast.makeText(this, shapenumber.check_number(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}

class ShapeOfNumber {
    int number;
    public String check_number() {
        boolean isTriangular = isTriangularNumber();
        boolean isSquare = isSquareNumber();
        if (isSquare && isTriangular) {
            return "The number is both triangular and square";
        } else if (isSquare) {
            return "The number is a square";
        } else if (isTriangular) {
            return "The number is triangular";
        } else {
            return "The number is neither triangular nor square";
        }
    }

    public boolean isTriangularNumber (){
        Integer triangular=0;
        while (triangular < number && number >=0){
            triangular = triangular + 1;
        }
        if (number == triangular){
            return true;
        }
        else{
            return false;
        }
    }
    
    public boolean isSquareNumber (){
        double sqrRoot = Math.sqrt(number);
        if (sqrRoot == Math.floor(sqrRoot)){
            return true;
        }
        else{
            return false;
        }
    }


}