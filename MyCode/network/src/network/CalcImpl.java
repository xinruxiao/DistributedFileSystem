package network;
public class CalcImpl implements Calc { // CalcImpl.java��Calc�ӿڵ�ʵ����
	public int add(int i, int j) {
		return i + j;
	} // ע�⣺���ಢû�г�Ա����

	public int sub(int i, int j) {
		return i - j;
	} // û�й����ͻ�������

	public static void main(String[] args) { // ��ɶ�add,sub�����Ĳ���
		Calc c = new CalcImpl();
		System.out.println("5 + 6 = " + c.add(5, 6));
		System.out.println("11 - 5 = " + c.sub(11, 5));
	}
}
