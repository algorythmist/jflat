package com.tecacet.jflat.io;

import com.tecacet.jflat.ResourceLoader;
import com.tecacet.jflat.impl.io.ClasspathResourceLoader;
import com.tecacet.jflat.impl.io.FileSystemResourceLoader;
import com.tecacet.jflat.impl.io.SequentialResourceLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SequentialResourceLoaderTest {

    @Test
    void loadResourceFromFile() throws IOException {
        ResourceLoader resourceLoader = new SequentialResourceLoader(new FileSystemResourceLoader(),
                new ClasspathResourceLoader());
        InputStream is = resourceLoader.loadResource("src/test/data/resource.txt");
        assertNotNull(is);
    }

    @Test
    void loadResourceFromClasspath() throws IOException {
        ResourceLoader resourceLoader = new SequentialResourceLoader(new FileSystemResourceLoader(),
                new ClasspathResourceLoader());
        InputStream is = resourceLoader.loadResource("resource.txt");
        assertNotNull(is);
    }
}
