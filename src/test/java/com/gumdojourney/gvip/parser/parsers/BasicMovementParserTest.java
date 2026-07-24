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
public class BasicMovementParserTest {

    public BasicMovementParserTest() {

    }

    @Test
    public void test1() {
        BasicMovementParser parser = new BasicMovementParser();
        String filename = "Basic movement [기본동작] commentary - Haidong Gumdo (2023-07-20 06.08).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("Basic Movement (2023-07) Commentary - Haidong Gumdo", md.getTitle());
        assertEquals("Basic movement [기본동작] commentary - Haidong Gumdo (2023-07-20 06.08)", md.getDescription());
        assertEquals("2023-07-20", md.getRecordingDate().toString());
        assertEquals(2, md.getPlaylists().size());
        assertEquals("Basic Movement", md.getPlaylists().get(0));
        assertEquals("Commentary", md.getPlaylists().get(1));
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }

    @Test
    public void test2() {
        BasicMovementParser parser = new BasicMovementParser();
        String filename = "Basic movement [기본동작] - Haidong Gumdo (2023-07-20 06.08).mp4";
        Optional<Metadata> mdOpt = parser.parse(null, filename);
        assertTrue(mdOpt.isPresent());

        Metadata md = mdOpt.get();
        assertEquals(filename, md.getSourceFilename());
        assertEquals("Basic Movement (2023-07) - Haidong Gumdo", md.getTitle());
        assertEquals("Basic movement [기본동작] - Haidong Gumdo (2023-07-20 06.08)", md.getDescription());
        assertEquals("2023-07-20", md.getRecordingDate().toString());
        assertEquals(1, md.getPlaylists().size());
        assertEquals("Basic Movement", md.getPlaylists().get(0));
        assertEquals(false, md.isMadeForKids());
        assertEquals(18, md.getTags().size());
    }
}