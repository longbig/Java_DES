import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class CLient extends JFrame implements Runnable{

	private JPanel contentPane;
    private int port;//设置的客户端端口号
    private String sendToip;//要接收方的ip地址
    private  int sendToport;//要接受方的端口号
    private String text;//要发送的明文
    private String result;
    ServerSocket ss=null;
    private Socket socket=null;
    private Thread thread=null;//该线程打开特定的端口号等待连接
    private DES des=null; 
    /**
	 * Launch the application.
	 */
    JTextArea resultArea;
    JTextArea mingwen;
    JTextArea ipArea;
    JTextArea portArea;
    JTextArea portSelf;
    
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CLient frame = new CLient();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the frame.
	 */
	public CLient() throws Exception{
		
		setTitle("\u52A0/\u89E3\u5BC6\u8F6F\u4EF6\u5BA2\u6237\u7AEF");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 661, 483);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 625, 248);
		contentPane.add(scrollPane);
		
		resultArea = new JTextArea();
		resultArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		resultArea.setLineWrap(true);
		scrollPane.setViewportView(resultArea);
		
		mingwen = new JTextArea();
		mingwen.setFont(new Font("Monospaced", Font.PLAIN, 18));
		mingwen.setBounds(101, 387, 413, 34);
		contentPane.add(mingwen);
		des=new DES();
		
		//为发送按钮添加消息响应函数
		JButton sendbtn = new JButton("\u53D1\u9001");
		sendbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text=mingwen.getText();
				try {
					DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
					resultArea.setText(text);
					// 利用des进行加密并发送密文
					des.setMingwen(text);
					try {
						byte []ctext=des.encrypt();//加密
						int len;
						len=ctext.length;
			        	dos.writeInt(len);
			        	System.out.println("len="+len);
						dos.write(ctext, 0, len);
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					//dos.writeUTF(text);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		sendbtn.setFont(new Font("宋体", Font.PLAIN, 15));
		sendbtn.setBounds(530, 387, 93, 34);
		contentPane.add(sendbtn);
		
		ipArea = new JTextArea();
		ipArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
		ipArea.setBounds(154, 343, 174, 34);
		contentPane.add(ipArea);
		
		portArea = new JTextArea();
		portArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
		portArea.setBounds(430, 343, 49, 34);
		contentPane.add(portArea);
		
		portSelf = new JTextArea();
		portSelf.setFont(new Font("Monospaced", Font.PLAIN, 18));
		portSelf.setBounds(217, 295, 93, 34);
		contentPane.add(portSelf);
		
		thread=new Thread(this);
		//点击设置后
		JButton set = new JButton("\u8BBE\u7F6E");
		set.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				port=Integer.parseInt(portSelf.getText());
				thread.start();
			}
		});
		set.setFont(new Font("宋体", Font.PLAIN, 15));
		set.setBounds(327, 289, 93, 34);
		contentPane.add(set);
		
		JLabel benjiPortLabel = new JLabel("\u5BA2\u6237\u7AEF\u7AEF\u53E3");
		benjiPortLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		benjiPortLabel.setBounds(138, 289, 83, 34);
		contentPane.add(benjiPortLabel);
		
		JLabel sendToportLabel = new JLabel("\u53D1\u9001\u65B9IP");
		sendToportLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		sendToportLabel.setBounds(88, 337, 56, 34);
		contentPane.add(sendToportLabel);
		
		JLabel mingwenLabel = new JLabel("\u660E\u6587");
		mingwenLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		mingwenLabel.setBounds(57, 387, 34, 34);
		contentPane.add(mingwenLabel);
		
		JLabel sendToPortLabel = new JLabel("\u53D1\u9001\u65B9\u7AEF\u53E3");
		sendToPortLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		sendToPortLabel.setBounds(338, 343, 70, 34);
		contentPane.add(sendToPortLabel);
		
		//为加密设置消息响应函数
		JButton jiemibtn = new JButton("\u89E3\u5BC6");
		jiemibtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		jiemibtn.setFont(new Font("宋体", Font.PLAIN, 15));
		jiemibtn.setBounds(473, 289, 118, 34);
		contentPane.add(jiemibtn);
		
		//为连接按钮添加消息响应函数
		JButton connect = new JButton("\u8FDE\u63A5");
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendToip=ipArea.getText();//目的IP
				sendToport=Integer.parseInt(portArea.getText());//目的端口号
				try {
					socket=new Socket(sendToip,sendToport);
					JOptionPane.showMessageDialog(null, "connect success!");
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		connect.setFont(new Font("宋体", Font.PLAIN, 15));
		connect.setBounds(508, 343, 83, 34);
		contentPane.add(connect);
	}
	@Override
	public void run() {
		System.out.println(port+" is listening! please connect……");
		try {
			ss=new ServerSocket(port);
			DataInputStream dis;
			while(true){
				Socket s=ss.accept();
				dis=new DataInputStream(s.getInputStream());
				int len=0;
				len=dis.readInt();
				System.out.println("len="+len);
				byte []ctext=new byte [len];
				dis.read(ctext, 0, len);
		        System.out.print(ctext);
		        
				try {
					byte []ptext=des.Decrypt(ctext);
					String text="接收到的密文为:\n"+ctext+"\n转换后的明文:\n";
					result=new String(ptext,"UTF8");
					result=text+result;
				    resultArea.setText(result);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
