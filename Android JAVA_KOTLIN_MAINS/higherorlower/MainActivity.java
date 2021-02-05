package com.example.higherorlower;
import java.util.Random;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private int randomNumber;

    public void generateRandomNumber(){
        Random rand = new Random();
        randomNumber = rand.nextInt(20 - 1) + 1;
    }

    public void guess (View view){
        EditText number = (EditText) findViewById(R.id.editTextNumber);
        if (Integer.parseInt(number.getText().toString()) == randomNumber){
            Toast.makeText(this, "You guessed it, it was "+randomNumber, Toast.LENGTH_SHORT).show();
            generateRandomNumber();

        }
        else if (Integer.parseInt(number.getText().toString()) > randomNumber){
            Toast.makeText(this, "Guess again, but lower!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Guess again, but higher! ", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateRandomNumber();
    }
}