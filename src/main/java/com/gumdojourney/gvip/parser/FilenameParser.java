package com.gumdojourney.gvip.parser;

import com.gumdojourney.gvip.model.Metadata;

import java.nio.file.Path;
import java.util.Optional;

public interface FilenameParser {
    Optional<Metadata> parse(Path filePath, String filename);
}
