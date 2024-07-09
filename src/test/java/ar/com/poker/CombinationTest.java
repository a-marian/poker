package ar.com.poker;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import ar.com.util.combinatorial.Combination;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNot.not;

@RunWith(value = Parameterized.class)
public class CombinationTest {
	
	
	private final int subItems;
	private final int items;
	private final int combinationsExpected;
	
	public CombinationTest(int subItems, int items, int combinationsEpected){
		this.subItems = subItems;
		this.items = items;
		this.combinationsExpected = combinationsEpected;
	}
	
	@Parameters
	public static Collection<Object>data(){
		Object data[][] = {
				{1, 1, 1},
				{1, 2, 2},
				{2, 2, 1},
				{1, 3, 3},
				{2, 3, 3},
				{3, 3, 1},
				{1, 4, 4},
				{2, 4, 6},
		        {3, 4, 4},
		        {4, 4, 1},
		        {1, 5, 5},
		        {2, 5, 10},
		        {5, 7, 21}
		};
		return Arrays.asList(data);
	}
	
	 @Test
	    public void testCombinationsStatic() {
	        System.out.println("combinations static: subItems:" + subItems + ", items: " + items + ", ExpectedResult: " + combinationsExpected);
	        
	        long result = Combination.combinations(subItems, items);
	        assertEquals(combinationsExpected, result);
	    }

	    @Test
	    public void testSize() {
	        System.out.println("size: " + subItems + ", " + items + ", " + combinationsExpected);
	        
	        Combination instance = new Combination(subItems, items);
	        int sizeResult = instance.size();
	        assertEquals(subItems, sizeResult);
	    }

	    @Test
	    public void testCombinations() {
	        System.out.println("combinations: " + subItems + ", " + items + ", " + combinationsExpected);
	        
	        Combination instance = new Combination(subItems, items);
	        long result = instance.combinations();
	        assertEquals(combinationsExpected, result);
	    }

	    @Test
	    public void tesHasNextFirst() {
	        System.out.println("hasNext(0): " + subItems + ", " + items + ", " + combinationsExpected);
	        
	        Combination instance = new Combination(subItems, items);
	        assertTrue(instance.hasNext());
	    }

	    @Test
	    public void testHasNextPreLast() {
	        System.out.println("hasNext(size -1): " + subItems + ", " + items + ", " + combinationsExpected);
	        
	        Combination instance = new Combination(subItems, items);
	        int[] indexes = new int[instance.size()];
	        for (int i = 0; i < combinationsExpected - 1; i++) {
	            instance.next(indexes);
	        }
	        assertTrue(instance.hasNext());
	    }

	    @Test
	    public void testHasNextLast() {
	        System.out.println("hashNext(last): " + subItems + ", " + items + ", " + combinationsExpected);
	        
	        Combination instance = new Combination(subItems, items);
	        int[] indexes = new int[instance.size()];
	        for (int i = 0; i < combinationsExpected; i++) {
	            instance.next(indexes);
	        }
	        assertFalse(instance.hasNext());
	    }

	    @Test
	    public void testHasNextAfterClear() {
	        System.out.println("hasNext() after clear(): " + subItems + ", " + items + ", " + combinationsExpected);

	        Combination instance = new Combination(subItems, items);
	        instance.clear();
	        assertTrue(instance.hasNext());
	    }

	    @Test
	    public void testHasNextAfterClearWithNext() {
	        System.out.println("hasNext() after next() and clear(): " + subItems + ", " + items + ", " + combinationsExpected);
	        
	        Combination instance = new Combination(subItems, items);
	        int indexes[] = new int[subItems];
	        instance.next(indexes);
	        instance.clear();
	        assertTrue(instance.hasNext());
	    }

	    @Test
	    public void testHasNextAfterFullLoopClear() {
	        System.out.println("hasNext() after next(combinations) and clear(): " + subItems + ", " + items + ", " + combinationsExpected);
	        
	        Combination instance = new Combination(subItems, items);
	        int indexes[] = new int[subItems];
	        for (int i = 0; i < combinationsExpected; i++) {
	            instance.next(indexes);
	        }
	        instance.clear();
	        assertTrue(instance.hasNext());
	    }

	    @Test
	    public void testNextItemsRange() {
	        System.out.println("next() in range: " + subItems + ", " + items + ", " + combinationsExpected);
	        
	        Combination instance = new Combination(subItems, items);
	        int[] indexes = new int[instance.size()];
	        for (int i = 0; i < combinationsExpected; i++) {
	            instance.next(indexes);

	            assertThat(indexes[0]).isBetween(0, items - 1);
	            for (int j = 1; j < subItems; j++) {
	                // Sort and range assert
	                assertThat(indexes[j]).isBetween(indexes[j - 1] + 1, items - 1);
	            }
	        }
	    }

	    @Test
	    public void testNextDontRepeat() {
	        System.out.println("next() doesn't repeat: " + subItems + ", " + items + ", " + combinationsExpected);
	        
	        Combination instance = new Combination(subItems, items);
	        int results[][] = new int[combinationsExpected][];
	        for (int i = 0; i < combinationsExpected; i++) {
	            results[i] = new int[subItems];
	            instance.next(results[i]);
	            for (int k = 0; k < i; k++) {
	                // No repeat previous combinations assert
	                assertThat(results[i], not(equalTo(results[k])));
	            }
	        }
	    }

	    @Test
	    public void testNextIgnored() {
	        System.out.println("next() ignored after next(combinations): " + subItems + ", " + items + ", " + combinationsExpected);

	        Combination instance = new Combination(subItems, items);
	        int indexes[] = new int[subItems];
	        int expIndexes[] = new int[subItems];
	        for (int i = 0; i < combinationsExpected; i++) {
	            instance.next(indexes);
	        }
	        Arrays.fill(indexes, -1);
	        Arrays.fill(expIndexes, -1);
	        instance.next(indexes);
	        assertArrayEquals(expIndexes, indexes);
	    }

	    @Test
	    public void testNextAfterClear() {
	        System.out.println("next() after next(combinations) and clear(): " + subItems + ", " + items + ", " + combinationsExpected);

	        Combination instance = new Combination(subItems, items);
	        instance.clear();
	        int expectIndexes[][] = new int[combinationsExpected][];
	        for (int i = 0; i < combinationsExpected; i++) {
	            expectIndexes[i] = new int[subItems];
	            instance.next(expectIndexes[i]);
	        }
	        instance.clear();
	        int[] indexes = new int[subItems];
	        for (int j = 0; j < combinationsExpected - 1; j++) {
	            instance.next(indexes);
	            // Check equals results for the same step
	            assertArrayEquals(expectIndexes[j], indexes);
	        }
	    }
	
	

}
