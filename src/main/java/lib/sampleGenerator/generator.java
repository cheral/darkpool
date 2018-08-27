package com.sampleGenerator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class generator {
	
	public static double market_activity = 0.2;
	public static int sleep_time = (int)(1-market_activity)*1000;
	public static double stock_volatilty = 1;
	public static int stock_volume_index = (int)stock_volatilty*1000;
	public static int no_of_fictious_users = 10;
	public static long [] fictious_user_ids;
	public static boolean generator_on=true;
	public static int no_of_random_orders = 50;
	public static int order_counter = no_of_random_orders;
	public static double tick_size=0.05;
	public static double ltp=155;
	public static int last_vol=stock_volume_index;
	 
	
	
	public static int generateVol(double ltp, double price) {
		int vol = last_vol;
		
		Random rnd_vol = new Random();
		
		if (price>ltp) {
			vol = (int)Math.round(rnd_vol.nextGaussian()*100 +(last_vol+stock_volume_index*Math.abs(ltp-price)));
		}
		else if (ltp>price) {
			vol = (int)Math.round(rnd_vol.nextGaussian()*100 +(last_vol+stock_volume_index*Math.abs(ltp-price)));
		}
			else {
			vol = (int)Math.round(rnd_vol.nextGaussian()*100 +last_vol);
			}
		
		return vol;
	}
	
	
	public static double generatePrice() {
		
		
		//int market_limit = ThreadLocalRandom.current().nextInt(0, 1);
		
		
		Random rnd_price = new Random();
		double price = (double) Math.round((rnd_price.nextGaussian() * 2 + 155) * 100) / 100;
		price= (double)Math.round((Math.round(price/tick_size )*tick_size)*100)/100;
		
		Random market_limit =new Random();
		
		if (market_limit.nextBoolean()) {
			price=ltp;
		}
		
		return price;
		
		
	}
	
	
	public static double generateMinFill() {
		

		Random rnd_min_fill = new Random();
	
		double min_fill = ThreadLocalRandom.current().nextInt(0, 2 + 1);
		
		if (min_fill == 2.0) {
			min_fill = (double) Math.round((rnd_min_fill.nextGaussian() * 0.2 + 0.5) * 100) / 100;
		}
		
		return min_fill;
		
		
	}
	
	
	public static boolean generateBuySell() {
		

		Random buy_sell =new Random();

		boolean buy = buy_sell.nextBoolean();
		
		return buy;
		
	}
	
	
	public static boolean findLimitMarket(double ltp, double price) {
		
		boolean isMarket;
		if (ltp==price) {
			isMarket = true;
		}
		
		else {
			isMarket = false;
		}
		
		return isMarket;
		
	}
	
	
	public static long[] generate_user_ids(int no_of_fictious_users) {
		long [] fictious_user_ids = new long [no_of_fictious_users];
		
		Random rnd_user_ids = new Random();
		
		for (int i=0; i<no_of_fictious_users; i++) {
			long user_id = ThreadLocalRandom.current().nextInt(1, 80000);
			
			fictious_user_ids[i] = user_id;
		}
		
		return fictious_user_ids;
	}
	
	public static long fetch_random_user_id(long user_ids[]) {
		Random rnd_user_id = new Random();
		long user_id = user_ids[ThreadLocalRandom.current().nextInt(0, user_ids.length)];
		return user_id;
		
	}
	
	
	
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		generator gen = new generator();
		
		long [] fictious_user_ids = gen.generate_user_ids(no_of_fictious_users);
		
//		for (int i=0; i<fictious_user_ids.length;i++) {
//			System.out.println(fictious_user_ids[i]);
//		}
//		
		while(generator_on) {
			
			
			if (order_counter<=1) {
				generator_on=false;
			}
			
			
			long user_id = gen.fetch_random_user_id(fictious_user_ids);
			
			
			double price = gen.generatePrice();

			boolean isMarket = gen.findLimitMarket(ltp,price);

			int vol = gen.generateVol(ltp,price);
			
			boolean buy = gen.generateBuySell();
				
			double min_fill = gen.generateMinFill();
			
			
			String isMarketString="Market";
			
			if (!isMarket) {
				isMarketString = "Limit";
			}
				
		
			
			if (buy) {
				System.out.println("User "+user_id+" placed a "+isMarketString+" Buy order for FB at: "+price+" for quantity: "+vol+" with a minimum fill of "+min_fill);
			}
			else {
				System.out.println("User "+user_id+" placed a "+isMarketString+ " Sell order for FB at: "+price+" for quantity: "+vol+" with a minimum fill of "+min_fill);

			}
			
			ltp=price;
			try {
			    Thread.sleep(sleep_time);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
			
			order_counter = order_counter-1;
		}
	}

}
