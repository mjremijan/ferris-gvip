package com.gumdojourney.gvip.parser;

import com.gumdojourney.gvip.model.Metadata;
import com.gumdojourney.gvip.parser.parsers.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParserManager {
    private final List<FilenameParser> parsers = new ArrayList<>();

    public ParserManager() {
        // register parsers in priority order
        parsers.add(new ColorBeltParser());
        parsers.add(new BlackBeltParser());
        parsers.add(new PersonalParser());
        parsers.add(new TwoHandedParser());
        parsers.add(new BasicMovementParser());
        parsers.add(new BeltTestParser());
    }

    public Optional<Metadata> parseFilename(Path filePath) {
        String filename = filePath.getFileName().toString();
        for (FilenameParser p : parsers) {
            Optional<Metadata> m = p.parse(filePath, filename);
            if (m.isPresent()) return m;
        }
        return Optional.empty();
    }
}
