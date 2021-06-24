package com.tecacet.jflat.impl.io;

import com.tecacet.jflat.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class SequentialResourceLoader implements ResourceLoader {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ResourceLoader[] loaders;

    public SequentialResourceLoader(ResourceLoader... loaders) {
        this.loaders = loaders;
    }

    @Override
    public InputStream loadResource(String path) throws IOException {
        for (ResourceLoader loader : loaders) {
            try {
                return loader.loadResource(path);
            } catch (IOException ioe) {
                logger.debug("Failed to load resource {} using loader {}",
                        path, loader.getClass());
            }
        }
        throw new IOException("Could not load resource " + path);

    }
}
