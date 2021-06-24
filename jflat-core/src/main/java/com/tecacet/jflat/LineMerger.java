/*
 Copyright 2008  TecaceT Ltd.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.tecacet.jflat;

/**
 * Merge a number of String fields in a single line.
 * Used by FlatFileWriter to compose a line of output.
 * 
 * @author Dimitri Papaioannou
 *
 */
public interface LineMerger {

    /**
     * Compose an array of String into a single output line
     * @param tokens individual tokens to be combined
     * @return a single line as a string
     */
    String makeLine(String[] tokens);
}
