package com.example.my_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends ArrayAdapter<AppUser> {
    public UserAdapter(Context context, List<AppUser> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_item, parent, false);
        }

        AppUser user = getItem(position);
        TextView title = convertView.findViewById(R.id.itemTitle);
        title.setText(user.getUsername());

        return convertView;
    }
}