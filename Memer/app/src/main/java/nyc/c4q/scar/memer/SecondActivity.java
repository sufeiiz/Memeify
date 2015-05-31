package nyc.c4q.scar.memer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by sufeizhao on 5/31/15.
 */
public class SecondActivity extends AppCompatActivity{

    private Intent intent;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ImageView imageView = (ImageView) findViewById(R.id.insert_pic_id);
        Button changeImage = (Button) findViewById(R.id.change_img);





        if (savedInstanceState == null) {
            Bitmap bitmap = this.getIntent().getExtras().getParcelable("image");
            imageView.setImageBitmap(bitmap);
        }

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListViewDialog();
            }
        });
    }

    //This handles the activity for the intent: using the camera and choosing from a gallery.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap;
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();

            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            cursor.close();
        }

        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (data != null) {
                bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
            }
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
