package network;
public class CalcImpl implements Calc { // CalcImpl.java，Calc接口的实现类
	public int add(int i, int j) {
		return i + j;
	} // 注意：本类并没有成员数据

	public int sub(int i, int j) {
		return i - j;
	} // 没有共享冲突这个现象

	public static void main(String[] args) { // 完成对add,sub方法的测试
		Calc c = new CalcImpl();
		System.out.println("5 + 6 = " + c.add(5, 6));
		System.out.println("11 - 5 = " + c.sub(11, 5));
	}
}
