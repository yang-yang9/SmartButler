package com.yangyang.smartbutler.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.yangyang.smartbutler.R;
import com.yangyang.smartbutler.entity.User;
import com.yangyang.smartbutler.ui.LocationActivity;
import com.yangyang.smartbutler.ui.LoginActivity;
import com.yangyang.smartbutler.ui.QrCodeActivity;
import com.yangyang.smartbutler.utils.L;
import com.yangyang.smartbutler.view.CustomDialog;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

/*
 *   项目名：SmartButler
 *   包名：com.yangyang.smartbutler.fragment
 *   文件名：PersonalFragment
 *   创建者：YangYang
 *   描述：个人中心Fragment
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PersonalFragment extends Fragment implements View.OnClickListener {
    private String mfrom;
    private TextView edit_user;
    private EditText et_username;
    private EditText et_gender;
    private EditText et_age;
    private EditText et_describe;
    private Button btn_exit;
    private Button btn_ack_change;
    private CircleImageView profile_image;


    private CustomDialog dialog;
    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;
    private File fileTemp;

    //二维码
    private LinearLayout ll_code;
    //扫一扫
    private LinearLayout ll_scan;
    private TextView tv_scan_result;

    //我的位置
    private LinearLayout ll_location;


    public PersonalFragment(){

    }

    public static Fragment newInstance(String from){
        PersonalFragment personalFragment = new PersonalFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        personalFragment.setArguments(bundle);

        return personalFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy( builder.build() );
        }
        if (getArguments() != null){
            mfrom = (String) getArguments().get("from");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        btn_exit = view.findViewById(R.id.btn_personal_exit);
        edit_user = view.findViewById(R.id.tv_edit_user);
        et_username = view.findViewById(R.id.et_personal_username);
        et_gender = view.findViewById(R.id.et_personal_gender);
        et_age = view.findViewById(R.id.et_personal_age);
        et_describe = view.findViewById(R.id.et_personal_describe);
        btn_ack_change = view.findViewById(R.id.btn_personal_ack);
        profile_image = view.findViewById(R.id.profile_image);

        ll_code = view.findViewById(R.id.ll_personal_QR_code);
        ll_scan = view.findViewById(R.id.ll_personal_scan);
        tv_scan_result = view.findViewById(R.id.tv_personal_scan_result);
        ll_location = view.findViewById(R.id.ll_personal_location);
        ll_code.setOnClickListener(this);
        ll_scan.setOnClickListener(this);
        ll_location.setOnClickListener(this);



        edit_user.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_ack_change.setOnClickListener(this);
        profile_image.setOnClickListener(this);

        //UtilTools.getImageToView(getActivity(), profile_image);
        //后台拿到头像并设置

        //默认不可编辑
        setChangeEnabled(false);

        try {
            //从后台拿到注册时填写的值
            User user = BmobUser.getCurrentUser(User.class);
            if (user != null){
                et_username.setText(user.getUsername());
                et_gender.setText(user.isSex()?"男":"女");
                et_age.setText(user.getAge() + "");
                et_describe.setText(user.getDescribe());

                //后台拿到头像并设置
                BmobQuery<User> query = new BmobQuery<>();
                query.getObject(user.getObjectId(), new QueryListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null){
                            if (user.getPhoto() != null){
                                download(user.getPhoto());
                            } else {
                                profile_image.setImageResource(R.mipmap.ic_launcher1);
                            }
                        }
                    }
                });
            }
        } catch (Exception e){
            L.i("有错误：personalfragment" + e.toString());
        }



        dialog = new CustomDialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, R.layout.dialog_photo, R.style.Theme_dialog, Gravity.BOTTOM, 0);
        btn_camera = dialog.findViewById(R.id.btn_camera);
        btn_picture = dialog.findViewById(R.id.btn_picture);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);

        btn_camera.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

    }

    private void download(BmobFile photo) {
        photo.download(new DownloadFileListener() {
            @Override
            public void done(String path, BmobException e) {
                if (e == null){
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    profile_image.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });
    }


    private void setChangeEnabled(boolean is) {
        et_username.setEnabled(is);
        et_age.setEnabled(is);
        et_gender.setEnabled(is);
        et_describe.setEnabled(is);
        if (is){
            et_username.requestFocus();
            et_username.setBackground(getResources().getDrawable(R.drawable.custom_edittext_background));
            et_age.setBackground(getResources().getDrawable(R.drawable.custom_edittext_background));
            et_gender.setBackground(getResources().getDrawable(R.drawable.custom_edittext_background));
            et_describe.setBackground(getResources().getDrawable(R.drawable.custom_edittext_background));
        } else {
            et_username.setBackground(null);
            et_age.setBackground(null);
            et_gender.setBackground(null);
            et_describe.setBackground(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //退出登录
            case R.id.btn_personal_exit:
                User.logOut();
                BmobUser currentUser = User.getCurrentUser(User.class);
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            //编辑资料
            case R.id.tv_edit_user:
                setChangeEnabled(true);
                btn_ack_change.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_personal_ack:
                String username = et_username.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String gender = et_gender.getText().toString().trim();
                String describe = et_describe.getText().toString().trim();

                if (TextUtils.isEmpty(username)){
                    Snackbar.make(et_username, "用户名不能为空", Snackbar.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(age)){
                    Snackbar.make(et_age, "年龄不能为空", Snackbar.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(gender)){
                    Snackbar.make(et_gender, "性别不能为空", Snackbar.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(gender)){
                    if (gender.equals("男") || gender.equals("女")){
                        User user = new User();
                        user.setUsername(username);
                        user.setAge(Integer.parseInt(age));
                        if (gender.equals("男")){
                            user.setSex(true);
                        } else {
                            user.setSex(false);
                        }
                        if (!TextUtils.isEmpty(describe)){
                            user.setDescribe(describe);
                        } else {
                            user.setDescribe(String.valueOf(R.string.def_describe));
                        }

                        //更新
                        BmobUser bmobUser = BmobUser.getCurrentUser(User.class);
                        user.update(bmobUser.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null){
                                    setChangeEnabled(false);
                                    btn_ack_change.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "修改失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Snackbar.make(et_gender, "性别错误", Snackbar.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.profile_image:
                dialog.show();
                break;
            case R.id.btn_camera:
                toCamera();
                break;
            case R.id.btn_picture:
                toPicture();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.ll_personal_QR_code:
                Intent qrCodeIntent = new Intent(getActivity(), QrCodeActivity.class);
                Bitmap bitmap = ((BitmapDrawable)profile_image.getDrawable()).getBitmap();
                qrCodeIntent.putExtra("key", bitmap);
                startActivity(qrCodeIntent);
                break;
            case R.id.ll_personal_scan:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                break;
            case R.id.ll_personal_location:
                startActivity(new Intent(getActivity(), LocationActivity.class));
                break;
        }
    }

    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final String PHOTO_IMAGE_FILE_TEMP_NAME = "fileTemp.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int PICTURE_REQUEST_CODE = 101;
    public static final int CROP_REQUEST_CODE = 102;
    public static final int REQUEST_CODE_SCAN = 103;

    public File tempFile = null;

    //打开相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    //打开相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICTURE_REQUEST_CODE);
        dialog.dismiss();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_CANCELED){
            switch (requestCode){
                //相机
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);

                    imageTailoring(Uri.fromFile(tempFile));
                    break;
                //图库
                case PICTURE_REQUEST_CODE:
                    imageTailoring(data.getData());
                    break;
                //拿到裁剪的图片
                case CROP_REQUEST_CODE:
                    //设置完删除剪裁完的图片
                    if (tempFile != null){
                        //文件上传
                        tempFile.delete();
                    }
                    setImageToView(data);
                    break;
                case REQUEST_CODE_SCAN:

                    if (data != null){
                        String scanResult = data.getStringExtra(Constant.CODED_CONTENT);
                        tv_scan_result.setText(scanResult);
                    }
                    break;

            }
        }


        /*//扫描结果处理
        if (resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            tv_scan_result.setText(scanResult);
        }*/
    }


    //图片裁剪
    public void imageTailoring(Uri uri){
        if (uri == null){
            L.i("uri为空");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        //裁剪属性
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX",320);
        intent.putExtra("outputY",320);

        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null){
            Bitmap bitmap = data.getParcelableExtra("data");
            //profile_image.setImageBitmap(bitmap);
            fileTemp = new File(Environment.getExternalStorageDirectory().getPath(), PHOTO_IMAGE_FILE_TEMP_NAME);

            //File fileTemp=new File("/mnt/sdcard/pic/01.jpg");
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileTemp));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            savePhoto(fileTemp, bitmap);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //UtilTools.putImageToShare(getActivity(), profile_image);


    }

    private void savePhoto(File fileTemp, Bitmap bitmap){
        BmobFile file = new BmobFile(fileTemp);
        file.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                    //profile_image.setImageBitmap(bitmap);
                    saveFile(file, bitmap);
                } else {
                    Toast.makeText(getActivity(), "上传失败：" + e.toString(), Toast.LENGTH_LONG).show();
                    if (fileTemp != null){
                        fileTemp.delete();
                    }
                }
            }
        });
    }

    private void saveFile(BmobFile file, Bitmap bitmap) {
        User user = BmobUser.getCurrentUser(User.class);
        user.setPhoto(file);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    //tempFile.delete();
                    profile_image.setImageBitmap(bitmap);
                    if (fileTemp != null){
                        fileTemp.delete();
                    }
                } else {
                    Toast.makeText(getActivity(), "设置失败：" + e.toString(), Toast.LENGTH_LONG).show();
                    if (fileTemp != null){
                        fileTemp.delete();
                    }
                }
            }
        });
    }
}
