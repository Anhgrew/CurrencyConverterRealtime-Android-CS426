package com.example.hw_01_18125002;


import android.content.Intent;

import android.os.Bundle;

import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import java.util.List;

import Adapter.MoneyNationAdapter;


import Entity.MoneyNation;

public class AddListMoneyNations extends AppCompatActivity {
    public GridView topScreen;
    public List<MoneyNation> transferList;
    public List<MoneyNation> initialList;
    public List<MoneyNation> moneyNationList;
    public List<MoneyNation> restoreItem;
    public MoneyNation moneyNation;

    int flat;
    public MoneyNationAdapter moneyNationAdapter;


    void init() {
        transferList = new ArrayList<>();
        restoreItem = new ArrayList<>();
        moneyNationList = new ArrayList<>();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list_money_nations);
        init();
        Intent intent = getIntent();
        flat = intent.getIntExtra("checkingFlat", 0);
        initialList = intent.getParcelableArrayListExtra("moneyNationInitialList");
        moneyNationList = initialList;
        if (flat != 0) {
            restoreItem = intent.getParcelableArrayListExtra("restoreItem");
            transferList = intent.getParcelableArrayListExtra("moneyNationTransferList");
            moneyNationList = intent.getParcelableArrayListExtra("moneyNationRemainedBackList");
            if (restoreItem != null && !restoreItem.isEmpty()) {
                for (MoneyNation tmpMoneyNation : restoreItem) {
                    moneyNationList.add(tmpMoneyNation);
                }
            }
        }
        createMidScreen();
        createBackToMainScreenEvent();
    }


    private void createBackToMainScreenEvent() {
        Button button = findViewById(R.id.back_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("backFlat", flat);
                intent.putParcelableArrayListExtra("moneyNationTransferList", (ArrayList<? extends Parcelable>) transferList);
                intent.putParcelableArrayListExtra("moneyNationRemainedList", (ArrayList<? extends Parcelable>) moneyNationList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void createMidScreen() {
        if (moneyNationList != null && !moneyNationList.isEmpty()) {
            topScreen = findViewById(R.id.nationList);
            moneyNationAdapter = new MoneyNationAdapter(AddListMoneyNations.this, R.layout.money_nation, moneyNationList);
            topScreen.setAdapter(moneyNationAdapter);
            topScreen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    moneyNation = moneyNationList.get(i);
                    if (moneyNation != null) {
                        Toast.makeText(view.getContext(),
                                "Item added : " + moneyNation.name, Toast.LENGTH_SHORT).show();
                        transferList.add(moneyNation);
                        moneyNationList.remove(i);
                        moneyNationAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }


}