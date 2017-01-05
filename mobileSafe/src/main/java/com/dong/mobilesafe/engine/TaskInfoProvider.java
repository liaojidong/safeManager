package com.dong.mobilesafe.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

import com.dong.mobilesafe.domain.TaskInfo;
import com.dong.mobilesafe.R;
import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

/**
 * 提供手机里面的进程信息
 * @author Administrator
 *
 */
public class TaskInfoProvider {
	/**
	 * 获取所有的进程信息
	 * @param context 上下文
	 * @return
	 */
	public static List<TaskInfo> getTaskInfos(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		List<AndroidAppProcess> processInfos = AndroidProcesses.getRunningAppProcesses();
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		for(AndroidAppProcess processInfo : processInfos){
			TaskInfo taskInfo = new TaskInfo();
			//应用程序的包名。
			String packname = processInfo.name;
			taskInfo.setPackname(packname);
			MemoryInfo[] memoryInfos = am.getProcessMemoryInfo(new int[]{processInfo.pid});
			long memsize = memoryInfos[0].getTotalPrivateDirty()*1024l;
			taskInfo.setMemsize(memsize);
			try {
				ApplicationInfo applicationInfo = pm.getApplicationInfo(packname, 0);
				Drawable icon = applicationInfo.loadIcon(pm);
				taskInfo.setIcon(icon);
				String name = applicationInfo.loadLabel(pm).toString();
				taskInfo.setName(name);
				if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM) == 0){
					//用户进程
					taskInfo.setUserTask(true);
				}else{
					//系统进程
					taskInfo.setUserTask(false);
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				taskInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_default));
				taskInfo.setName(packname);
			}
			taskInfos.add(taskInfo);
		}
		return taskInfos;
	}

	public static final int AID_APP = 10000;
	public static final int AID_USER = 100000;

	public static String getForegroundApp() {
		File[] files = new File("/proc").listFiles();
		int lowestOomScore = Integer.MAX_VALUE;
		String foregroundProcess = null;
		for (File file : files) {
			if (!file.isDirectory()) {
				continue;
			}
			int pid;

			try {
				pid = Integer.parseInt(file.getName());
			} catch (NumberFormatException e) {
				continue;
			}

			try {
				String cgroup = read(String.format("/proc/%d/cgroup", pid));
				String[] lines = cgroup.split("\n");
				String cpuSubsystem = null;
				String cpuaccctSubsystem = null;

				// 我们取cpu和cpuacct两行数据
				for (String s : lines) {
					if (s.contains(":cpu:")) {
						cpuSubsystem = s;
					}
					if (s.contains(":cpuacct:")) {
						cpuaccctSubsystem = s;
					}
				}

				if (!cpuaccctSubsystem.endsWith(Integer.toString(pid))) {
					// not an application process
					continue;
				}
				if (cpuSubsystem.endsWith("bg_non_interactive")) {
					// background policy
					continue;
				}

				int uid = Integer.parseInt(cpuaccctSubsystem.split(":")[2]
						.split("/")[1].replace("uid_", ""));
				if (uid >= 1000 && uid <= 1038) {
					// system process
					continue;
				}
				int appId = uid - AID_APP;
				int userId = 0;
				// loop until we get the correct user id.
				// 100000 is the offset for each user.

				while (appId > AID_USER) {
					appId -= AID_USER;
					userId++;
				}

				if (appId < 0) {
					continue;
				}
				// u{user_id}_a{app_id} is used on API 17+ for multiple user
				// account support.
				// String uidName = String.format("u%d_a%d", userId, appId);
				File oomScoreAdj = new File(String.format(
						"/proc/%d/oom_score_adj", pid));
				if (oomScoreAdj.canRead()) {
					String str = read(oomScoreAdj.getAbsolutePath());
					int oomAdj = Integer.valueOf(str);
					if (oomAdj != 0) {
						continue;
					}
				}

				String cmdline = read(String.format("/proc/%d/cmdline", pid));
				if (cmdline.contains("com.android.systemui") || cmdline.contains("dirac")) {
					continue;
				}

				int oomscore = Integer.parseInt(read(String.format(
						"/proc/%d/oom_score", pid)));
				if (oomscore < lowestOomScore) {
					lowestOomScore = oomscore;
					foregroundProcess = cmdline;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return foregroundProcess;

	}

	private static String read(String path) throws IOException {
		StringBuilder output = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(path));
		output.append(reader.readLine());

		for (String line = reader.readLine(); line != null; line = reader
				.readLine()) {
			output.append('\n').append(line);
		}
		reader.close();
		return output.toString().trim();// 不调用trim()，包名后会带有乱码
	}


}
