package com.interestfriend.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VideoMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;
import com.interestfriend.R;
import com.interestfriend.activity.BaiduMapActivity;
import com.interestfriend.activity.ImageGridActivity;
import com.interestfriend.adapter.ChatGridViewAdapter;
import com.interestfriend.adapter.ExpressionAdapter;
import com.interestfriend.adapter.ExpressionPagerAdapter;
import com.interestfriend.adapter.GroupChatAdapter;
import com.interestfriend.adapter.GroupChatAdapter.MessageOnLongClick;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.interfaces.VoicePlayClickListener;
import com.interestfriend.popwindow.MessageCopyPopWindow;
import com.interestfriend.popwindow.MessageCopyPopWindow.OnlistOnclick;
import com.interestfriend.utils.CommonUtils;
import com.interestfriend.utils.ImageUtils;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.SmileUtils;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.ExpandGridView;

@SuppressLint("NewApi")
public class CircleGroupChatFragment extends Fragment implements
		OnClickListener, OnItemClickListener, MessageOnLongClick {
	private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
	public static final int REQUEST_CODE_CONTEXT_MENU = 3;
	private static final int REQUEST_CODE_MAP = 4;
	public static final int REQUEST_CODE_TEXT = 5;
	public static final int REQUEST_CODE_VOICE = 6;
	public static final int REQUEST_CODE_PICTURE = 7;
	public static final int REQUEST_CODE_LOCATION = 8;
	public static final int REQUEST_CODE_NET_DISK = 9;
	public static final int REQUEST_CODE_FILE = 10;
	public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
	public static final int REQUEST_CODE_PICK_VIDEO = 12;
	public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
	public static final int REQUEST_CODE_VIDEO = 14;
	public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
	public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
	public static final int REQUEST_CODE_SEND_USER_CARD = 17;
	public static final int REQUEST_CODE_CAMERA = 18;
	public static final int REQUEST_CODE_LOCAL = 19;
	public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
	public static final int REQUEST_CODE_GROUP_DETAIL = 21;
	public static final int REQUEST_CODE_SELECT_VIDEO = 23;
	public static final int REQUEST_CODE_SELECT_FILE = 24;
	public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

	public static final int RESULT_CODE_COPY = 1;
	public static final int RESULT_CODE_DELETE = 2;
	public static final int RESULT_CODE_FORWARD = 3;
	public static final int RESULT_CODE_OPEN = 4;
	public static final int RESULT_CODE_DWONLOAD = 5;
	public static final int RESULT_CODE_TO_CLOUD = 6;
	public static final int RESULT_CODE_EXIT_GROUP = 7;

	public static final int CHATTYPE_SINGLE = 1;
	public static final int CHATTYPE_GROUP = 2;

	public static final String COPY_IMAGE = "EASEMOBIMG";
	private View recordingContainer;
	private ImageView micImage;
	private TextView recordingHint;
	private ListView listView;
	private EditText mEditTextContent;
	private View buttonSetModeKeyboard;
	private View buttonSetModeVoice;
	private Button buttonSend;
	private View buttonPressToSpeak;
	private ViewPager expressionViewpager;
	private RelativeLayout expressionContainer;
	private LinearLayout btnContainer;
	private View more;
	private InputMethodManager manager;
	private List<String> reslist;
	private Drawable[] micImages;
	private EMConversation conversation;
	private NewMessageBroadcastReceiver receiver;
	// ��˭������Ϣ
	private String toChatUsername = "";
	private VoiceRecorder voiceRecorder;
	private GroupChatAdapter adapter;
	private File cameraFile;
	static int resendPos;

	private ImageView iv_emoticons_normal;
	private ImageView iv_emoticons_checked;
	private RelativeLayout edittext_layout;
	private ProgressBar loadmorePB;
	private boolean isloading;
	private final int pagesize = 20;
	private boolean haveMoreData = true;
	private Button btnMore;
	private ClipboardManager clipboard;

	private TextView txt_title;

	private PowerManager.WakeLock wakeLock;
	private GridView mGridView;
	private ImageView back;

	private List<View> views = new ArrayList<View>();
	private List<View> dots = new ArrayList<View>();

	private Handler micImageHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// �л�msg�л�ͼƬ
			micImage.setImageDrawable(micImages[msg.what]);
		}
	};
	private Handler mEmjoiHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.circle_chat, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		setUpView();
	}

	/**
	 * initView
	 */
	protected void initView() {
		back = (ImageView) getView().findViewById(R.id.back);
		back.setOnClickListener(this);
		mGridView = (GridView) getView().findViewById(R.id.m_gridview);
		mGridView.setAdapter(new ChatGridViewAdapter(getActivity(), 1));
		mGridView.setOnItemClickListener(this);
		txt_title = (TextView) getView().findViewById(R.id.title_txt);
		txt_title.setText(MyApplation.getCircle_name());
		recordingContainer = getView().findViewById(R.id.recording_container);
		micImage = (ImageView) getView().findViewById(R.id.mic_image);
		recordingHint = (TextView) getView().findViewById(R.id.recording_hint);
		listView = (ListView) getView().findViewById(R.id.list);
		mEditTextContent = (EditText) getView().findViewById(
				R.id.et_sendmessage);
		buttonSetModeKeyboard = getView().findViewById(
				R.id.btn_set_mode_keyboard);
		edittext_layout = (RelativeLayout) getView().findViewById(
				R.id.edittext_layout);
		buttonSetModeVoice = getView().findViewById(R.id.btn_set_mode_voice);
		buttonSend = (Button) getView().findViewById(R.id.btn_send);
		buttonPressToSpeak = getView().findViewById(R.id.btn_press_to_speak);
		expressionViewpager = (ViewPager) getView().findViewById(R.id.vPager);
		expressionContainer = (RelativeLayout) getView().findViewById(
				R.id.ll_face_container);
		btnContainer = (LinearLayout) getView().findViewById(
				R.id.ll_btn_container);
		iv_emoticons_normal = (ImageView) getView().findViewById(
				R.id.iv_emoticons_normal);
		iv_emoticons_checked = (ImageView) getView().findViewById(
				R.id.iv_emoticons_checked);
		loadmorePB = (ProgressBar) getView().findViewById(R.id.pb_load_more);
		btnMore = (Button) getView().findViewById(R.id.btn_more);
		btnMore.setOnClickListener(this);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		more = getView().findViewById(R.id.more);
		// ������Դ�ļ�,����¼������ʱ
		micImages = new Drawable[] {
				getResources().getDrawable(R.drawable.record_animate_01),
				getResources().getDrawable(R.drawable.record_animate_02),
				getResources().getDrawable(R.drawable.record_animate_03),
				getResources().getDrawable(R.drawable.record_animate_04),
				getResources().getDrawable(R.drawable.record_animate_05),
				getResources().getDrawable(R.drawable.record_animate_06),
				getResources().getDrawable(R.drawable.record_animate_07),
				getResources().getDrawable(R.drawable.record_animate_08),
				getResources().getDrawable(R.drawable.record_animate_09),
				getResources().getDrawable(R.drawable.record_animate_10),
				getResources().getDrawable(R.drawable.record_animate_11),
				getResources().getDrawable(R.drawable.record_animate_12),
				getResources().getDrawable(R.drawable.record_animate_13),
				getResources().getDrawable(R.drawable.record_animate_14), };
		// ����list
		reslist = getExpressionRes(160);
		new Thread() {
			public void run() { // ��ʼ������viewpager
				for (int i = 1; i < 9; i++) {
					views.add(getGridChildView(i));
				}
				mEmjoiHandler.sendEmptyMessage(0);
			}
		}.start();
		edittext_layout.requestFocus();
		voiceRecorder = new VoiceRecorder(micImageHandler);
		initDots();
		setListener();
	}

	private void initDots() {
		View view = (View) getView().findViewById(R.id.dot1);
		dots.add(view);
		view = (View) getView().findViewById(R.id.dot2);
		dots.add(view);
		view = (View) getView().findViewById(R.id.dot3);
		dots.add(view);
		view = (View) getView().findViewById(R.id.dot4);
		dots.add(view);
		view = (View) getView().findViewById(R.id.dot5);
		dots.add(view);
		view = (View) getView().findViewById(R.id.dot6);
		dots.add(view);
		view = (View) getView().findViewById(R.id.dot7);
		dots.add(view);
		view = (View) getView().findViewById(R.id.dot8);
		dots.add(view);
	}

	private void setListener() {
		iv_emoticons_normal.setOnClickListener(this);
		iv_emoticons_checked.setOnClickListener(this);
		buttonSend.setOnClickListener(this);
		buttonSetModeVoice.setOnClickListener(this);
		buttonSetModeKeyboard.setOnClickListener(this);
		buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
		mEditTextContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				more.setVisibility(View.GONE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				expressionContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
			}
		});
		// �������ֿ�
		mEditTextContent.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)) {
					btnMore.setVisibility(View.GONE);
					buttonSend.setVisibility(View.VISIBLE);
				} else {
					btnMore.setVisibility(View.VISIBLE);
					buttonSend.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideKeyboard();
				more.setVisibility(View.GONE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				expressionContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
				return false;
			}
		});
		expressionViewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				for (View view : dots) {
					view.setBackgroundResource(R.drawable.viewpager_normal_circle);
				}
				dots.get(index).setBackgroundResource(
						R.drawable.viewpager_select_circle);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void setUpView() {
		clipboard = (ClipboardManager) getActivity().getSystemService(
				Context.CLIPBOARD_SERVICE);
		manager = (InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		wakeLock = ((PowerManager) getActivity().getSystemService(
				Context.POWER_SERVICE)).newWakeLock(
				PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
		// �жϵ��Ļ���Ⱥ��
		toChatUsername = MyApplation.getCircle_group_id();
		Utils.print("to:::::::::::::::" + toChatUsername);
		conversation = EMChatManager.getInstance().getConversation(
				toChatUsername);
		// �Ѵ˻Ự��δ������Ϊ0
		conversation.resetUnsetMsgCount();
		adapter = new GroupChatAdapter(getActivity(), toChatUsername);
		adapter.setmCallBack(this);
		// ��ʾ��Ϣ
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new ListScrollListener());
		int count = listView.getCount();
		if (count > 0) {
			listView.setSelection(count - 1);
		}
		// ע�������Ϣ�㲥
		receiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		// ���ù㲥�����ȼ������Mainacitivity,���������Ϣ����ʱ��������chatҳ�棬ֱ����ʾ��Ϣ����������ʾ��Ϣδ��
		intentFilter.setPriority(5);
		getActivity().registerReceiver(receiver, intentFilter);

		// ע��һ��ack��ִ��Ϣ��BroadcastReceiver
		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager
				.getInstance().getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(5);
		getActivity().registerReceiver(ackMessageReceiver,
				ackMessageIntentFilter);

		// ע��һ����Ϣ�ʹ��BroadcastReceiver
		IntentFilter deliveryAckMessageIntentFilter = new IntentFilter(
				EMChatManager.getInstance()
						.getDeliveryAckMessageBroadcastAction());
		deliveryAckMessageIntentFilter.setPriority(5);
		getActivity().registerReceiver(deliveryAckMessageReceiver,
				deliveryAckMessageIntentFilter);

	}

	/**
	 * ת����Ϣ
	 * 
	 * @param forward_msg_id
	 */
	protected void forwardMessage(String forward_msg_id) {
		EMMessage forward_msg = EMChatManager.getInstance().getMessage(
				forward_msg_id);
		EMMessage.Type type = forward_msg.getType();
		switch (type) {
		case TXT:
			// ��ȡ��Ϣ���ݣ�������Ϣ
			String content = ((TextMessageBody) forward_msg.getBody())
					.getMessage();
			sendText(content);
			break;
		case IMAGE:
			// ����ͼƬ
			String filePath = ((ImageMessageBody) forward_msg.getBody())
					.getLocalUrl();
			if (filePath != null) {
				File file = new File(filePath);
				if (!file.exists()) {
					// �����ڴ�ͼ��������ͼ
					filePath = ImageUtils.getThumbnailImagePath(filePath);
				}
				sendPicture(filePath);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * onActivityResult
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CODE_EXIT_GROUP) {
			getActivity().setResult(getActivity().RESULT_OK);
			// getActivity().finish();
			return;
		}

		if (resultCode == getActivity().RESULT_OK) { // �����Ϣ
			if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
				// ��ջỰ
				EMChatManager.getInstance().clearConversation(toChatUsername);
				adapter.refresh();
			} else if (requestCode == REQUEST_CODE_CAMERA) { // ������Ƭ
				if (cameraFile != null && cameraFile.exists())
					sendPicture(cameraFile.getAbsolutePath());
			} else if (requestCode == REQUEST_CODE_SELECT_VIDEO) { // ���ͱ���ѡ�����Ƶ

				int duration = data.getIntExtra("dur", 0);
				String videoPath = data.getStringExtra("path");
				File file = new File(PathUtil.getInstance().getImagePath(),
						"thvideo" + System.currentTimeMillis());
				Bitmap bitmap = null;
				FileOutputStream fos = null;
				try {
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
					if (bitmap == null) {
						EMLog.d("chatactivity",
								"problem load video thumbnail bitmap,use default icon");
						bitmap = BitmapFactory.decodeResource(getResources(),
								R.drawable.app_panel_video_icon);
					}
					fos = new FileOutputStream(file);

					bitmap.compress(CompressFormat.JPEG, 100, fos);

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						fos = null;
					}
					if (bitmap != null) {
						bitmap.recycle();
						bitmap = null;
					}

				}
				sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);

			} else if (requestCode == REQUEST_CODE_LOCAL) { // ���ͱ���ͼƬ
				if (data != null) {
					Uri selectedImage = data.getData();
					if (selectedImage != null) {
						sendPicByUri(selectedImage);
					}
				}
			} else if (requestCode == REQUEST_CODE_SELECT_FILE) { // ����ѡ����ļ�
				if (data != null) {
					Uri uri = data.getData();
					if (uri != null) {
						sendFile(uri);
					}
				}

			} else if (requestCode == REQUEST_CODE_MAP) { // ��ͼ
				double latitude = data.getDoubleExtra("latitude", 0);
				double longitude = data.getDoubleExtra("longitude", 0);
				String locationAddress = data.getStringExtra("address");
				if (locationAddress != null && !locationAddress.equals("")) {
					more(more);
					sendLocationMsg(latitude, longitude, "", locationAddress);
				} else {
					Toast.makeText(getActivity(), "�޷���ȡ������λ����Ϣ��", 0).show();
				}
				// �ط���Ϣ
			} else if (requestCode == REQUEST_CODE_TEXT) {
				resendMessage();
			} else if (requestCode == REQUEST_CODE_VOICE) {
				resendMessage();
			} else if (requestCode == REQUEST_CODE_PICTURE) {
				resendMessage();
			} else if (requestCode == REQUEST_CODE_LOCATION) {
				resendMessage();
			} else if (requestCode == REQUEST_CODE_VIDEO
					|| requestCode == REQUEST_CODE_FILE) {
				resendMessage();
			}
		}
	}

	/**
	 * ��Ϣͼ�����¼�
	 * 
	 * @param view
	 */
	@Override
	public void onClick(View view) {

		int id = view.getId();
		if (id == R.id.btn_send) {// ������Ͱ�ť(�����ֺͱ���)
			String s = mEditTextContent.getText().toString();
			sendText(s);
		} else if (id == R.id.iv_emoticons_normal) { // �����ʾ�����
			more.setVisibility(View.VISIBLE);
			iv_emoticons_normal.setVisibility(View.INVISIBLE);
			iv_emoticons_checked.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.GONE);
			expressionContainer.setVisibility(View.VISIBLE);
			hideKeyboard();
		} else if (id == R.id.iv_emoticons_checked) { // ������ر����
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			expressionContainer.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
		} else if (id == R.id.btn_more) {
			more(more);
		} else if (id == R.id.btn_set_mode_voice) {
			setModeVoice(view);
		} else if (id == R.id.btn_set_mode_keyboard) {
			setModeKeyboard(view);
		} else if (id == R.id.back) {
			getActivity().finish();
			Utils.rightOut(getActivity());

		}
	}

	/**
	 * �����ȡͼƬ
	 */
	public void selectPicFromCamera() {
		if (!CommonUtils.isExitsSdcard()) {
			Toast.makeText(getActivity(), "SD�������ڣ���������", 0).show();
			return;
		}

		cameraFile = new File(PathUtil.getInstance().getImagePath(),
				SharedUtils.getHXId() + System.currentTimeMillis() + ".jpg");
		cameraFile.getParentFile().mkdirs();
		startActivityForResult(
				new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
						MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
				REQUEST_CODE_CAMERA);
	}

	/**
	 * ѡ���ļ�
	 */
	private void selectFileFromLocal() {
		Intent intent = null;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("*/*");
			intent.addCategory(Intent.CATEGORY_OPENABLE);

		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
	}

	/**
	 * ��ͼ���ȡͼƬ
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}

	/**
	 * �����ı���Ϣ
	 * 
	 * @param content
	 *            message content
	 * @param isResend
	 *            boolean resend
	 */
	private void sendText(String content) {

		if (content.length() > 0) {
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			message.setChatType(ChatType.GroupChat);
			TextMessageBody txtBody = new TextMessageBody(content);
			// ������Ϣbody
			message.addBody(txtBody);
			// ����Ҫ����˭,�û�username����Ⱥ��groupid
			message.setReceipt(toChatUsername);
			message.setAttribute("user_name", SharedUtils.getAPPUserName());
			message.setAttribute("user_avatar", SharedUtils.getAPPUserAvatar());
			message.setAttribute("user_id", SharedUtils.getIntUid());
			message.setAttribute("circle_name", MyApplation.getCircle_name());

			// ��messgage�ӵ�conversation��
			conversation.addMessage(message);
			// ֪ͨadapter����Ϣ�䶯��adapter����ݼ��������message��ʾ��Ϣ�͵���sdk�ķ��ͷ���
			adapter.refresh();
			listView.setSelection(listView.getCount() - 1);
			mEditTextContent.setText("");

			getActivity().setResult(getActivity().RESULT_OK);

		}
	}

	/**
	 * ��������
	 * 
	 * @param filePath
	 * @param fileName
	 * @param length
	 * @param isResend
	 */
	private void sendVoice(String filePath, String fileName, String length,
			boolean isResend) {
		if (!(new File(filePath).exists())) {
			return;
		}
		try {
			final EMMessage message = EMMessage
					.createSendMessage(EMMessage.Type.VOICE);
			// �����Ⱥ�ģ�����chattype,Ĭ���ǵ���

			message.setChatType(ChatType.GroupChat);
			message.setReceipt(toChatUsername);
			message.setAttribute("user_name", SharedUtils.getAPPUserName());
			message.setAttribute("user_avatar", SharedUtils.getAPPUserAvatar());
			message.setAttribute("user_id", SharedUtils.getIntUid());
			message.setAttribute("circle_name", MyApplation.getCircle_name());

			int len = Integer.parseInt(length);
			VoiceMessageBody body = new VoiceMessageBody(new File(filePath),
					len);
			message.addBody(body);

			conversation.addMessage(message);
			adapter.refresh();
			listView.setSelection(listView.getCount() - 1);
			getActivity().setResult(getActivity().RESULT_OK);
			// send file
			// sendVoiceSub(filePath, fileName, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param filePath
	 */
	private void sendPicture(final String filePath) {
		String to = toChatUsername;
		// create and add image message in view
		final EMMessage message = EMMessage
				.createSendMessage(EMMessage.Type.IMAGE);
		// �����Ⱥ�ģ�����chattype,Ĭ���ǵ���
		message.setChatType(ChatType.GroupChat);
		message.setAttribute("user_name", SharedUtils.getAPPUserName());
		message.setAttribute("user_avatar", SharedUtils.getAPPUserAvatar());
		message.setAttribute("user_id", SharedUtils.getIntUid());
		message.setAttribute("circle_name", MyApplation.getCircle_name());

		message.setReceipt(to);
		ImageMessageBody body = new ImageMessageBody(new File(filePath));
		// Ĭ�ϳ���100k��ͼƬ��ѹ���󷢸��Է����������óɷ���ԭͼ
		// body.setSendOriginalImage(true);
		message.addBody(body);
		conversation.addMessage(message);

		listView.setAdapter(adapter);
		adapter.refresh();
		listView.setSelection(listView.getCount() - 1);
		getActivity().setResult(getActivity().RESULT_OK);
		// more(more);
	}

	/**
	 * ������Ƶ��Ϣ
	 */
	private void sendVideo(final String filePath, final String thumbPath,
			final int length) {
		final File videoFile = new File(filePath);
		if (!videoFile.exists()) {
			return;
		}
		try {
			EMMessage message = EMMessage
					.createSendMessage(EMMessage.Type.VIDEO);
			// �����Ⱥ�ģ�����chattype,Ĭ���ǵ���
			message.setChatType(ChatType.GroupChat);
			String to = toChatUsername;
			message.setAttribute("user_name", SharedUtils.getAPPUserName());
			message.setAttribute("user_avatar", SharedUtils.getAPPUserAvatar());
			message.setAttribute("user_id", SharedUtils.getIntUid());
			message.setAttribute("circle_name", MyApplation.getCircle_name());

			message.setReceipt(to);
			VideoMessageBody body = new VideoMessageBody(videoFile, thumbPath,
					length, videoFile.length());
			message.addBody(body);
			conversation.addMessage(message);
			listView.setAdapter(adapter);
			adapter.refresh();
			listView.setSelection(listView.getCount() - 1);
			getActivity().setResult(getActivity().RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ����ͼ��ͼƬuri����ͼƬ
	 * 
	 * @param selectedImage
	 */
	private void sendPicByUri(Uri selectedImage) {
		// String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().getContentResolver().query(selectedImage,
				null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex("_data");
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			cursor = null;

			if (picturePath == null || picturePath.equals("null")) {
				Toast toast = Toast.makeText(getActivity(), "�Ҳ���ͼƬ",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			sendPicture(picturePath);
		} else {
			File file = new File(selectedImage.getPath());
			if (!file.exists()) {
				Toast toast = Toast.makeText(getActivity(), "�Ҳ���ͼƬ",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;

			}
			sendPicture(file.getAbsolutePath());
		}

	}

	/**
	 * ����λ����Ϣ
	 * 
	 * @param latitude
	 * @param longitude
	 * @param imagePath
	 * @param locationAddress
	 */
	private void sendLocationMsg(double latitude, double longitude,
			String imagePath, String locationAddress) {
		EMMessage message = EMMessage
				.createSendMessage(EMMessage.Type.LOCATION);
		// �����Ⱥ�ģ�����chattype,Ĭ���ǵ���

		message.setChatType(ChatType.GroupChat);
		LocationMessageBody locBody = new LocationMessageBody(locationAddress,
				latitude, longitude);
		message.addBody(locBody);
		message.setReceipt(toChatUsername);
		message.setAttribute("user_name", SharedUtils.getAPPUserName());
		message.setAttribute("user_avatar", SharedUtils.getAPPUserAvatar());
		message.setAttribute("user_id", SharedUtils.getIntUid());
		message.setAttribute("circle_name", MyApplation.getCircle_name());

		conversation.addMessage(message);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		listView.setSelection(listView.getCount() - 1);
		getActivity().setResult(getActivity().RESULT_OK);
	}

	/**
	 * �����ļ�
	 * 
	 * @param uri
	 */
	private void sendFile(Uri uri) {
		String filePath = null;
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = getActivity().getContentResolver().query(uri,
						projection, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					filePath = cursor.getString(column_index);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			filePath = uri.getPath();
		}
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			Toast.makeText(getActivity(), "�ļ�������", 0).show();
			return;
		}
		if (file.length() > 10 * 1024 * 1024) {
			Toast.makeText(getActivity(), "�ļ����ܴ���10M", 0).show();
			return;
		}

		// ����һ���ļ���Ϣ
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
		// �����Ⱥ�ģ�����chattype,Ĭ���ǵ���

		message.setChatType(ChatType.GroupChat);

		message.setReceipt(toChatUsername);
		// add message body
		NormalFileMessageBody body = new NormalFileMessageBody(new File(
				filePath));
		message.addBody(body);
		message.setAttribute("user_name", SharedUtils.getAPPUserName());
		message.setAttribute("user_avatar", SharedUtils.getAPPUserAvatar());
		message.setAttribute("user_id", SharedUtils.getIntUid());
		message.setAttribute("circle_name", MyApplation.getCircle_name());

		conversation.addMessage(message);
		listView.setAdapter(adapter);
		adapter.refresh();
		listView.setSelection(listView.getCount() - 1);
		getActivity().setResult(getActivity().RESULT_OK);
	}

	/**
	 * �ط���Ϣ
	 */
	private void resendMessage() {
		EMMessage msg = null;
		msg = conversation.getMessage(resendPos);
		// msg.setBackSend(true);
		msg.status = EMMessage.Status.CREATE;

		adapter.refresh();
		listView.setSelection(resendPos);
	}

	/**
	 * ��ʾ����ͼ�갴ť
	 * 
	 * @param view
	 */
	public void setModeVoice(View view) {
		hideKeyboard();
		edittext_layout.setVisibility(View.GONE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeKeyboard.setVisibility(View.VISIBLE);
		buttonSend.setVisibility(View.GONE);
		btnMore.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.VISIBLE);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		btnContainer.setVisibility(View.VISIBLE);
		expressionContainer.setVisibility(View.GONE);

	}

	/**
	 * ��ʾ����ͼ��
	 * 
	 * @param view
	 */
	public void setModeKeyboard(View view) {
		// mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener()
		// {
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// if(hasFocus){
		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		// }
		// }
		// });
		edittext_layout.setVisibility(View.VISIBLE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeVoice.setVisibility(View.VISIBLE);
		// mEditTextContent.setVisibility(View.VISIBLE);
		mEditTextContent.requestFocus();
		// buttonSend.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.GONE);
		if (TextUtils.isEmpty(mEditTextContent.getText())) {
			btnMore.setVisibility(View.VISIBLE);
			buttonSend.setVisibility(View.GONE);
		} else {
			btnMore.setVisibility(View.GONE);
			buttonSend.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * �����������¼
	 * 
	 * @param view
	 */
	public void emptyHistory(View view) {
		startActivityForResult(new Intent(getActivity(), AlertDialog.class)
				.putExtra("titleIsCancel", true).putExtra("msg", "�Ƿ�������������¼")
				.putExtra("cancel", true), REQUEST_CODE_EMPTY_HISTORY);
	}

	/**
	 * �������Ⱥ������
	 * 
	 * @param view
	 */
	public void toGroupDetails(View view) {
		// startActivityForResult(
		// (new Intent(this, GroupDetailsActivity.class).putExtra(
		// "groupId", toChatUsername)), REQUEST_CODE_GROUP_DETAIL);
	}

	/**
	 * ��ʾ������ͼ�갴ťҳ
	 * 
	 * @param view
	 */
	public void more(View view) {
		if (more.getVisibility() == View.GONE) {
			System.out.println("more gone");
			hideKeyboard();
			more.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			expressionContainer.setVisibility(View.GONE);
		} else {
			if (expressionContainer.getVisibility() == View.VISIBLE) {
				expressionContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.VISIBLE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
			} else {
				more.setVisibility(View.GONE);
			}

		}

	}

	/**
	 * ������������
	 * 
	 * @param v
	 */
	public void editClick(View v) {
		listView.setSelection(listView.getCount() - 1);
		if (more.getVisibility() == View.VISIBLE) {
			more.setVisibility(View.GONE);
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
		}

	}

	/**
	 * ��Ϣ�㲥������
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String username = intent.getStringExtra("from");
			String msgid = intent.getStringExtra("msgid");
			// �յ�����㲥��ʱ��message�Ѿ���db���ڴ����ˣ�����ͨ��id��ȡmesage����
			EMMessage message = EMChatManager.getInstance().getMessage(msgid);
			// �����Ⱥ����Ϣ����ȡ��group id
			System.out.println("id:::::::::::::::" + message.getChatType());
			if (message.getChatType() == ChatType.GroupChat) {
				username = message.getTo();
			}
			if (!username.equals(toChatUsername)) {
				// ��Ϣ���Ƿ�����ǰ�Ự��return
				return;
			}
			// conversation =
			// EMChatManager.getInstance().getConversation(toChatUsername);
			// ֪ͨadapter������Ϣ������ui
			adapter.refresh();
			listView.setSelection(listView.getCount() - 1);
			// �ǵðѹ㲥���ս��
			abortBroadcast();
		}
	}

	/**
	 * ��Ϣ��ִBroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(from);
			if (conversation != null) {
				// ��message��Ϊ�Ѷ�
				EMMessage msg = conversation.getMessage(msgid);
				if (msg != null) {
					msg.isAcked = true;
				}
			}
			abortBroadcast();
			adapter.notifyDataSetChanged();
		}
	};

	/**
	 * ��Ϣ�ʹ�BroadcastReceiver
	 */
	private BroadcastReceiver deliveryAckMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(from);
			if (conversation != null) {
				// ��message��Ϊ�Ѷ�
				EMMessage msg = conversation.getMessage(msgid);
				if (msg != null) {
					msg.isDelivered = true;
				}
			}
			abortBroadcast();
			adapter.notifyDataSetChanged();
		}
	};

	/**
	 * ��ס˵��listener
	 * 
	 */
	class PressToSpeakListen implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				getActivity().setVisible(false);
				if (!CommonUtils.isExitsSdcard()) {
					Toast.makeText(getActivity(), "����������Ҫsdcard֧�֣�",
							Toast.LENGTH_SHORT).show();
					return false;
				}
				try {
					v.setPressed(true);
					wakeLock.acquire();
					if (VoicePlayClickListener.isPlaying)
						VoicePlayClickListener.currentPlayListener
								.stopPlayVoice();
					recordingContainer.setVisibility(View.VISIBLE);
					recordingHint
							.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
					voiceRecorder.startRecording(null, toChatUsername,
							getActivity());
				} catch (Exception e) {
					e.printStackTrace();
					v.setPressed(false);
					if (wakeLock.isHeld())
						wakeLock.release();
					recordingContainer.setVisibility(View.INVISIBLE);
					Toast.makeText(getActivity(), R.string.recoding_fail,
							Toast.LENGTH_SHORT).show();
					return false;
				}

				return true;
			case MotionEvent.ACTION_MOVE: {
				if (event.getY() < 0) {
					recordingHint
							.setText(getString(R.string.release_to_cancel));
					recordingHint
							.setBackgroundResource(R.drawable.recording_text_hint_bg);
				} else {
					recordingHint
							.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
				}
				return true;
			}
			case MotionEvent.ACTION_UP:
				getActivity().setVisible(true);
				v.setPressed(false);
				recordingContainer.setVisibility(View.INVISIBLE);
				if (wakeLock.isHeld())
					wakeLock.release();
				if (event.getY() < 0) {
					// discard the recorded audio.
					voiceRecorder.discardRecording();

				} else {
					// stop recording and send voice file
					try {
						int length = voiceRecorder.stopRecoding();
						if (length > 0) {
							sendVoice(voiceRecorder.getVoiceFilePath(),
									voiceRecorder
											.getVoiceFileName(toChatUsername),
									Integer.toString(length), false);
						} else {
							Toast.makeText(getActivity(), "¼��ʱ��̫��", 0).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(getActivity(), "����ʧ�ܣ�����������Ƿ�����",
								Toast.LENGTH_SHORT).show();
					}

				}
				return true;
			default:
				return false;
			}
		}
	}

	/**
	 * ��ȡ�����gridview����view
	 * 
	 * @param i
	 * @return
	 */
	private View getGridChildView(int i) {
		View view = View.inflate(getActivity(), R.layout.expression_gridview,
				null);
		ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		List<String> list1 = null;
		if (i == 1) {
			list1 = reslist.subList(0, 20);
			list.addAll(list1);
		} else if (i == 2) {
			list1 = reslist.subList(21, 41);
			list.addAll(list1);
		} else if (i == 3) {
			list1 = reslist.subList(41, 61);
			list.addAll(list1);
		} else if (i == 4) {
			list1 = reslist.subList(61, 81);
			list.addAll(list1);
		} else if (i == 5) {
			list1 = reslist.subList(81, 101);
			list.addAll(list1);
		} else if (i == 6) {
			list1 = reslist.subList(101, 121);
			list.addAll(list1);
		} else if (i == 7) {
			list1 = reslist.subList(121, 141);
			list.addAll(list1);
		} else if (i == 8) {
			list1 = reslist.subList(141, 160);
			list.addAll(list1);
		}
		list.add("delete_expression");
		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(
				getActivity(), 1, list);
		gv.setAdapter(expressionAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String filename = expressionAdapter.getItem(position);
				try {
					// ���������ɼ�ʱ���ſ��������
					// ��ס˵���ɼ��������������
					if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

						if (filename != "delete_expression") { // ����ɾ��������ʾ����
							// �����õķ��䣬���Ի�����ʱ��Ҫ����SmileUtils�����
							Class clz = Class
									.forName("com.interestfriend.utils.SmileUtils");
							Field field = clz.getField(filename);
							mEditTextContent.append(SmileUtils.getSmiledText(
									getActivity(), (String) field.get(null)));
						} else { // ɾ�����ֻ��߱���
							if (!TextUtils.isEmpty(mEditTextContent.getText())) {

								int selectionStart = mEditTextContent
										.getSelectionStart();// ��ȡ����λ��
								if (selectionStart > 0) {
									String body = mEditTextContent.getText()
											.toString();
									String tempStr = body.substring(0,
											selectionStart);
									int i = tempStr.lastIndexOf("[");// ��ȡ���һ�������λ��
									if (i != -1) {
										CharSequence cs = tempStr.substring(i,
												selectionStart);
										if (SmileUtils.containsKey(cs
												.toString()))
											mEditTextContent.getEditableText()
													.delete(i, selectionStart);
										else
											mEditTextContent.getEditableText()
													.delete(selectionStart - 1,
															selectionStart);
									} else {
										mEditTextContent.getEditableText()
												.delete(selectionStart - 1,
														selectionStart);
									}
								}
							}

						}
					}
				} catch (Exception e) {
				}

			}
		});
		return view;
	}

	public List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;

			reslist.add(filename);

		}
		return reslist;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ע���㲥
		try {
			getActivity().unregisterReceiver(receiver);
			receiver = null;
		} catch (Exception e) {
		}
		try {
			getActivity().unregisterReceiver(ackMessageReceiver);
			ackMessageReceiver = null;
			getActivity().unregisterReceiver(deliveryAckMessageReceiver);
			deliveryAckMessageReceiver = null;
		} catch (Exception e) {
		}
	}

	/**
	 * ���������
	 */
	private void hideKeyboard() {
		if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * listview��������listener
	 * 
	 */
	private class ListScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				if (view.getFirstVisiblePosition() == 0 && !isloading
						&& haveMoreData) {
					loadmorePB.setVisibility(View.VISIBLE);
					// sdk��ʼ�����ص������¼Ϊ20��������ʱȥdb���ȡ����
					List<EMMessage> messages;
					try {
						// ��ȡ����messges�����ô˷�����ʱ���db��ȡ��messages
						// sdk���Զ����뵽��conversation��

						messages = conversation.loadMoreGroupMsgFromDB(adapter
								.getItem(0).getMsgId(), pagesize);
					} catch (Exception e1) {
						loadmorePB.setVisibility(View.GONE);
						return;
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
					if (messages.size() != 0) {
						// ˢ��ui
						adapter.notifyDataSetChanged();
						listView.setSelection(messages.size() - 1);
						if (messages.size() != pagesize)
							haveMoreData = false;
					} else {
						haveMoreData = false;
					}
					loadmorePB.setVisibility(View.GONE);
					isloading = false;

				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}

	}

	public String getToChatUsername() {
		return toChatUsername;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		switch (position) {
		case 0:
			selectPicFromLocal(); // ���ͼƬͼ��

			break;
		case 1:
			selectPicFromCamera();// �������ͼ��

			break;
		case 2:
			// �������ͼ��
			Intent intent = new Intent(getActivity(), ImageGridActivity.class);
			startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
			Utils.leftOutRightIn(getActivity());

			break;
		case 3:
			selectFileFromLocal();

			break;
		case 4:
			startActivityForResult(new Intent(getActivity(),
					BaiduMapActivity.class), REQUEST_CODE_MAP);
			break;

		default:
			break;
		}
	}

	private MessageCopyPopWindow pop;

	@Override
	public void onLongClick(final int position_message, View v,
			View v_layoutparent) {
		pop = new MessageCopyPopWindow(getActivity(), v, v_layoutparent,
				new String[] { "������Ϣ", "@TA" });
		pop.setOnlistOnclick(new OnlistOnclick() {

			@SuppressWarnings("deprecation")
			@Override
			public void onclick(int position) {
				EMMessage copyMsg = ((EMMessage) adapter
						.getItem(position_message));
				switch (position) {
				case 0:
					clipboard.setText(((TextMessageBody) copyMsg.getBody())
							.getMessage());

					break;
				case 1:
					String user_name;
					try {
						user_name = copyMsg.getStringAttribute("user_name");
						mEditTextContent.setText(Html
								.fromHtml("<font color=#37b669>@" + user_name
										+ "</font> "));
						mEditTextContent.setSelection(mEditTextContent
								.getText().toString().length());
					} catch (EaseMobException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		});
		pop.show();
	}
}
