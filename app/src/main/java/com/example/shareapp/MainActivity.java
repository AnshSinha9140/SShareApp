package com.example.shareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity {

    private TextView text;
    String TAG = "loggingME";
    private EditText chat_box;
    int SELECT_PHOTO = 1;
    private ImageButton send_button,imagePickerButton;
    Socket socket;

    {
        try {
            socket = IO.socket("");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);
        imagePickerButton = findViewById(R.id.imagePickerButton);

        imagePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);

            }
        });
        chat_box = findViewById(R.id.chat_box);
        send_button = findViewById(R.id.sendButton);


    }


}