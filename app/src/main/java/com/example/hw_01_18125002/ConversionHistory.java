package com.example.hw_01_18125002;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.kosalgeek.android.caching.FileCacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapter.RecordAdapter;
import Connect.OnTaskCompleted;
import Entity.GlobalStorage;
import Entity.MoneyNation;
import Entity.Record;
import Entity.RecordDto;

public class ConversionHistory extends AppCompatActivity {
    ArrayList<ArrayList<Record>> recordsList;
    public List<RecordDto> recordDtoList;
    public GridView gridView;
    public RecordAdapter recordAdapter;
    public Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion_history);
        gridView = findViewById(R.id.history_list);
        backBtn = findViewById(R.id.back_btn);
        initialBackEvent();
        try {
            readHistory();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initialReCordDtoList();
        recordAdapter = new RecordAdapter(this, R.layout.history, recordDtoList);
        gridView.setAdapter(recordAdapter);
    }

    private void initialBackEvent() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConversionHistory.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initialReCordDtoList() {
        recordDtoList = new ArrayList<>();
        RecordDto recordDto = null;
        if (recordsList != null) {
            for (ArrayList<Record> recordArrayList : recordsList) {
                for (Record record : recordArrayList) {
                    List<MoneyNation> targetCurrencyList = record.target;
                    if (targetCurrencyList != null) {
                        for (MoneyNation moneyNation : targetCurrencyList) {
                            recordDto = new RecordDto();
                            recordDto.base = record.base.name;
                            recordDto.baseResult = Double.toString(record.base.result);
                            recordDto.datetime = record.datetime;
                            recordDto.target = moneyNation.name;
                            recordDto.targetResult = Double.toString(moneyNation.result);
                            recordDtoList.add(recordDto);
                        }
                    }
                }
            }
        }

    }


    private void readHistory() throws IOException {
        FileCacher<ArrayList<Record>> recordArrayListFileCacher = new FileCacher<>(this, "history");
        if (recordArrayListFileCacher.hasCache()) {
            try {
                recordsList = (ArrayList<ArrayList<Record>>) recordArrayListFileCacher.getAllCaches();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}