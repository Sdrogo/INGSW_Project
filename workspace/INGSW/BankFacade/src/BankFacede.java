
public class BankFacede {

	private int accountNumber;
	private int securityCode;
	
	AccountNumberCheck accChecker;
	checkSecurityCode codeChecker;
	FundsCheck fundCheck;
	
	WelcomeToBank bankWelcome;
	
	public BankFacede(int newAccountNum, int NewSecCode) {

		accountNumber = newAccountNum;
		securityCode = NewSecCode;
		
		bankWelcome = new WelcomeToBank();
		accChecker = new AccountNumberCheck();
		codeChecker = new checkSecurityCode();
		fundCheck = new FundsCheck();
	}

	public int getAccountNumber(){ return accountNumber;}
	public int getSecurityCode(){return securityCode;}
	
	
	public void preleva(double d) {
		
		if(accChecker.accountActive(getAccountNumber())&&
				codeChecker.isCodeCorrect(securityCode)&&
				fundCheck.haveEnoughMoney(d)){
			System.err.println("[ Transazione Completata ]\n");
		}else{
			System.err.println("[ Transazione Fallita ]");
		}
		
		
	}

	public void deposita(double d) {

		if(accChecker.accountActive(accountNumber)&&codeChecker.isCodeCorrect(securityCode)){
			fundCheck.makeDeposit(d);

			System.err.println("[ Transazione Completata ]\n");
		}else{
			System.err.println("[ Transazione Fallita ]");
		}
	}

}
