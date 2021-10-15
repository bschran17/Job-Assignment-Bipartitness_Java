import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

class Main {

	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			int[] nAndM = readNandM(br);
			int n = nAndM[0];
			int m = nAndM[1];
			Map<Integer,ArrayList<Integer>> adj = new HashMap<>();
			adj = readSi(adj, m, n, br);
			int[] processors = performBFS(adj, n);
			boolean bipartite = checkBipartiteness(adj, processors);
			
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(System.out));
			writeOutput(bipartite, processors, wr, n);
		    
			br.close();
		    wr.flush();
		    wr.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	static int[] readNandM(BufferedReader br) throws Exception {
		String text = br.readLine();
	    String[] parts = text.split(" ");
	    int[] nAndM = {Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
	    return nAndM;
	}
	
	static Map<Integer,ArrayList<Integer>> readSi(Map<Integer,ArrayList<Integer>> adj, int m, int n, BufferedReader br) throws Exception{
		for (int i = 0; i <= n; i++) {
			ArrayList<Integer> arrayLst = new ArrayList<>();
			adj.put(i, arrayLst);
		}
		for (int j = 0; j < m; j++) {
			String text = br.readLine();
			String[] parts = text.split(" ");
			addEdge(adj, Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
		}
		return adj;
	}
	
	static void writeOutput(boolean bipartite, int[] processors, BufferedWriter wr, int n) throws Exception {
		int output = bipartite ? 1 : 0;
		wr.write(String.valueOf(output) + "\n");
		if (bipartite) {
			for (int i = 1; i <= n; i++) {
				wr.write(String.valueOf(processors[i] + "\n"));
			}
		}
	}
	
	static void addEdge(Map<Integer,ArrayList<Integer>> adj, int u, int v) {
			adj.get(u).add(v);
			adj.get(v).add(u);
	}
	
	static int[] performBFS(Map<Integer,ArrayList<Integer>> adj, int n) {
		boolean[] discovered = new boolean[n+1];
		int[] processors = new int[n+1];
		int start = 1;
		while (start != 0) {
			discovered[start] = true;
			processors[start] = 1;
			Map<Integer,ArrayList<Integer>> layers = new HashMap<Integer,ArrayList<Integer>>();
			ArrayList<Integer> arrayLst = new ArrayList<>();
			arrayLst.add(start);
			layers.put(0, arrayLst);
			int i = 0;
			while (!layers.get(i).isEmpty()) {
				ArrayList<Integer> new_layer = new ArrayList<Integer>();
				layers.put(i+1, new_layer);
				for (int u : layers.get(i)) {
					for(int v : adj.get(u)) {
						if (discovered[v] == false) {
							discovered[v] = true;
							new_layer.add(v);
							processors[v] = ((i+1) % 2) + 1;
						}
					}
				}
			i++;
			}
		start = checkAllDiscovered(discovered);
		}
		return processors;
	}
	
	static boolean checkBipartiteness(Map<Integer,ArrayList<Integer>> adj, int[] processors) {
		boolean condition = true;
		int i = 1;
		while (condition && adj.containsKey(i)) {
			for (int v : adj.get(i)) {
				condition = (condition && !(processors[i] == processors[v]));
			}
			i++;
		}
		return condition;
	}
	
	static int checkAllDiscovered(boolean[] discovered) {
		for (int i = 1; i < discovered.length; i++) {
			if (!discovered[i]) {
				return i;
			}
		}
		return 0;
	}
}
