/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.interestfriend.activity;

import java.text.SimpleDateFormat;
import java.util.UUID;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMCallStateChangeListener;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EMServiceNotReadyException;
import com.interestfriend.R;
import com.interestfriend.utils.Constants;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.view.RoundAngleImageView;

/**
 * ����ͨ��ҳ��
 * 
 */
public class VoiceCallActivity extends BaseActivity implements OnClickListener {
	private LinearLayout comingBtnContainer;
	private Button hangupBtn;
	private Button refuseBtn;
	private Button answerBtn;
	private ImageView muteImage;
	private ImageView handsFreeImage;

	private boolean isMuteState;
	private boolean isHandsfreeState;
	private boolean isInComingCall;
	private TextView callStateTextView;
	private SoundPool soundPool;
	private int streamID;
	private boolean endCallTriggerByMe = false;
	private Handler handler = new Handler();
	private Ringtone ringtone;
	private int outgoing;
	private TextView nickTextView;
	private TextView durationTextView;
	private SimpleDateFormat dateFormat;
	private WindowManager windowManager;
	private AudioManager audioManager;
	private Chronometer chronometer;

	private String callDruationText;
	private String username;
	private CallingState callingState = CallingState.CANCED;
	String msgid;
	private boolean isAnswered;
	private LinearLayout voiceContronlLayout;
	private RoundAngleImageView user_avatar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_call);
		user_avatar = (RoundAngleImageView) findViewById(R.id.user_avatar);
		comingBtnContainer = (LinearLayout) findViewById(R.id.ll_coming_call);
		refuseBtn = (Button) findViewById(R.id.btn_refuse_call);
		answerBtn = (Button) findViewById(R.id.btn_answer_call);
		hangupBtn = (Button) findViewById(R.id.btn_hangup_call);
		muteImage = (ImageView) findViewById(R.id.iv_mute);
		handsFreeImage = (ImageView) findViewById(R.id.iv_handsfree);
		callStateTextView = (TextView) findViewById(R.id.tv_call_state);
		nickTextView = (TextView) findViewById(R.id.tv_nick);
		durationTextView = (TextView) findViewById(R.id.tv_calling_duration);
		chronometer = (Chronometer) findViewById(R.id.chronometer);
		voiceContronlLayout = (LinearLayout) findViewById(R.id.ll_voice_control);
		nickTextView.setText(getIntent().getStringExtra("user_name"));
		UniversalImageLoadTool.disPlay(getIntent()
				.getStringExtra("user_avatar"), user_avatar,
				R.drawable.picture_default_head);
		;
		refuseBtn.setOnClickListener(this);
		answerBtn.setOnClickListener(this);
		hangupBtn.setOnClickListener(this);
		muteImage.setOnClickListener(this);
		handsFreeImage.setOnClickListener(this);

		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		audioManager = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setMicrophoneMute(false);

		// ע�������绰��״̬�ļ���
		addCallStateListener();
		msgid = UUID.randomUUID().toString();

		username = getIntent().getStringExtra("username");
		// �����绰�Ƿ�Ϊ���յ�
		isInComingCall = getIntent().getBooleanExtra("isComingCall", false);

		// ����ͨ����
		nickTextView.setText(username);
		if (!isInComingCall) {// ����绰
			soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
			outgoing = soundPool.load(this, R.raw.outgoing, 1);

			comingBtnContainer.setVisibility(View.INVISIBLE);
			hangupBtn.setVisibility(View.VISIBLE);
			callStateTextView.setText("���ں���...");
			handler.postDelayed(new Runnable() {
				public void run() {
					streamID = playMakeCallSounds();
				}
			}, 300);
			try {
				// ���������绰
				EMChatManager.getInstance().makeVoiceCall(username);
			} catch (EMServiceNotReadyException e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(VoiceCallActivity.this, "��δ������������", 0);
					}
				});
			}
		} else { // �е绰����
			voiceContronlLayout.setVisibility(View.INVISIBLE);
			Uri ringUri = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			audioManager.setMode(AudioManager.MODE_RINGTONE);
			audioManager.setSpeakerphoneOn(true);
			ringtone = RingtoneManager.getRingtone(this, ringUri);
			ringtone.play();
		}
	}

	/**
	 * ���õ绰����
	 */
	void addCallStateListener() {
		EMChatManager.getInstance().addVoiceCallStateChangeListener(
				new EMCallStateChangeListener() {

					@Override
					public void onCallStateChanged(CallState callState,
							CallError error) {
						// Message msg = handler.obtainMessage();
						switch (callState) {

						case CONNECTING: // �������ӶԷ�
							VoiceCallActivity.this
									.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											callStateTextView
													.setText("�������ӶԷ�...");
										}

									});
							break;
						case CONNECTED: // ˫���Ѿ���������
							VoiceCallActivity.this
									.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											callStateTextView
													.setText("�Ѿ��ͶԷ��������ӣ��ȴ��Է�����...");
										}

									});
							break;

						case ACCEPTED: // �绰��ͨ�ɹ�
							VoiceCallActivity.this
									.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											try {
												if (soundPool != null)
													soundPool.stop(streamID);
											} catch (Exception e) {
											}
											closeSpeakerOn();
											chronometer
													.setVisibility(View.VISIBLE);
											chronometer.setBase(SystemClock
													.elapsedRealtime());
											// ��ʼ��ʱ
											chronometer.start();
											callStateTextView.setText("ͨ����...");
											callingState = CallingState.NORMAL;
										}

									});
							break;
						case DISCONNNECTED: // �绰����
							final CallError fError = error;
							VoiceCallActivity.this
									.runOnUiThread(new Runnable() {
										private void postDelayedCloseMsg() {
											handler.postDelayed(new Runnable() {

												@Override
												public void run() {
													saveCallRecord();
													Animation animation = new AlphaAnimation(
															1.0f, 0.0f);
													animation.setDuration(800);
													findViewById(
															R.id.root_layout)
															.startAnimation(
																	animation);
													finish();
												}

											}, 200);
										}

										@Override
										public void run() {
											chronometer.stop();
											callDruationText = chronometer
													.getText().toString();

											if (fError == CallError.REJECTED) {
												callingState = CallingState.BEREFUESD;
												callStateTextView
														.setText("�Է��ܾ����ܣ�...");
											} else if (fError == CallError.ERROR_TRANSPORT) {
												callStateTextView
														.setText("���ӽ���ʧ�ܣ�...");
											} else if (fError == CallError.ERROR_INAVAILABLE) {
												callingState = CallingState.OFFLINE;
												callStateTextView
														.setText("�Է������ߣ����Ժ��ٲ�...");
											} else if (fError == CallError.ERROR_BUSY) {
												callingState = CallingState.BUSY;
												callStateTextView
														.setText("�Է�����ͨ���У����Ժ��ٲ�");
											} else if (fError == CallError.ERROR_NORESPONSE) {
												callingState = CallingState.NORESPONSE;
												callStateTextView
														.setText("�Է�δ����");
											} else {
												if (isAnswered) {
													callingState = CallingState.NORMAL;
													if (endCallTriggerByMe) {
														callStateTextView
																.setText("�Ҷ�...");
													} else {
														callStateTextView
																.setText("�Է��Ѿ��Ҷ�...");
													}
												} else {
													if (isInComingCall) {
														callingState = CallingState.UNANSWERED;
														callStateTextView
																.setText("δ����");
													} else {
														callingState = CallingState.CANCED;
														callStateTextView
																.setText("��ȡ��");
													}
												}
											}
											postDelayedCloseMsg();
										}

									});

							break;

						default:
							break;
						}

					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_refuse_call: // �ܾ�����
			if (ringtone != null)
				ringtone.stop();
			try {
				EMChatManager.getInstance().rejectCall();
			} catch (Exception e1) {
				e1.printStackTrace();
				saveCallRecord();
				finish();
			}
			callingState = CallingState.REFUESD;
			break;

		case R.id.btn_answer_call: // �����绰
			comingBtnContainer.setVisibility(View.INVISIBLE);
			hangupBtn.setVisibility(View.VISIBLE);
			voiceContronlLayout.setVisibility(View.VISIBLE);
			if (ringtone != null)
				ringtone.stop();
			closeSpeakerOn();
			if (isInComingCall) {
				try {
					isAnswered = true;
					EMChatManager.getInstance().answerCall();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					saveCallRecord();
					finish();
				}
			}
			break;

		case R.id.btn_hangup_call: // �Ҷϵ绰
			if (soundPool != null)
				soundPool.stop(streamID);
			endCallTriggerByMe = true;
			try {
				EMChatManager.getInstance().endCall();
			} catch (Exception e) {
				e.printStackTrace();
				saveCallRecord();
				finish();
			}
			break;

		case R.id.iv_mute: // ��������
			if (isMuteState) {
				// �رվ���
				muteImage.setImageResource(R.drawable.icon_mute_normal);
				audioManager.setMicrophoneMute(false);
				isMuteState = false;
			} else {
				// �򿪾���
				muteImage.setImageResource(R.drawable.icon_mute_on);
				audioManager.setMicrophoneMute(true);
				isMuteState = true;
			}
			break;
		case R.id.iv_handsfree: // ���Ὺ��
			if (isHandsfreeState) {
				// �ر�����
				handsFreeImage.setImageResource(R.drawable.icon_speaker_normal);
				closeSpeakerOn();
				isHandsfreeState = false;
			} else {
				handsFreeImage.setImageResource(R.drawable.icon_speaker_on);
				openSpeakerOn();
				isHandsfreeState = true;
			}
			break;
		default:
			break;
		}
	}

	/**
	 * ���Ų�������
	 * 
	 * @param sound
	 * @param number
	 */
	private int playMakeCallSounds() {
		try {
			// �������
			float audioMaxVolumn = audioManager
					.getStreamMaxVolume(AudioManager.STREAM_RING);
			// ��ǰ����
			float audioCurrentVolumn = audioManager
					.getStreamVolume(AudioManager.STREAM_RING);
			float volumnRatio = audioCurrentVolumn / audioMaxVolumn;

			audioManager.setMode(AudioManager.MODE_RINGTONE);
			audioManager.setSpeakerphoneOn(false);

			// ����
			int id = soundPool.play(outgoing, // ������Դ
					volumnRatio, // ������
					volumnRatio, // ������
					1, // ���ȼ���0���
					-1, // ѭ��������0�ǲ�ѭ����-1����Զѭ��
					1); // �ط��ٶȣ�0.5-2.0֮�䡣1Ϊ�����ٶ�
			return id;
		} catch (Exception e) {
			return -1;
		}
	}

	@Override
	protected void onDestroy() {
		if (soundPool != null)
			soundPool.release();
		if (ringtone != null && ringtone.isPlaying())
			ringtone.stop();
		audioManager.setMode(AudioManager.MODE_NORMAL);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		EMChatManager.getInstance().endCall();
		callDruationText = chronometer.getText().toString();
		saveCallRecord();
		finish();
	}

	// ��������
	public void openSpeakerOn() {
		try {
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

			if (!audioManager.isSpeakerphoneOn())
				audioManager.setSpeakerphoneOn(true);
			audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
			// audioManager.setMode(AudioManager.MODE_IN_CALL);
			// audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
			// audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL ),
			// AudioManager.STREAM_VOICE_CALL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �ر�������
	public void closeSpeakerOn() {

		try {
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			if (audioManager != null) {
				if (audioManager.isSpeakerphoneOn())
					audioManager.setSpeakerphoneOn(false);
				audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
				// audioManager.setMode(AudioManager.MODE_IN_CALL);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����ͨ����Ϣ��¼
	 */
	private void saveCallRecord() {
		EMMessage message = null;
		TextMessageBody txtBody = null;
		if (!isInComingCall) { // ���ȥ��ͨ��
			message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			message.setReceipt(username);
			message.setAttribute("user_name", SharedUtils.getAPPUserName());
			message.setAttribute("user_avatar", SharedUtils.getAPPUserAvatar());
		} else {
			message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
			message.setFrom(username);
			message.setAttribute("user_name",
					getIntent().getStringExtra("user_name"));
			message.setAttribute("user_avatar",
					getIntent().getStringExtra("user_avatar"));
		}

		switch (callingState) {
		case NORMAL:
			txtBody = new TextMessageBody("ͨ��ʱ�� " + callDruationText);
			break;
		case REFUESD:
			txtBody = new TextMessageBody("�Ѿܾ�");
			break;
		case BEREFUESD:
			txtBody = new TextMessageBody("�Է��Ѿܾ�");
			break;
		case OFFLINE:
			txtBody = new TextMessageBody("�Է�������");
			break;
		case BUSY:
			txtBody = new TextMessageBody("�Է�����ͨ����");
			break;
		case NORESPONSE:
			txtBody = new TextMessageBody("�Է�δ����");
			break;
		case UNANSWERED:
			txtBody = new TextMessageBody("δ����");
			break;
		default:
			txtBody = new TextMessageBody("��ȡ��");
			break;
		}
		// ������չ����
		message.setAttribute(Constants.MESSAGE_ATTR_IS_VOICE_CALL, true);

		// ������Ϣbody
		message.addBody(txtBody);
		message.setMsgId(msgid);

		// ����
		EMChatManager.getInstance().saveMessage(message, false);
	}

	enum CallingState {
		CANCED, NORMAL, REFUESD, BEREFUESD, UNANSWERED, OFFLINE, NORESPONSE, BUSY
	}
}
