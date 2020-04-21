package com.varrock.tools;

public class CouchbasePlayerConverter {

	public static void main(String[] args) {
		
		/*CouchbaseManager.initialize();
		
		File[] characters = new File("data/saves/characters").listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(".json");
			}
			
		});
		
		for(File character : characters) {
			
			String content = null;
			
			try(BufferedReader br = new BufferedReader(new FileReader(Paths.get("data/saves/characters", character.getName()).toString()))) {
			   
				StringBuilder sb = new StringBuilder();
			    String line = br.readLine();

			    while(line != null) {
			        sb.append(line);
			        sb.append(System.lineSeparator());
			        line = br.readLine();
			    }
			    
			    content = sb.toString();
			    
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JsonObject user = JsonObject.fromJson(content);
			String name = user.getString("username");
			
			PlayerBucket.getSingleton().store(name, content);
			
			System.out.println("Convered: "+name);
			
		}*/
		
	}
	
}
