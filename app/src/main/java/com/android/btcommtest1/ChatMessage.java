package com.android.btcommtest1;

/**
 * Created by Gowtham on 04-10-2016.
 */

public class ChatMessage {

    private String msgContent;
    private boolean isSender;
    private boolean isInitMsg;

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public boolean isSender() {
        return isSender;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }

    public boolean isInitMsg() {
        return isInitMsg;
    }

    public void setInitMsg(boolean initMsg) {
        isInitMsg = initMsg;
    }

    public ChatMessage(String msgContent, boolean isSender, boolean isInitMsg) {
        this.msgContent = msgContent;
        this.isSender = isSender;
        this.isInitMsg = isInitMsg;
    }



}
