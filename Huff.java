import java.util.*;
import java.io.*;

public class Huff{
	
	String fileName;
	HashMap<Byte, HuffNode> map;
	PriorityQueue<HuffNode> queue;
	HashMap<Byte, ArrayList<Integer>> codes;
	HuffNode root;
	ArrayList<Integer> bits;
	
	public Huff(String file){
		fileName = file;
		map = new HashMap<Byte, HuffNode>();
		queue = new PriorityQueue<HuffNode>();
		codes = new HashMap<Byte, ArrayList<Integer>>();
	}
	
	public void countFrequencies() {
		for(int i = -128;i<=127;i++){
			HuffNode node = new HuffNode((byte)i);
			map.put((byte)i, node);
		}
         try (FileReader reader = new FileReader(fileName)) {
            int character;
            while ((character = reader.read()) != -1) {
                byte key = (byte)character;
				map.get(key).incrementFreq();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		Iterator<Byte> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			Byte key = iterator.next();
			if (map.get(key).getFrequency() == 0) {
				iterator.remove(); 
			}else{
			//	System.out.println(map.get(key));
				queue.add(map.get(key));
			}
		}
    }
	
	public void buildTree(){
		if(queue.size() == 1){
			root = queue.poll();
		} else if(queue.size() > 1){
			HuffNode parent = new HuffNode(queue.poll(), queue.poll());
			queue.add(parent);
			buildTree();
		}
	}
	
	public void getCodes(HuffNode node, ArrayList<Integer> code) {
	  if (node == null) {
            return;
        }else{
			
        ArrayList<Integer> leftCode = new ArrayList<Integer>(code);
		leftCode.add(0);
		if(!node.containsData()){
			getCodes(node.getLeftChild(), leftCode);
		}else{ 
			codes.put(node.getCharacter(), leftCode);
			return;
		}

        ArrayList<Integer> rightCode = new ArrayList<Integer>(code);
		rightCode.add(1);
		if(!node.containsData()){
			getCodes(node.getRightChild(), rightCode);
		}else{ 
			codes.put(node.getCharacter(), rightCode);
			return;
		}

    }
	}
	
	public void makeBitList(){
		try (FileReader reader = new FileReader(fileName)) {
            int character;
			bits = new ArrayList<Integer>();
            while ((character = reader.read()) != -1) {
                Byte key = (byte)character;
				for(int i = 0; i < codes.get(key).size();i++){
					bits.add(codes.get(key).get(i));
				}
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void setOutputFile(){
		try{
			File f = new File(fileName + ".huff");
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
			
			Integer numberOfZeros = 0;
			while(bits.size() % 8 != 0){
				bits.add(0);
				numberOfZeros++;
			}
			out.writeObject(numberOfZeros);
			out.writeObject(Twiddle.bitsToBytes(bits));
			out.writeObject(root);
			out.flush();
			out.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public HashMap<Byte, HuffNode> getMap(){
		return map;
	}
	
	public PriorityQueue<HuffNode> getQueue(){
		return queue;
	}
	
	public HashMap<Byte, ArrayList<Integer>> getCodes(){
		return codes;
	}
	
	public HuffNode getParent(){
		return root;
	}
	
	public static void printTree(HuffNode n){
		if(n == null){
			
		}else{
			System.out.println(n.getLeftChild() + " " + n.getRightChild());
			printTree(n.getLeftChild());
			printTree(n.getRightChild());
		}
		
	}
	
	public static void main(String [] args){
		
		String file = args[0];
		Huff h = new Huff(file);
		h.countFrequencies();	
		h.buildTree();
		ArrayList<Integer> list = new ArrayList<Integer>();
		h.getCodes(h.getParent(), list); 
		h.makeBitList();
		h.setOutputFile();
	}
}