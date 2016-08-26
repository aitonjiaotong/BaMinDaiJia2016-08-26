package bamin.com.bamindaijia.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.igexin.sdk.PushConsts;

import bamin.com.bamindaijia.constant.Constant;
import bamin.com.bamindaijia.model.Message;

/**
 * Created by zjb on 2016/6/28.
 */
public class PushPassagerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID:

                String cid = bundle.getString("clientid");
                Log.e("onReceive ", "cid： "+cid);
                // TODO:处理cid返回
                break;
            case PushConsts.GET_MSG_DATA:
                Log.e("onReceive ", "有透传过来");
                String taskid = bundle.getString("taskid");
                Log.e("onReceive ", "taskid： "+taskid);
                String messageid = bundle.getString("messageid");
                Log.e("onReceive ", "messageid: "+messageid);
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String data = new String(payload);
                    Log.e("onReceive ", "透传消息"+data);


                    Message message = GsonUtils.parseJSON(data, Message.class);
                    String title = message.getTitle();
                    Log.e("title",title);


                    String content = message.getContent();
                    Log.e("content",content);
                    String replace = content.replace("\'", "\"");
                    if ("driverConfirmOrder".equals(title)) {
                        Log.e("PushPassagerReceiver", "driverConfirmOrder: --->>" +replace );

                        Intent intent1 = new Intent();
                        intent1.putExtra(Constant.INTENT_KEY.PUSH_ORDER_ING_VALUE,replace);
                        intent1.setAction(Constant.BROADCASTCODE.HAS_DRIVER);
                        context.sendBroadcast(intent1);
                    }else if ("getPassager".equals(title)){
                        Intent intent1 = new Intent();
                        intent1.putExtra(Constant.INTENT_KEY.GETPASSAGER,replace);
                        intent1.setAction(Constant.BROADCASTCODE.GETPASSAGER);
                        context.sendBroadcast(intent1);
                    }else if("driverChangeMileage".equals(title)){
                        Log.e("onMessage ", "content:"+replace);
                        Intent intent1 = new Intent();
                        intent1.putExtra(Constant.INTENT_KEY.DRIVER_CHANGE_MILEAGE,replace);
                        intent1.setAction(Constant.BROADCASTCODE.DRIVER_CHANGE_MILEAGE);
                        context.sendBroadcast(intent1);
                    }else if ("changeOrder".equals(title)){
                        Log.e("PushPassagerReceiver", "changeOrder: --->>" +replace );

                        Intent intent1 = new Intent();
                        intent1.putExtra(Constant.INTENT_KEY.CHANGE_END,replace);
                        intent1.setAction(Constant.BROADCASTCODE.CHANGE_END);
                        context.sendBroadcast(intent1);
                    }
                }
                break;
            default:
                break;
        }
    }
//    Log.e("onMessage ", "乘客onMessage "+s);
//    Message message = GsonUtils.parseJSON(s, Message.class);
//    String title = message.getTitle();

}
