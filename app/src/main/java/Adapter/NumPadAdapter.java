package Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hw_01_18125002.R;

import java.util.List;

import Entity.Key;


public class NumPadAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<Key> keyList;

    public NumPadAdapter(Context context, int layout, List<Key> keyList) {
        this.context = context;
        this.layout = layout;
        this.keyList = keyList;
    }

    @Override
    public int getCount() {
        return keyList.size();
    }

    @Override
    public Object getItem(int i) {
        return keyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view= layoutInflater.inflate(layout,null);
        TextView textView = view.findViewById(R.id.button);
        Key key = keyList.get(i);
        textView.setText(key.value);
        return view;
    }
}
