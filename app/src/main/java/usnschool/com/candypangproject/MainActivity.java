package usnschool.com.candypangproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button loginbtn, startbtn, recordbtn, exitbtn, logoutbtn, loginexitbtn, registerbtn;
    private static DBConnector connector;
    private String id, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ClientSocket();

        loginbtn = (Button)findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new ButtonEventHandler());

        logoutbtn = (Button)findViewById(R.id.logoutbtn);
        logoutbtn.setOnClickListener(new ButtonEventHandler());

        startbtn = (Button)findViewById(R.id.startbtn);
        startbtn.setOnClickListener(new ButtonEventHandler());

        recordbtn = (Button)findViewById(R.id.recordbtn);
        recordbtn.setOnClickListener(new ButtonEventHandler());

        exitbtn = (Button) findViewById(R.id.exitbtn);
        exitbtn.setOnClickListener(new ButtonEventHandler());

        loginexitbtn = (Button)findViewById(R.id.loginexitbtn);
        loginexitbtn.setOnClickListener(new ButtonEventHandler());

        registerbtn = (Button)findViewById(R.id.registerbtn);
        registerbtn.setOnClickListener(new ButtonEventHandler());

        connector = new DBConnector(this);
        String sql = "select * from logininfotbl";
        SQLiteDatabase db = connector.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);


        if(cursor.moveToNext()){
            id = cursor.getString(0);
            password = cursor.getString(1);
            loginbtn.setVisibility(View.INVISIBLE);
            loginexitbtn.setVisibility(View.INVISIBLE);
            registerbtn.setVisibility(View.INVISIBLE);
            logoutbtn.setVisibility(View.VISIBLE);
            startbtn.setVisibility(View.VISIBLE);
            exitbtn.setVisibility(View.VISIBLE);
            recordbtn.setVisibility(View.VISIBLE);
        }else{

        }

    }

    public static DBConnector getConnector(){
        return connector;
    }

    class ButtonEventHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.loginbtn: {
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivityForResult(intent, Constants.LOGIN_REQUEST_NUM);
                    break;
                }
                case R.id.logoutbtn: {
                    SQLiteDatabase db = connector.getWritableDatabase();
                    db.delete("logininfotbl", null, null);
                    loginbtn.setVisibility(View.VISIBLE);
                    loginexitbtn.setVisibility(View.VISIBLE);
                    registerbtn.setVisibility(View.VISIBLE);
                    logoutbtn.setVisibility(View.INVISIBLE);
                    startbtn.setVisibility(View.INVISIBLE);
                    exitbtn.setVisibility(View.INVISIBLE);
                    recordbtn.setVisibility(View.INVISIBLE);
                    break;
                }
                case R.id.startbtn :{
                    //여기에 쓰레드를 이용해서 스타트액티버티를 하여도 oncreate는 메인 스레드로 실행된다.
                    Intent intent = new Intent(MainActivity.this, StartActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("password", password);
                    startActivityForResult(intent, Constants.START_REQUEST_NUM);
                    break;
                }
                case R.id.recordbtn :{
                    Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                    intent.putExtra("id", id);
                    startActivityForResult(intent, Constants.RECORD_REQUEST_NUM);
                    break;
                }
                case R.id.exitbtn :{
                    finish();
                    break;
                }
                case R.id.loginexitbtn :{
                    finish();
                    break;
                }
                case R.id.registerbtn :{
                    Intent intent = new Intent(MainActivity.this, RegisterActi.class);
                    startActivityForResult(intent, Constants.REGISTER_REQUEST_NUM);
                    break;
                }

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode){
            case Constants.LOGIN_SUCCESS : {
                loginbtn.setVisibility(View.INVISIBLE);
                loginexitbtn.setVisibility(View.INVISIBLE);
                registerbtn.setVisibility(View.INVISIBLE);
                logoutbtn.setVisibility(View.VISIBLE);
                startbtn.setVisibility(View.VISIBLE);
                exitbtn.setVisibility(View.VISIBLE);
                recordbtn.setVisibility(View.VISIBLE);
                this.id = data.getStringExtra("id");
                this.password = data.getStringExtra("password");
                SQLiteDatabase db = connector.getWritableDatabase();
                db.delete("logininfotbl", null, null);
                String sql = "insert into logininfotbl (id, password) values('"+id+"', '"+password+"')";
                db.execSQL(sql);
                break;
            }

        }
    }
}
