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
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.interestfriend.activity.ChatActivity;
import com.interestfriend.activity.DissolveCircleActivity;
import com.interestfriend.activity.HomeActivity;
import com.interestfriend.activity.JoinCircleActivity;
import com.interestfriend.activity.PraiseAndCommentActivity;
import com.interestfriend.activity.ReceiveJoinCircleActivity;
import com.interestfriend.activity.RefuseJoinCircleActivity;
import com.interestfriend.data.CircleMember;
import com.interestfriend.db.DBUtils;
import com.interestfriend.receive.VoiceCallReceiver;
import com.interestfriend.utils.CheckImageLoaderConfiguration;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.SharedUtils;

public class MyApplation extends Application {
	private static MyApplation instance;
	private static int circle_id;
	private static String circle_group_id = "";
	private static String circle_name = "";
	private static List<Activity> activityList = new ArrayList<Activity>();
	private static double nLatitude = 0;// ά��
	private static double nLontitude = 0;// ����
	private static String address = "";

	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;

	@Override
	public void onCreate() {
		super.onCreate();
		CheckImageLoaderConfiguration.checkImageLoaderConfiguration(this);
		instance = this;
		initBaiduLocation();
		initHuanxin();
	}

	public static MyApplation getInstance() {
		return instance;
	}

	// ���Activity��������
	public static void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// ��������Activity��finish
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
		// ���ʹ�õ��ٶȵ�ͼ������������remote service�ĵ������⣬���if�жϲ�����
		if (processAppName == null || processAppName.equals("")) {
			// workaround for baidu location sdk
			// �ٶȶ�λsdk����λ����������һ�������Ľ��̣�ÿ�ζ�λ����������ʱ�򣬶������application::onCreate
			// �����µĽ��̡�
			// �����ŵ�sdkֻ��Ҫ���������г�ʼ��һ�Ρ� ������⴦���ǣ������pid �Ҳ�����Ӧ��processInfo
			// processName��
			// ���application::onCreate �Ǳ�service ���õģ�ֱ�ӷ���
			return;
		}
		EMChat.getInstance().setDebugMode(true);
		// ��ʼ������SDK,һ��Ҫ�ȵ���init()
		EMChat.getInstance().init(instance);
		Log.d("EMChat Demo", "initialize EMChat SDK");
		// debugmode��Ϊtrue�󣬾��ܿ���sdk��ӡ��log��

		// ��ȡ��EMChatOptions����
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		// Ĭ����Ӻ���ʱ���ǲ���Ҫ��֤�ģ��ĳ���Ҫ��֤
		options.setAcceptInvitationAlways(false);
		// �����յ���Ϣ�Ƿ�������Ϣ֪ͨ��Ĭ��Ϊtrue
		options.setNotifyBySoundAndVibrate(SharedUtils
				.getSettingMsgNotification());
		// �����յ���Ϣ�Ƿ���������ʾ��Ĭ��Ϊtrue
		options.setNoticeBySound(SharedUtils.getSettingMsgSound());
		// �����յ���Ϣ�Ƿ��� Ĭ��Ϊtrue
		options.setNoticedByVibrate(SharedUtils.getSettingMsgVibrate());
		// ����������Ϣ�����Ƿ�����Ϊ���������� Ĭ��Ϊtrue
		options.setUseSpeaker(true);
		// ����notification��Ϣ���ʱ����ת��intentΪ�Զ����intent
		options.setOnNotificationClickListener(new OnNotificationClickListener() {

			@Override
			public Intent onNotificationClick(EMMessage message) {
				Intent intent = null;
				ChatType chatType = message.getChatType();
				String username = message.getFrom();
				if (chatType == ChatType.Chat) { // ������Ϣ
					if (Constants.JOIN_CIRCLE_USER_ID.equals(username)) {
						intent = new Intent(instance, JoinCircleActivity.class);
					} else if (Constants.RECEIVE_JOIN_CIRCLE_USER_ID
							.equals(username)) {
						intent = new Intent(instance,
								ReceiveJoinCircleActivity.class);
					} else if (Constants.REFUSE_JON_CIRCLE_USER_ID
							.equals(username)) {
						intent = new Intent(instance,
								RefuseJoinCircleActivity.class);
					} else if (Constants.PRAISE_USER_ID.equals(username)) {
						intent = new Intent(instance, HomeActivity.class);
					} else {
						intent = new Intent(instance, ChatActivity.class);
						intent.putExtra("chatType",
								ChatActivity.CHATTYPE_SINGLE);
					}
					intent.putExtra("userId", message.getFrom());

				} else {
					if (Constants.DISSOLVE_CIRCLE_USER_ID.equals(username)) {
						intent = new Intent(instance,
								DissolveCircleActivity.class);
						intent.putExtra("userId", message.getTo());

					} else {
						intent = new Intent(instance, HomeActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					}
				}
				return intent;
			}
		});
		// ����һ��connectionlistener�����˻��ظ���½
		EMChatManager.getInstance().addConnectionListener(
				new MyConnectionListener());
		// // ȡ��ע�ͣ�app�ں�̨��������Ϣ��ʱ��״̬������Ϣ��ʾ�����Լ�д��
		options.setNotifyText(new OnMessageNotifyListener() {

			@Override
			public String onNewMessageNotify(EMMessage message) {
				CircleMember mbmer = new CircleMember();
				mbmer.setUser_chat_id(message.getFrom());
				mbmer.getNameAndAvatarByUserChatId(DBUtils.getDBsa(1));
				return "���Ȥ�� " + mbmer.getUser_name() + " ������һ����Ϣ";
			}

			@Override
			public String onLatestMessageNotify(EMMessage message,
					int fromUsersNum, int messageNum) {
				return fromUsersNum + "��Ȥ�ѣ�������" + messageNum + "����Ϣ";
			}

			@Override
			public String onSetNotificationTitle(EMMessage message) {
				// �޸ı���
				return "Ȥ��";
			}

		});

		// // ע��һ�����Ե绰�Ĺ㲥������
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
				Intent intent = new Intent(instance, HomeActivity.class);
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

	private void initBaiduLocation() {
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ���ö�λģʽ
		option.setCoorType("gcj02");// ���صĶ�λ����ǰٶȾ�γ�ȣ�Ĭ��ֵgcj02
		option.setScanSpan(1000);// ���÷���λ����ļ��ʱ��Ϊ5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		mLocationClient.start();

	}

	/**
	 * �˳���¼,�������
	 */
	public static void logoutHuanXin() {
		// �ȵ���sdk logout��������app���Լ�������
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

	public static String getAddress() {
		return address;
	}

	public static void setAddress(String address) {
		MyApplation.address = address;
	}

	/**
	 * ʵ��ʵλ�ص�����
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			nLatitude = location.getLatitude();
			nLontitude = location.getLongitude();
			address = location.getAddrStr();
		}

	}

}
