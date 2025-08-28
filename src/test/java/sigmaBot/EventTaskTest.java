package sigmaBot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;;

public class EventTaskTest {

    @Test
    public void initFromStringTest() {
        EventTask task = EventTask.initFromString("eat /from 1212-12-12 /to 0420-42-42");
        
        assertEquals("eat", task.getDescription());
        assertEquals("1212-12-12", task.start);
        assertEquals("0420-42-42", task.end);
    }

    @Test
    public void getTaskIconTest() {
        EventTask task = EventTask.initFromString("sleep /from 1212-12-12 /to 0420-42-42");
        
        assertEquals("E", task.getTaskIcon());
    }

    @Test
    public void encodeSaveFormatTest() {
        EventTask task = EventTask.initFromString("poop /from 1212-12-12 /to 0420-42-42");
        String expected = "E,false,poop,1212-12-12,0420-42-42";
        
        assertEquals(expected, task.encodeSaveFormat());
    }

    @Test
    public void decodeSaveFormatTest() {
        String encoded = "E,false,code,1212-12-12,0420-42-42";
        EventTask task = EventTask.decodeSaveFormat(encoded);
        
        assertEquals("code", task.getDescription());
        assertEquals("1212-12-12", task.start);
        assertEquals("0420-42-42", task.end);
        assertFalse(task.isDone);
    }

    @Test
    public void toStringTest() {
        EventTask task = EventTask.initFromString("fly /from 1212-12-12 /to 0420-42-42");
        String expected = "[E][ ] fly (from: 1212-12-12 to: 0420-42-42)";
        
        assertEquals(expected, task.toString());
    }
}
