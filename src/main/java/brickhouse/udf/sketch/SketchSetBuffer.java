package brickhouse.udf.sketch;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator.AggregationBuffer;

import brickhouse.analytics.uniques.SketchSet;

class SketchSetBuffer implements AggregationBuffer {
	private SketchSet sketchSet = null;
	

	public void init(int size) {
		if( sketchSet == null || 
		    ((sketchSet.getMaxItems() != size) && (size != -1))) {
			sketchSet = new SketchSet( size);
		} else {
			sketchSet.clear();
		}
	}
	public void reset() {
	  sketchSet.clear();
	}
	
	public int getSize() {
	  if ( sketchSet != null) {
	      return sketchSet.getMaxItems();
	  } else {
	    return -1;
	  }
	}
	
	public List<String> getSketchItems() {
       return sketchSet.getMinHashItems();
	}
	
    public Map<BigInteger,String> getPartialMap() {
	    Map<BigInteger,String> partial =  sketchSet.getHashItemMap();
	    partial.put( BigInteger.valueOf(sketchSet.getMaxItems()), SketchSetUDAF.SKETCH_SIZE_STR);
	    return partial;
    }
    
    public void addItem( String str) {
       sketchSet.addItem( str) ;
    }
    public void addHash( Long hash, String str) {
    	sketchSet.addHashItem( SketchSet.LongToByteArr(hash), str );
    }
    
    public void addHash( byte[] hash, String str) {
    	sketchSet.addHashItem( hash, str );
    }
}