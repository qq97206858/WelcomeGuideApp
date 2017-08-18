package com.example.fqzhang.myapplication.Util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.Build.VERSION;
import android.os.Debug.MemoryInfo;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Created by fqzhang on 2017/8/18.
 */
public class DeviceUtil {
    private static final int kSystemRootStateUnknow = -1;
    private static final int kSystemRootStateDisable = 0;
    private static final int kSystemRootStateEnable = 1;
    private static int systemRootState = -1;
    private static Boolean isEmulatorDevice;
    private static int windowWidth;
    private static int windowHeight;
    private static String mobileUUID = "";
    private static String macAddress = "";
    private static String androidID = "";
    private static final int BLUETOOTH_OFF = 0;
    private static int mStatusBarHeight = 0;

    public DeviceUtil() {
    }

    public static String getMacAddress() {
        if(!StringUtil.emptyOrNull(macAddress)) {
            return macAddress;
        } else {
            String macAddr = "";
            WifiManager wifiManager = (WifiManager)FoundationContextHolder.context.getSystemService("wifi");
            if(wifiManager != null) {
                WifiInfo interfaces = wifiManager.getConnectionInfo();
                if(interfaces != null && interfaces.getMacAddress() != null) {
                    macAddr = interfaces.getMacAddress().replace(":", "");
                }
            }

            if(StringUtil.emptyOrNull(macAddr) || macAddr.equalsIgnoreCase("020000000000")) {
                try {
                    Enumeration var11 = NetworkInterface.getNetworkInterfaces();

                    label65:
                    while(true) {
                        NetworkInterface iF;
                        byte[] addr;
                        do {
                            do {
                                if(!var11.hasMoreElements()) {
                                    break label65;
                                }

                                iF = (NetworkInterface)var11.nextElement();
                                addr = iF.getHardwareAddress();
                            } while(addr == null);
                        } while(addr.length == 0);

                        StringBuilder buf = new StringBuilder();
                        byte[] mac = addr;
                        int var7 = addr.length;

                        for(int var8 = 0; var8 < var7; ++var8) {
                            byte b = mac[var8];
                            buf.append(String.format("%02X:", new Object[]{Byte.valueOf(b)}));
                        }

                        if(buf.length() > 0) {
                            buf.deleteCharAt(buf.length() - 1);
                        }

                        String var12 = buf.toString();
                        if(iF.getName().startsWith("wlan0")) {
                            macAddr = var12.replace(":", "");
                            break;
                        }

                        if(iF.getName().startsWith("eth0")) {
                            macAddr = var12.replace(":", "");
                            break;
                        }
                    }
                } catch (SocketException var10) {
                    ;
                }
            }

            if(!StringUtil.emptyOrNull(macAddr) && macAddr.contains("000000000000")) {
                macAddr = getMac();
                if(!StringUtil.emptyOrNull(macAddr)) {
                    macAddr.replace(":", "");
                }
            }

            macAddr = StringUtil.getUnNullString(macAddr);
            macAddress = macAddr.toUpperCase();
            return macAddr;
        }
    }

    public static String getMac() {
        String macSerial = "";
        String str = "";

        try {
            Process var5 = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(var5.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            while(null != str) {
                str = input.readLine();
                if(str != null) {
                    macSerial = str.trim();
                    break;
                }
            }
        } catch (Throwable var51) {
            var51.printStackTrace();
        }

        return macSerial;
    }

    public static String getTelePhoneIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager)FoundationContextHolder.context.getSystemService("phone");
        if(telephonyManager != null) {
            try {
                return StringUtil.getUnNullString(telephonyManager.getDeviceId());
            } catch (Exception var2) {
                ;
            }
        }

        return "";
    }

    public static String getTelePhoneIMSI() {
        TelephonyManager telephony = (TelephonyManager)FoundationContextHolder.context.getSystemService("phone");
        if(telephony != null) {
            try {
                return StringUtil.getUnNullString(telephony.getSubscriberId());
            } catch (SecurityException var2) {
                ;
            }
        }

        return "";
    }

    public static String getAndroidID() {
        if(!StringUtil.emptyOrNull(androidID)) {
            return androidID;
        } else {
            try {
                String androidId = Secure.getString(FoundationContextHolder.context.getContentResolver(), "android_id");
                androidID = androidId;
                return androidId;
            } catch (Exception var1) {
                return "";
            }
        }
    }

    public static String getSerialNum() {
        String serialNum = "";

        try {
            Class e = Class.forName("android.os.SystemProperties");
            Method getMethod = e.getMethod("get", new Class[]{String.class, String.class});
            Object obj = getMethod.invoke(e, new Object[]{"ro.serialno", "unknown"});
            if(obj != null && obj instanceof String) {
                serialNum = (String)obj;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return serialNum;
    }

    public static String getRomVersion() {
        String miuiVersion = checkAndGetMIUIVersion();
        if(!StringUtil.emptyOrNull(miuiVersion)) {
            return miuiVersion;
        } else {
            String emuiVersion = checkAndGetEmuiVesion();
            return !StringUtil.emptyOrNull(emuiVersion)?emuiVersion:VERSION.INCREMENTAL;
        }
    }

    public static String checkAndGetMIUIVersion() {
        return !StringUtil.emptyOrNull(getSystemProperty("ro.miui.ui.version.name"))?"MIUI_" + VERSION.INCREMENTAL:null;
    }

    public static String checkAndGetEmuiVesion() {
        String emuiVersion = getSystemProperty("ro.build.version.emui");
        return !StringUtil.emptyOrNull(emuiVersion)?emuiVersion:null;
    }

    public static boolean isRoot() {
        if(systemRootState == 1) {
            return true;
        } else if(systemRootState == 0) {
            return false;
        } else {
            File f = null;
            String[] kSuSearchPaths = new String[]{"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};

            try {
                for(int i = 0; i < kSuSearchPaths.length; ++i) {
                    f = new File(kSuSearchPaths[i] + "su");
                    if(f != null && f.exists()) {
                        systemRootState = 1;
                        return true;
                    }
                }
            } catch (Exception var3) {
                ;
            }

            systemRootState = 0;
            return false;
        }
    }

    public static void setScreenBrightness(Activity activity, float brightNess) {
        if(activity != null) {
            LayoutParams layoutParams = activity.getWindow().getAttributes();
            layoutParams.screenBrightness = brightNess;
            activity.getWindow().setAttributes(layoutParams);
        }

    }

    public static float getScreenBrightness(Activity activity) {
        float value = 0.0F;
        if(activity != null) {
            LayoutParams cr = activity.getWindow().getAttributes();
            value = cr.screenBrightness;
        }

        if(value <= 0.0F) {
            ContentResolver cr1 = activity.getContentResolver();

            try {
                value = (float)System.getInt(cr1, "screen_brightness") / 255.0F;
            } catch (SettingNotFoundException var4) {
                value = 0.6F;
            }
        }

        return value;
    }

    public static double getRunningMemory(Context context) {
        ActivityManager mActivityManager = (ActivityManager)context.getSystemService("activity");
        List runningAppProcessesList = mActivityManager.getRunningAppProcesses();
        double mDirty = 0.0D;
        Iterator var5 = runningAppProcessesList.iterator();

        while(var5.hasNext()) {
            RunningAppProcessInfo runningAppProcessInfo = (RunningAppProcessInfo)var5.next();
            String processName = runningAppProcessInfo.processName;
            if(processName.equalsIgnoreCase("ctrip.android.view")) {
                int pid = runningAppProcessInfo.pid;
                int uid = runningAppProcessInfo.uid;
                int[] pids = new int[]{pid};
                MemoryInfo[] memoryInfo = mActivityManager.getProcessMemoryInfo(pids);
                int memorySize = memoryInfo[0].dalvikPrivateDirty;
                double memorySizeFl = (double)memorySize / 1024.0D;
                int heapSize = memoryInfo[0].dalvikPss;
                double heapSizeDo = (double)heapSize / 1024.0D;
                LogUtil.d("QQ", "dalvikPrivateDirty=" + memorySizeFl + "MB, heapSize=" + heapSizeDo + "MB");
                LogUtil.d("QQ", "nativePrivateDirty=" + (double)memoryInfo[0].nativePrivateDirty / 1024.0D + "MB, heapSize=" + (double)memoryInfo[0].nativePss / 1024.0D + "MB");
                LogUtil.d("QQ", "otherPrivateDirty=" + (double)memoryInfo[0].otherPrivateDirty / 1024.0D + "MB, heapSize=" + (double)memoryInfo[0].otherPss / 1024.0D + "MB");
                mDirty = (double)memoryInfo[0].otherPrivateDirty / 1024.0D;
                break;
            }
        }

        return mDirty;
    }

    public static String getAvailMemory(Context context) {
        ArrayList infos = new ArrayList();
        ActivityManager am = (ActivityManager)context.getSystemService("activity");
        android.app.ActivityManager.MemoryInfo mi = new android.app.ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        infos.add(Formatter.formatFileSize(context, mi.availMem));
        if(VERSION.SDK_INT >= 16) {
            infos.add(Formatter.formatFileSize(context, mi.totalMem));
        }

        infos.add(String.valueOf(mi.lowMemory));
        return infos.toString();
    }

    public static String getDeviceModel() {
        return Build.MODEL == null?"":Build.MODEL;
    }

    public static String getDeviceBrand() {
        return Build.BRAND == null?"":Build.BRAND;
    }

    public static boolean isNubia() {
        return "nubia".equalsIgnoreCase(getDeviceBrand());
    }

    public static int getSDKVersionInt() {
        return VERSION.SDK_INT;
    }

    public static boolean getAnimationSetting(Context context) {
        ContentResolver cv = context.getContentResolver();
        String animation = System.getString(cv, "transition_animation_scale");
        return StringUtil.toDouble(animation) > 0.0D;
    }

    public static int[] getScreenSize(DisplayMetrics dm) {
        int[] result = new int[]{dm.widthPixels, dm.heightPixels};
        return result;
    }

    public static int getPixelFromDip(DisplayMetrics dm, float dip) {
        return (int)(TypedValue.applyDimension(1, dip, dm) + 0.5F);
    }

    public static int getPixelFromDip(float dip) {
        return (int)(TypedValue.applyDimension(1, dip, FoundationContextHolder.context.getResources().getDisplayMetrics()) + 0.5F);
    }

    public static boolean isARMCPU() {
        String cpu = Build.CPU_ABI;
        return cpu != null && cpu.toLowerCase().contains("arm");
    }

    public static String getSystemProperty(String propName) {
        String line = null;
        BufferedReader input = null;

        Object var4;
        try {
            Process e = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(e.getInputStream()), 1024);
            line = input.readLine();
            input.close();
            return line;
        } catch (Exception var14) {
            var14.printStackTrace();
            var4 = line;
        } finally {
            if(input != null) {
                try {
                    input.close();
                } catch (Exception var13) {
                    var13.printStackTrace();
                }
            }

        }

        return (String)var4;
    }

    public static float getDesity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    public static boolean isAppInstalled(Context context, String pkgName) {
        if(context == null) {
            return false;
        } else {
            try {
                context.getPackageManager().getPackageInfo(pkgName, 0);
                return true;
            } catch (NameNotFoundException var3) {
                return false;
            }
        }
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        if(context != null && intent != null) {
            PackageManager pkgManager = context.getPackageManager();
            if(pkgManager == null) {
                return false;
            } else {
                List list = pkgManager.queryIntentActivities(intent, 65536);
                return list.size() > 0;
            }
        } else {
            return false;
        }
    }

    public static boolean isEmulator() {
        if(isEmulatorDevice == null) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager)FoundationContextHolder.context.getSystemService("phone");
                String imei = telephonyManager.getDeviceId();
                if((imei == null || imei.equals("000000000000000")) && StringUtil.emptyOrNull(getTelePhoneIMSI())) {
                    isEmulatorDevice = Boolean.valueOf(true);
                    return isEmulatorDevice.booleanValue();
                }

                isEmulatorDevice = Boolean.valueOf(Build.MODEL.equals("sdk") || Build.MODEL.equals("google_sdk") || Build.BRAND.equals("generic") || Build.MANUFACTURER.contains("Genymotion") || Build.PRODUCT.contains("vbox"));
                return isEmulatorDevice.booleanValue();
            } catch (Exception var2) {
                isEmulatorDevice = Boolean.valueOf(false);
            }
        }

        return isEmulatorDevice.booleanValue();
    }

    public static boolean isBluetoothPersistedStateOn() {
        try {
            return Secure.getInt(FoundationContextHolder.context.getContentResolver(), "bluetooth_on", 0) != 0;
        } catch (Exception var1) {
            return false;
        }
    }

    public static String getMobileUUID() {
        if(!StringUtil.emptyOrNull(mobileUUID)) {
            return mobileUUID;
        } else {
            String uuid = "";

            try {
                WifiManager e = (WifiManager)FoundationContextHolder.context.getSystemService("wifi");
                if(e != null) {
                    WifiInfo imei = e.getConnectionInfo();
                    if(imei != null && imei.getMacAddress() != null) {
                        uuid = imei.getMacAddress().replace(":", "");
                    }
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            try {
                TelephonyManager e1 = (TelephonyManager)FoundationContextHolder.context.getSystemService("phone");
                String imei1 = e1.getDeviceId();
                uuid = uuid + imei1;
            } catch (SecurityException var3) {
                var3.printStackTrace();
            }

            if(uuid != null && uuid.length() > 64) {
                uuid = uuid.substring(0, 64);
            }

            mobileUUID = uuid;
            return uuid;
        }
    }

    public static boolean isAlwaysBedestroy() {
        int i = System.getInt(FoundationContextHolder.context.getContentResolver(), "always_finish_activities", 0);
        return i == 1;
    }

    public static boolean isDontKeepActivities(Application sAppInstance) {
        int setting = System.getInt(sAppInstance.getContentResolver(), "always_finish_activities", 0);
        return setting == 1;
    }

    public static int getWindowWidth() {
        return windowWidth;
    }

    public static void setWindowWidth(int windowWidth) {
        windowWidth = windowWidth;
    }

    public static int getWindowHeight() {
        return windowHeight;
    }

    public static void setWindowHeight(int windowHeight) {
        windowHeight = windowHeight;
    }

    public static boolean isSDCardAvailaleSize() {
        try {
            String ex = Environment.getExternalStorageState();
            if("mounted".equals(ex)) {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = (long)stat.getBlockSize();
                long availableBlocks = (long)stat.getAvailableBlocks();
                return availableBlocks * blockSize > 31457280L;
            } else {
                return false;
            }
        } catch (Exception var7) {
            return false;
        }
    }

    public static String getProductName() {
        String product = Build.PRODUCT;
        return product == null?"":product;
    }

    public static boolean isARMCPU(Context context) {
        return !isX86CPU(context);
    }

    public static boolean isX86CPU(Context context) {
        boolean isX86 = false;
        String osArch = java.lang.System.getProperty("os.arch");
        String abi1 = get(context, "ro.product.cpu.abi");
        String vendor = get(context, "ro.cpu.vendor");
        if(!TextUtils.isEmpty(abi1) && abi1.toLowerCase().startsWith("x86") || "intel".equalsIgnoreCase(vendor) || "i686".equalsIgnoreCase(osArch)) {
            isX86 = true;
        }

        return isX86;
    }

    public static String get(Context context, String key) {
        String ret = "";

        try {
            ClassLoader ex = context.getClassLoader();
            Class SystemProperties = ex.loadClass("android.os.SystemProperties");
            Class[] paramTypes = new Class[]{String.class};
            Method get = SystemProperties.getMethod("get", paramTypes);
            Object[] params = new Object[]{new String(key)};
            ret = (String)get.invoke(SystemProperties, params);
        } catch (Throwable var8) {
            var8.printStackTrace();
        }

        return ret;
    }

    public static int getStatusBarHeight(Context context) {
        if(mStatusBarHeight != 0) {
            return mStatusBarHeight;
        } else {
            try {
                Class e1 = Class.forName("com.android.internal.R$dimen");
                Object obj = e1.newInstance();
                Field field = e1.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                mStatusBarHeight = context.getResources().getDimensionPixelSize(x);
            } catch (Exception var5) {
                var5.printStackTrace();
            }

            return mStatusBarHeight;
        }
    }

    public static String getIpAddress() {
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();

            while(e.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface)e.nextElement();
                Enumeration enumIpAddr = intf.getInetAddresses();

                while(enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                    if(!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return null;
    }

    public static int getScreenWidth() {
        Display display = ((WindowManager)FoundationContextHolder.context.getSystemService("window")).getDefaultDisplay();
        return display.getWidth();
    }

    public static int getScreenHeight() {
        Display display = ((WindowManager)FoundationContextHolder.context.getSystemService("window")).getDefaultDisplay();
        return display.getHeight();
    }

    public static long getSDTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize;
        if(VERSION.SDK_INT >= 18) {
            blockSize = stat.getBlockSizeLong();
        } else {
            blockSize = (long)stat.getBlockSize();
        }

        long totalBlocks;
        if(VERSION.SDK_INT >= 18) {
            totalBlocks = stat.getBlockCountLong();
        } else {
            totalBlocks = (long)stat.getBlockCount();
        }

        return blockSize * totalBlocks;
    }

    public static long getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize;
        if(VERSION.SDK_INT >= 18) {
            blockSize = stat.getBlockSizeLong();
        } else {
            blockSize = (long)stat.getBlockSize();
        }

        long availableBlocks;
        if(VERSION.SDK_INT >= 18) {
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            availableBlocks = (long)stat.getAvailableBlocks();
        }

        return blockSize * availableBlocks;
    }

    public static long getDiskTotalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize;
        if(VERSION.SDK_INT >= 18) {
            blockSize = stat.getBlockSizeLong();
        } else {
            blockSize = (long)stat.getBlockSize();
        }

        long totalBlocks;
        if(VERSION.SDK_INT >= 18) {
            totalBlocks = stat.getBlockCountLong();
        } else {
            totalBlocks = (long)stat.getBlockCount();
        }

        return blockSize * totalBlocks;
    }

    public static long getDiskAvailableSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize;
        if(VERSION.SDK_INT >= 18) {
            blockSize = stat.getBlockSizeLong();
        } else {
            blockSize = (long)stat.getBlockSize();
        }

        long availableBlocks;
        if(VERSION.SDK_INT >= 18) {
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            availableBlocks = (long)stat.getAvailableBlocks();
        }

        return blockSize * availableBlocks;
    }

    public static long getTotalMemorySize() {
        String dir = "/proc/meminfo";

        try {
            FileReader e = new FileReader(dir);
            BufferedReader br = new BufferedReader(e, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            return (long)Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) * 1024L;
        } catch (IOException var5) {
            var5.printStackTrace();
            return 0L;
        }
    }

    public static long getAvailableMemory() {
        ActivityManager am = (ActivityManager)FoundationContextHolder.context.getSystemService("activity");
        android.app.ActivityManager.MemoryInfo memoryInfo = new android.app.ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static String getUserAgent() {
        String userAgent = "";
        StringBuffer sb = new StringBuffer();

        try {
            if(VERSION.SDK_INT >= 17) {
                try {
                    userAgent = WebSettings.getDefaultUserAgent(FoundationContextHolder.context);
                } catch (Exception var5) {
                    userAgent = java.lang.System.getProperty("http.agent");
                }
            } else {
                userAgent = java.lang.System.getProperty("http.agent");
            }

            int i = 0;

            for(int length = userAgent.length(); i < length; ++i) {
                char c = userAgent.charAt(i);
                if(c > 31 && c < 127) {
                    sb.append(c);
                } else {
                    sb.append(String.format("\\u%04x", new Object[]{Integer.valueOf(c)}));
                }
            }

            if(sb.length() > 0) {
                sb.append("_CtripAPP_Android_").append(getAppVersion());
            }
        } catch (Exception var6) {
            ;
        }

        return sb.toString();
    }

    public static String getAppVersion() {
        String version = "";
        String versionNameForHuaweiCtch1 = "ctch1";
        if(FoundationContextHolder.context != null) {
            PackageManager packageManager = FoundationContextHolder.context.getPackageManager();

            try {
                PackageInfo e = packageManager.getPackageInfo(FoundationContextHolder.context.getPackageName(), 0);
                version = e.versionName.endsWith("ctch1")?e.versionName.replace("ctch1", ""):e.versionName;
            } catch (NameNotFoundException var4) {
                var4.printStackTrace();
            }
        }

        return version;
    }

    public static boolean isNoHaveSIM() {
        TelephonyManager telephonyManager = (TelephonyManager)FoundationContextHolder.context.getSystemService("phone");
        return telephonyManager.getSimState() == 1;
    }
}

