package com.pan.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * <dl>
 * <dt>TOPSend</dt>
 * <dd>Description:����ģʽ���Ͷ�</dd>
 * <dd>Copyright: Copyright (C) 2016</dd>
 * <dd>CreateDate: 2017-6-7</dd>
 * </dl>
 *
 * @author panyk
 */
public class TOPSub {
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
    //������
    private MessageConsumer consumer;

    private String name;

    public TOPSub(String name){
        this.name = name;
    }

    public static void main(String[] args) {
        TOPSub sub = new TOPSub("��÷÷");
        sub.start();
        TOPSub sub2 = new TOPSub("����");
        sub2.start();
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
            Topic topic = session.createTopic("topic-text");
            //=======================================================


            //��session�У���ȡһ����Ϣ������
            consumer = session.createConsumer(topic);
            consumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    TextMessage tm = (TextMessage) message;
                    try{
                        System.out.println(name + "��" + tm.getText());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
//            if(connection != null){
//                connection.close();
//            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}