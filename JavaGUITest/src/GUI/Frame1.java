package GUI;

import java.awt.EventQueue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;


public class Frame1 {

	private JFrame frame;
	private JLabel lblNewLabel;
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
//	private static String subject = "VALLYSOFTQ";
	private static String subject = "TestQueueOne";
	
	private String messageLastReceive="";
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame1 window = new Frame1();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame1() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnShow = new JButton("Show");
		btnShow.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
					Connection connection = connectionFactory.createConnection();
					try{
						System.out.println("start consumer connection");

						Session session = connection.createSession(false,
								Session.AUTO_ACKNOWLEDGE);
						Queue queue = session.createQueue(subject);
						MessageConsumer consumer = session.createConsumer(queue);
						//					Message message = consumer.receive();
						//					System.out.println("initialize selesai");
						consumer.setMessageListener(new MessageListener() {	
							public void onMessage(Message message) {
								try{
									if (!(message instanceof TextMessage)) {
										lblNewLabel.setText("no new message");
									}
									else{
										TextMessage textMessage = (TextMessage) message;
										System.out.println(textMessage.getText());
										lblNewLabel.setText(textMessage.getText());
										//									lblNewLabel.setText("asd");
										//								messageLastReceive=textMessage.getText();
									}}catch(JMSException error){
										lblNewLabel.setText("error reading message");
									}

							}
						});
						connection.start();
						Thread.sleep(500);
						System.out.println("finish consumer connection");



						//				JOptionPane.showMessageDialog(null, "hi yan");
						//					lblNewLabel.setText("hi yan isinya = " + messageLastReceive);
					}catch(Exception error){
						System.out.println(error);

					}
					finally{
						connection.close();
						System.out.println("exit consumer connection");
					}
				}catch(Exception error){
					System.out.println(error);
					

				}
				

			}
		});

		btnShow.setBounds(237, 191, 89, 23);
		frame.getContentPane().add(btnShow);

		lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(26, 23, 379, 63);
		frame.getContentPane().add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(54, 97, 284, 34);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JButton btnProduce = new JButton("Produce");
		btnProduce.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					System.out.println("start Producer connection");
					ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
					Connection connection = connectionFactory.createConnection();
					connection.start();
					Session session = connection.createSession(false,
							Session.AUTO_ACKNOWLEDGE);
					Destination destination = session.createQueue(subject);
					MessageProducer producer = session.createProducer(destination);
					//				TextMessage message = session.createTextMessage("Hello welcome come to vallysoft ActiveMQ!");
					TextMessage message = session.createTextMessage(textField.getText());
					producer.send(message);
					System.out.println("Sentage '" + message.getText() + "'");
					connection.close();
					System.out.println("exit Producer connection");
				}catch(Exception error){
					System.out.println(error);
				}

			}
		});
		btnProduce.setBounds(71, 191, 89, 23);
		frame.getContentPane().add(btnProduce);
	}
}
