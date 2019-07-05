import java.lang.Math;
import java.util.Arrays;
public class Maths{	
	static double[][] rollchancearrarr = {
		{1.0, 1.0, 0.65, 0.55, 0.37, 0.245, 0.2, 0.15, 0.1},
		{0.0, 0.0, 0.3, 0.35, 0.35, 0.35, 0.3, 0.25, 0.15},
		{0.0, 0.0, 0.05, 0.15, 0.25, 0.30, 0.33, 0.35, 0.35},
		{0.0, 0.0, 0.0, 0.0, 0.03, 0.1, 0.15, 0.2, 0.3},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.005, 0.02, 0.05, 0.1},
	}; 
	//roll chance for tier 1 - 5 for levels 1-9
	static double[] exptolvlarr = {2.0, 2.0, 6.0, 10.0, 18.0, 30.0, 46.0, 70.0};
	//exp needed to level to next level from levels 1-8
	static double[] unitspertier = {11.0, 12.0, 12.0, 9.0, 6.0};
	//number of units in the pool for tier 1 - 5
	static double[] copiespertier = {39.0, 26.0, 21.0, 13.0, 10.0};
	//number of copies of units in the pool for tier 1 - 5

	static double expperround = 2.0;
	static double moneytoexp = 1.0;
	static double priceofroll = 2.0;
	static double minionsperpage = 5.0;

	public static double largest(double... dubs){
		double highest = dubs[0];
		for(Double doubles : dubs){
			highest = Math.max(highest, doubles);
		}
		return highest;
	}

	public static double smallest(double... dubs){
		double smallest = dubs[0];
		for(Double doubles : dubs){
			smallest = Math.min(smallest, doubles);
		}
		return smallest;
	}

	public static double moneyNeededtoLvl(double startinglvl){
		if(startinglvl > exptolvlarr.length){ return 1000.0;}
		return exptolvlarr[(int)(startinglvl - 1.0)];
	}

	public static double logbasex(double number, double x){
		if(number == 0){return -10000.0;}
		return Math.log10(number)/Math.log10(x);
	}

	public static double geometricmean(double psuccess){
		return 1.0/psuccess;
	}

	public static double geometircvariance(double psuccess){
		return((1-psuccess)/(Math.pow(psuccess,2.0)));
	}

	public static double geometricSD(double psuccess){
		return Math.pow(geometircvariance(psuccess),0.5);
	}

	public static double rollchanceget(double tiertarget, double startinglvl){
		if(tiertarget>5 || startinglvl>9){return 0.0;}
		return rollchancearrarr[(int)(tiertarget-1.0)][(int)(startinglvl-1.0)];
	}

	public static double copypenalty(double tiertarget, double numtaken, double otherssametiertaken){
		return ((unitspertier[(int)(tiertarget-1.0)]-numtaken) / ((copiespertier[(int)(tiertarget-1.0)] * unitspertier[(int)(tiertarget-1.0)])-otherssametiertaken-numtaken));
	}
	

	public static void lowestavgmoneytohit2(double startinglvl, double tiertarget, double numtaken, double desirednum, double otherssametiertaken){
		double num0 = 0.0; double num1 = 0.0; double num2 = 0.0; double num3 = 0.0; double avgmoneyspent; double rolls = 0; double comp; int i = 0; double sd = 0;
		double purchasecost = desirednum*tiertarget;
		double takenreduc = (copiespertier[(int)(tiertarget-1.0)] - numtaken) / copiespertier[(int)(tiertarget-1.0)];
		while (i < desirednum){
			num0 += geometricmean(rollchanceget(tiertarget,startinglvl) * copypenalty(tiertarget, (numtaken+i), otherssametiertaken));
			sd += geometricSD(geometricmean(num0))/(minionsperpage/priceofroll);
			i++;
		}
		rolls = num0/minionsperpage;
		avgmoneyspent = rolls * priceofroll + purchasecost;
		num0 = avgmoneyspent;
		System.out.println("Average money needed straight rolling at level " + startinglvl + " is " + avgmoneyspent + " with a standard deviation of " + sd);
		bkpntlvlorroll(startinglvl, tiertarget);
		rolls = 0; i = 0; sd = 0;

		while (i < desirednum){
			num1 += geometricmean(rollchanceget(tiertarget,startinglvl+1) * copypenalty(tiertarget, (numtaken+i), otherssametiertaken));
			sd += geometricSD(geometricmean(num1))/(minionsperpage/priceofroll);
			i++;
		}
		rolls = num1/minionsperpage;
		avgmoneyspent = moneyNeededtoLvl(startinglvl) + purchasecost;
		num1 = avgmoneyspent + (rolls*priceofroll);
		System.out.println("Average money needed rolling at level " + (startinglvl+1) + " is " + (avgmoneyspent + (rolls*priceofroll)) + " with " + (int)(avgmoneyspent-purchasecost) + " money for leveling with a standard deviation of " + sd);
		bkpntlvlorroll(startinglvl+1, tiertarget);
		rolls = 0; i = 0; sd = 0;

		while (i < desirednum){
			num2 += geometricmean(rollchanceget(tiertarget,startinglvl+2) * copypenalty(tiertarget, (numtaken+i), otherssametiertaken));
			sd += geometricSD(geometricmean(num2))/(minionsperpage/priceofroll);
			i++;		}
		rolls = num2/minionsperpage;
		avgmoneyspent = moneyNeededtoLvl(startinglvl) + moneyNeededtoLvl(startinglvl+1) + purchasecost;
		num2 = avgmoneyspent + (rolls*priceofroll);
		System.out.println("Average money needed rolling at level " + (startinglvl+2) + " is " + (avgmoneyspent + (rolls*priceofroll)) + " with " + (int)(avgmoneyspent-purchasecost) + " money for leveling with a standard deviation of " + sd);
		bkpntlvlorroll(startinglvl+2, tiertarget);
		rolls = 0; i = 0; sd = 0;

		while (i < desirednum){
			num3 += geometricmean(rollchanceget(tiertarget,startinglvl+3) * copypenalty(tiertarget, (numtaken+i), otherssametiertaken));
			sd += geometricSD(geometricmean(num3))/(minionsperpage/priceofroll);
			i++;
		}
		rolls = num3/minionsperpage;
		avgmoneyspent = moneyNeededtoLvl(startinglvl) + moneyNeededtoLvl(startinglvl+1) + moneyNeededtoLvl(startinglvl+2) + purchasecost;
		num3 = avgmoneyspent + (rolls*priceofroll);
		System.out.println("Average money needed rolling at level " + (startinglvl+3) + " is " + (avgmoneyspent + (rolls*priceofroll)) + " with " + (int)(avgmoneyspent-purchasecost) + " money for leveling with a standard deviation of " + sd);
		bkpntlvlorroll(startinglvl+3, tiertarget);
		//rolls = 0; i = 0; sd = 0;
		comp = smallest(num0, num1, num2, num3);
		System.out.println(comp + " Is the best case");

	}

	//this method does not value the cost of a unit slot
	public static void bkpntlvlorroll(double startinglvl, double tiertarget){
		if(startinglvl>(rollchancearrarr[0].length+1.0)){
			return;
		}
		double percentinc = (rollchanceget(tiertarget,startinglvl+1)/rollchanceget(tiertarget,startinglvl)) - 1.0;
		if(percentinc <= 0.0){
			System.out.println("Rolling is better always");
			return;
		}
		double breakpointmoney = (int)(moneyNeededtoLvl(startinglvl)/moneytoexp) / (percentinc/(percentinc+1.0));
		System.out.println("Leveling is better after " + breakpointmoney + " money spent");
	}

	public static void besthitmutiple (double startinglvl, double money){

	}
	
	public static void main(String[] args){

	//	costefficienttohit(6, 100, 4, 0);
	/*	double i = avghits(0.001);
		System.out.println(i);*/
		lowestavgmoneytohit2(6, 4, 0, 2, 0);
	}

	/* These are vast underestimates using bad napkin math
	public static double avghits(double number){
		double i;
		if(number == 0.0){return 0.0;}
		i = Math.pow(number, -1);
		return logbasex(i, 2.0);
	}

	public static void costefficienttohit(double startinglvl, double money, double tiertarget, double numtaken){
		double num0; double comp; double budget; double effmoney;
		double num1 = 1.0; double num2 = 1.0; double num3 = 1.0;
		double takenreduc = (copiespertier[(int)(tiertarget-1.0)] - numtaken) / copiespertier[(int)(tiertarget-1.0)];//does not accurately account for the last couple of a small copy pool
		num0 = Math.pow(1.0 - ((rollchancearrarr[(int)(tiertarget-1.0)][(int)(startinglvl-1.0)] / unitspertier[(int)(tiertarget-1.0)] * takenreduc)),	Math.floor(money*5/priceofroll));
		System.out.println("Straight rolling chance " + num0);
		budget = moneyNeededtoLvl(startinglvl);
		if(money > budget){
			effmoney = money - budget;
			num1 = Math.pow(1.0 - (rollchancearrarr[(int)(tiertarget-1.0)][(int)(startinglvl)] / unitspertier[(int)(tiertarget-1.0)]),	Math.floor(effmoney*5/priceofroll));
			System.out.println("Straight rolling chance after 1 lvl " + num1);
		}
		budget += moneyNeededtoLvl(startinglvl+1.0);
		if(money > budget){
			effmoney = money - budget;
			num2 = Math.pow(1.0 - (rollchancearrarr[(int)(tiertarget-1.0)][(int)(startinglvl+1.0)] / unitspertier[(int)(tiertarget-1.0)]),	Math.floor(effmoney*5/priceofroll));
			System.out.println("Straight rolling chance after 2 lvl " + num2);
		}
		budget += moneyNeededtoLvl(startinglvl+2.0);
		if(money > budget){
			effmoney = money - budget;
			num3 = Math.pow(1.0 - (rollchancearrarr[(int)(tiertarget-1.0)][(int)(startinglvl+2.0)] / unitspertier[(int)(tiertarget-1.0)]),	Math.floor(effmoney*5/priceofroll));
			System.out.println("Straight rolling chance after 3 lvl " + num3);
		}
		comp = smallest(num0, num1, num2, num3);
		System.out.println(comp + " is the most cost efficient method");

	}

	public static void lowestavgmoneytohit(double startinglvl, double tiertarget, double numtaken, double desirednum){
		double num0 = 0.0; double num1 = 0.0; double num2 = 0.0; double num3 = 0.0; double avgmoneyspent; int rolls = 0; double comp;
		double purchasecost = desirednum*tiertarget;
		double takenreduc = (copiespertier[(int)(tiertarget-1.0)] - numtaken - (desirednum/2)) / copiespertier[(int)(tiertarget-1.0)];//does not accurately account for the last couple of a small copy pool
		if (takenreduc<0){return;}
		while(avghits(num0)<desirednum){
			rolls += 1;
			num0 = Math.pow(1.0 - ((rollchancearrarr[(int)(tiertarget-1.0)][(int)(startinglvl-1.0)] / unitspertier[(int)(tiertarget-1.0)] * takenreduc)),	5*rolls);
			//System.out.println(num0);
		}

		avgmoneyspent = rolls * 2 + purchasecost;
		num0 = avgmoneyspent;
		System.out.println("Average money needed straight rolling " + avgmoneyspent);
		rolls = 0;

		while(avghits(num1)<desirednum){
			rolls += 1;
			num1 = Math.pow(1.0 - ((rollchancearrarr[(int)(tiertarget-1.0)][(int)startinglvl] / unitspertier[(int)(tiertarget-1.0)] * takenreduc)),	5*rolls);
		}
		avgmoneyspent = rolls * 2 + moneyNeededtoLvl(startinglvl) + purchasecost;
		num1 = avgmoneyspent;
		System.out.println("Average money needed needed after 1 lvl " + avgmoneyspent + " with " + (avgmoneyspent-(rolls*2)) + " money for leveling");
		rolls = 0;
		
		while(startinglvl+1.0 < exptolvlarr.length && avghits(num2)<desirednum){
			rolls += 1;
			num2 = Math.pow(1.0 - ((rollchancearrarr[(int)(tiertarget-1.0)][(int)(startinglvl+1.0)] / unitspertier[(int)(tiertarget-1.0)] * takenreduc)),	5*rolls);
		}
		avgmoneyspent = rolls * 2 + moneyNeededtoLvl(startinglvl) + moneyNeededtoLvl(startinglvl+1) + purchasecost;
		num2 = avgmoneyspent;
		System.out.println("Average money needed needed after 2 lvl " + avgmoneyspent + " with " + (avgmoneyspent-(rolls*2)) + " money for leveling");
		rolls = 0;
		
		while(startinglvl+2.0 < exptolvlarr.length && avghits(num3)<desirednum){
			rolls += 1;
			num3 = Math.pow(1.0 - ((rollchancearrarr[(int)(tiertarget-1.0)][(int)(startinglvl+2.0)] / unitspertier[(int)(tiertarget-1.0)] * takenreduc)),	5*rolls);
		}
		avgmoneyspent = rolls*2 + moneyNeededtoLvl(startinglvl)+ moneyNeededtoLvl(startinglvl+1)+moneyNeededtoLvl(startinglvl+2) + purchasecost;
		num3 = avgmoneyspent;
		System.out.println("Average money needed rolling after 3 lvl " + avgmoneyspent + " with " + (avgmoneyspent-(rolls*2)) + " money for leveling");
		comp = smallest(num0, num1, num2, num3);
		System.out.println(comp + " Is the best case");

	}
	*/
}