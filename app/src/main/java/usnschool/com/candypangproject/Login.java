package usnschool.com.candypangproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Login extends AppCompatActivity {
    private Button loginbtn;
    private EditText loginid, loginpassword;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private int result = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ois = ClientSocket.getObjectInputStream();
        oos = ClientSocket.getObjectOutputStream();

        loginid = (EditText)findViewById(R.id.loginid);
        loginpassword = (EditText)findViewById(R.id.loginpassword);

        loginbtn = (Button)findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    oos.writeInt(Constants.ID_PASSWORD_CHECK);
                    oos.flush();
                    oos.writeUTF(loginid.getText().toString());
                    oos.flush();
                    oos.writeUTF(loginpassword.getText().toString());
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
                    while(result == 0){
                        SystemClock.sleep(100);
                    }
                    if(result==Constants.RIGHT_PASSWORD){
                        Intent intent = getIntent();
                        intent.putExtra("id", loginid.getText().toString());
                        intent.putExtra("password", loginpassword.getText().toString());
                        setResult(Constants.LOGIN_SUCCESS, intent);
                        finish();
                    }else {
                        Toast.makeText(Login.this, "아이디/비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });






    }
}
