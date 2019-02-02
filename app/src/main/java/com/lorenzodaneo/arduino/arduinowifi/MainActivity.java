package com.lorenzodaneo.arduino.arduinowifi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.lorenzodaneo.arduino.arduinowifi.services.TcpClientConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TcpClientConnection.OnTcpResponse {

    Button buttonConnect;
    Button buttonSwitch;

    TcpClientConnection tcpClientConnection;

    boolean ledOff = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonConnect = findViewById(R.id.connect);
        buttonConnect.setOnClickListener(this);
        buttonSwitch = findViewById(R.id.on_off);
        buttonSwitch.setOnClickListener(this);
        tcpClientConnection = new TcpClientConnection(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.connect:
                if(tcpClientConnection.isRunning()){
                    Toast.makeText(this, "Device was already connected.", Toast.LENGTH_SHORT).show();
                    tcpClientConnection.stopTcpClient();
                    tcpClientConnection = new TcpClientConnection(this);
                }
                tcpClientConnection.execute();
                Toast.makeText(this, "Device connected.", Toast.LENGTH_LONG).show();
                break;
            case R.id.on_off:
            default:
                if(view.getId() == R.id.on_off){
                    if(ledOff) {
                        tcpClientConnection.sendMessage("ON");
                    } else {
                        tcpClientConnection.sendMessage("OFF");
                    }
                }
                break;
        }
    }

    @Override
    public void reply(String response) {
        if(response.endsWith("OK")){
            ledOff = !ledOff;
            if(ledOff){
                buttonSwitch.setText("ON");
            } else {
                buttonSwitch.setText("OFF");
            }
        }
    }

    @Override
    public void catchException(String exceptionMessage) {
        Toast.makeText(this, "Connection exception.", Toast.LENGTH_SHORT).show();
    }
}
