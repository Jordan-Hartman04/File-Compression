import java.util.*;
import java.io.*;

public class Puff{
	
	private StringBuilder fileString;
	
	private String fileName;
	private FileInputStream inFile;
	private ObjectInputStream in;
	private byte[] bigBits;
	private Integer numberOfZeros;
	private HuffNode root;
	private ArrayList<Integer> bits;
	private ArrayList<Integer> list = new ArrayList<Integer>();
	
	
	public Puff(String file){
		try{ 
		
		fileName = file;
		inFile = new FileInputStream(fileName);
		in = new ObjectInputStream(inFile);
		
		numberOfZeros = (Integer) in.readObject();
		bigBits = (byte[]) in.readObject();
		root = (HuffNode) in.readObject();
		fileString = new StringBuilder();
		bits = new ArrayList<Integer>(Twiddle.bytesToBits(bigBits));
		bits.subList((bits.size()-1)-numberOfZeros, bits.size()-1);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	public HuffNode getRoot(){
		return root;
	}
	
	public ArrayList<Integer> getList(){
		return list;
	}
	
	public StringBuilder getString(){
		return fileString;
	}
	
	public void decoder(HuffNode node, Integer index){
		if(index >= bits.size()) return;
		else{
			if(!node.containsData()){
			if(bits.get(index) == 0){
				decoder(node.getLeftChild(), index + 1);
				
			}else{
				decoder(node.getRightChild(), index + 1);
			}
		}else if(node.containsData()){
			fileString.append((char)node.getCharacter());
			if(index < bits.size()){
				decoder(root, index + 1);
			}
		}
	}
	}
	
	public void puffFile(){
		String putInFile = fileString.toString();
		try{
			File f = new File(fileName.substring(0, fileName.lastIndexOf(".huff")) + ".puff");
			FileOutputStream out = new FileOutputStream(f);
			PrintWriter write = new PrintWriter(new OutputStreamWriter(out));
			
			write.print(putInFile);
			write.flush();
			write.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static void main(String [] args){
		String file = args[0];
		Puff p = new Puff(file);
		p.decoder(p.getRoot(), 0);
		p.puffFile();
	}
}