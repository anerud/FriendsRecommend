package data;

import java.io.*;
import java.util.*;

import com.echonest.api.v4.*;

public class DataCollector {

	private String delimiter = "\t";

	private EchoNestAPI en;
	private static boolean trace = false;
	private HashSet<String> artistsMap;
	private HashSet<String> relatedArtistsMap;

	public DataCollector() throws EchoNestException {
		en = new EchoNestAPI();
		en.setTraceSends(trace);
		en.setTraceRecvs(trace);
	}

	public DataCollector(String APIKey) throws EchoNestException {
		en = new EchoNestAPI(APIKey);
		en.setTraceSends(trace);
		en.setTraceRecvs(trace);
	}

	/**
	 * Via random walk on the echonest API finds:
	 * for every seed in seedNames, nArtists related artists to seed along with their nRelated related artists.
	 * It also appends them to files "data/artists.txt" and "data/relatedArtists.txt".
	 * @param nArtists number of artists related to the seed
	 * @param nRelated number of related artists for every artist
	 * @param seedNames the different starting points for the random walk
	 * @throws EchoNestException
	 * @throws FileNotFoundException
	 */
	public void randomWalkArtists(int nArtists, int nRelated, String... seedNames) throws EchoNestException, FileNotFoundException {
		try {

			// Create/open file for all aritsts
			File f_artists = new File("data/artists.txt");
			if(!f_artists.exists()) {
				f_artists.createNewFile();
			}
			importArtists(f_artists);

			// Create/open file for related artists
			File f_related = new File("data/relatedArtists.txt");
			if(!f_related.exists()) {
				f_related.createNewFile();
			}
			importRelatedArtists(f_related);
			
			// For all seed names, find nArtists related artists to seed along with their nRelated related artists
			for(String seedName : seedNames) {
				List<Artist> searched_artists = en.searchArtists(seedName);
				if (searched_artists.size() > 0) {
					Artist seed = searched_artists.get(0);
					for (int i = 0; i < nArtists; i++) {
						FileWriter fw_artists = new FileWriter(f_artists, true);
						FileWriter fw_related = new FileWriter(f_related, true);
						List<Artist> relatedArtists = seed.getSimilar(nRelated);

						// See if seed artist is already in list of artists with related artists
						if(!relatedArtistsMap.contains(seed.getID())) {
							
							// Write seed to related artists
							relatedArtistsMap.add(seed.getID());
							fw_related.write(seed.getID());
							System.out.println("Artist " + (i + 1) + ": " + seed.getName());
							
							// Write seed to artists
							if(!artistsMap.contains(seed.getID())) {
								artistsMap.add(seed.getID());
								fw_artists.write(seed.getID() + delimiter + seed.getName() + "\n");
							}
							
							// Write all related artists to related artists and artists
							for(Artist a : relatedArtists) {
								fw_related.write(delimiter + a.getID());
								if(!artistsMap.contains(a.getID())) {
									artistsMap.add(a.getID());
									fw_artists.write(a.getID() + delimiter + a.getName() + "\n");
								}
							}
							fw_related.write("\n");
						} else {
							// Decrement i to really get nArtists since current seed already exists among artists
							i--;
						}

						//Find new related artist which have not appeared yet.
						if (relatedArtists.size() > 0) {
							Collections.shuffle(relatedArtists);
							seed = relatedArtists.get(0);
						} else {
							seed = searched_artists.get(0);
						}
						
						fw_artists.close();
						fw_related.close();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void importRelatedArtists(File f_related) {
		try {
			Scanner sc = new Scanner(f_related);
			relatedArtistsMap = new HashSet<String>();
			while(sc.hasNextLine()) {
				String nextLine = sc.nextLine();
				String[] a = nextLine.split(delimiter);
				String id = a[0];
				relatedArtistsMap.add(id);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void importArtists(File f) {
		try {
			Scanner sc = new Scanner(f);
			artistsMap = new HashSet<String>();
			while(sc.hasNextLine()) {
				String nextLine = sc.nextLine();
				String[] a = nextLine.split(delimiter);
				String id = a[0];
				artistsMap.add(id);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
