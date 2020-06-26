import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class ServerThread extends Thread{
	Socket sock;
	BufferedWriter output;
	BufferedReader input;
	TextArea display;
	Label info;
	TextField text;
	String clientdata;
	String serverdata = "";
	ChatMessageS cs;
	
	public static final String SEPARATOR = "|";
	public static final int REQ_LOGON = 1001;
	public static final int REQ_SENDWORDS = 1021;
	
	public ServerThread(ChatMessageS c, Socket s, TextArea ta, Label l) {
		sock = s;
		display = ta;
		info = l;
		cs = c;
		
		try {
			input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void run() {
		cs.list.add(this);
		
		try {
			while((clientdata = input.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(clientdata, SEPARATOR);
				int command = Integer.parseInt(st.nextToken());
				int cnt = cs.list.size();
				
				switch (command) {
					case REQ_LOGON : {
						String ID = st.nextToken();
						String message = st.nextToken();
						display.append("클라이언트 " + ID + "(으)로 로그인하였습니다.\r\n");
						break;
					}
					case REQ_SENDWORDS : {
						String ID = st.nextToken();
						String message = st.nextToken();
						display.append(ID + " : " + message + "\r\n");
						for (int i=0; i<cnt; i++) {
							ServerThread SThread = (ServerThread)cs.list.get(i);
							SThread.output.write(ID + " : " + message + "\r\n");
							SThread.output.flush();
						}
						break;
					}
				}
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		cs.list.remove(this);
		try {
			sock.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
