package task2_1;

import task2_1.cache.Cache;

public class Runner {

    public static void main(String[] args) {
        
        Cache cache = new Cache(4,  0.3f);
        cache.print();
        cache.put("A", 1);
        cache.put("B", 2);
        cache.put("C", 3);
        cache.put("D", 4);
        cache.print();
        
        cache.get("B");
        cache.put("B", 5);        
        cache.get("B");
        cache.get("B");
        cache.get("B");
        cache.get("B");
        
        cache.get("C");
        cache.get("C");
        cache.get("C");
        cache.get("C");
        cache.get("C");
               
        cache.get("A");
        cache.get("A");
        cache.get("A");
        
        cache.get("D");
        cache.get("D");
        cache.get("D");
        cache.get("A");
        cache.get("A");
        cache.get("A");
        
        cache.put("V", 6);  
        cache.print();
        
        cache.remove("A"); 
        cache.put("V", 7);         
        cache.print();
        
        cache.put("K", 8);
        cache.print();
    }

}
