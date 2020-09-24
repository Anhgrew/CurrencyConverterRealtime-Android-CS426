package com.example.hw_01_18125002;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.kosalgeek.android.caching.FileCacher;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import Adapter.NumPadAdapter;
import Adapter.TargetMoneyAdapter;
import Connect.OnTaskCompleted;
import Connect.ReadJSObject;
import Entity.GlobalStorage;
import Entity.Key;
import Entity.MoneyNation;
import Entity.Record;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {
    int REQUEST_CODE_EDIT = 123;
    int flat;
    public Button addingBtn;
    List<MoneyNation> restoreItem = new ArrayList<>();
    List<MoneyNation> transferList;
    List<MoneyNation> initialList;
    List<MoneyNation> remainList;
    List<Record> records;
    public TargetMoneyAdapter targetMoneyAdapter;
    public List<MoneyNation> moneyTargetNationList = new ArrayList<>();
    TextView expressionTextView;
    MoneyNation moneyNation;
    TextView textView, targetTextView;
    public String expression = new String("");
    public GridView leftNumPad, rightNumPad, topScreen, targetNationList;
    String[] leftPadElements = new String[]{"7", "8", "9",
            "4", "5", "6",
            "1", "2", "3",
            ".", "0", "="};
    String[] rightPadElements = new String[]{"\u232b", "/", "*", "-", "+"};
    public GlobalStorage storageCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expressionTextView = findViewById(R.id.moneyInput);
        init();
        loadDataOffline();
        createBaseCurrency();
        createNumPad();

        if (savedInstanceState != null) {
            transferList = savedInstanceState.getParcelableArrayList("listOfTransferMoneyNation");
            restoreItem = savedInstanceState.getParcelableArrayList("restoreItemList");
            moneyTargetNationList = savedInstanceState.getParcelableArrayList("listOfTargetMoneyNation");
            remainList = savedInstanceState.getParcelableArrayList("remainMoneyNationList");
            String inputText = savedInstanceState.getString("inputTextView").toString();
            if (inputText.length() >= 0) {
                expressionTextView.setText(inputText);
                expressionTextView.setTextColor(Color.RED);
                expressionTextView.setTextSize(20);
                expressionTextView.setTypeface(expressionTextView.getTypeface(), Typeface.BOLD_ITALIC);
            }
        }
        AddListMoneyIntent();
        createTargetCurrency();

    }

    private void loadDataOffline() {
        FileCacher<ArrayList<MoneyNation>> moneyNFileCacher = new FileCacher<>(this, "currency");
        if (moneyNFileCacher.hasCache()) {
            try {
                initialList = moneyNFileCacher.readCache();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() {
        ReadJSObject readJSObject = new ReadJSObject(getApplicationContext(), this);
        readJSObject.execute("https://api.exchangeratesapi.io/latest");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history:
                Intent intent = new Intent(MainActivity.this, ConversionHistory.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK && data != null) {
            transferList = data.getParcelableArrayListExtra("moneyNationTransferList");
            flat = data.getIntExtra("backFlat", 1000);
            flat = 1000;
            remainList = data.getParcelableArrayListExtra("moneyNationRemainedList");
            restoreItem.clear();
            moneyTargetNationList = transferList;
            createTargetCurrency();
        }
    }

    private void AddListMoneyIntent() {
        addingBtn = findViewById(R.id.addBtn);
        addingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddListMoneyNations.class);
                intent.putExtra("checkingFlat", flat);
                intent.putParcelableArrayListExtra("moneyNationInitialList", (ArrayList<? extends Parcelable>) initialList);
                intent.putParcelableArrayListExtra("moneyNationTransferList", (ArrayList<? extends Parcelable>) transferList);
                intent.putParcelableArrayListExtra("restoreItem", (ArrayList<? extends Parcelable>) restoreItem);
                intent.putParcelableArrayListExtra("moneyNationRemainedBackList", (ArrayList<? extends Parcelable>) remainList);
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("inputTextView", (String) expressionTextView.getText().toString());
        outState.putParcelableArrayList("listOfTransferMoneyNation", (ArrayList<? extends Parcelable>) transferList);
        outState.putParcelableArrayList("listOfTargetMoneyNation", (ArrayList<? extends Parcelable>) moneyTargetNationList);
        outState.putParcelableArrayList("restoreItemList", (ArrayList<? extends Parcelable>) restoreItem);
        outState.putParcelableArrayList("remainMoneyNationList", (ArrayList<? extends Parcelable>) remainList);
        outState.putInt("checkingFlat", flat);
    }


    private void createTargetCurrency() {
        targetNationList = findViewById(R.id.secondNation);
        if (moneyTargetNationList != null && moneyTargetNationList.size() >= 0) {
            targetMoneyAdapter = new TargetMoneyAdapter(MainActivity.this, R.layout.second_money_nation, moneyTargetNationList);
            targetNationList.setAdapter(targetMoneyAdapter);
            targetNationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MoneyNation unCheckMoneyNation = moneyTargetNationList.get(i);
                    if (unCheckMoneyNation != null) {
                        Toast.makeText(view.getContext(),
                                "Item removed : " + unCheckMoneyNation.name, Toast.LENGTH_SHORT).show();
                        restoreItem.add(unCheckMoneyNation);
                        moneyTargetNationList.remove(i);
                        targetMoneyAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }


    private void createBaseCurrency() {
        LinearLayout baseLayout = findViewById(R.id.firstNation);
        View view = LayoutInflater.from(this).inflate(R.layout.first_money_nation, baseLayout);
        getDefaultFirst();
    }

    private void getDefaultFirst() {
        moneyNation = new MoneyNation(1, "EUR", R.drawable.eur, "");
        TextView amountTextView = findViewById(R.id.amount);
        TextView unitTextView = findViewById(R.id.unit);
        TextView fullNameUnitTextView = findViewById(R.id.full_name_unit);
        ImageView imageView = findViewById(R.id.nationImage);
        unitTextView.setText(moneyNation.name);
        fullNameUnitTextView.setText(moneyNation.information);
        amountTextView.setText(Double.toString(moneyNation.result));
        imageView.setImageResource(moneyNation.ensignImage);
    }


    public void createNumPad() {
        rightNumPad = findViewById(R.id.rightNumPad);
        leftNumPad = findViewById(R.id.leftNumPad);
        final List<Key> leftKeyList = createLeftKeyList();
        final List<Key> rightKeyList = createRightKeyList();
        NumPadAdapter leftPadArrayAdapter = new NumPadAdapter(MainActivity.this, R.layout.key, leftKeyList);
        NumPadAdapter rightPadArrayAdapter = new NumPadAdapter(MainActivity.this, R.layout.operator, rightKeyList);
        leftNumPad.setAdapter(leftPadArrayAdapter);
        rightNumPad.setAdapter(rightPadArrayAdapter);

        leftNumPad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Key currentLeftClick;
                currentLeftClick = leftKeyList.get(i);
                if (currentLeftClick.value.toString().equals("=")) {
                    if (expression.equals("")) {
                        expressionTextView.setText("Bad Expression!");
                        expression = "";
                        expressionTextView.setTextColor(Color.RED);
                        expressionTextView.setTextSize(20);
                        expressionTextView.setTypeface(expressionTextView.getTypeface(), Typeface.BOLD_ITALIC);
                    } else {
                        ScriptEngineManager checker = new ScriptEngineManager();
                        ScriptEngine engine = checker.getEngineByName("js");
                        try {
                            double res = (double) engine.eval(expression);
                            if (Double.isInfinite(res) || Double.isNaN(res)) {
                                expressionTextView.setText("Devided By Zero!");
                                expressionTextView.setTextColor(Color.RED);
                                expressionTextView.setTextSize(20);
                                expressionTextView.setTypeface(expressionTextView.getTypeface(), Typeface.BOLD_ITALIC);
                                expression = "";
                            } else {
                                String result = Double.valueOf((double) Math.ceil(res * 10000) / 10000).toString();
                                expressionTextView.setText(result);
                                textView = findViewById(R.id.amount);
                                textView.setText(result);
                                for (MoneyNation mNation : moneyTargetNationList) {
                                    Double output = (res * (mNation.costRate));
                                    output = Math.ceil(output * 10000) / 10000;
                                    mNation.result = output;
                                }
                                targetMoneyAdapter.notifyDataSetChanged();
                                records = new ArrayList<>();
                                String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                                MoneyNation currentBase = new MoneyNation(1, "EUR", R.drawable.eur, "");
                                currentBase.result = (double) (Math.ceil(res * 10000) / 10000);
                                Log.d("UUU", "onItemClick: " + moneyTargetNationList.toString());
                                Record record = new Record(currentBase, moneyTargetNationList, currentDateTimeString);
                                if (record != null) {
                                    records.add(record);
                                }
                                FileCacher<ArrayList<Record>> recordsArrayListFileCacher = new FileCacher<>(MainActivity.this, "history");
                                try {
                                    recordsArrayListFileCacher.appendOrWriteCache((ArrayList<Record>) records);
                                    Log.d("AD", "onItemClick: "+ records.toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                expression = result;
                            }
                        } catch (ScriptException e) {
                            expressionTextView.setText("Bad Expression!");
                            expressionTextView.setTextColor(Color.RED);
                            expressionTextView.setTextSize(20);
                            expressionTextView.setTypeface(expressionTextView.getTypeface(), Typeface.BOLD_ITALIC);
                            expression = "";
                        }
                        expressionTextView.setTextColor(Color.RED);
                        expressionTextView.setTextSize(20);
                        expressionTextView.setTypeface(expressionTextView.getTypeface(), Typeface.BOLD_ITALIC);
                    }
                } else {
                    expression += currentLeftClick.value.toString();
                    expressionTextView.setText(expression);
                    expressionTextView.setTextColor(Color.RED);
                    expressionTextView.setTextSize(20);
                    expressionTextView.setTypeface(expressionTextView.getTypeface(), Typeface.BOLD_ITALIC);
                }
                //  Toast.makeText(view.getContext(), currentLeftClick.value, Toast.LENGTH_SHORT).show();
            }
        });
        rightNumPad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Key currentRightClick;
                currentRightClick = rightKeyList.get(i);
                if (currentRightClick.value.toString().equals("\u232b") && expression.length() >= 1) {
                    expression = expression.substring(0, expression.length() - 1);
                    expressionTextView.setText(expression);
                } else {
                    if (!currentRightClick.value.toString().equals("\u232b")) {
                        expression += currentRightClick.value.toString();
                    }
                }
                expressionTextView.setText(expression);
                expressionTextView.setTextColor(Color.RED);
                expressionTextView.setTextSize(20);
                expressionTextView.setTypeface(expressionTextView.getTypeface(), Typeface.BOLD_ITALIC);
            }
        });


    }

    private List<Key> createLeftKeyList() {
        List<Key> keyList = new ArrayList<>();
        for (String val : leftPadElements) {
            Key key = new Key(val);
            keyList.add(key);
        }
        return keyList;
    }

    private List<Key> createRightKeyList() {
        List<Key> keyList = new ArrayList<>();
        for (String val : rightPadElements) {
            Key key = new Key(val);
            keyList.add(key);
        }
        return keyList;
    }

    @Override
    public void onTaskCompleted(GlobalStorage storage) throws IOException {
        storageCurrent = new GlobalStorage();
        storageCurrent = storage;
        if (storageCurrent != null) {
            initialList = storage.getMoneyList();

        }
    }

}