package company.com.locationfinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import company.com.locationfinder.fragments.LocationAdderFragment;

public class DataViewActivity extends AppCompatActivity implements LocationAdderFragment.OnFragmentInteractionListener {

    Intent intent;
    String url_str;
    String qrDataJSON;
    TextView qrDataValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view);

        intent = getIntent();
        url_str = intent.getStringExtra("urldata");
        new JsonTask().execute(url_str);
        initViews();
    }
    private void initViews() {
        qrDataValue = findViewById(R.id.ReturnDataValue);
    }
    @Override
    protected void onResume() {
        super.onResume();
        qrDataValue.setText(qrDataJSON);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class JsonTask extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(DataViewActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //whole response

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            qrDataValue.setText(result);

            Log.d("QRData",result);


//            Bundle bundle = new Bundle();
//            bundle.putString("apidata", result);
            // set Fragmentclass Arguments
//            LocationAdderFragment fragobj = new LocationAdderFragment();
//            fragobj.setArguments(bundle);
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.mainFrame, fragobj);
//            ft.commit();
//            finish();
            Intent intent =new Intent(DataViewActivity.this,MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("apidata", result);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }
}
