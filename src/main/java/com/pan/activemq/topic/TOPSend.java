package com.pan.activemq.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * <dl>
 * <dt>TOPSend</dt>
 * <dd>Description:����ģʽ���Ķ�</dd>
 * <dd>Copyright: Copyright (C) 2016</dd>
 * <dd>CreateDate: 2017-6-7</dd>
 * </dl>
 *
 * @author panyk
 */
public class TOPSend {
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
    //�����ߣ����ǲ������ݵĶ���
    private MessageProducer producer;

    public static void main(String[] args) {
        TOPSend send = new TOPSend();
        send.start();
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


            //=======================================================
            //��Ե��붩��ģʽΨһ��ͬ�ĵط���������һ�д��룬��Ե㴴������Queue��������ģʽ��������Topic
            destination = session.createTopic("topic-text");
            //=======================================================


            //��session�У���ȡһ����Ϣ������
            producer = session.createProducer(destination);
            //���������ߵ�ģʽ�������ֿ�ѡ
            //DeliveryMode.PERSISTENT ��activemq�رյ�ʱ�򣬶������ݽ��ᱻ����
            //DeliveryMode.NON_PERSISTENT ��activemq�رյ�ʱ�򣬶�����������ݽ��ᱻ���
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            long s = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                //����һ����Ϣ����Ȼ����Ϣ�������кܶ࣬�����֣��ֽڣ������,����ͨ��session.create..��������������
                TextMessage textMsg = session.createTextMessage("����" + i);
                //����һ����Ϣ
                producer.send(textMsg);
            }
            long e = System.currentTimeMillis();
            System.out.println("������Ϣ�ɹ���������=" + (e - s));
            //���������ߵĶ���ر��ˣ�����������Ŷ
            producer.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}