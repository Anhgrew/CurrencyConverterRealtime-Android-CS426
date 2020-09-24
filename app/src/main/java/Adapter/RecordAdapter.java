package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hw_01_18125002.R;

import java.util.List;

import Entity.RecordDto;

public class RecordAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<RecordDto> recordList;

    public RecordAdapter(Context context, int layout, List<RecordDto> recordList) {
        this.context = context;
        this.layout = layout;
        this.recordList = recordList;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int i) {
        return recordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(layout, null);
        TextView dateTime = view.findViewById(R.id.date_time);
        TextView baseCurrency = view.findViewById(R.id.base_currency);
        TextView targetCurrency = view.findViewById(R.id.target_currency);
        TextView baseResult = view.findViewById(R.id.base_result);
        TextView targetResult = view.findViewById(R.id.target_result);
        RecordDto record = recordList.get(i);
        dateTime.setText("Date time: "+record.datetime);
        baseCurrency.setText("Base Currency: "+record.base);
        targetCurrency.setText("Target Currency: "+record.target);
        baseResult.setText(record.baseResult);
        targetResult.setText(record.targetResult);
        return view;
    }
}
