package usnschool.com.candypangproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RegisterActi extends AppCompatActivity {
    private Button registersavebtn;
    private EditText registerid, registerpasswordone, registerpasswordtwo;
    private String id, passwordone, passwordtwo;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private final int REGIST_ACTION = 3;
    private final int REGIST_SUCCESS = 31;
    private int result = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerid = (EditText)findViewById(R.id.registerid);
        registerpasswordone = (EditText)findViewById(R.id.registerpasswordone);
        registerpasswordtwo = (EditText)findViewById(R.id.registerpasswordtwo);

        ois = ClientSocket.getObjectInputStream();
        oos = ClientSocket.getObjectOutputStream();


        registersavebtn = (Button)findViewById(R.id.registersavebtn);
        registersavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = registerid.getText().toString();
                passwordone = registerpasswordone.getText().toString();
                passwordtwo = registerpasswordtwo.getText().toString();

                if(passwordone.equals(passwordtwo)){
                    try {
                        oos.writeInt(REGIST_ACTION);
                        oos.flush();
                        oos.writeUTF(id);
                        oos.flush();
                        oos.writeUTF(passwordone);
                        oos.flush();
                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    result = ois.readInt();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        while(result==0){
                            SystemClock.sleep(100);
                        }
                        Log.i("통과","헤헤");
                        if(result==REGIST_SUCCESS){
                            Intent intent = getIntent();
                            setResult(REGIST_SUCCESS, intent);
                            finish();
                        }else if(result ==Constants.REGIST_ID_EXIST){
                            Toast.makeText(RegisterActi.this, "이미존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(RegisterActi.this, "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
