package network;
//��󣬴���һ���ͻ��˵���������Ϊ�˱Ƚϣ�Ҳ������һ��
//�ͻ��˵ļ������ı��ض���
public class CalcClient { // CalcClient.java���ͻ���������
	public static void main(String[] args) throws Exception {
		Calc c1 = new CalcImpl(); // �ͻ���������ڲ��ļ���������
		Calc c2 = new RemoteCalc("localhost", 4321); // Զ�̶���ı��ش���
		System.out.println("call local object:");
		System.out.println("5 + 6 = " + c1.add(5, 6));
		System.out.println("11 - 5 = " + c1.sub(11, 5));
		System.out.println("call remote object:");
		System.out.println("5 + 6 = " + c2.add(5, 6));
		System.out.println("11 - 5 = " + c2.sub(11, 5));
	}
}
