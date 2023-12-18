package com.annisarahmah02.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
public class formrutin extends AppCompatActivity {

    private AutoCompleteTextView etBadge;
    private TextView tNama, tAlamat, tfitnon;
    private EditText etsuhu, etsis, etdias;
    private EditText etnadi;
    private Button btnsv, btncancel, btnview;
    private JSONArray jsonData;
    private List<BadgeInfo> badgeInfoList;

    private class FetchDataAsyncTask extends AsyncTask<String, Void, String> {

        private class ApiClient {
            private static final String BASE_URL = "http://192.168.100.251:80/save_data.php"; // Ganti dengan URL API Anda
            private OkHttpClient client = new OkHttpClient();

            public String fetchData(String lokasi, String noBadge, String name, String Perusahaan, String SuhuBadan, String tekananSistolik, String tekananDiastolik, String nadiIstirahat) throws IOException {
                String url = BASE_URL + "?lokasi=" + lokasi + "&&No_Badge=" + noBadge + "&&NAME=" + name + "&&Perusahaan=" + Perusahaan + "&&Suhu_Badan=" + SuhuBadan + "&&Tekanan_Sistolik=" + tekananSistolik + "&&Tekanan_Diastolik=" +
                        tekananDiastolik + "&&Nadi_istirahat=" + nadiIstirahat ;

                Log.d("ResponseJSON", url);

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    return response.body().string();
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String selectedLocation = params[0];
            String badgeNumber = params[1];
            String Name = params[2];
            String Perusahaan = params[3];
            String suhuBadan = params[4];
            String tekananSistolik = params[5];
            String tekananDiastolik = params[6];
            String nadiIstirahat = params[7];

            ApiClient apiClient = new ApiClient();
            try {
                return apiClient.fetchData(selectedLocation, badgeNumber, Name, Perusahaan, suhuBadan, tekananSistolik
                , tekananDiastolik, nadiIstirahat);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            Log.d("ResponseJSON", response);

            if (response != null) {
                // Check if the response starts with '{' to indicate it's a JSON object
                if (response.startsWith("{")) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        // Lakukan proses terhadap objek JSON yang diterima di sini
                        String suhuValue = jsonResponse.optString("Suhu_Badan");
                        String sisValue = jsonResponse.optString("Tekanan_Sistolik");
                        String diasValue = jsonResponse.optString("Tekanan_Diastolik");

                        // Kemudian Anda dapat melakukan apapun dengan nilai-nilai ini, misalnya menampilkan dalam TextView
                        etsuhu.setText(suhuValue);
                        etsis.setText(sisValue);
                        etdias.setText(diasValue);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Tangani kesalahan JSON di sini
                    }
                } else {
                    // Handle the case where the response is not valid JSON
                    // For example, log the response for debugging purposes
                    Log.e("Response", "Not a valid JSON: " + response);
                }
            } else {
                // Tangani situasi respons null atau kesalahan lainnya
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formrutin);
        tfitnon = findViewById(R.id.tfitnon);
        etsuhu = findViewById(R.id.etsuhu);
        etsis = findViewById(R.id.etsis);
        etdias = findViewById(R.id.etdias);
        etnadi = findViewById(R.id.etnadi);
        etBadge = findViewById(R.id.etbadge);
        tNama = findViewById(R.id.tnama);
        tAlamat = findViewById(R.id.talamat);
        btnsv = findViewById(R.id.btnsave);
        btncancel = findViewById(R.id.btncancel);
        btnview = findViewById(R.id.btnview);
        String selectedLocation = getIntent().getStringExtra("selectedItem");

        // Read JSON data from the raw resource
        String json = readJSONFromResource(getResources(), R.raw.nama);

        // Parse JSON data
        try {
            JSONObject jsonObject = new JSONObject(json);
            jsonData = jsonObject.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a custom adapter and set it for the AutoCompleteTextView
        badgeInfoList = extractBadgeInfoFromJson(jsonData);
        customAutoCompleteAdapter adapter = new customAutoCompleteAdapter(this, badgeInfoList);
        etBadge.setAdapter(adapter);

        // Handle selection event when an item is clicked in the autocomplete dropdown
        etBadge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BadgeInfo selectedBadgeInfo = (BadgeInfo) parent.getItemAtPosition(position);
                etBadge.setText(selectedBadgeInfo.getBadgeNumber());
                tNama.setText(selectedBadgeInfo.getName());
                tAlamat.setText(selectedBadgeInfo.getAddress());// Set badge number in etBadge
            }
        });

        etsuhu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateAndSetFitStatus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etsis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateAndSetFitStatus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etdias.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateAndSetFitStatus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etnadi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateAndSetFitStatus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Set up a TextWatcher to monitor user input
        etBadge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This method is called before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This method is called after the text has been changed
            }
        });

        btnsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedLocation = getIntent().getStringExtra("selectedItem");
                String badgeNumber = etBadge.getText().toString();
                String Name = tNama.getText().toString();
                String Perusahaan = tAlamat.getText().toString();
                String suhuBadan = etsuhu.getText().toString();
                String tekananSistolik = etsis.getText().toString();

                String tekananDiastolik = etdias.getText().toString();
                String nadiIstirahat = etnadi.getText().toString();

                FetchDataAsyncTask fetchDataAsyncTask = new FetchDataAsyncTask();
                fetchDataAsyncTask.execute(selectedLocation, badgeNumber, Name, Perusahaan, suhuBadan, tekananSistolik
                ,tekananDiastolik,nadiIstirahat);
            }
        });

        StringBuilder dataToShow = new StringBuilder(); // Initialize the StringBuilder

        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize the StringBuilder to store fetched data
                StringBuilder dataToShow = new StringBuilder();
                Log.d("ResponseJSON", "masuk method view");


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.100.251:80/tampil_data.php/") // Adjust the base URL as needed
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();


                ApiInterface apiInterface = retrofit.create(ApiInterface.class);

                Call<List<HealthData>> call = apiInterface.getHealthData();
                call.enqueue(new Callback<List<HealthData>>() {
                    @Override
                    public void onResponse(Call<List<HealthData>> call, retrofit2.Response<List<HealthData>> response) {
                        if (response.isSuccessful()) {
                            List<HealthData> healthDataList = response.body();
                            Log.d("ResponseJSON",response.toString() );
                            // Append fetched data to the StringBuilder
                            dataToShow.append("Lokasi | No Badge | nama | Perusahaan | Suhu Badan | Tekanan Sistolik | Tekanan Diastolik | Nadi Istirahat \n");
                            for (HealthData data : healthDataList) {
                                String lokasi = data.getLokasi();
                                String noBadge = data.getNoBadge();
                                String name = data.getName();
                                String perusahaan = data.getPerusahaan();
                                String suhuBadan = data.getSuhuBadan();
                                String tekananSistolik = data.getTekananSistolik();
                                String tekananDiastolik = data.getTekananDiastolik();
                                String nadiIstirahat = data.getNadiIstirahat();

                                // Append the data to the StringBuilder
                                dataToShow.append(lokasi + " | " + noBadge + " | " + name + " | " + perusahaan + " | " + suhuBadan + " | " + tekananSistolik + " | " + tekananDiastolik + " | " + nadiIstirahat).append("\n");
                                //dataToShow.append("No Badge: ").append(noBadge).append("\n");
                                //dataToShow.append("Nama: ").append(name).append("\n");
                                //dataToShow.append("Perusahaan: ").append(perusahaan).append("\n");
                                //dataToShow.append("Suhu Badan: ").append(suhuBadan).append("\n");
                                //dataToShow.append("Tekanan Sistolik: ").append(tekananSistolik).append("\n");
                                //dataToShow.append("Tekanan Diastolik: ").append(tekananDiastolik).append("\n");
                                //dataToShow.append("Nadi Istirahat: ").append(nadiIstirahat).append("\n");
                                //dataToShow.append("\n");
                                Log.d("ResponseJSON", dataToShow.toString() );
                            }
                            showMessage("Data", dataToShow.toString());

                        } else {
                            int statusCode = response.code(); // Get the HTTP status code
                            String errorMessage = "Error: " + statusCode;
                            // You can provide more detailed error handling based on the status code
                            // For example, show a toast or update a TextView with the error message
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<HealthData>> call, Throwable t) {
                        // Handle failure
                    }
                });
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(formrutin.this, menu.class);
                startActivity(intent);
            }
        });
    }
    public void showMessage (String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    private void validateAndSetFitStatus() {
        String suhuText = etsuhu.getText().toString().trim();
        String sisText = etsis.getText().toString().trim();
        String diasText = etdias.getText().toString().trim();
        String nadiText = etnadi.getText().toString().trim();

        boolean isSuhuFit = true;
        boolean isSisFit = true;
        boolean isDiasFit = true;
        boolean isNadiFit = true;

        if (!suhuText.isEmpty()) {
            double suhu = Double.parseDouble(suhuText);
            if (suhu < 36 || suhu > 37) {
                isSuhuFit = false;
            }
        }

        if (!sisText.isEmpty()) {
            int sis = Integer.parseInt(sisText);
            if (sis < 90 || sis > 120) {
                isSisFit = false;
            }
        }

        if (!diasText.isEmpty()) {
            int dias = Integer.parseInt(diasText);
            if (dias < 60 || dias > 80) {
                isDiasFit = false;
            }
        }

        if (!isSuhuFit || !isSisFit || !isDiasFit) {
            tfitnon.setTextColor(Color.RED);
            tfitnon.setText("Anda Tidak Fit");
        }
        if (!nadiText.isEmpty()) {
            int nadi = Integer.parseInt(nadiText);
            if (nadi < 50 || nadi > 110) {
                isNadiFit = false;
            }
        }

        if (!isSuhuFit || !isSisFit || !isDiasFit || !isNadiFit) {
            tfitnon.setTextColor(Color.RED);
            tfitnon.setText("Anda Tidak Fit");
        } else {
            tfitnon.setTextColor(Color.GREEN);
            tfitnon.setText("Anda fit");
        }
    }

    private void updateAutofillFields(BadgeInfo badgeInfo) {
        tNama.setText(badgeInfo.getName());
        tAlamat.setText(badgeInfo.getAddress());
    }

    private String readJSONFromResource(Resources resources, int resourceId) {
        InputStream inputStream = resources.openRawResource(resourceId);
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    private List<BadgeInfo> extractBadgeInfoFromJson(JSONArray jsonData) {
        List<BadgeInfo> badgeInfoList = new ArrayList<>();
        for (int i = 0; i < jsonData.length(); i++) {
            try {
                JSONObject item = jsonData.getJSONObject(i);
                String badgeNumber = item.getString("nobadge");
                String name = item.getString("name");
                String address = item.getString("address");
                badgeInfoList.add(new BadgeInfo(badgeNumber, name, address));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return badgeInfoList;
    }
}

class BadgeInfo {
    private String badgeNumber;
    private String name;
    private String address;

    public BadgeInfo(String badgeNumber, String name, String address) {
        this.badgeNumber = badgeNumber;
        this.name = name;
        this.address = address;
    }

    public String getBadgeNumber() {
        return badgeNumber;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return badgeNumber + " - " + name + " - " + address;
    }
}

class customAutoCompleteAdapter extends ArrayAdapter<BadgeInfo> {
    private final LayoutInflater inflater;

    public customAutoCompleteAdapter(Context context, List<BadgeInfo> badgeInfoList) {
        super(context, R.layout.dropdown_item, badgeInfoList);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.dropdown_item, parent, false);
        }

        BadgeInfo badgeInfo = getItem(position);

        TextView badgeTextView = view.findViewById(R.id.etbadge);
        badgeTextView.setText(badgeInfo.getBadgeNumber());

        TextView nameAddressTextView = view.findViewById(R.id.talamat);
        nameAddressTextView.setText(badgeInfo.getName() + " - " + badgeInfo.getAddress());

        return view;
    }

}
