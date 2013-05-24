package com.linkspritedirect.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;

public class WifiRobotControlClient implements Runnable {
	
	// IP��ַ���˿�
	private String mHost = null;
	private int mPort = -1;

	// �׽��֡����롢���
	private Socket mSocket = null;
	private BufferedReader mIn = null;
	private PrintWriter mOut = null;

	// �����׽��ִ����ɹ�����ʧ����Ϣ����
	private Handler mHandler = null;

	public WifiRobotControlClient(Handler handler, String host, int port) {
		super();
		this.mHandler = handler;
		this.mHost = host;
		this.mPort = port;
	}

	// ��ʼ��
	private void initSocket() {
		try {
			mSocket = new Socket(mHost, mPort);
			mIn = new BufferedReader(new InputStreamReader(
					mSocket.getInputStream()));
			mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					mSocket.getOutputStream())), true);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// �ж��׽����Ƿ��ʼ���ɹ�
	private Boolean isAvailable() {
		return mSocket != null;
	}

	// ����
	public void send(String msg) {
		if (isAvailable() && mSocket.isConnected()) {
			if (!mSocket.isOutputShutdown()) {
				mOut.print(msg);
				mOut.flush();
			}
		}
	}

	// ����
	public void destroy() {
		if (isAvailable()) {
			try {
				mOut.close();
				mIn.close();
				mSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		initSocket();

		Message msg = new Message();
		if (!isAvailable()) {
			msg.what = -1;
		} else {
			msg.what = 0;
		}
		mHandler.sendMessage(msg);
	}
}
