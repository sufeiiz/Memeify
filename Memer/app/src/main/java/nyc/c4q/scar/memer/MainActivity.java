package nyc.c4q.scar.memer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ViewSwitcher;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private TextToSpeech t1;
    private Intent intent;
    private Uri imageFileUri;
    private String stringVariable = "file:///sdcard/_pictureholder_id.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton camera = (ImageButton) findViewById(R.id.camera);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.speak("Meemer", TextToSpeech.QUEUE_FLUSH, null);
                showListViewDialog();
            }
        });
    }

    //This handles the activity for the intent: using the camera and choosing from a gallery.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageFileUri = data.getData();


        }

        if (requestCode == 0 && resultCode == RESULT_OK) {
            imageFileUri = Uri.parse(stringVariable);

        }

        Intent changeActivity = new Intent(MainActivity.this, SecondActivity.class);
        changeActivity.putExtra("luckyM", imageFileUri);
        if (imageFileUri != null) {
            startActivity(changeActivity);
        }
    }

    //This is for the dialog box: Camera or Gallery
    private void showListViewDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Please Choose:");
        final String[] items = {"Camera", "Gallery"};
        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (items[which].equalsIgnoreCase("Camera")) {
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.parse(stringVariable));

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, 0);
                    }
                }

                if (items[which].equalsIgnoreCase("Gallery")) {
                    intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                }
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

}