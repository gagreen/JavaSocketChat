import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.awt.*;
import java.awt.event.*;

public class ChatMessageC extends Frame implements ActionListener, KeyListener{
	TextArea display;
	TextField wtext, ltext;
	Label mlbl, wlbl, loglbl;
	BufferedWriter output;
	BufferedReader input;
	Socket client;
	StringBuffer clientdata;
	String serverdata;
	String ID;
	
	private static final String SEPARATOR = "|";
	private static final int REQ_LOGON = 1001;
	private static final int REQ_SENDWORDS = 1021;
	
	public ChatMessageC() {
		super("Ŭ���̾�Ʈ");
		
		mlbl = new Label("ä�� ���¸� �����ݴϴ�.");
		add(mlbl, BorderLayout.NORTH);
		
		display = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		display.setEditable(false);
		add(display, BorderLayout.CENTER);
		
		Panel ptotal = new Panel(new BorderLayout());
		
		Panel pword = new Panel(new BorderLayout());
		wlbl = new Label("��ȭ��");
		wtext = new TextField(30);
		wtext.addKeyListener(this); // �Էµ� ������ �۽��ϱ� ���� �̺�Ʈ ����
		pword.add(wlbl, BorderLayout.WEST);
		pword.add(wtext, BorderLayout.EAST);
		ptotal.add(pword, BorderLayout.CENTER);
		
		Panel plabel = new Panel(new BorderLayout());
		loglbl = new Label("�α׿�");
		ltext = new TextField(30); // ������ �Է� �ʵ�
		ltext.addActionListener(this);
		plabel.add(loglbl, BorderLayout.WEST);
		plabel.add(ltext, BorderLayout.EAST);
		
		ptotal.add(plabel, BorderLayout.SOUTH);
		
		add(ptotal, BorderLayout.SOUTH);
		
		addWindowListener(new WinListener());
		setSize(300, 250);
		setVisible(true);
	}
	
	public void runClient() {
		try {
			client = new Socket(InetAddress.getLocalHost(), 5000);
			mlbl.setText("����� �����̸� : " + client.getInetAddress().getHostName());
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			clientdata = new StringBuffer(2048);
			mlbl.setText("���� �Ϸ� ����� ���̵� �Է��ϼ���.");
			
			while(true) {
				serverdata = input.readLine();
				display.append(serverdata + "\r\n");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent ae) {
		if(ID == null) {
			ID = ltext.getText();
			mlbl.setText(ID + "(��)�� �α��� �Ͽ����ϴ�.");
			
			try {
				clientdata.setLength(0);
				clientdata.append(REQ_LOGON);
				clientdata.append(SEPARATOR);
				clientdata.append(ID);
				output.write(clientdata.toString() + "\r\n");
				output.flush();
				ltext.setVisible(false);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) {
		ChatMessageC c = new ChatMessageC();
		c.runClient();
	}
	
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		if(ke.getKeyChar() == KeyEvent.VK_ENTER) {
			String message = new String();
			if(ID == null) {
				mlbl.setText("�ٽ� �α��� �ϼ���");
				wtext.setText("");
			} else {
				try {
					clientdata.setLength(0);
					clientdata.append(REQ_LOGON);
					clientdata.append(SEPARATOR);
					clientdata.append(ID);
					clientdata.append(SEPARATOR);
					clientdata.append(message);
					output.write(clientdata.toString() + "\r\n");
					output.flush();
					wtext.setText("");
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent ke) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent ke) {
		// TODO Auto-generated method stub
		
	}
	
	
}