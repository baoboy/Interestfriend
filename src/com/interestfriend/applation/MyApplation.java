package com.interestfriend.applation;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.easemob.EMCallBack;
import com.huanxin.helper.QuYouHXSDKHelper;
import com.interestfriend.data.CircleMember;
import com.interestfriend.utils.CheckImageLoaderConfiguration;
import com.interestfriend.utils.CrashHandler;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.Utils;

public class MyApplation extends Application {
	private static MyApplation instance;
	private static int circle_id;
	private static String circle_group_id = "";
	private static String circle_name = "";
	private static List<Activity> activityList = new ArrayList<Activity>();
	private static double nLatitude = 0;// ά��
	private static double nLontitude = 0;// ����
	private static String address = "";
	public static QuYouHXSDKHelper hxSDKHelper = new QuYouHXSDKHelper();
	private static CircleMember memberSelf = new CircleMember();;

	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;

	@Override
	public void onCreate() {
		super.onCreate();
		CheckImageLoaderConfiguration.checkImageLoaderConfiguration(this);
		instance = this;
		CrashHandler catchHandler = CrashHandler.getInstance();
		catchHandler.init(this);
		initBaiduLocation();
		initHuanxin();
		boolean res = hxSDKHelper.onInit(this);
		initMemberSelf();
		Utils.print("uit::::::::::::::::===" + memberSelf.getUser_id());

	}

	public static void initMemberSelf() {
		memberSelf.setUser_avatar(SharedUtils.getAPPUserAvatar());
		memberSelf.setUser_birthday(SharedUtils.getAPPUserBirthday());
		memberSelf.setUser_declaration(SharedUtils.getAPPUserDeclaration());
		memberSelf.setUser_description(SharedUtils.getAPPUserDescription());
		memberSelf.setUser_gender(SharedUtils.getAPPUserGender());
		memberSelf.setUser_id(SharedUtils.getIntUid());
		memberSelf.setUser_name(SharedUtils.getAPPUserName());
		memberSelf.setUser_register_time(SharedUtils.getAPPUserRegisterTime());
		memberSelf.setSortkey(SharedUtils.getAPPUserSortKey());
		memberSelf.setUser_address(SharedUtils.getAPPUserAddress());
		memberSelf.setUser_province(SharedUtils.getAPPUserProvince());
		memberSelf.setUser_province_key(SharedUtils.getAPPUserProvinceKey());
	}

	public static CircleMember getMemberSelf() {
		return memberSelf;
	}

	public void setMemberSelf(CircleMember memberSelf) {
		this.memberSelf = memberSelf;
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
		/**
		 * this function will initialize the HuanXin SDK
		 * 
		 * @return boolean true if caller can continue to call HuanXin related
		 *         APIs after calling onInit, otherwise false.
		 * 
		 *         ���ų�ʼ��SDK��������
		 *         ����true�����ȷ��ʼ��������false���������Ϊfalse�����ں����ĵ����в�Ҫ�����κκͻ�����صĴ���
		 * 
		 *         for example: ���ӣ�
		 * 
		 *         public class DemoHXSDKHelper extends HXSDKHelper
		 * 
		 *         HXHelper = new DemoHXSDKHelper();
		 *         if(HXHelper.onInit(context)){ // do HuanXin related work }
		 */

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
	public static void logoutHuanXin(final EMCallBack emCallBack) {
		// �ȵ���sdk logout��������app���Լ�������
		hxSDKHelper.logout(emCallBack);

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
