package com.example.shareapp;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView text;
    int room = 1234;

    private static  final int Message_sender  = 1;
    private static  final int Message_Other_sender  = 2;
    private static  final  int Image_Sender = 3;
    private static  final int  Image_Receiver = 4;
    String encoded = "";
   private  RecyclerView recyclerView;
    byte[] bt = null;
   int SELECT_PHOTO = 1;
   private EditText chat_box;
   private ImageButton send_button,imagePickerButton;
   String TAG = "loggingME";

   private ShareAdapter shareAdapter;
   private ArrayList<ShareModel> chatList = new ArrayList<>();
    Socket socket;


    {
        try {
            socket = IO.socket("https://socket-servers.herokuapp.com/");
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
        send_button.setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shareAdapter = new ShareAdapter(chatList);
        recyclerView.setAdapter(shareAdapter);

        socket.connect();
        socket.on("connects", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
               final String msg = (String) args[0];
                runOnUiThread(new Runnable() {
                  @Override
                  public void run() {

                      Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();

                      socket.emit("room", room );
                  }
              });


            }
        });

        socket.on("textreceived", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                final String receivedmsg = (String) args[0];
                chatList.add(new ShareModel(receivedmsg, "Rahul", Message_Other_sender));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareAdapter.notifyDataSetChanged();
                    }
                });
            }
        });


        socket.on("receivedImage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {


                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          shareAdapter.notifyDataSetChanged();
                      }
                  });


            }
        });


    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkIfAlreadyhavePermission() {
        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {

            //show dialog to ask permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                            ,
                    1);

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.sendButton:
                String message = chat_box.getText().toString();
                if (!TextUtils.isEmpty(message)){
                    Log.i("sendmsg is :", message);
                    socket.emit("sendText", message);

                    chatList.add(new ShareModel( message, "Ansh", Message_sender));

                    chat_box.setText("");
                    if (shareAdapter!=null) {
                        shareAdapter.notifyDataSetChanged();
                    }

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data!=null){
           System.out.println(data);
             Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                Log.i(TAG, "onActivityBitmap : " + bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                byte [] bytes = byteArrayOutputStream.toByteArray();
                encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
                Log.i(TAG, "onActivityResult_encoded image : " + encoded);

            } catch (IOException e) {
                e.printStackTrace();
            }

             chatList.add(new ShareModel(uri,"Ansh",Image_Receiver ));
             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     Log.i(TAG, "run: + " +  encoded );
                     socket.emit("sendImage",  "data:image/jpeg;base64,"+encoded);
                     Log.i(TAG, "run: + " +  "gya socket ab yha se" );
                     shareAdapter.notifyDataSetChanged();
                 }
             });

            }
        super.onActivityResult(requestCode, resultCode, data);
        }
    }
