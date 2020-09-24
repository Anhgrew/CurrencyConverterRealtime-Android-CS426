package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hw_01_18125002.R;

import java.util.List;

import Entity.MoneyNation;

public class TargetMoneyAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<MoneyNation> moneyNationList;

    public TargetMoneyAdapter(Context context, int layout, List<MoneyNation> moneyNationList) {
        this.context = context;
        this.layout = layout;
        this.moneyNationList = moneyNationList;
    }

    @Override
    public int getCount() {
        return moneyNationList.size();
    }

    @Override
    public Object getItem(int i) {
        return moneyNationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(layout, null);
        TextView amountTextView = view.findViewById(R.id.target_amount);
        TextView unitTextView = view.findViewById(R.id.target_unit);
        TextView fullNameUnitTextView = view.findViewById(R.id.full_name_target_unit);
        ImageView imageView = view.findViewById(R.id.target_image);
        MoneyNation moneyNation = moneyNationList.get(i);
        unitTextView.setText(moneyNation.name);
        fullNameUnitTextView.setText(moneyNation.information);
        amountTextView.setText(Double.toString(moneyNation.result));
        imageView.setImageResource(moneyNation.ensignImage);
        return view;
    }

}
