public class Datashare {
	    private int value;

	    public Datashare () {
	        value = 0;
	    }

	    public synchronized boolean setValue(int val) {
	        value = val;
	        return true;        
	    }

	    public synchronized int getValue() {
	        int val = value;
	        return val;
	    }    
}