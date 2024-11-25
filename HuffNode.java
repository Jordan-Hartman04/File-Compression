import java.util.*;
import java.io.*;

public class HuffNode implements Comparable<HuffNode>, Serializable{
	
	byte character;
	int frequency;
	boolean data;
	HuffNode left;
	HuffNode right;
	HuffNode parent;
	
	public HuffNode(byte ch){
		data = true;
		character = ch;
		frequency = 0;
		parent = null;
		left = null;
		right = null;
	}
	
	public HuffNode(HuffNode leaf1, HuffNode leaf2){
		data = false;
		frequency = leaf1.getFrequency() + leaf2.getFrequency();
		if(leaf1.getFrequency() >= leaf2.getFrequency()){
			right = leaf1;
			left = leaf2;
		}else if(leaf1.getFrequency() <= leaf2.getFrequency()){
			right = leaf2;
			left = leaf1;
		}
		if(left != null) left.setParent(this);
		if(right != null) right.setParent(this);
	}
	
	public byte getCharacter(){
		return character;
	}
	
	public int getFrequency(){
		return frequency;
	}
	
	public HuffNode getParent(){
		return parent;
	}

	public HuffNode getLeftChild(){
		return left;
	}
	
	public HuffNode getRightChild(){
		return right;
	}
	
	public void incrementFreq(){
		frequency += 1;
	}
	
	public boolean containsData(){
		return data;
	}
	
	public int compareTo(HuffNode node){
		if(frequency < node.getFrequency()){
			return -1;
		}else if(frequency > node.getFrequency()){
			return 1;
		}else{
			return 0;
		}
	}
	
	public void setParent(HuffNode node){
		parent = node;
	}
	
	@Override
	public String toString(){
		return "[" + (char)character + " " + frequency + "]";
	}
	
}