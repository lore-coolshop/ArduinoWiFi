package com.lorenzodaneo.arduino.arduinowifi.services;

import android.os.AsyncTask;
import android.util.Log;



/**
 * @author Lorenzo Daneo (mail to lorenzo.daneo@coolshop.it)
 * Coolshop Srl
 */
public class TcpClientConnection extends AsyncTask<String, String, TcpClient> implements TcpClient.OnMessageReceived {

    TcpClient tcpClient;
    OnTcpResponse sendResponse;

    private static final String exception = "->exception";

    public TcpClientConnection(OnTcpResponse sendResponse){
        this.tcpClient = new TcpClient(this);
        this.sendResponse = sendResponse;
    }

    public void stopTcpClient(){
        tcpClient.stopClient();
        cancel(true);
    }

    public boolean isRunning(){
        return tcpClient.isRunning();
    }

    @Override
    protected TcpClient doInBackground(String... message) {
        try
        {
            tcpClient.run();
        } catch (Exception e){
            publishProgress(exception, e.getMessage());
        }
        return tcpClient;
    }

    public void sendMessage(String message){
        tcpClient.sendMessage(message);
    }

    @Override
    public void messageReceived(String message) {
        publishProgress(message);
    }


    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        //response received from server
        Log.d("test", "response " + values[0]);
        //process server response here....
        if(!values[0].equals(exception)){
            sendResponse.reply(values[0]);
        } else {
            sendResponse.catchException(values[1]);
        }
    }


    public interface OnTcpResponse{
        void reply(String response);
        void catchException(String exceptionMessage);
    }
}
