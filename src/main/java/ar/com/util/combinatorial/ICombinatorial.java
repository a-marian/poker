package ar.com.util.combinatorial;

public interface ICombinatorial {
	
	public long combinations();
	
	public int size();
	
	public void clear();
	
	public int[] next(int[] items);
	
	public boolean hasNext();
	

}
