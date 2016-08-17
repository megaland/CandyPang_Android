package usnschool.com.candypangproject;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {

    private TextView textrank, textid, textscore;
    private TextView myranktextview, myscoretextview;
    private ListView listview;
    private DBConnector connector;
    private int myscore;
    private int myrank;
    private String id;
    private ArrayList<RankerData> rankerlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        ObjectOutputStream oos = ClientSocket.getObjectOutputStream();
        ObjectInputStream ois = ClientSocket.getObjectInputStream();
        try {
            oos.writeInt(Constants.GET_RANKER_AND_ME);
            oos.writeUTF(id);
            oos.flush();

            //오류는 뜨지만 제대로 작동한다.
            //이 방법외에 쓰레드를 돌리는 방법이 있다.
            StrictMode.enableDefaults();

            myrank = ois.readInt();
            myscore = ois.readInt();
            rankerlist = (ArrayList<RankerData>)ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        myranktextview = (TextView)findViewById(R.id.myranktextview);
        myranktextview.setText(String.valueOf(myrank+1));

        myscoretextview = (TextView)findViewById(R.id.myscoretextview);
        myscoretextview.setText(String.valueOf(myscore));

        listview = (ListView)findViewById(R.id.listView);
        RankListAdapter adapter = new RankListAdapter(rankerlist, R.layout.eachranklayout, this);
        listview.setAdapter(adapter);

    }

    class RankListAdapter extends BaseAdapter{
        ArrayList<RankerData> rankeradapterlist;
        int layout;
        Context ctx;
        LayoutInflater inflater;

        public RankListAdapter(ArrayList<RankerData> rankeradapterlist, int layout, Context ctx) {
            this.rankeradapterlist = rankeradapterlist;
            this.layout = layout;
            this.ctx = ctx;
            inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return rankeradapterlist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView ==null){
                convertView = inflater.inflate(layout, parent, false);
            }
            textrank = (TextView)convertView.findViewById(R.id.ranktextview);
            textrank.setText(String.valueOf(rankeradapterlist.get(position).getRank()));
            textid = (TextView)convertView.findViewById(R.id.rankidtextview);
            textid.setText(rankeradapterlist.get(position).getId());
            textscore = (TextView)convertView.findViewById(R.id.rankscoretextview);
            textscore.setText(String.valueOf(rankeradapterlist.get(position).getScore()));

            return convertView;
        }
    }
}
