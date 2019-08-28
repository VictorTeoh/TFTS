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
	

	public static double lowestavgmoneytohit2(double startinglvl, double tiertarget, double numtaken, double desirednum, double otherssametiertaken){
		double num0 = 0.0; double num1 = 0.0; double num2 = 0.0; double num3 = 0.0; double avgmoneyspent; double rolls = 0; double comp; int i = 0; double sd = 0;
		double purchasecost = desirednum*tiertarget;
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
			i++;		
		}
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
		return comp;
	}

	//this method does not value the cost of a unit slot
	public static boolean bkpntlvlorroll(double startinglvl, double tiertarget){
		if(startinglvl>(rollchancearrarr[0].length+1.0)){
			return false;
		}
		double percentinc = (rollchanceget(tiertarget,startinglvl+1)/rollchanceget(tiertarget,startinglvl)) - 1.0;
		if(percentinc <= 0.0){
			System.out.println("Rolling is better always");
			return false;
		}
		double breakpointmoney = (int)(moneyNeededtoLvl(startinglvl)/moneytoexp) / (percentinc/(percentinc+1.0));
		System.out.println("Leveling is better after " + breakpointmoney + " money spent");
		return true;
	}

	public static boolean bkpntlvlorrolltextless(double startinglvl, double tiertarget){
		if(startinglvl>(rollchancearrarr[0].length+1.0)){
			return false;
		}
		double percentinc = (rollchanceget(tiertarget,startinglvl+1)/rollchanceget(tiertarget,startinglvl)) - 1.0;
		if(percentinc <= 0.0){
			return false;
		}
		return true;
	}

	//percent increase of getting something after buying things of the same tier
	public static void buygarbage(double tiertarget, double numtaken, double otherssametiertaken, double garbage){
		double i; double i1;
		i = copypenalty(tiertarget,0,otherssametiertaken);
		i1 = copypenalty(tiertarget,numtaken,otherssametiertaken);
		System.out.println("Percent increase is " + (((i1-i)/i)*100));
	}

	public static void buybeforestolen(double startinglvl, double tiertarget, double numtaken, double desirednum, double otherssametiertaken){
		double num0 = 0.0; double num1 = 0.0; double avgmoneyspent; double rolls = 0; double comp; int i = 0; int i2 = 0; double sd = 0;
		double purchasecost = desirednum*tiertarget;
		while (i < desirednum){
			num0 += geometricmean(rollchanceget(tiertarget,startinglvl) * copypenalty(tiertarget, (numtaken+i), otherssametiertaken));
			i++;
		}
		rolls = num0/minionsperpage;
		avgmoneyspent = rolls * priceofroll + purchasecost;
		num0 = avgmoneyspent;
		System.out.println("Average money needed straight rolling at level " + startinglvl + " is " + num0);
		rolls = 0; i = 0; sd = 0;

		while(num1 < num0 && bkpntlvlorrolltextless(startinglvl, tiertarget)){
			while (i < desirednum && bkpntlvlorrolltextless(startinglvl, tiertarget)){
				num1 += geometricmean(rollchanceget(tiertarget,startinglvl+1) * copypenalty(tiertarget, (numtaken+i+i2), otherssametiertaken));
				i++;
			}
			rolls = num1/minionsperpage;
			avgmoneyspent = moneyNeededtoLvl(startinglvl) + purchasecost;
			num1 = (rolls*priceofroll);
			i2++;
			i = 0;
		}
		if(i2 == 0){
			System.out.println("Just roll");
			return;
		}
		System.out.println("Rolling immediately would cost " + num0 + " and " + i2 + " more have to be taken for leveling to be equal for " + num1);
	}

	public static void besthitmutiple (double startinglvl, double money){

	}
	
	public static void main(String[] args){

	//	costefficienttohit(6, 100, 4, 0);
/*
		buygarbage(1,9,90,8);//.235
		buygarbage(2,0,0,10);//.32
		buygarbage(3,0,0,10);//.4
		buygarbage(4,0,0,10);//.87
		buygarbage(5,1,0,0);//1.7
		lowestavgmoneytohit2(6, 4, 0, 2, 0);
		*/
		buybeforestolen(6,4,2,2,4);
	}
}