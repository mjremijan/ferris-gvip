package com.gumdojourney.gvip.parser.parsers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
 
/**
 *
 * @author Michael
 */
public class BlackBeltParserTest {

    public BlackBeltParserTest() {

    }

    @Test
    public void testParseBlackBelt() {
        BlackBeltParser parser = new BlackBeltParser();
        String filename = "Black belt 1st Dan 2nd self-defense form [유단자 1단 두번째 격검] commentary - Haidong Gumdo (2026-04-05 14.15).mp4";
        assertTrue(parser.parse(null, filename).isPresent());
    }


}