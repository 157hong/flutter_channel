package com.example.flutter_tranfer;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {


    // 通道名称
    private static final String ChannelName = "BatteryChannelName";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Android 端需要在恰当的时机获取到 FlutterEngine 对象
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);

        // 新建一个 Channel 对象
        MethodChannel batteryMethodChannel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), ChannelName);

        // 实现 MethodChannel.MethodCallHandler 接口，重写 onMethodCall 方法
        // 对这个 channel 对象设置 MethodCallHandler
        batteryMethodChannel.setMethodCallHandler(
            new MethodChannel.MethodCallHandler() {
                @Override
                // 通道的回调
                public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
                    if (call.method.equals("getBatteryLevel")){// 注：字符串判断用equals
                        String msg = call.arguments();// 获取传入的参数
                        int batteryLevel = getBatteryLevel();
                        if (batteryLevel != -1){
                            result.success(batteryLevel);
                        }else{
                            result.error("", "error", "");  // 未识别方法名， 通知执行失败
                        }
                    } else {
                        result.notImplemented();  // 未实现方法
                    }
                }
            }
        );



    }

    // 获取电量的原生方法
    private int getBatteryLevel() {
        int batteryLevel = -1;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        } else {
            Intent intent = new ContextWrapper(getApplicationContext()).
                    registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
                    intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }
        return batteryLevel;
    }


}