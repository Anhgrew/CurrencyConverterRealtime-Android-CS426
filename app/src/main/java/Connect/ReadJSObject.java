package Connect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.hw_01_18125002.AddListMoneyNations;
import com.example.hw_01_18125002.R;
import com.kosalgeek.android.caching.FileCacher;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Entity.GlobalStorage;
import Entity.MoneyNation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ReadJSObject extends AsyncTask<String, Void, GlobalStorage> {
    public HashMap<String, Double> listRates;
    @SuppressLint("StaticFieldLeak")
    private Context context;


    public ReadJSObject(Context context, OnTaskCompleted listener) {
        this.context = context;
        this.listener = listener;
    }

    private OnTaskCompleted listener;

    public ReadJSObject(OnTaskCompleted listener) {
        this.listener = listener;
    }


    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(15000, TimeUnit.MILLISECONDS)
            .writeTimeout(15000, TimeUnit.MILLISECONDS)
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build();

    @Override
    protected GlobalStorage doInBackground(String... strings) {
        Request.Builder builder = new Request.Builder();
        builder.url(strings[0]);
        Request request = builder.build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response != null) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONObject rates = jsonObject.getJSONObject("rates");
                Iterator keysToCopyIterator = rates.keys();
                String baseCurrent = jsonObject.getString("base");
                String date = jsonObject.getString("date");
                listRates = new HashMap<>();
                while (keysToCopyIterator.hasNext()) {
                    String key = (String) keysToCopyIterator.next();
                    double rateOfKeyName = (double) rates.get(key);
                    listRates.put(key, rateOfKeyName);
                }
                return new GlobalStorage(listRates, baseCurrent, date);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(GlobalStorage globalStorage) {
        super.onPostExecute(globalStorage);
        Log.d("EEE", "onPostExecute: AAA");

        if (globalStorage != null) {
            MoneyNation adder = null;
            ArrayList<MoneyNation> listTmp = new ArrayList<>();
            for (String current : globalStorage.getSetData().keySet()) {
                adder = new MoneyNation(globalStorage.getSetData().get(current), current, R.drawable.coin,
                        "");
                listTmp.add(adder);
            }

            FileCacher<ArrayList<MoneyNation>> moneyNationArrayListFileCacher = new FileCacher<>(context, "currency");
            if (!moneyNationArrayListFileCacher.hasCache()) {
                try {
                    moneyNationArrayListFileCacher.appendOrWriteCache(listTmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            FileCacher<ArrayList<MoneyNation>> moneyNFileCacher = new FileCacher<>(context, "currency");
            List<MoneyNation> resultList = new ArrayList<>();
            if (moneyNFileCacher.hasCache()) {
                try {
                    resultList = moneyNFileCacher.readCache();
                    globalStorage.setMoneyList(resultList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            listener.onTaskCompleted(globalStorage);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}


