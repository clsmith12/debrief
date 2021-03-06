/**
 * Copyright 2005-2010 Noelios Technologies.
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL 1.0 (the
 * "Licenses"). You can select the license that you prefer but you may not use
 * this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0.html
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1.php
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1.php
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0.php
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.noelios.com/products/restlet-engine
 * 
 * Restlet is a registered trademark of Noelios Technologies.
 */

package org.restlet.test.engine;

import junit.framework.TestCase;

import org.restlet.engine.http.header.ContentType;

/**
 * Test {@link ContentType}
 * 
 * @author Jerome Louvel
 */
public class ContentTypeTestCase extends TestCase {

    public void testParsingInvalid() {
        String h1 = "application/docbook+xml; version='my version 1.0'";

        try {
            new ContentType(h1);
            fail("Shouldn't reach this point");
        } catch (IllegalArgumentException iae) {
            // OK
        }
    }

    public void testParsing() {
        String h1 = "application/docbook+xml; version=\"my version 1.0\"";
        String h2 = "application/docbook+xml; version='my%20version%201.0'";

        ContentType ct1 = new ContentType(h1);
        ContentType ct2 = new ContentType(h2);

        assertEquals(h1, ct1.getMediaType().getName());
        assertEquals("my version 1.0", ct1.getMediaType().getParameters()
                .getFirstValue("version"));

        assertEquals(h2, ct2.getMediaType().getName());
        assertEquals("'my%20version%201.0'", ct2.getMediaType().getParameters()
                .getFirstValue("version"));
    }

}
