package nyc.c4q.scar.memer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
public class TopMeme extends Activity {

    private static final String ENDPOINT = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=meme+";
    EditText editText;
    Button search;
    ListView listview;
    ImageAdapter adapter;
    TextView loading;
    AsyncLoad images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_meme);

        loading = (TextView) findViewById(R.id.loading);
        editText = (EditText) findViewById(R.id.edittext);
        search = (Button) findViewById(R.id.search);
        listview = (ListView) findViewById(R.id.list);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {}
                images = new AsyncLoad();
                images.execute();
                loading.setVisibility(View.VISIBLE);
            }
        });
    }

    private class AsyncLoad extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {

            List<String> list = new ArrayList<>();
            String url = ENDPOINT + editText.getText().toString();

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
                JSONObject response = jsonObject.getJSONObject("responseData");
                JSONArray results = response.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject image = (JSONObject) results.get(i);
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

        @Override
        protected void onPostExecute(List<String> list) {
            adapter = new ImageAdapter(TopMeme.this, list, false);
            listview.setAdapter(adapter);
            loading.setVisibility(View.INVISIBLE);
        }
    }
}
