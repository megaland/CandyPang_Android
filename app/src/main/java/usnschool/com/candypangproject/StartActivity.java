package usnschool.com.candypangproject;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class StartActivity extends AppCompatActivity {
    private int blockrow = 7;
    private int blockcol = 7;
    private Block[][] block = new Block[blockrow][blockcol];
    private int blockpictures[] = {R.drawable.choa, R.drawable.jin, R.drawable.mably, R.drawable.park, R.drawable.shin};
    private int boompictures[] = {R.drawable.boomone, R.drawable.boomtwo, R.drawable.boomthree};
    private int eachblockwidth;
    private int eachblockheight;
    private int baseleft;
    private int basetop;
    private int currentboomnum;
    private String id;
    private String password;
    private Intent intent;
    private ArrayList<Block> blocklist;
    private Bitmap bitmap[] = new Bitmap[5];
    private Bitmap boom[] = new Bitmap[3];
    private RelativeLayout relativelayout;
    private ScreenView sview;
    private TimeThread timethread;
    private int totalscore = 0 ;
    private MediaPlayer playerone;
    private MediaPlayer playertwo;
    private boolean playerflag = true;
//fisrt commit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        playerone = MediaPlayer.create(this, R.raw.boomsound);
        playertwo = MediaPlayer.create(this, R.raw.boomsound);
        intent = getIntent();
        id = intent.getStringExtra("id");
        password = intent.getStringExtra("password");

        sview = new ScreenView(StartActivity.this);
        relativelayout = (RelativeLayout)findViewById(R.id.relativescreen);
        relativelayout.addView(sview);

        timethread = new TimeThread();
        timethread.start();

    }

    class ScreenView extends SurfaceView implements SurfaceHolder.Callback{

        SurfaceHolder holder;

        public ScreenView(Context context) {
            super(context);
            holder = getHolder();
            holder.addCallback(this);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int disappearedcol[] = new int[7];
            Log.e("currentThread",""+Thread.currentThread());
            blocklist = new ArrayList<>();
            for (int i = 0; i < block.length; i++) {
                for (int j = 0; j < block[i].length ; j++) {
                    if(block[i][j].getLeft() < event.getX() && event.getX() < block[i][j].getX2()){
                        if(block[i][j].getTop() < event.getY() && event.getY() < block[i][j].getY2()){

                            block[i][j].setIschecked(true);
                            Log.e("block[][] :", i+ " " +j);
                            Log.e("비트맵넘 : ", ""+block[i][j].getBitmapnum());
                            blocklist.add(block[i][j]);
                            Log.e(""+block[i][j].getBitmapnum(),"www");
                            int bitmapnum = block[i][j].getBitmapnum();
                            for (int k = 0; k < blocklist.size(); k++) {
                                //블록 위
                                if(blocklist.get(k).getBlockrow()-1 >= 0){
                                    Block upblock = block[blocklist.get(k).getBlockrow()-1][blocklist.get(k).getBlockcol()];
                                    if(!upblock.ischecked()){
                                        if(bitmapnum==upblock.getBitmapnum()){
                                            upblock.setIschecked(true);
                                            blocklist.add(upblock);
                                    }

                                    }
                                }


                                //블록 좌
                                if(blocklist.get(k).getBlockcol()-1 >= 0){
                                    Block leftblock =  block[blocklist.get(k).getBlockrow()][blocklist.get(k).getBlockcol()-1];
                                    if(!leftblock.ischecked()){
                                        if(bitmapnum==leftblock.getBitmapnum()) {
                                            leftblock.setIschecked(true);
                                            blocklist.add(leftblock);
                                        }
                                    }
                                }

                                //블록 우
                                if(blocklist.get(k).getBlockcol()+1 < block[i].length){
                                    Block rightblock = block[blocklist.get(k).getBlockrow()][blocklist.get(k).getBlockcol()+1];
                                    if(!rightblock.ischecked()){
                                        if(bitmapnum==rightblock.getBitmapnum()) {
                                            rightblock.setIschecked(true);
                                            blocklist.add(rightblock);
                                        }
                                    }
                                }

                                //블록 아래
                                if(blocklist.get(k).getBlockrow()+1 < block.length){
                                    Block downblock = block[blocklist.get(k).getBlockrow()+1][blocklist.get(k).getBlockcol()];
                                    if(!downblock.ischecked()){
                                        if(bitmapnum==downblock.getBitmapnum()){
                                            downblock.setIschecked(true);
                                            blocklist.add(downblock);
                                        }

                                    }
                                }

                            }
                        }
                    }
                }
            }
            for (int i = 0; i < block.length; i++) {
                for (int j = 0; j < block[i].length ; j++) {
                    block[i][j].setIschecked(false);
                }
            }

            if(blocklist.size() > 2){
                for (int i = 0; i < blocklist.size(); i++) {
                    Log.e(""+blocklist.get(i).getBlockrow(), ""+blocklist.get(i).getBlockcol());
                    blocklist.get(i).setIsexist(false);
                    //blocklist.get(i).setIschecked(false);
                    disappearedcol[blocklist.get(i).getBlockcol()]++;
                    
                }
            }else{
                return false;
            }

            totalscore += blocklist.size()*blocklist.size()*100;

            //원래 자리에서 떨어지던 블록들 해쉬맵에 담기
            //사라진 블록에 해당되는 컬럼에 사라진 블록만큼 + 해주고 리스트에 담아서 아래로 내린다.

            LinkedHashSet<Block> linkhashset = new LinkedHashSet<>();
            ArrayList<Block> tempblocklist = new ArrayList<>();
            for (int j = 0; j < blocklist.size(); j++) {
                for (int k = blocklist.get(j).getBlockrow()-1; k >= 0; k--) {
                    Block tempblock = block[k][blocklist.get(j).getBlockcol()];
                    if(tempblock.isexist()){
                        linkhashset.add(tempblock);
                        tempblocklist.add(tempblock);
                    }
                }
            }

            for (int i = 0; i < tempblocklist.size(); i++) {
                tempblocklist.get(i).setBlockrow(tempblocklist.get(i).getBlockrow()+1);

                Log.i("count", ""+i);
            }

            Iterator iterator = linkhashset.iterator();
            ArrayList<Integer> arraylefttoplist = new ArrayList<>();
            ArrayList<Integer> arrayx2y2list = new ArrayList<>();
            while(iterator.hasNext()){
                Block tempblock = (Block)iterator.next();
                Log.e("row :"+tempblock.getBlockrow(),"col"+ tempblock.getBlockcol());
                int tempblockrow = tempblock.getBlockrow();
                int tempblockcol = tempblock.getBlockcol();
                arraylefttoplist.add(block[tempblockrow][tempblockcol].getLeft());
                arraylefttoplist.add(block[tempblockrow][tempblockcol].getTop());

                arrayx2y2list.add(block[tempblockrow][tempblockcol].getX2());
                arrayx2y2list.add(block[tempblockrow][tempblockcol].getY2());
            }

            //소리 재생
            if(playerflag){
                playerone.start();
            }else {
                playertwo.start();
            }
            playerflag = !playerflag;

            //블록 내려가는 모션
            for (int i = 0; i < 3; i++) {
                iterator = linkhashset.iterator();
                while(iterator.hasNext()){
                    Block tempblock = (Block)iterator.next();
                    tempblock.setTop(tempblock.getTop()+eachblockheight/2);
                }
                currentboomnum = i;
                SystemClock.sleep(100);
                renewCanvas();
            }

            //블록을 젤 아래로 옮김
            iterator = linkhashset.iterator();
            int count = 0;
            while(iterator.hasNext()){
                Block tempblock = (Block)iterator.next();
                block[tempblock.getBlockrow()][tempblock.getBlockcol()] = tempblock;
                tempblock.setLeft(arraylefttoplist.get(count));
                tempblock.setX2(arrayx2y2list.get(count));
                count++;
                tempblock.setTop(arraylefttoplist.get(count));
                tempblock.setY2(arrayx2y2list.get(count));
                count++;
            }
            //새로운 블록넣기
            baseleft = 40;
            basetop = getHeight()- 200 - eachblockheight * blockrow;
            ArrayList<Integer> toplist = new ArrayList<>();
            for (int i = 0; i < disappearedcol.length; i++) {
                if(disappearedcol[i] != 0){
                    for (int j = 0; j < disappearedcol[i]; j++) {
                        int randomnum = (int)(Math.random()*5);
                        block[j][i] = new Block(baseleft+eachblockwidth*i, eachblockheight*j, eachblockwidth, eachblockheight, j, i, bitmap[randomnum], randomnum, true);
                        block[j][i].setY2(basetop+eachblockheight*j+eachblockheight);
                        toplist.add(basetop+eachblockheight*j);
                    }
                }
            }
            renewCanvas();

            for (int k = 0; k < 3; k++) {
                for (int i = 0; i < disappearedcol.length; i++) {
                    if(disappearedcol[i] != 0){
                        for (int j = 0; j < disappearedcol[i]; j++) {
                            block[j][i].setTop(block[j][i].getTop()+eachblockheight/2);
                        }
                    }
                }
                //SystemClock.sleep(10);
                renewCanvas();
            }
            //새로넣은 블럭 제자리로
            int toplistcount = 0;
            for (int i = 0; i < disappearedcol.length; i++) {
                if(disappearedcol[i] != 0){
                    for (int j = 0; j < disappearedcol[i]; j++) {
                        block[j][i].setTop(toplist.get(toplistcount));
                        toplistcount++;
                    }
                }
            }


            //아래에서 3행 랜덤 그림 바꾸기
            int rowrandom = 0;
            while(true){
                rowrandom = (int)(6-Math.random()*3);
                if(rowrandom >= 3 && rowrandom < 6){
                    break;
                }
            }
            int colrandom = (int)(Math.random()*6);
            int randombitnum = block[rowrandom][colrandom].getBitmapnum();
            Bitmap randombitmap = block[rowrandom][colrandom].getBitmap();
            int caserandom = 0;

            //세개의 블록중 두번째
            while(true){
                //좌우위아래순
                caserandom = (int)(Math.random()*4);
                if(caserandom == 0){
                    if(colrandom - 1 >= 0){
                        colrandom--;
                        break;
                    }
                }else if(caserandom == 1){
                    if(colrandom + 1 < 7){
                        colrandom++;
                        break;
                    }
                }else if(caserandom == 2){
                    if(rowrandom - 1 >= 0){
                        rowrandom--;
                        break;
                    }
                }else if(caserandom == 3){
                    if(rowrandom + 1 < 7){
                        rowrandom++;
                        break;
                    }
                }
            }
            Log.e("first rowrandom : "+ rowrandom, "colrandom : " + colrandom);
            block[rowrandom][colrandom].setBitmap(randombitmap);
            block[rowrandom][colrandom].setBitmapnum(randombitnum);

            //세개의 블록중 세번째
            int beforecaserandom = caserandom;
            while(true){
                //좌우위아래순

                caserandom = (int)(Math.random()*4);
                Log.e("세컨드", ""+caserandom);
                if(caserandom == 0 && !(beforecaserandom == 1)){
                    if(colrandom - 1 >= 0){
                        colrandom--;
                        break;
                    }
                }else if(caserandom == 1 && !(beforecaserandom == 0)){
                    if(colrandom + 1 < 7){
                        colrandom++;
                        break;
                    }
                }else if(caserandom == 2 && !(beforecaserandom == 2)){
                    if(rowrandom - 1 >= 0){
                        rowrandom--;
                        break;
                    }
                }else if(caserandom == 3 && !(beforecaserandom == 3)){
                    if(rowrandom + 1 < 7){
                        rowrandom++;
                        break;
                    }
                }
            }
            Log.e("second rowrandom : "+ rowrandom, "colrandom : " + colrandom);
            block[rowrandom][colrandom].setBitmap(randombitmap);
            block[rowrandom][colrandom].setBitmapnum(randombitnum);

            Log.e("Totalscore", "www"+totalscore);
            renewCanvas();
            return false;
        }

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            eachblockwidth = (getWidth()-80)/7;
            eachblockheight = (getWidth()-80)/7;
            Log.e(""+eachblockwidth, ""+eachblockheight);
            baseleft = 40;
            basetop = getHeight()- 200 - eachblockheight * blockrow;
            //비트맵 객체 생성
            for (int i = 0; i < blockpictures.length; i++) {
                bitmap[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                        blockpictures[i]), eachblockwidth, eachblockheight , true);
            }
            for (int i = 0; i < boompictures.length; i++) {
                boom[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                        boompictures[i]), eachblockwidth, eachblockheight, true);
            }


            for (int i = 0; i < block.length; i++) {
                for (int j = 0; j < block[i].length; j++) {
                    int randomnum = (int)(Math.random()*5);
                    block[i][j] = new Block(baseleft, basetop, eachblockwidth, eachblockheight, i, j, bitmap[randomnum], randomnum, true);
                    baseleft += eachblockwidth;
                }
                basetop += eachblockheight;
                baseleft = 40;
            }
            renewCanvas();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }

        public void renewCanvas(){
            Canvas canvas = null;

            try{
                canvas = holder.lockCanvas();
                synchronized (holder) {
                    canvas.drawColor(Color.WHITE);
                    for (int i = 0; i < block.length; i++) {
                        for (int j = 0; j < block[i].length; j++) {
                            if(block[i][j].isexist()){
                                canvas.drawBitmap(block[i][j].getBitmap(), block[i][j].getLeft(), block[i][j].getTop(), null);
                            }else {
                                canvas.drawBitmap(boom[currentboomnum], block[i][j].getLeft(), block[i][j].getTop(), null);
                            }
                        }
                    }
                    Paint paint = new Paint();
                    paint.setTextAlign(Paint.Align.RIGHT);
                    paint.setTextSize(80);
                    canvas.drawText(""+totalscore,750,295,paint);
                }
            }finally{
                if(canvas != null){
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    class TimeThread extends Thread {
        int timesec = 0;

        @Override
        public void run() {
            while(true){
                SystemClock.sleep(1000);
                timesec++;
                if(timesec == 60){
                    Log.e("timesec", ""+ timesec);
                    Looper.prepare();
                    AlertDialog.Builder alert = new AlertDialog.Builder(StartActivity.this);
                    TextView textview = new TextView(StartActivity.this);
                    textview.setText(String.valueOf(totalscore));
                    textview.setTextSize(80);
                    textview.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    Log.e("id : "+id, "totalscore : "+totalscore);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ObjectOutputStream oos = ClientSocket.getObjectOutputStream();
                            Log.e("id : "+id, "totalscore : "+totalscore);
                            try {
                                oos.writeInt(Constants.UPDATE_RECORD);
                                oos.writeUTF(id);
                                oos.writeInt(totalscore);
                                oos.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            finish();
                        }
                    });

                    alert.setView(textview);
                    alert.show();
                    Looper.loop();
                }
            }
        }
    }

}

