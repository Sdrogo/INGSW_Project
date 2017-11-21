
public class main {

	private static BankFacede bankAccess;
	
	public static void main(String[]args){
		
		bankAccess = new BankFacede(12345, 1234);
		
		bankAccess.preleva(50.00);
		bankAccess.preleva(1250.00);
		bankAccess.deposita(200.00);
	}
}
