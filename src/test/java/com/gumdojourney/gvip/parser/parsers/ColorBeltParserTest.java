package com.gumdojourney.gvip.parser.parsers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.gumdojourney.gvip.model.Metadata;

/**
 *
 * @author Michael
 */
public class ColorBeltParserTest {

    public ColorBeltParserTest() {

    }

    @Test
    public void test1() {
        ColorBeltParser parser = new ColorBeltParser();
        String filename = "Color belt 1st self-defense form [유급자 첫번째 격검] commentary - Haidong Gumdo (2022-08-13 05.28).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("Color 1st Self-Defense (2022-08) Commentary - Haidong Gumdo", md.getTitle());
        assertEquals("Color belt 1st self-defense form [유급자 첫번째 격검] commentary - Haidong Gumdo (2022-08-13 05.28)", md.getDescription());
        assertEquals("2022-08-13", md.getRecordingDate().toString());
        assertEquals(2, md.getPlaylists().size());
        assertEquals("Color 1st Self-Defense", md.getPlaylists().get(0));
        assertEquals("Commentary", md.getPlaylists().get(1));   
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }

    @Test
    public void test2() {
        ColorBeltParser parser = new ColorBeltParser();
        String filename = "Color belt 1st self-defense form [유급자 첫번째 격검] - Haidong Gumdo (2022-08-13 05.28).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("Color 1st Self-Defense (2022-08) - Haidong Gumdo", md.getTitle());
        assertEquals("Color belt 1st self-defense form [유급자 첫번째 격검] - Haidong Gumdo (2022-08-13 05.28)", md.getDescription());
        assertEquals("2022-08-13", md.getRecordingDate().toString());
        assertEquals(1, md.getPlaylists().size());
        assertEquals("Color 1st Self-Defense", md.getPlaylists().get(0));
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }

    @Test
    public void test3() {
        ColorBeltParser parser = new ColorBeltParser();
        String filename = "Color belt 4th self-defense form [유급자 첫번째 격검] commentary - Haidong Gumdo (2022-08-13 05.28).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("Color 4th Self-Defense (2022-08) Commentary - Haidong Gumdo", md.getTitle());
        assertEquals("Color belt 4th self-defense form [유급자 첫번째 격검] commentary - Haidong Gumdo (2022-08-13 05.28)", md.getDescription());
        assertEquals("2022-08-13", md.getRecordingDate().toString());
        assertEquals(2, md.getPlaylists().size());
        assertEquals("Color 4th Self-Defense", md.getPlaylists().get(0));
        assertEquals("Commentary", md.getPlaylists().get(1));
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }


}