package com.pan.activemq.p2p;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;


/**
 * <dl>
 * <dt>PTPReceive</dt>
 * <dd>Description:��Ե���ն�</dd>
 * <dd>Copyright: Copyright (C) 2016</dd>
 * <dd>CreateDate: 2017-6-7</dd>
 * </dl>
 *
 * @author panyk
 */
public class PTPReceive {
    //�����˺�
    private String userName = "";
    //��������
    private String password = "";
    //���ӵ�ַ
    private String brokerURL = "tcp://127.0.0.1:61616";
    //connection�Ĺ���
    private ConnectionFactory factory;
    //���Ӷ���
    private Connection connection;
    //һ�������Ự
    private Session session;
    //Ŀ�ĵأ���ʵ�������ӵ��ĸ����У�����ǵ�Ե㣬��ô����ʵ����Queue������Ƕ���ģʽ��������ʵ����Topic
    private Destination destination;
    //�����ߣ����ǽ������ݵĶ���
    private MessageConsumer consumer;

    private String receiveName;//����������

    public PTPReceive(String receiveName){
        this.receiveName = receiveName;
    }

    public static void main(String[] args) {
        PTPReceive receive = new PTPReceive("first");
        receive.start();
        PTPReceive receive2 = new PTPReceive("second");
        receive2.start();
    }

    public void start() {
        try {
            //�����û��������룬url����һ�����ӹ���
            factory = new ActiveMQConnectionFactory(userName, password, brokerURL);
            //�ӹ����л�ȡһ������
            connection = factory.createConnection();
            //���Թ�������費дҲ�ǿ��Եģ��������ϵĸ����ĵ���д��
            connection.start();
            //����һ��session
            //��һ������:�Ƿ�֧���������Ϊtrue�������Եڶ�����������jms����������ΪSESSION_TRANSACTED
            //�ڶ�������Ϊfalseʱ��paramB��ֵ��ΪSession.AUTO_ACKNOWLEDGE��Session.CLIENT_ACKNOWLEDGE��DUPS_OK_ACKNOWLEDGE����һ����
            //Session.AUTO_ACKNOWLEDGEΪ�Զ�ȷ�ϣ��ͻ��˷��ͺͽ�����Ϣ����Ҫ������Ĺ����������ǽ��ն˷����쳣��Ҳ�ᱻ�����������ͳɹ���
            //Session.CLIENT_ACKNOWLEDGEΪ�ͻ���ȷ�ϡ��ͻ��˽��յ���Ϣ�󣬱������javax.jms.Message��acknowledge������jms�������Żᵱ�����ͳɹ�����ɾ����Ϣ��
            //DUPS_OK_ACKNOWLEDGE��������ȷ��ģʽ��һ�����շ�Ӧ�ó���ķ������ôӴ�����Ϣ�����أ��Ự����ͻ�ȷ����Ϣ�Ľ��գ����������ظ�ȷ�ϡ�
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //����һ�������Ŀ�ĵأ���ʵ��һ�¾�֪���ˣ�activemq������ͬʱֻ����һ�����аɣ��������������һ����Ϊ"text-msg"�Ķ��У�����Ự���ᵽ������У���Ȼ�����������в����ڣ����ᱻ����
            destination = session.createQueue("text-msg");
            //����session������һ�������߶���
            consumer = session.createConsumer(destination);
            //ʵ��һ����Ϣ�ļ�����
            //ʵ��������������Ժ�ֻҪ����Ϣ���ͻ�ͨ��������������յ�
            consumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    try {
                        //��ȡ�����յ�����
                        String text = ((TextMessage) message).getText();
                        System.out.println(receiveName + "��" + text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
            //�رս��նˣ�Ҳ������ֹ����Ŷ
//            consumer.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
