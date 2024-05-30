package ru.mirea.venikovea.mireaproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;

public class WeatherFragment extends Fragment {
    private TextView locationView;
    private TextView weatherView;
    private Context context;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        locationView = view.findViewById(R.id.locationInfoView);
        weatherView = view.findViewById(R.id.weatherView);
        context = getContext();

        loadInfo();

        return view;
    }

    private void loadInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;

        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadInfoTask().execute("https://ipinfo.io/json",
                    "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current_weather=true");
        } else {
            Toast.makeText(context, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadInfoTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            locationView.setText("Loading...");
            weatherView.setText("Loading...");
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String ipData = downloadInfo(strings[0]);
                JSONObject ipResponse = new JSONObject(ipData);

                JSONObject locationInfo = new JSONObject();
                locationInfo.put("country", ipResponse.getString("country"));
                locationInfo.put("region", ipResponse.getString("region"));
                locationInfo.put("city", ipResponse.getString("city"));
                locationInfo.put("ip", ipResponse.getString("ip"));

                String loc = ipResponse.getString("loc");
                String latitude = loc.split(",")[0];
                String longitude = loc.split(",")[1];
                Log.d("location", latitude);

                String weatherUrl = String.format(
                        strings[1],
                        latitude, longitude
                );

                String weatherData = downloadInfo(weatherUrl);

                JSONObject weatherResponse = new JSONObject(weatherData);
                JSONObject current = weatherResponse.getJSONObject("current_weather");
                String weather = current.getString("temperature");

                JSONObject info = new JSONObject();
                info.put("location", locationInfo);
                info.put("weather", weather);

                return info.toString();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(MainActivity.class.getSimpleName(), s);
            try {
                JSONObject info = new JSONObject(s);
                JSONObject location = info.getJSONObject("location");

                String country = location.getString("country");
                String region = location.getString("region");
                String city = location.getString("city");
                String ip = location.getString("ip");

                String weather = info.getString("weather");

                locationView.setText("Страна: " + country +
                        "\nРегион: " + region + "\nГород: " + city + "\nIP: " + ip);
                weatherView.setText("Температура " + weather + "°C");
            } catch (JSONException e) {
                locationView.setText("Error. Try again.");
                weatherView.setText("Error. Try again.");
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }

        private String downloadInfo(String address) throws IOException {
            InputStream inputStream = null;
            String data = "";

            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(100000);
                connection.setConnectTimeout(100000);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);
                connection.setDoInput(true);
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int read = 0;
                    while ((read = inputStream.read()) != -1) {
                        bos.write(read);
                    }
                    bos.close();
                    data = bos.toString();
                } else {
                    data = connection.getResponseMessage() + ". Error Code: " + responseCode;
                }
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return data;
        }
    }
}