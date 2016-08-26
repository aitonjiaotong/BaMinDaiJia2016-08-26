package com.aiton.bamin.bamindaijiadrier.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.utils.UILUtils;
import com.aiton.administrator.shane_library.shane.widget.AvatarImageView;
import com.aiton.bamin.bamindaijiadrier.R;
import com.aiton.bamin.bamindaijiadrier.constant.Constant;
import com.aiton.bamin.bamindaijiadrier.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class MineActivity extends ZjbBaseActivity implements View.OnClickListener {

    private AvatarImageView mImg_Avatar;
    private TextView mTv_Name;
    private TextView mTv_User_Phone;
    private ACache aCache;
    private User mUser;
    private User.DatasEntity datas;
    private String imgUrl;
    private String mImage;
    private String mPhoneNum;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        init();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        User.DatasEntity datas = mUser.getDatas();
        if (datas != null) {
            mImage = datas.getImage();
            mPhoneNum = datas.getPhone();
            name = datas.getName();
            String sex = datas.getReserve3();
            String age = datas.getReserve4();

            String phone = datas.getPhone();
            long id = datas.getId();
            if (TextUtils.isEmpty(mImage)) {
                String Path = "/upload/" + mPhoneNum + "upload.jpeg";
                File pictureFile = new File(Environment.getExternalStorageDirectory(), Path);
                if (pictureFile.exists()) {
                    Uri uri = Uri.fromFile(pictureFile);
                    Bitmap bitmap = decodeUriAsBitmap(uri);
                    mImg_Avatar.setImageBitmap(bitmap);
                    Log.e("checkLogin", "本地图片存在");
                } else {
                    Log.e("checkLogin", "本地图片不存在");
                    mImg_Avatar.setImageResource(R.mipmap.ic_avatar);
                }
            } else {
                if (mImage != null) {
                    Log.e("checkLogin", "图片URL" + mImage);
                    UILUtils.displayImageNoAnim(mImage, mImg_Avatar);
                }
            }
//            if (sex != null && !TextUtils.isEmpty(sex)) {
//                mTv_User_Sex.setText(sex);
//
//            }
//            if (age != null && !TextUtils.isEmpty(age)) {
//                mTv_User_Age.setText(age);
//
//            }
            if (name != null && !TextUtils.isEmpty(name)) {
                mTv_Name.setText(name);
            }

            if (!TextUtils.isEmpty(phone)) {
                mImg_Avatar.setFileName(phone + "upload");
                mTv_User_Phone.setText(phone);
            }
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    @Override
    protected void setListeners() {
        findViewById(R.id.imageView_back).setOnClickListener(this);
        findViewById(R.id.rela_user_avatar).setOnClickListener(this);
        findViewById(R.id.rela_user_name).setOnClickListener(this);

        //头像控件监听
        mImg_Avatar.setAfterCropListener(new AvatarImageView.AfterCropListener() {
            @Override
            public void afterCrop(Bitmap photo) {
                Toast.makeText(MineActivity.this, "设置新的头像成功", Toast.LENGTH_SHORT).show();

                Log.e("MainActivity", "afterCrop: --->>" + mImg_Avatar.getFileName());
                postFile(mImg_Avatar.getFileName());
            }
        });
    }

    @Override
    protected void initSP() {
        aCache = ACache.get(this);
        mUser = (User) aCache.getAsObject(Constant.ACACHE.USER);
        datas = mUser.getDatas();
        imgUrl = datas.getImage();
        Log.e("MineActivity", "ID: --->>" + mUser.getDatas().getId());


    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void findID() {
        mImg_Avatar = (AvatarImageView) findViewById(R.id.imageView_touXiang);
        mTv_Name = (TextView) findViewById(R.id.tv_user_name);
        mTv_User_Phone = (TextView) findViewById(R.id.tv_user_phone);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        String userName = mTv_Name.getText().toString().trim();
        String image = mUser.getDatas().getImage();
        if (userName.equals("请填写姓名")) {
            userName = "";
        }
        switch (v.getId()) {
            case R.id.imageView_back:
                intent.putExtra(Constant.INTENT_KEY.USER_NAME, userName);
                intent.putExtra(Constant.INTENT_KEY.USER_AVATAR, image);
                setResult(Constant.REQUEST_RESULT_CODE.USER_INFO, intent);
                finishTo();
                break;

            case R.id.rela_user_name:
                intent.setClass(this, ChangeNameActivity.class);
                intent.putExtra(Constant.INTENT_KEY.CHANGE_NAME, mTv_Name.getText().toString());
                startActivityForResultTo(intent, Constant.REQUEST_RESULT_CODE.CHANGE_NAEM);

                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_RESULT_CODE.CHANGE_NAEM && resultCode == Constant.REQUEST_RESULT_CODE.CHANGE_NAEM) {

            String name = data.getStringExtra(Constant.INTENT_KEY.CHANGE_NAME);
            Log.e("MineActivity", "name: --->>" + name);
            mTv_Name.setText(name);

        }
        //在拍照、选取照片、裁剪Activity结束后，调用的方法
        if (mImg_Avatar != null) {
            mImg_Avatar.onActivityResult(requestCode, resultCode, data);
        }

    }

    /**
     * 上传图片
     *
     * @param uploadFile
     */
    public void postFile(Uri uploadFile) {
        String path = uploadFile.getPath();
        Log.e("postFile", "path" + path);
        File file = new File(path);

        String actionUrl = Constant.Url.CUSTOMERIMAGE;
        if (file.exists() && file.length() > 0) {
            RequestParams params = new RequestParams();
            params.put("driver_id", mUser.getDatas().getId());
            Log.e("postFile", "用户ID" + mUser.getDatas().getId());
            try {
                params.put("data", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.post(actionUrl, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.e("onSuccess: ", "---> " + new String(responseBody));
                    try {
                        String imgUrl = new String(responseBody);
                        datas.setImage(imgUrl);
                        mUser.setDatas(datas);
                        aCache.put(Constant.ACACHE.USER, mUser, 30 * ACache.TIME_DAY);
                        Log.e("MineActivity", "图片imgUrl:>>" + imgUrl);
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e("onFailure", "失败");
                }
            });
        } else {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
        }
        ImageLoader.getInstance().clearDiskCache();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pushUserInfo();
    }
    /**
     * 上传用户信息
     */
    private void pushUserInfo() {
        String name = mTv_Name.getText().toString().trim();
//        String age = mTv_User_Age.getText().toString().trim();

        if (name != "请填写姓名" && name != mUser.getDatas().getName()) {


//        if (age != "请选择") {
//            datas.setReserve4(age);
//        }

            String url = Constant.Url.USER_INFO;
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("id", mUser.getDatas().getId());
            params.put("name", name);
            params.put("phone", mUser.getDatas().getPhone());
            Log.e("上传用户信息", "id: --->>" + mUser.getDatas().getId());
            Log.e("上传用户信息", "name: --->>" + name);
            Log.e("上传用户信息", "phone: --->>" + mUser.getDatas().getPhone());


            asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    try {
                        mUser = GsonUtils.parseJSON(s, User.class);
                        if (mUser.getStatus() == 0) {

                            aCache.put(Constant.ACACHE.USER, mUser, 30 * ACache.TIME_DAY);

                            Log.e("MineActivity", "用户名字: --->>" + mUser.getDatas().getName());

                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(MineActivity.this, "用户信息保存失败", Toast.LENGTH_SHORT).show();
                    Log.e("MineActivity", "用户信息保存失败"  );

                }
            });
        }

        //保存用户信息在本地


        Log.e("MineActivity", "onStop:imgUrl>>" + imgUrl);
        Log.e("MineActivity", "onStop:Name>>" + name);
//        Log.e("MineActivity", "onStop:imgUrl>>" + age);
    }
}
