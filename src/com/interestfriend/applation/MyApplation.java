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
		options.setNotifyBySoundAndVibrate(true);
		// �����յ���Ϣ�Ƿ���������ʾ��Ĭ��Ϊtrue
		options.setNoticeBySound(true);
		// �����յ���Ϣ�Ƿ��� Ĭ��Ϊtrue
		options.setNoticedByVibrate(true);
		// ����������Ϣ�����Ƿ�����Ϊ���������� Ĭ��Ϊtrue
		options.setUseSpeaker(true);
		// ����notification��Ϣ���ʱ����ת��intentΪ�Զ����intent
		options.setOnNotificationClickListener(new OnNotificationClickListener() {

			@Override
			public Intent onNotificationClick(EMMessage message) {
				Intent intent = new Intent(instance, ChatActivity.class);
				ChatType chatType = message.getChatType();
				System.out.println("type:::::::::;" + chatType);
				if (chatType == ChatType.Chat) { // ������Ϣ
					intent.putExtra("userId", message.getFrom());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
				} else { // Ⱥ����Ϣ
							// message.getTo()ΪȺ��id
					intent.putExtra("groupId", message.getTo());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
				}
				return intent;
			}
		});
		// ����һ��connectionlistener�����˻��ظ���½
		EMChatManager.getInstance().addConnectionListener(
				new MyConnectionListener());
		// // ȡ��ע�ͣ�app�ں�̨��������Ϣ��ʱ��״̬������Ϣ��ʾ�����Լ�д��
		// options.setNotifyText(new OnMessageNotifyListener() {
		//
		// @Override
		// public String onNewMessageNotify(EMMessage message) {
		// // ���Ը���message��������ʾ��ͬ����(�ɲο�΢�Ż�qq)��demo�򵥵ĸ�����ԭ������ʾ
		// return "��ĺû���" + message.getFrom() + "������һ����ϢŶ";
		// }
		//
		// @Override
		// public String onLatestMessageNotify(EMMessage message, int
		// fromUsersNum, int messageNum) {
		// return fromUsersNum + "�����ѣ�������" + messageNum + "����Ϣ";
		// }
		//
		// @Override
		// public String onSetNotificationTitle(EMMessage message) {
		// //�޸ı���
		// return "����notification";
		// }
		//
		//
		// });

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
