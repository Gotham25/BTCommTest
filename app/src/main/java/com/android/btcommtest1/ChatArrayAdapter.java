package com.android.btcommtest1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gowtham on 04-10-2016.
 */

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private final Context context;
    private final int resource;
    private final List<ChatMessage> chatMessageList;
    private TextView msgContent;

    public ChatArrayAdapter(Context context, int resource, List<ChatMessage> chatMessageList) {
        super(context, resource, chatMessageList);

        this.context = context;
        this.resource = resource;
        this.chatMessageList = chatMessageList;
        msgContent = null;
    }


    @Override
    public int getViewTypeCount() {

        return 2;//super.getViewTypeCount();

    }


    @Override
    public int getItemViewType(int position) {
        return position % 2 ;//super.getItemViewType(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) ( (Activity) context ).getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ChatMessage chatMessage = getItem(position);
        View chatRowView =  inflater.inflate( chatMessage.isSender() ? R.layout.chat_row_out_msg : R.layout.chat_row_in_msg , parent, false);
        if( chatMessage.isSender() )
            msgContent = (TextView) chatRowView.findViewById(R.id.out_msg_txtView);
        else
            msgContent = (TextView) chatRowView.findViewById(R.id.in_msg_txtView);

        msgContent.setText( chatMessage.getMsgContent() );

        return chatRowView;

    }
}
