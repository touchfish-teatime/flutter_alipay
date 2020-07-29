import UIKit
import Flutter

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
    GeneratedPluginRegistrant.register(with: self)
    
    // 通道
    let controller : FlutterViewController = window?.rootViewController as! FlutterViewController;
    
    let batteryChannel = FlutterMethodChannel.init(name: "com.sendroids.alipaytest/alipay.method", binaryMessenger: controller.binaryMessenger);
    
    batteryChannel.setMethodCallHandler({
      (call: FlutterMethodCall, result: FlutterResult) -> Void in
        if call.method == "pay" {
            print("you are pend to pay")
            let payment = Pay();
            payment.createOrder();
        } else {
            result(FlutterMethodNotImplemented);
        }
      // Handle battery messages.
    });
    
    // end of 通道

    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
}
