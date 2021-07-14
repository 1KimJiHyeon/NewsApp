package com.myandroid.kimJH_60181628;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateEconomy extends AppCompatActivity {
    SQLiteDatabase db;
    DbHelperEconomy DbHelper;
    private final int GET_IMG_FROM_GALLERY = 200; //갤러리 사진 요청 request
    private ImageView imageview;
    private Uri selectedImageUri;
    String photoImgName;
    private String currentPhotoPathl;
    Bitmap bitmap;
    EditText mTitleText;
    EditText mDateText;
    EditText mSummaryText;
    EditText mSourceText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_economy);

        DbHelper = new DbHelperEconomy(this);
        db = DbHelper.getWritableDatabase();

        mTitleText = (EditText) findViewById(R.id.title);
        mDateText = (EditText) findViewById(R.id.date);
        mSummaryText = (EditText) findViewById(R.id.summary);
        mSourceText = (EditText) findViewById(R.id.source);

        //image view 위에 갤러리 접근 허가 요청 하기
        imageview = (ImageView) findViewById(R.id.getImage);//이미지뷰 연결
        imageview.setOnClickListener(new View.OnClickListener() {//이미지 뷰 클릭 -> 갤러리에서 url 가져오는 요청
            @Override
            public void onClick(View view) {
                Intent gal = new Intent(Intent.ACTION_PICK);
                gal.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(gal, GET_IMG_FROM_GALLERY);
            }
        });//image view 클릭시 동작 끝

    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, ListEconomy.class);
        startActivity(setIntent);
    }

    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                String title = mTitleText.getText().toString();
                String date = mDateText.getText().toString();
                String summary = mSummaryText.getText().toString();
                String source = mSourceText.getText().toString();
                byte[] imageBitmap = null;
                if (bitmap == null) {
                    imageBitmap = null;
                    Log.v("check", "bitmap is null");
                }
                if (bitmap != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    imageBitmap = byteArrayOutputStream.toByteArray();
                    String test = imageBitmap.toString();
                    Log.v("check", "bit map: " + imageBitmap);
                }
                ContentValues cv = new ContentValues();
                cv.put(DbHelper.IMAGE, imageBitmap);
                cv.put(DbHelper.TITLE, title);
                cv.put(DbHelper.DATE, date);
                cv.put(DbHelper.SUMMARY, summary);
                cv.put(DbHelper.SOURCE, source);


                db.insert(DbHelper.TABLE_NAME, null, cv);

                Intent openMainScreen = new Intent(this, ListEconomy.class);
                openMainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(openMainScreen);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    //사진 이미지 요청 들어오면 url 받아오는 동작
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_IMG_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {

            selectedImageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImageUri);
                Bitmap.createScaledBitmap(bitmap, 150, 150, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            //url db에 저장
            Log.v("check", "gallery uri: " + selectedImageUri.toString());
            imageview.setImageURI(selectedImageUri);
        }
    }

    private File createImageFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory() + "/./");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        photoImgName = timeStamp + ".jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/./" + photoImgName);
        currentPhotoPathl = storageDir.getAbsolutePath();
        Log.v("check", "path creadted: " + currentPhotoPathl);
        return storageDir;
    }

    public void checkPermission() {
        String temp = "";
        //read 권한 요청, permission 허가 안되어있으면 temp string에 권한 추가
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        //write권한 요청, permission 허가 안되어있으면 temp string에 권한 추가
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        if (TextUtils.isEmpty(temp) == false) {//temp에 내용이 있다면, 허가되지 않은 요청이 았다는 의미이므로
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), 1);//" "를 기준으로 권한요청
        } else {
            Toast.makeText(this, "어플리케이션 사용을 위해 갤러리, 위치 접근이 허가되어있습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    //사용자의 권한에 대한 반응 처리 함수 (동의인지, 거부인지)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한 사용 허가되었습니다.", Toast.LENGTH_SHORT).show();
                } else//사용자가 거부하면?
                {
                    Toast.makeText(this, "기능 사용을 위해 권한 동의가 필요합니다.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
