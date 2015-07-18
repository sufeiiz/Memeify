package nyc.c4q.scar.memer;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by sufeizhao on 7/17/15.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> imageList;
    private boolean template;

    public ImageAdapter(Context c, List<String> imageList, boolean template) {
        mContext = c;
        this.imageList = imageList;
        this.template = template;
    }

    public int getCount() {
        return imageList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null)
            imageView = new ImageView(mContext);
        else
            imageView = (ImageView) convertView;

        Picasso.with(mContext).load(imageList.get(position)).into(imageView);
        final String imageURL = imageList.get(position);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (template) {
                    String url = imageList.get(position);
                    new DownloadImageTask(url, imageView).execute();
                } else
                    showListViewDialog(imageURL, imageView);
            }
        });

        return imageView;
    }

    private void showListViewDialog(final String imageURL, final ImageView imageView) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        dialogBuilder.setTitle("Choose an option:");
        final String[] items = {"Save", "Share"};
        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (items[which].equalsIgnoreCase("Save")) {

                    File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File path = new File(pictureFolder, "Memer");
                    if (!path.exists())
                        path.mkdirs();
                    String filepath = path.getAbsolutePath();
                    String timeStamp = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";

                    DownloadManager dlManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri downloadUri = Uri.parse(imageURL);
                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);

                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false).setTitle("Memer")
                            .setDescription("Downloaded by Memer")
                            .setDestinationInExternalPublicDir(filepath, timeStamp);

                    dlManager.enqueue(request);
                    Toast.makeText(mContext, "Meme has been saved!", Toast.LENGTH_LONG).show();
                }

                if (items[which].equalsIgnoreCase("Share")) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/jpeg");
                    imageView.buildDrawingCache();
                    Bitmap bitmap = imageView.getDrawingCache();
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
                    try {
                        f.createNewFile();
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(bytes.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
                    mContext.startActivity(Intent.createChooser(share, "Share Image"));

                }
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
        String url;
        ImageView imageView;

        public DownloadImageTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(Void... params) {
            Bitmap bm = null;

            try {
                InputStream in = new java.net.URL(this.url).openStream();
                bm = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bm;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.buildDrawingCache();
            Bitmap bitmap = imageView.getDrawingCache();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
            try {
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent changeActivity = new Intent(mContext, EditMeme.class);
            changeActivity.putExtra("image", Uri.parse("file:///sdcard/temporary_file.jpg"));
            mContext.startActivity(changeActivity);
        }
    }
}