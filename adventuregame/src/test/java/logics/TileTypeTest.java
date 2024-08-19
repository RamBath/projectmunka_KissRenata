package logics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TileTypeTest {

    @Test
    public void testGetByCode() {
        // Test valid codes
        assertEquals(TileType.TYPE_01, TileType.getByCode("01"));
        assertEquals(TileType.TYPE_02, TileType.getByCode("02"));
        assertEquals(TileType.TYPE_03, TileType.getByCode("03"));
        assertEquals(TileType.TYPE_04, TileType.getByCode("04"));
        assertEquals(TileType.TYPE_05, TileType.getByCode("05"));
        assertEquals(TileType.TYPE_06, TileType.getByCode("06"));
        assertEquals(TileType.TYPE_07, TileType.getByCode("07"));
        assertEquals(TileType.TYPE_08, TileType.getByCode("08"));
        assertEquals(TileType.TYPE_09, TileType.getByCode("09"));
        assertEquals(TileType.TYPE_10, TileType.getByCode("10"));
        assertEquals(TileType.TYPE_11, TileType.getByCode("11"));
        assertEquals(TileType.TYPE_12, TileType.getByCode("12"));
        assertEquals(TileType.TYPE_13, TileType.getByCode("13"));
        assertEquals(TileType.TYPE_14, TileType.getByCode("14"));
        assertEquals(TileType.TYPE_15, TileType.getByCode("15"));

        // Test invalid code
        assertNull(TileType.getByCode("99"));
        assertNull(TileType.getByCode(null));
    }

    @Test
    public void testGetCode() {
        assertEquals("01", TileType.TYPE_01.getCode());
        assertEquals("02", TileType.TYPE_02.getCode());
        // Add additional assertions as needed for all types
    }

    @Test
    public void testGetDirections() {
        assertArrayEquals(new boolean[]{true, true, true, true}, TileType.TYPE_01.getDirections());
        assertArrayEquals(new boolean[]{true, true, true, false}, TileType.TYPE_02.getDirections());
        // Add additional assertions as needed for all types
    }

    @Test
    public void testGetImage() {
        assertEquals("tile00.png", TileType.TYPE_01.getImage());
        assertEquals("tile04.png", TileType.TYPE_02.getImage());
        // Add additional assertions as needed for all types
    }
}
