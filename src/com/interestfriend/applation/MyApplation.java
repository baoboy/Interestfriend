package com.interestfriend.applation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;

import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnNotificationClickListener;
import com.interestfriend.activity.ChatActivity;
import com.interestfriend.activity.MainActivity;
import com.interestfriend.receive.VoiceCallReceiver;
import com.interestfriend.utils.CheckImageLoaderConfiguration;

public class MyApplation extends Application {
	private static MyApplation instance;
	private static int circle_id;
	private static String circle_group_id = "";
	private static List<Activity> activityList = new ArrayList<Activity>();

	@Override
	public void onCreate() {
		super.onCreate();
		CheckImageLoaderConfiguration.checkImageLoaderConfiguration(this);
		instance = this;
		initHuanxin();
	}

	public static MyApplation getInstance() {
		return instance;
	}

	// 添加Activity到容器中
	public static void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish
	public static void exit(boolean flag) {
		for (int i = 0; i < activityList.size(); i++) {
			Activity activity = activityList.get(i);
			if (activity != null) {
				activity.finish();
			}
		}
		activityList.clear();
		if (flag) {
			System.exit(0);
		}
	}

	private void initHuanxin() {
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// 如果使用到百度地图或者类似启动remote service的第三方库，这个if判断不能少
		if (processAppName == null || processAppName.equals("")) {
			// workaround for baidu location sdk
			// 百度定位sdk，定位服务运行在一个单独的进程，每次定位服务启动的时候，都会调用application::onCreate
			// 创建新的进程。
			// 但环信的sdk只需要在主进程中初始化一次。 这个特殊处理是，如果从pid 找不到对应的processInfo
			// processName，
			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}
		EMChat.getInstance().setDebugMode(true);
		// 初始化环信SDK,一定要先调用init()
		EMChat.getInstance().init(instance);
		Log.d("EMChat Demo", "initialize EMChat SDK");
		// debugmode设为true后，就能看到sdk打印的log了

		// 获取到EMChatOptions对象
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		// 默认添加好友时，是不需要验证的，改成需要验证
		options.setAcceptInvitationAlways(false);
		// 设置收到消息是否有新消息通知，默认为true
		options.setNotifyBySoundAndVibrate(true);
		// 设置收到消息是否有声音提示，默认为true
		options.setNoticeBySound(true);
		// 设置收到消息是否震动 默认为true
		options.setNoticedByVibrate(true);
		// 设置语音消息播放是否设置为扬声器播放 默认为true
		options.setUseSpeaker(true);
		// 设置notification消息点击时，跳转的intent为自定义的intent
		options.setOnNotificationClickListener(new OnNotificationClickListener() {

			@Override
			public Intent onNotificationClick(EMMessage message) {
				Intent intent = new Intent(instance, ChatActivity.class);
				ChatType chatType = message.getChatType();
				System.out.println("type:::::::::;" + chatType);
				if (chatType == ChatType.Chat) { // 单聊信息
					intent.putExtra("userId", message.getFrom());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
				} else { // 群聊信息
							// message.getTo()为群聊id
					intent.putExtra("groupId", message.getTo());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
				}
				return intent;
			}
		});
		// 设置一个connectionlistener监听账户重复登陆
		EMChatManager.getInstance().addConnectionListener(
				new MyConnectionListener());
		// // 取消注释，app在后台，有新消息来时，状态栏的消息提示换成自己写的
		// options.setNotifyText(new OnMessageNotifyListener() {
		//
		// @Override
		// public String onNewMessageNotify(EMMessage message) {
		// // 可以根据message的类型提示不同文字(可参考微信或qq)，demo简单的覆盖了原来的提示
		// return "你的好基友" + message.getFrom() + "发来了一条消息哦";
		// }
		//
		// @Override
		// public String onLatestMessageNotify(EMMessage message, int
		// fromUsersNum, int messageNum) {
		// return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
		// }
		//
		// @Override
		// public String onSetNotificationTitle(EMMessage message) {
		// //修改标题
		// return "环信notification";
		// }
		//
		//
		// });

		// // 注册一个语言电话的广播接收者
		IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance()
				.getIncomingVoiceCallBroadcastAction());
		registerReceiver(new VoiceCallReceiver(), callFilter);
	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm
							.getApplicationInfo(info.processName,
									PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}

	class MyConnectionListener implements ConnectionListener {
		@Override
		public void onReConnecting() {
		}

		@Override
		public void onReConnected() {
		}

		@Override
		public void onDisConnected(String errorString) {
			if (errorString != null && errorString.contains("conflict")) {
				Intent intent = new Intent(instance, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("conflict", true);
				startActivity(intent);
			}

		}

		@Override
		public void onConnecting(String progress) {

		}

		@Override
		public void onConnected() {
		}
	}

	public static int getCircle_id() {
		return circle_id;
	}

	public static void setCircle_id(int circle_id) {
		MyApplation.circle_id = circle_id;
	}

	public static String getCircle_group_id() {
		return circle_group_id;
	}

	public static void setCircle_group_id(String circle_group_id) {
		MyApplation.circle_group_id = circle_group_id;
	}

}
