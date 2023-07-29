import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CommonTests {
    @Test
    fun testIntersectYes() {
        assertTrue(intersect(Coords(0, 0), Coords(0, 0), Coords(0, 0), Coords(0, 0)))
        assertTrue(intersect(Coords(0, 0), Coords(0, 1), Coords(0, 1), Coords(1, 1)))
        assertTrue(intersect(Coords(1, 0), Coords(0, 0), Coords(0, 1), Coords(0, 0)))
        assertTrue(intersect(Coords(0, 0), Coords(1, 1), Coords(1, 0), Coords(0, 1)))
        assertTrue(intersect(Coords(0, 0), Coords(2, 2), Coords(3, 3), Coords(1, 1)))
        assertTrue(intersect(Coords(0, 0), Coords(2, 2), Coords(1, 1), Coords(2, 0)))
    }

    @Test
    fun testIntersectNo() {
        assertFalse(intersect(Coords(0, 0), Coords(0, 0), Coords(1, 1), Coords(1, 1)))
        assertFalse(intersect(Coords(2, 0), Coords(3, 0), Coords(1, 0), Coords(0, 0)))
        assertFalse(intersect(Coords(0, 0), Coords(1, 1), Coords(1, 0), Coords(2, 2)))
        assertFalse(intersect(Coords(0, 0), Coords(1, 0), Coords(0, 1), Coords(1, 1)))
    }
}
