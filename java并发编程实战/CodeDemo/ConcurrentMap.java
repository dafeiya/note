package test;

/**使用分段锁的map伪代码
 * 详细可参考ConcurrentHashMap
 * @author limeng
 */
public class ConcurrentMap {

	private final int DEFAULT_LOCKS=16;//默认的分段锁数量
	private final Object[] locks;//分段锁数组
	private final Node[] buckets;//map
	
	/**仅作示例
	 * @author T-lim1
	 */
	private static class  Node{
		private Object key;
		private Object value;
		public Node getNext(){
			return null;
		}
		
		public Object getKey() {
			return key;
		}
		public void setKey(Object key) {
			this.key = key;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
	}
	
	/**创建容器时一并创建分段锁
	 * @param size
	 */
	public ConcurrentMap(int size){
		this.buckets=new Node[size];
		this.locks=new Object[DEFAULT_LOCKS];
		for(int i=0;i<DEFAULT_LOCKS;i++){
			locks[i]=new Object();
		}
	}
	
	/**一种简单的散列算法
	 * @param key
	 * @return
	 */
	public final int hash(Object key){
		return Math.abs(key.hashCode()% this.DEFAULT_LOCKS); 
	}
	
	/**容器初始化时已经将容器中元素分配到相应的分段锁区间中，通过计算key的hash拿到该区间，
	 * 获取分段锁后仅遍历该区间下的元素来获取值
	 * @param key
	 * @return
	 */
	public Object get(Object key){
		int hash=hash(key);
		int lockIndex=hash%DEFAULT_LOCKS;
		synchronized(locks[lockIndex]){
			for(Node node=buckets[hash];node!=null;node.getNext()){ //for循环的灵活应用
				if(node.getKey().equals(key)){
					return node.getValue();
				}
			}
		}
		return null;
	}
	
	/**
	 * 删除所有元素
	 * 删除前需要先拿到分段锁
	 */
	public void clearAll(){
		for(int i=0;i<buckets.length;i++){
			synchronized(locks[i%DEFAULT_LOCKS]){
				buckets[i]=null;
			}
		}
	}
}
