import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key}) : super(key: key);

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {

  // 默认电量
  String _batteryLevel = '还未获取：';

  // 获取电池电量的异步方法
  _getBatteryLevel () async {
    _batteryLevel = await BatteryChannel.getBatteryLevel();
    setState(() {
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('通信传递信息'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
                '当前电量为：$_batteryLevel'
            ),
            ElevatedButton(
              onPressed: (){
                _getBatteryLevel();
              },
              child: Text("获取电量"),
            ),
          ],
        ),
      ),
    );
  }
}

class BatteryChannel {

  static const String _methodChannelName = "BatteryChannelName";
  static const MethodChannel _methodChannel = MethodChannel(_methodChannelName);

  // 异步任务，通过平台通道与特定平台进行通信，获取电量，这里的宿主平台是 Android
  static getBatteryLevel() async {
    String _batteryLevel;
    try{
      // 通过通道调用原生方法
      int result = await _methodChannel.invokeMethod('getBatteryLevel');
      _batteryLevel = " ${result} %";
    } on PlatformException catch (e) {
      _batteryLevel = "Failed to get battery level: '${e.message}'.";
    }
    // print('battery>>>>>>>>${_batteryLevel}');
    return _batteryLevel;
  }

}
