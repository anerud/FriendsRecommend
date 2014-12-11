package data;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.echonest.api.v4.EchoNestException;

public class CollectData {
	
	private static int nArtists = 5;
	private static int nRelated = 20;
	private static String seed = "Led Zeppelin";
	private static String[] seeds = {"rolling stones", "deep purple", "Led Zeppelin"}; //,				// 60-70's rock
////		      "NICKELBACK", "RISE AGAINST",					// modern rock
//		      "run dmc", "beastie boys",					// 80's hip-hop
//			  "Kanye west", "Eminem",						// modern hip-hop
//			  "depeche mode", "joy division",				// 80's electro/punk
//			  "britney spears", "aqua",						// 90/2000 pop
//			  "slipknot", "opeth",							// 90/2000 Death metal
//			  "lumineers", "mumford and sons",				// new popular music
//			  "justin bieber", "nicki minaj",				// GOOD SHIT!!!!
//			  "rihanna", "beyonce"};	
	
	public static void main(String[] args) throws IOException {
		DataCollector dataCollector;
		double startTime = System.currentTimeMillis();
		try {
			dataCollector = new DataCollector();
//			cd.randomWalkBioData(seeds, nArtists, nRelated, delimitor);
			dataCollector.randomWalkArtists(nArtists, nRelated, seed);
			double seconds = (System.currentTimeMillis() - startTime)/1000;
			System.out.println("Ended successfully after " + seconds + " seconds.");
		} catch (EchoNestException e) {
			double seconds = (System.currentTimeMillis() - startTime)/1000;
			System.out.println("Ended with exception after " + seconds + " seconds.");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
    }

}
