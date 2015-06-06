package nyc.c4q.scar.memer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sufeizhao on 5/31/15.
 */
public class SecondActivity extends AppCompatActivity {

    private ViewSwitcher viewSwitcher;
    private Uri uri;
    private Intent intent;
    private ImageView imageView;
    private ImageView imageView2;
    private String stringVariable = "file:///sdcard/_pictureholder_id.jpg";
    private boolean isVanilla = true;
    private float fontSize;
    private String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //This loads up the last saved boolean for which layout mode was selected
        if (savedInstanceState != null) {
            isVanilla = (boolean) savedInstanceState.get("isVanilla");
        }

        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher);
        imageView = (ImageView) findViewById(R.id.insert_pic_id);
        imageView2 = (ImageView) findViewById(R.id.insert_pic_id2);
        ImageButton changeImage = (ImageButton) findViewById(R.id.change_img);
        ImageButton shareImage = (ImageButton) findViewById(R.id.share);
        ImageButton saveImage = (ImageButton) findViewById(R.id.save);
        final EditText top = (EditText) findViewById(R.id.top);
        final EditText bottom = (EditText) findViewById(R.id.bottom);

        //This loads up dialog
        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListViewDialog();
            }
        });

        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.app_name)));
                //File file = createImageFile();
            }
        });

//        shareImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_STREAM, MediaStore.Images.Media.getContentUri("image" + timeStamp + ".jpg"));
//                shareIntent.setType("image/jpeg");
//                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.app_name)));
//            }
//        });

        // Hides editText is nothing has been entered, brings it back after image has been saved
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (top.getText().toString().matches("")) {
                    top.setVisibility(View.GONE);
                }
                if (bottom.getText().toString().matches("")) {
                    bottom.setVisibility(View.GONE);
                }

                View v1 = viewSwitcher.getCurrentView();
                v1.setDrawingCacheEnabled(true);
                Bitmap bm = v1.getDrawingCache();
                MediaStore.Images.Media.insertImage(getContentResolver(), bm, "image" + timeStamp + ".jpg" , timeStamp.toString());
                Toast.makeText(getApplicationContext(), "Image was saved", Toast.LENGTH_SHORT).show();

                top.setVisibility(View.VISIBLE);
                bottom.setVisibility(View.VISIBLE);
            }
        });

        Button switcherButton = (Button) findViewById(R.id.switcherButton);
        switcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewSwitcher.showNext();
                isVanilla = !isVanilla;
            }
        });

        //This loads up any existing savedInstanceStates
        if (savedInstanceState != null) {
            uri = (Uri) savedInstanceState.get("luckyM");
        } else {
            uri = (Uri) getIntent().getExtras().get("luckyM");
        }
        imageView.setImageURI(uri);
        imageView2.setImageURI(uri);
        fontSize = getImageSize(uri) / 40;

        //This sets the layout according to which layout mode is selected
        if (isVanilla) {
            Typeface impact = Typeface.createFromAsset(getAssets(), "Impact.ttf");
            top.setTypeface(impact);
            bottom.setTypeface(impact);

            top.setTextSize(fontSize);
            bottom.setTextSize(fontSize);
        } else {
            top.setTypeface(Typeface.create("serif", Typeface.NORMAL));
            bottom.setTypeface(Typeface.create("serif", Typeface.NORMAL));
        }
    }

    //This handles the activity for the intent: using the camera and choosing from a gallery.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            imageView.setImageURI(uri);
            fontSize = getImageSize(uri) / 40;

        }

        if (requestCode == 0 && resultCode == RESULT_OK) {
            uri = Uri.parse("file:///sdcard/picture.jpg");
            imageView.setImageURI(null);
            imageView.setImageURI(Uri.parse(stringVariable));
            fontSize = getImageSize(uri) / 40;

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

                    Uri imageFileUri = Uri.parse(stringVariable);
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
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

    //saves the current state
    @Override
    public void onSaveInstanceState(Bundle toSave) {
        super.onSaveInstanceState(toSave);
        toSave.putParcelable("luckyM", uri);
        toSave.putBoolean("isVanilla", isVanilla);
    }

    // gets image height to adjust edittext accordingly
    public int getImageSize(Uri uri) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(getAbsolutePath(uri), o);
        return o.outHeight;
    }

    public String getAbsolutePath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
}