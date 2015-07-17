package nyc.c4q.scar.memer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sufeizhao on 5/31/15.
 */
public class EditMeme extends AppCompatActivity implements Serializable {

    public static Bitmap bm;
    public final String IMAGE_FILE = "memer";
    private ViewSwitcher viewSwitcher;
    private Uri uri, uri2;
    private Intent intent;
    private ImageView imageView, imageView2;
    private String stringVariable = "file:///sdcard/_pictureholder_id.jpg";
    private boolean isVanilla = true;
    private String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    private SharedPreferences preferences = null;
    private EditText top, bottom, big, small;
    private String string1, string2;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meme);
        preferences = this.getSharedPreferences(IMAGE_FILE, Context.MODE_PRIVATE);

        if (savedInstanceState != null) {
            isVanilla = (boolean) savedInstanceState.get("isVanilla");
            uri = savedInstanceState.getParcelable("uri");
            uri2 = savedInstanceState.getParcelable("uri2");
        }

        ImageButton changeImage = (ImageButton) findViewById(R.id.change_img);
        ImageButton shareImage = (ImageButton) findViewById(R.id.share);
        ImageButton saveImage = (ImageButton) findViewById(R.id.save);
        Button switcherButton = (Button) findViewById(R.id.switcherButton);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher);
        imageView = (ImageView) findViewById(R.id.insert_pic_id);
        imageView2 = (ImageView) findViewById(R.id.insert_pic_id2);
        top = (EditText) findViewById(R.id.top);
        bottom = (EditText) findViewById(R.id.bottom);
        big = (EditText) findViewById(R.id.bigtext);
        small = (EditText) findViewById(R.id.smalltext);
        top.setMovementMethod(null);
        bottom.setMovementMethod(null);

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListViewDialog();
            }
        });
        shareImage.setOnClickListener(new shareClickListener());
        saveImage.setOnClickListener(new saveClickListener());
        switcherButton.setOnClickListener(new switchClickListener());

        //This loads up any existing savedInstanceStates
        if (savedInstanceState != null) {
            uri = (Uri) savedInstanceState.get("luckyM");
        } else {
            uri = (Uri) getIntent().getExtras().get("luckyM");
        }
        imageView.setImageURI(uri);
        imageView2.setImageURI(uri);


        //This sets the layout according to which layout mode is selected
        if (isVanilla) {
            Typeface impact = Typeface.createFromAsset(getAssets(), "Impact.ttf");
            top.setTypeface(impact);
            bottom.setTypeface(impact);
        }
    }

    /**
     * OnClickListener Methods
     */
    public class shareClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            removeCursor();
            View v1 = viewSwitcher.getFocusedChild();
            v1.setDrawingCacheEnabled(true);

            Bitmap bm = v1.getDrawingCache();
            uri2 = getImageUri(getApplicationContext(), bm);
            Intent shareIntent = new Intent();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("image_file", MODE_PRIVATE);
            editor.commit();

            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri2);
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.app_name)));
            showCursor();
        }
    };

    public class saveClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            removeCursor();
            View v1 = viewSwitcher.getCurrentView();
            v1.setDrawingCacheEnabled(true);
            bm = v1.getDrawingCache();
            MediaStore.Images.Media.insertImage(getContentResolver(), bm, "image" + timeStamp + ".jpg", timeStamp.toString());
            Toast.makeText(getApplicationContext(), "Image was saved", Toast.LENGTH_SHORT).show();
            showCursor();
        }
    }

    public class switchClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            viewSwitcher.showNext();
            if (isVanilla) {
                string1 = top.getText().toString();
                string2 = bottom.getText().toString();
                big.setText(string1);
                small.setText(string2);
                isVanilla = !isVanilla;
            } else {
                string1 = big.getText().toString();
                string2 = small.getText().toString();
                top.setText(string1);
                bottom.setText(string2);
                isVanilla = true;
            }
        }
    }

    public void removeCursor() {
        if (isVanilla) {
            if (top.getText().toString().matches(""))
                top.setVisibility(View.GONE);
            if (bottom.getText().toString().matches(""))
                bottom.setVisibility(View.GONE);
            top.setCursorVisible(false);
            bottom.setCursorVisible(false);
        } else {
            if (big.getText().toString().matches(""))
                big.setVisibility(View.GONE);
            if (small.getText().toString().matches(""))
                small.setVisibility(View.GONE);
            big.setCursorVisible(false);
            small.setCursorVisible(false);
        }
    }

    public void showCursor() {
        top.setVisibility(View.VISIBLE);
        bottom.setVisibility(View.VISIBLE);
        big.setVisibility(View.VISIBLE);
        small.setVisibility(View.VISIBLE);
        top.setCursorVisible(true);
        bottom.setCursorVisible(true);
        big.setCursorVisible(true);
        small.setCursorVisible(true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        int width = imageView.getWidth();
        int height = imageView.getHeight();

        ViewGroup.LayoutParams topLP = top.getLayoutParams();
        ViewGroup.LayoutParams bottomLP = bottom.getLayoutParams();
        Typeface impact = Typeface.createFromAsset(getAssets(), "Impact.ttf");
        top.setTypeface(impact);
        bottom.setTypeface(impact);

        float fontsize;
        if (width < height) {
            topLP.width = width - 20;
            bottomLP.width = width - 20;
            top.setLayoutParams(topLP);
            bottom.setLayoutParams(bottomLP);
            fontsize = height / 20;
            top.setTextSize(fontsize);
            bottom.setTextSize(fontsize);
        } else {
            topLP.width = width - 10;
            bottomLP.width = width - 10;
            top.setLayoutParams(topLP);
            bottom.setLayoutParams(bottomLP);
            fontsize = height / 13;
            top.setTextSize(fontsize);
            bottom.setTextSize(fontsize);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    //This handles the activity for the intent: using the camera and choosing from a gallery.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            imageView.setImageURI(uri);
            imageView2.setImageURI(uri);
        }

        if (requestCode == 0 && resultCode == RESULT_OK) {
            uri = Uri.parse(stringVariable);
            imageView.setImageURI(uri.normalizeScheme());
            imageView2.setImageURI(uri.normalizeScheme());
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
                    if (intent.resolveActivity(getPackageManager()) != null)
                        startActivityForResult(intent, 0);
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

    @Override
    public void onSaveInstanceState(Bundle toSave) {
        super.onSaveInstanceState(toSave);
        toSave.putParcelable("luckyM", uri);
        toSave.putParcelable("luckyM2", uri2);
        toSave.putBoolean("isVanilla", isVanilla);
    }
}

