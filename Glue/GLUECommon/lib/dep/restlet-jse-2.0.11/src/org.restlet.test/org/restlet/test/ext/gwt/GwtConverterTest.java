/**
 * Copyright 2005-2011 Noelios Technologies.
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

package org.restlet.test.ext.gwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.restlet.ext.gwt.GwtConverter;
import org.restlet.representation.Representation;
import org.restlet.test.RestletTestCase;

/**
 * Tests basic conversion using the GwtConverter.
 * 
 * @author Thierry Boileau
 */
public class GwtConverterTest extends RestletTestCase {

    public void testObjectToRepresentation() throws IOException {
        MyBean myBean = new MyBean();
        myBean.setList(new ArrayList<String>());
        myBean.getList().add("list1");
        myBean.getList().add("list2");
        myBean.setMap(new HashMap<String, String>());
        myBean.getMap().put("key1", "value1");
        myBean.getMap().put("key2", "value2");
        myBean.setName("myname");
        Representation rep = new GwtConverter().toRepresentation(myBean, null,
                null);

        assertNotNull(rep);
        String test = rep.getText();
        System.out.println(test);
        assertTrue(test.contains("myname"));
        assertTrue(test.contains("list2"));
        assertTrue(test.contains("key1"));
        assertTrue(test.contains("value2"));
    }

}
