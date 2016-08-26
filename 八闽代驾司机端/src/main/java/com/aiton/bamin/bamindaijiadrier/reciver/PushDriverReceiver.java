package com.aiton.bamin.bamindaijiadrier.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.bamin.bamindaijiadrier.constant.Constant;
import com.aiton.bamin.bamindaijiadrier.model.Message;
import com.igexin.sdk.PushConsts;

/**
 * Created by zjb on 2016/6/28.
 */
public class PushDriverReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID:

                String cid = bundle.getString("clientid");
                // TODO:处理cid返回
                break;
            case PushConsts.GET_MSG_DATA:

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String data = new String(payload);

                    // TODO:接收处理透传（payload）数据
                    Log.e("onReceive ", "透传消息"+data);

                    Message message = GsonUtils.parseJSON(data, Message.class);
                    String title = message.getTitle();
                    Log.e("title",title);


                    String content = message.getContent();
                    Log.e("content",content);
                    String replace = content.replace("\'", "\"");
                    Log.e("replace",replace);
                    if ("driverGetOrder".equals(title)) {
                        Log.e("onMessage ", "replace:"+replace);
                        Intent intent1 = new Intent();
                        intent1.putExtra(Constant.INTENT_KEY.SEND_ORDER_VALUE,replace);
                        intent1.setAction(Constant.BROADCASTCODE.SEND_ORDER);
                        context.sendBroadcast(intent1);
                    }else if("cancelOrder".equals(title)){
                        Log.e("onMessage ", "replace:"+replace);
                        Intent intent1 = new Intent();
                        intent1.putExtra(Constant.INTENT_KEY.CANCELORDER,replace);
                        intent1.setAction(Constant.BROADCASTCODE.CANCEL_ORDER);
                        context.sendBroadcast(intent1);
                    }else if("paySuccess".equals(title)){
                        Log.e("onMessage ", "replace:"+replace);
                        Intent intent1 = new Intent();
                        intent1.putExtra(Constant.INTENT_KEY.PAY_SUCCESS,replace);
                        intent1.setAction(Constant.BROADCASTCODE.PAY_SUCCESS);
                        context.sendBroadcast(intent1);
                    }
                }
                break;
            default:
                break;
        }
    }
    //    Log.e("onMessage ", "onMessage "+s+"第二个字段"+s1);
//    Message message = GsonUtils.parseJSON(s, Message.class);
//    String title = message.getTitle();
////        0:向司机派单

}
