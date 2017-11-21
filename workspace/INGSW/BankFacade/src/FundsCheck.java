
public class FundsCheck {

	private double cashInAccount = 1000.00;
	
	private double getCashInAccount(){return cashInAccount;}
	
	public void decreseCashInAccount(double cashToWithdrawn){
		cashInAccount -= cashToWithdrawn;
	}
	public void increseCashInAccount(double cashToDeposited){
		cashInAccount += cashToDeposited;
	}
	
	public boolean haveEnoughMoney(double cashToWithdrawn){
		if(cashInAccount>cashToWithdrawn){
			decreseCashInAccount(cashToWithdrawn);
			System.out.println("[ Prelievo effettuato con successo, nuovo saldo : "+getCashInAccount()+" ]\n");
			return true;
			
		}else{
			System.out.println("[ il saldo disponibile è inferiore alla richiesta. Saldo disponibile : "+getCashInAccount()+" ]\n");
			return false;
		}
	}	
	public void makeDeposit(double cashToDeposit){
		increseCashInAccount(cashToDeposit);
		System.out.println("[ Deposito effettuato con successo.\n Nuovo saldo : "+getCashInAccount()+" ]\n");
	}
	
	
}
