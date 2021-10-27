package twelvecabsoft.education.restartrouter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.apache.commons.net.telnet.TelnetInputListener;
import org.apache.commons.net.telnet.TelnetNotificationHandler;

import java.io.BufferedInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements TelnetInputListener, TelnetNotificationHandler {
    TextView twRes;
    private TelnetConnection mConnection;
    private String mServerPort="23";
    private String mServerAddress="192.168.1.101";
    private String Answer="";

    private Dialog mDialogAbout;
    private Dialog mDialogConnect;
    private EditText mEditTextCommand;
    private Button mButtonSend;
    private Button mButtonConnect;
    private ImageView mImageViewIcon;
   // private TextView mTextViewContent;

    private BufferedInputStream mInputBuffer;
    private ScrollView mScrollViewContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        twRes=findViewById(R.id.twRes);
        mImageViewIcon = (ImageView)findViewById(R.id.imageViewIcon);
        mConnection = new TelnetConnection();
        mConnection.getConnection().setReaderThread(true);
        mConnection.getConnection().registerInputListener(this);
        mButtonSend = (Button)findViewById(R.id.buttonSend);
        mButtonSend.setEnabled(false);
        mButtonConnect=(Button)findViewById(R.id.btnConnect);
        mButtonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mConnection.isConnected()) {
                    new ConnectTask().execute();
                }
            }
        });


    }

/*
if(!mConnection.isConnected()) {
        new ConnectTask().execute();
    }
*/

    public void btnDisconnectClick(View view) {
        mConnection.disconnect();
        updateConnectionStatus(false);
    }

private void Dialog(){
        //Log.d("HUI","вижу хуй");
        if(Answer=="HUI")
        {sendCommand(getString(R.string.command_login));
            Log.d("HUI","вижу хуй");}
        else
            if (Answer.contains("Password"))
            {sendCommand(getString(R.string.command_password));
             Log.d("HUI","вижу пасворд");}
        else
            if (Answer.contains("(config)")){
                sendCommand("system");
                Log.d("HUI","вижу конфиг");}
        else
            if (Answer.contains("Rebooting")) {
                //sendCommand("reboot");
                Log.d("HUI","ребутаю нахуй");
                runOnUiThread(new Runnable() {
                    public void run() {
                        mConnection.disconnect();
                        updateConnectionStatus(false);
                    }
                });




            }
        else
            if (Answer.contains("(system)")) {
                sendCommand("reboot");
                Log.d("HUI","вижу  систем");
            }
}


    @Override
    public void telnetInputAvailable() {
        int ostatok=0;
        if(mConnection.isConnected()) {
            try {

                while (mInputBuffer.available() > 0) {
                    final String s = Character.toString((char) mInputBuffer.read());
                    Answer+=s;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            twRes.append(s);

                        }
                    });
                }

                /*mScrollViewContent.post(new Runnable() {
                    public void run() {
                        mScrollViewContent.fullScroll(View.FOCUS_DOWN);
                    }
                });*/
            }
            catch (IOException e) {
                Log.d("TINYY", e.getMessage());
                e.printStackTrace();
            }
          Dialog();
        }

    }

    @Override
    public void receivedNegotiation(int negotiation_code, int option_code) {
        final String strCommand;
        switch(negotiation_code) {
            case RECEIVED_DO:
                strCommand = "RECEIVED_DO\n";
                break;
            case RECEIVED_DONT:
                strCommand = "RECEIVED_DONT\n";
                break;
            case RECEIVED_WILL:
                strCommand = "RECEIVED_WILL\n";
                break;
            case RECEIVED_WONT:
                strCommand = "RECEIVED_WONT\n";
                break;
            case RECEIVED_COMMAND:
                strCommand = "RECEIVED_COMMAND\n";
                break;
            default:
                strCommand = "\n";
                break;
        }

        runOnUiThread(new Runnable() {
            public void run() {
                twRes.append(strCommand);
            }
        });
    }


    private void updateConnectionStatus(boolean connected) {

        if(connected) {
            //mButtonConnect.setText(R.string.stringDisconnect);
            mImageViewIcon.setImageResource(R.drawable.dot_green);
            //mEditTextCommand.setEnabled(true);
            mButtonSend.setEnabled(true);
        }
        else {
            //mButtonConnect.setText(R.string.stringConnect);
            mImageViewIcon.setImageResource(R.drawable.dot_gray);
            //mEditTextCommand.setEnabled(false);
            mButtonSend.setEnabled(false);
        }
    }
    private void sendCommand(String strCommand) {
        Answer="";
        twRes.append(strCommand);
       // final String strCommand = mEditTextCommand.getText().toString();
        if (strCommand.length() < 1) {
            //нет команды
        }
        else {
            new Thread("SEND"){
                public void run(){
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(mConnection.sendCommand(strCommand + "\n")) {
                        //mEditTextCommand.setText("");
                    }
                }
            }.start();


        }
    }

    public void btnSendClick(View view) {
        Answer="HUI";
        Dialog();
    }

    private class ConnectTask extends AsyncTask<Void, Void, Boolean> {

        private BusyDialog mBusyDialog = new BusyDialog(MainActivity.this);

        @Override
        protected void onProgressUpdate(Void... arg0) {
            if (mBusyDialog != null) {
                // Show dialog if it wasn't shown yet or was removed on configuration (rotation) change
                if (!mBusyDialog.isShowing())
                    mBusyDialog.show();
            }
        }

        @Override
        protected void onPreExecute() {
            if (mBusyDialog != null) {
                mBusyDialog.setCancelable(false);
                mBusyDialog.show();
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (mBusyDialog != null) {
                mBusyDialog.dismiss();
                mBusyDialog = null;
            }

            updateConnectionStatus(result);
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            if(!mConnection.isConnected()) {
                try {
                    //mConnection.connect("217.41.71.142", 7373);
                    //mConnection.connect("178.216.9.11", 2049);

                    mConnection.connect(mServerAddress, Integer.parseInt(mServerPort));
                    mInputBuffer = mConnection.getInput();

                    Thread.sleep(1000);

                    return true;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return false;
        }
    }
}