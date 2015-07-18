package nyc.c4q.scar.memer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sufeizhao on 5/31/15.
 */
public class EditMeme extends AppCompatActivity implements Serializable {

    public final String stringVariable = "file:///sdcard/_pictureholder_id.jpg";
    private ViewSwitcher viewSwitcher;
    private Uri uri;
    private Bitmap image;
    private Intent intent;
    private ImageView imageView, imageView2;
    private boolean isVanilla = true;
    private EditText top, bottom, big, small;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_meme);

        ImageButton changeImage = (ImageButton) findViewById(R.id.change_img);
        ImageButton shareImage = (ImageButton) findViewById(R.id.share);
        ImageButton saveImage = (ImageButton) findViewById(R.id.save);
        Button switcherButton = (Button) findViewById(R.id.switcherButton);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher);
        imageView = (ImageView) findViewById(R.id.vanilla_image);
        imageView2 = (ImageView) findViewById(R.id.demo_image);
        top = (EditText) findViewById(R.id.top);
        bottom = (EditText) findViewById(R.id.bottom);
        big = (EditText) findViewById(R.id.big);
        small = (EditText) findViewById(R.id.small);
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
        showCursor();

        if (savedInstanceState != null) {
            isVanilla = (boolean) savedInstanceState.get("isVanilla");
            uri = savedInstanceState.getParcelable("image");
        } else
            uri = getIntent().getExtras().getParcelable("image");

        try {
            image = decodeUri(this, uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(image);
        imageView2.setImageBitmap(image);

        showCursor();
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
            Uri uri2 = getImageUri(getApplicationContext(), bm);
            Intent shareIntent = new Intent();
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
            Bitmap bm = v1.getDrawingCache();

            File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Memer");
            if (!path.exists())
                path.mkdirs();
            File filepath = new File(path, "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg");

            try {
                FileOutputStream os = new FileOutputStream(filepath);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();

                MediaStore.Images.Media.insertImage(getContentResolver(), filepath.getAbsolutePath(), filepath.getName(), "Created by Memer");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(), "Image was saved", Toast.LENGTH_SHORT).show();
            showCursor();
        }
    }

    public class switchClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            viewSwitcher.showNext();
            String string1;
            String string2;
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
        run.run();
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
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
    };

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "memer", null);
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

    public static Bitmap decodeUri(Context c, Uri uri) throws FileNotFoundException {
        int requiredSize = 2048;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth;
        int height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }

    @Override
    public void onSaveInstanceState(Bundle toSave) {
        super.onSaveInstanceState(toSave);
        toSave.putParcelable("image", uri);
        toSave.putBoolean("isVanilla", isVanilla);
    }
}

