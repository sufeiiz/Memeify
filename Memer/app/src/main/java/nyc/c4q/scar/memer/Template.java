package nyc.c4q.scar.memer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by sufeizhao on 7/17/15.
 */
public class Template extends Activity {

    private static final String url = "https://api.imgflip.com/get_memes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template);

        ListView template_list = (ListView) findViewById(R.id.template_list);

    }

    private class AsyncLoad extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... params) {
            List<String> list = new ArrayList<>();

            try {
                URL jsonUrl = new URL(url);
                HttpsURLConnection connection = null;
                connection = (HttpsURLConnection) jsonUrl.openConnection();
                connection.setConnectTimeout(0);
                connection.setReadTimeout(0);
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                String jsonString = builder.toString();

                JSONObject jsonObject = new JSONObject(jsonString);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray memes = data.getJSONArray("memes");
                for (int i = 0; i < memes.length(); i++) {
                    JSONObject image = (JSONObject) memes.get(i);
                    String imageUrl = image.getString("url");
                    if (imageUrl != null) {
                        list.add(imageUrl);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return list;
        }
    }
}
