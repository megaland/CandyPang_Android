package usnschool.com.candypangproject;

import android.graphics.Bitmap;

/**
 * Created by it on 2016-08-04.
 */
public class Block {
    private int left;
    private int top;
    private int width;
    private int height;
    private int blockrow;
    private int blockcol;
    Bitmap bitmap;
    private boolean isexist;
    private int x2, y2;
    private int bitmapnum;
    private boolean ischecked = false;
    Block(int left, int top, int width, int height, int blockrow, int blockcol ,Bitmap bitmap,int bitmapnum , boolean isexist){
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
        this.blockrow = blockrow;
        this.blockcol = blockcol;
        this.bitmap = bitmap;
        this.bitmapnum = bitmapnum;
        this.isexist = isexist;
        this.x2 = left+width;
        this.y2 = top+height;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBlockrow() {
        return blockrow;
    }

    public void setBlockrow(int blockrow) {
        this.blockrow = blockrow;
    }

    public int getBlockcol() {
        return blockcol;
    }

    public void setBlockcol(int blockcol) {
        this.blockcol = blockcol;
    }

    public boolean isexist() {
        return isexist;
    }

    public void setIsexist(boolean isexist) {
        this.isexist = isexist;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getBitmapnum() {
        return bitmapnum;
    }

    public void setBitmapnum(int bitmapnum) {
        this.bitmapnum = bitmapnum;
    }

    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }
}
