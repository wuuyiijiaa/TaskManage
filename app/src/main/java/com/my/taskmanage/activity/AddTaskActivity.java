package com.my.taskmanage.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.my.taskmanage.R;
import com.my.taskmanage.db.DbHelper;
import com.my.taskmanage.entity.TaskEntity;
import com.my.taskmanage.utils.TakePhotoUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class AddTaskActivity extends Activity {
    private EditText etName;
    private EditText etDes;
    private CheckBox cb1;
    private CheckBox cb2;
    private Spinner spinner;
    private Button btnPic;
    private ImageView iv;
    private Button btnAdd;
    
    private Uri uri;
    private Bitmap bitmap;
    private static final int TAKE_PHOTO_REQUEST = 111;
    private static final int IMAGE_PICK_REQUEST=222;
    private String photoPath;
    
    private boolean isUrgent=false;
    private boolean isImportant=false;
    
    private String category="Work";
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        
        initView();
        initEvent();
    }
    
    
    private void initView() {
        etName = (EditText) findViewById(R.id.et_name);
        etDes = (EditText) findViewById(R.id.et_des);
        cb1 = (CheckBox) findViewById(R.id.cb1);
        cb2 = (CheckBox) findViewById(R.id.cb2);
        spinner = (Spinner) findViewById(R.id.spinner);
        btnPic = (Button) findViewById(R.id.btn_pic);
        iv = (ImageView) findViewById(R.id.iv);
        btnAdd = (Button) findViewById(R.id.btn_add);
    }
    
    private void initEvent() {
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    isUrgent=true;
                }else {
                    isUrgent=false;
                }
            }
        });
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    isImportant=true;
                }else {
                    isImportant=false;
                }
            }
        });
        
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                category= (String) adapterView.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        
        btnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(AddTaskActivity.this);
                dialog.setItems(new String[]{"Take a Picture", "Photo Album"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                try {
                                    uri = TakePhotoUtils.takePhoto(AddTaskActivity.this, TAKE_PHOTO_REQUEST);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent, IMAGE_PICK_REQUEST);
                                break;
                        }
                    }
                }).setCancelable(true).create().show();
            }
        });
        
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=etName.getText().toString().trim();
                String des=etDes.getText().toString().trim();
                if (!TextUtils.isEmpty(name)){
                    if (!TextUtils.isEmpty(des)){
                        if (bitmap!=null){
                            saveBitmapToSD();
                            DbManager dbManager= DbHelper.getInstance().getDbManager();
                            TaskEntity entity=new TaskEntity();
                            entity.setCategory(category);
                            entity.setDes(des);
                            entity.setImportant(isImportant);
                            entity.setUrgent(isUrgent);
                            entity.setName(name);
                            entity.setPic(photoPath);
                            try {
                                dbManager.save(entity);
                                Toast.makeText(AddTaskActivity.this,"Add successfully",Toast.LENGTH_SHORT).show();
                                finish();
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(AddTaskActivity.this,"Please Choose Photo",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(AddTaskActivity.this,"Task description must not be null",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(AddTaskActivity.this,"Task name must not be null",Toast.LENGTH_SHORT).show();
                }
                
                
            }
        });
    }
    
    private void saveBitmapToSD() {
        File saveFile= TakePhotoUtils.getOwnCacheDirectory(this,"TaskManage"+ File.separator+"Photo");
        
        if (!saveFile.exists()){
            try {
                saveFile.createNewFile();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        FileOutputStream fileOutputStream= null;
        
        photoPath=saveFile+ File.separator+System.currentTimeMillis()+".jpg";
        try {
            fileOutputStream = new FileOutputStream(photoPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fileOutputStream!=null){
            /**
             * 第一个参数format为压缩的格式
             * 第二个参数quality为图像压缩比的值，0-100意味着小尺寸的压缩，100意味着高质量的压缩
             * 第三个stream为输出流
             */
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO_REQUEST:
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                bitmap = BitmapFactory.decodeFile(uri.getPath(), getOptions(uri.getPath()));
                iv.setBackgroundResource(R.color.transparent);
                iv.setImageBitmap(bitmap);
                break;
            case IMAGE_PICK_REQUEST:
                uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    iv.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    /**
     * 获取压缩图片的options
     */
    public static BitmapFactory.Options getOptions(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 4;      //此项参数可以根据需求进行计算
        options.inJustDecodeBounds = false;
        return options;
    }
}
