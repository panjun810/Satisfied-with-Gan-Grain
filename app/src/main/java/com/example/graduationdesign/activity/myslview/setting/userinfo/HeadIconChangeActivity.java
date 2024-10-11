package com.example.graduationdesign.activity.myslview.setting.userinfo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.graduationdesign.PopupWindow.PhotoPopupWindow;
import com.example.graduationdesign.R;
import com.example.graduationdesign.customControl.TitleLayout;
import com.example.graduationdesign.activity.BaseActivity;
import com.example.graduationdesign.bean.UserBean;
import com.example.graduationdesign.utils.AnalysisUtils;
import com.example.graduationdesign.utils.DBUtils;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

public class HeadIconChangeActivity extends BaseActivity {
    private TextView tv_save;

    private static final int REQUEST_IMAGE_GET = 0;             //用于图像获取的请求码
    private static final int REQUEST_IMAGE_CAPTURE = 1;         //用于图像拍摄的请求码
    private static final int REQUEST_BIG_IMAGE_CUTTING = 2;     //用于大图像剪裁的请求码
    private static final String IMAGE_FILE_NAME = "icon.jpg";   //保存图像文件的文件名

    private String title;
    private ImageView main_icon;
    private PhotoPopupWindow mPhotoPopupWindow;
    private Uri mImageUri;
    private String spUserName;

    private LinearLayout main_ll;

    private static String[] PERMISSIONS_STORAGE ={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headicon_change);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //从SharedPreferences中获取登录时的用户名
        spUserName = AnalysisUtils.readLoginUserName(this);
        init();
    }

    /**
     * 获取界面控件
     */
    private void init() {
        // 从个人资料界面传递过来的标题和内容
        title = getIntent().getStringExtra("title");

        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.headIcon_change_title);
        titleLayout.setText(title);

        tv_save=(TextView) findViewById(R.id.tv_save);
        tv_save.setVisibility(View.VISIBLE);

        main_ll = (LinearLayout)findViewById(R.id.main_ll);

        //保存键的点击事件
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                if (mImageUri != null) {
                    // imageUri 不为空
                    //getEncodedPath() 方法返回的是一个 String 类型的文件路径
                    String IconPath = mImageUri.getEncodedPath();
                    data.putExtra("IconPath", IconPath);
                    setResult(RESULT_OK, data);
                    Toast.makeText(HeadIconChangeActivity.this, "保存成功",
                            Toast.LENGTH_SHORT).show();
                    HeadIconChangeActivity.this.finish();
                } else {
                    // imageUri 为空
                    Toast.makeText(HeadIconChangeActivity.this, "你还未修改头像",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        main_icon = findViewById(R.id.main_icon);
        UserBean bean = null;
        bean = DBUtils.getInstance(this).getUserInfo(spUserName);

        // 首先判断一下数据库是否有数据，若无数据则为用户设置默认数据
        if(bean.iconPath == null) {
            main_icon.setImageResource(R.drawable.calender);
        }else{
            Bitmap bitmap = BitmapFactory.decodeFile(bean.iconPath);
            main_icon.setImageBitmap(bitmap);
        }

        Button main_btn = findViewById(R.id.main_btn);
        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建 PhotoPopupWindow 对象，传入了两个参数作为弹窗中两个按钮的点击事件监听器。
                //参数1MainActivity.this表示当前活动的上下文，作为弹窗的依附活动；参数2和3分别是相册选择按钮和拍照按钮的点击事件监听器，当相应按钮被点击时，会触发相应的回调方法
                mPhotoPopupWindow = new PhotoPopupWindow(HeadIconChangeActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {    // 相册选择按钮点击事件以及进入相册选择的逻辑处理
                        if(Build.VERSION.SDK_INT >= 23){
                            // 检查权限是否已被授予 TODO 在 Android 6.0 及以上的版本中，动态权限申请成为了必要的步骤！！！！！！
                            if (ContextCompat.checkSelfPermission(HeadIconChangeActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                //权限还没有授予，需要在这里写申请权限的代码（这里申请的是请求写入外部存储的权限）
                                ActivityCompat.requestPermissions(HeadIconChangeActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                            } else {
                                toPhotoSelect();
                            }
                        }else {
                            toPhotoSelect();
                        }
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {  // 拍照按钮点击事件以及拍照的逻辑处理
/*                        // 权限申请
                        if (ContextCompat.checkSelfPermission(HeadIconChangeActivity.this,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                && ContextCompat.checkSelfPermission(HeadIconChangeActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            // 权限还没有授予，需要在这里写申请权限的代码
                            ActivityCompat.requestPermissions(HeadIconChangeActivity.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
                        } else {
                            // 权限已经申请，直接拍照
                            mPhotoPopupWindow.dismiss();
                            imageCapture();
                        }*/

                        if(Build.VERSION.SDK_INT >= 23){
                            //记录是否需要申请权限
                            boolean needApply = false;
                            for (String permission : PERMISSIONS_STORAGE){
                                /*TODO ContextCompat.checkSelfPermission()方法来检查权限是否已被授权。该方法返回下述2个常量之一：
                                   返回PackageManager.PERMISSION_GRANTED表示应用程序已经被授予了该权限。
                                   返回PackageManager.PERMISSION_DENIED表示应用程序没有被授予该权限。*/
                                int checkPermission = ContextCompat.checkSelfPermission(HeadIconChangeActivity.this,permission);
                                if(checkPermission != PackageManager.PERMISSION_GRANTED){
                                    needApply = true;
                                    break; // 如果有一个权限未被授予，则跳出循环
                                }
                            }
                            if (needApply){
                                /*TODO 如果需要申请权限，则使用ActivityCompat.requestPermissions()方法，申请当前应用程序需要的权限。
                                   这个方法的第一个参数是当前活动的实例，第二个参数是包含权限字符串数组的常量。第三个参数是请求代码，以便在回调方法中处理它。*/
                                ActivityCompat.requestPermissions(HeadIconChangeActivity.this,PERMISSIONS_STORAGE,300);
                            }else {
                                mPhotoPopupWindow.dismiss();
                                imageCapture();
                            }
                        }else {
                            // Android 版本低于 23，直接执行操作
                            mPhotoPopupWindow.dismiss();
                            imageCapture();
                        }
                    }
                });

                // 获取当前活动的根视图
                View rootView = LayoutInflater.from(HeadIconChangeActivity.this)
                        .inflate(R.layout.activity_main, null);
                /*TODO showAtLocation() 是 Android 中 PopupWindow 类的一个方法，用于在指定位置显示弹窗。它有以下几个参数：
                   view: 表示弹窗要依附的视图，即在哪个视图上显示弹窗。
                   gravity: 表示弹窗在依附视图中的位置，使用 Gravity 类的常量来指定，如 Gravity.TOP、Gravity.BOTTOM、Gravity.CENTER 等，
                            可以通过使用 | 运算符组合多个位置。
                   x: 表示弹窗在 x 方向上的偏移量，单位为像素。
                   y: 表示弹窗在 y 方向上的偏移量，单位为像素。*/
                // 在根视图的底部居中位置显示弹窗
                mPhotoPopupWindow.showAtLocation(rootView,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });


    }

    private void toPhotoSelect(){
        // 如果权限已经申请过，直接进行图片选择
        mPhotoPopupWindow.dismiss();  //隐藏 PhotoPopupWindow 弹窗
        //TODO Intent.ACTION_PICK 操作是从系统中选择一个图片
        Intent intent = new Intent(Intent.ACTION_PICK);

        //设置选择的文件类型为图片。这样在选择图片时，只会显示图片类型的文件。
        intent.setType("image/*");

        /*TODO getPackageManager() 是一个用于获取设备上已安装的应用程序包的管理器。
                               resolveActivity() 方法会根据 Intent 中的动作和数据源，
                               检查设备上是否存在能够处理该 Intent 的 Activity*/
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        } else {
            Toast.makeText(HeadIconChangeActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
        }
    }





    /**
     * 判断系统及拍照
     */
    private void imageCapture() {
        Intent intent;
        Uri pictureUri;
        /*TODO Environment.getExternalStorageDirectory() 是 Android 系统内置的获取外部存储目录的方法，这个路径通常指的是外部存储设备（如 SD 卡）的根目录。
               IMAGE_FILE_NAME 是一个常量，表示图片文件的文件名。在这里，它被用作图片文件的名称。*/
        //创建了一个 File 对象，用于表示图片文件的路径;pictureFile 对象可以用于保存拍照后的图片文件
        File pictureFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);

        // 判断当前设备的 Android 版本是否大于等于 Android N（7.0） ; Android7.0 对 Uri 添加了保护，需要额外处理！
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //为启动相机应用的 Intent 添加读取 URI 权限，以便相机应用能够读取到保存照片的文件路径。
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            /*TODO 使用 FileProvider 获取一个内容 URI，以便在 Android N 及以上版本中共享文件。这里传入了三个参数：
                   this 表示当前的 Context 对象，
                   "com.example.graduationdesign.fileProvider" 表示之前在 AndroidManifest.xml 中定义的 FileProvider 的授权标识，
                   pictureFile 表示保存图片的文件对象*/
            pictureUri = FileProvider.getUriForFile(this,
                    "com.example.graduationdesign.fileProvider", pictureFile);
        } else {
            //TODO MediaStore.ACTION_IMAGE_CAPTURE 是 Android 系统内置的拍照动作常量，用于启动相机应用进行拍照操作
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //将文件路径转换为 Uri 对象
            pictureUri = Uri.fromFile(pictureFile);
        }
        // 去拍照, TODO MediaStore.EXTRA_OUTPUT 是 Android 系统内置的保存图片路径的常量，其值为 "output",将URI指向相应的file:///...
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

    }

    /**
     * 处理回调结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 回调成功
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 大图切割
                case REQUEST_BIG_IMAGE_CUTTING:
                    // 通过Uri 获取文件路径，并将文件路径解码为 Bitmap 对象，getEncodedPath() 方法返回的是一个 String 类型的文件路径
                    Bitmap bitmap = BitmapFactory.decodeFile(mImageUri.getEncodedPath());
                    main_icon.setImageBitmap(bitmap);
                    break;

                // 相册选取
                case REQUEST_IMAGE_GET:
                    try {
                        //TODO data.getData() 返回的是前一个 Activity 返回的 Intent 中包含的数据的位置,这里是获取选择到的图片的 Uri
                        //android 7.0后，file:// 这样的 Uri 不能附着在 Intent 上，否则会引发 FileUriExposedException
                        //但是选择图片不会崩溃，因为选择图片后传入的 Uri 本身就是 Content Uri
                        startBigPhotoZoom(data.getData());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    break;
                // 拍照
                case REQUEST_IMAGE_CAPTURE:
                    File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                    //判断Android 的版本是否在7.0以上
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //使用 FileProvider 生成的 Uri 是一个 content Uri，而不是一个绝对路径的 Uri
                        Uri imageUri = FileProvider.getUriForFile(this,
                                "com.example.graduationdesign.fileProvider", temp);
                        startBigPhotoZoom(imageUri);
                    }else{
                        //在 Android 7.0 及以上的版本中，直接使用 Uri.fromFile(temp) 方法获取的文件 Uri
                        // 在某些情况下可能会导致 FileUriExposedException 异常
                        // 转换出来的 Uri 对象表示的是文件在文件系统中的绝对路径的 Uri
                        startBigPhotoZoom(Uri.fromFile(temp));
                    }
            }
        }
    }

    /**
     * 大图模式切割图片
     * 直接创建一个文件将切割后的图片写入
     */
    public void startBigPhotoZoom(Uri uri) {
        // 创建大图文件夹
        Uri imageUri = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String storage = Environment.getExternalStorageDirectory().getPath();
            File dirFile = new File(storage + "/bigIcon");
            if (!dirFile.exists()) {
                if (!dirFile.mkdirs()) {
                    Log.e("创建", "文件夹创建失败");
                } else {
                    Log.e("创建", "文件夹创建成功");
                }
            }

            File file = new File(dirFile, System.currentTimeMillis() + ".jpg");
            imageUri = Uri.fromFile(file);
            mImageUri = imageUri; // 将 uri 传出，方便设置到视图中
        }

        // 开始切割
        //设置其Action为裁剪图片的动作
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置Intent的数据源和数据类型，其中uri参数表示要裁剪的图片的URI，而数据类型设置为image/*，表示可以处理所有类型的图片。
        intent.setDataAndType(uri, "image/*");

        //为启动相机应用的 Intent 设置读取 URI 权限，以便相机应用能够读取到保存照片的文件路径。
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //发送裁剪信号,设置裁剪参数，将裁剪参数crop设置为true，表示要进行裁剪操作
        intent.putExtra("crop", "true");
        // 设置裁剪框比例 表示裁剪框的宽度和高度比例为1:1，即正方形
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);


        // 设置输出图片大小，表示裁剪后输出图片的宽度和高度均为600像素。
        intent.putExtra("outputX", 600); // 输出图片大小
        intent.putExtra("outputY", 600);
        //设置裁剪参数，将参数scale设置为true，表示裁剪时保持图片的比例
        intent.putExtra("scale", true);

        //TODO 参数return-data设置为true时，表示裁剪后的图片数据将以Bitmap的形式返回
        intent.putExtra("return-data", false); // 此时为false表示不直接返回数据

        intent.putExtra("circleCrop", true);  // 进行圆形裁剪

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 返回一个文件
        //将图片保存的输出格式设置为 JPEG 格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // intent.putExtra("noFaceDetection", true); // no face detection  将拍照时的面部检测功能禁用
        startActivityForResult(intent, REQUEST_BIG_IMAGE_CUTTING);
    }



    /**
     * 处理权限回调结果
     */
    /* TODO onRequestPermissionsResult() 是一个回调方法，用于接收处理请求权限的结果。
            当调用 ActivityCompat.requestPermissions() 方法向用户请求权限后，用户选择授权或拒绝权限时，系统会调用该方法，并传入相应的参数：
                  requestCode：请求权限时传入的请求码，用于标识是哪个请求权限的回调。
                  permissions：请求的权限数组，包含了请求的权限。
                  grantResults：请求权限的结果数组，包含了每个权限的授权结果，其中 grantResults[i] 表示第 i 个权限的授权结果，值为 PackageManager.
                  PERMISSION_GRANTED 表示授权，值为 PackageManager.PERMISSION_DENIED 表示拒绝授权。*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toPhotoSelect();
                } else {
                    mPhotoPopupWindow.dismiss();
                    Snackbar.make(main_ll,"存储权限被拒绝,请重新授权",Snackbar.LENGTH_SHORT).setAction("前往", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //打开应用程序的详细设置页面
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    .setData(Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    }).show();
                }
                break;
            case 300:
                //grantResults 是一个整型数组，包含了请求权限的结果，其中 grantResults[0] 表示第一个权限的授权结果。
/*                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    mPhotoPopupWindow.dismiss();
                    imageCapture();
                } else {
                    mPhotoPopupWindow.dismiss();
                    Snackbar.make(main_ll,"相机权限或存储权限被拒绝,请重新授权",Snackbar.LENGTH_SHORT).setAction("前往", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    .setData(Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }).show();
                }*/
                // 检查每个权限的授予结果
                boolean allPermissionsGranted = true;
                for(int result : grantResults){
                    if(result !=PackageManager.PERMISSION_GRANTED){
                        allPermissionsGranted = false;
                        break; // 如果有一个权限未被授予，则跳出循环
                    }
                }

                // 如果所有权限都被授予，则执行相应的操作
                mPhotoPopupWindow.dismiss();
                if (allPermissionsGranted) {
                    imageCapture();
                } else {
                    // 如果有权限未被授予，您可以向用户显示一个提示或者执行其他操作
                    Snackbar.make(main_ll,"相机权限或存储权限被拒绝,请重新授权",Snackbar.LENGTH_SHORT).setAction("前往", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    .setData(Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }).show();
                }
                break;
        }
    }
}
