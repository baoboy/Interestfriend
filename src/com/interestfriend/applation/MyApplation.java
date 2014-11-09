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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.interestfriend.activity.ChatActivity;
import com.interestfriend.data.CircleMember;
import com.interestfriend.db.DBUtils;
import com.interestfriend.receive.VoiceCallReceiver;
import com.interestfriend.utils.CheckImageLoaderConfiguration;
import com.interestfriend.utils.SharedUtils;

public class MyApplation extends Application {
	private static MyApplation instance;
	private static int circle_id;
	private static String circle_group_id = "";
	private static String circle_name = "";
	private static List<Activity> activityList = new ArrayList<Activity>();
	private static LocationClient locationClient = null;
	private static double nLatitude = 0;// 维度
	private static double nLontitude = 0;// 经度

	@Override
	public void onCreate() {
		super.onCreate();
		CheckImageLoaderConfiguration.checkImageLoaderConfiguration(this);
		instance = this;
		initBaiduLocation();
		locationClient.start();
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
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.stop();
			locationClient = null;
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
		options.setNotifyBySoundAndVibrate(SharedUtils
				.getSettingMsgNotification());
		// 设置收到消息是否有声音提示，默认为true
		options.setNoticeBySound(SharedUtils.getSettingMsgSound());
		// 设置收到消息是否震动 默认为true
		options.setNoticedByVibrate(SharedUtils.getSettingMsgVibrate());
		// 设置语音消息播放是否设置为扬声器播放 默认为true
		options.setUseSpeaker(true);
		// 设置notification消息点击时，跳转的intent为自定义的intent
		options.setOnNotificationClickListener(new OnNotificationClickListener() {

			@Override
			public Intent onNotificationClick(EMMessage message) {
				Intent intent = new Intent(instance, ChatActivity.class);
				ChatType chatType = message.getChatType();
				if (chatType == ChatType.Chat) { // 单聊信息
					intent.putExtra("userId", message.getFrom());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
				}
				return intent;
			}
		});
		// 设置一个connectionlistener监听账户重复登陆
		EMChatManager.getInstance().addConnectionListener(
				new MyConnectionListener());
		// // 取消注释，app在后台，有新消息来时，状态栏的消息提示换成自己写的
		options.setNotifyText(new OnMessageNotifyListener() {

			@Override
			public String onNewMessageNotify(EMMessage message) {
				CircleMember mbmer = new CircleMember();
				mbmer.setUser_chat_id(message.getFrom());
				mbmer.getNameAndAvatarByUserChatId(DBUtils.getDBsa(1));
				return "你的趣友 " + mbmer.getUser_name() + " 发来了一条消息";
			}

			@Override
			public String onLatestMessageNotify(EMMessage message,
					int fromUsersNum, int messageNum) {
				return fromUsersNum + "个趣友，发来了" + messageNum + "条消息";
			}

			@Override
			public String onSetNotificationTitle(EMMessage message) {
				// 修改标题
				return "趣友";
			}

		});

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
				// Intent intent = new Intent(instance, MainActivity.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// intent.putExtra("conflict", true);
				// startActivity(intent);
			}

		}

		@Override
		public void onConnecting(String progress) {

		}

		@Override
		public void onConnected() {
		}
	}

	private void initBaiduLocation() {
		locationClient = new LocationClient(this);
		// 设置定位条件
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 是否打开GPS
		option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
		option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
		option.setProdName("趣友"); // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
		locationClient.setLocOption(option);

		// 注册位置监听器
		locationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null) {
					return;
				}
				setnLontitude(location.getLongitude());
				setnLatitude(location.getLatitude());
			}

			@Override
			public void onReceivePoi(BDLocation location) {
			}

		});
	}

	/**
	 * 退出登录,清空数据
	 */
	public static void logoutHuanXin() {
		// 先调用sdk logout，在清理app中自己的数据
		EMChatManager.getInstance().logout();

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

	public static String getCircle_name() {
		return circle_name;
	}

	public static void setCircle_name(String circle_name) {
		MyApplation.circle_name = circle_name;
	}

	public static double getnLatitude() {
		return nLatitude;
	}

	public static void setnLatitude(double nLatitude) {
		MyApplation.nLatitude = nLatitude;
	}

	public static double getnLontitude() {
		return nLontitude;
	}

	public static void setnLontitude(double nLontitude) {
		MyApplation.nLontitude = nLontitude;
	}

}
