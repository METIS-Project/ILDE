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

package org.restlet.test.ext.spring;

import org.restlet.Component;
import org.restlet.Server;
import org.restlet.test.RestletTestCase;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Unit test case for the Spring extension.
 * 
 * @author Jerome Louvel
 */
public class SpringTestCase extends RestletTestCase {

    public void testSpring() throws Exception {
        // Load the Spring container
        ClassPathResource resource = new ClassPathResource(
                "org/restlet/test/ext/spring/SpringTestCase.xml");
        BeanFactory factory = new XmlBeanFactory(resource);

        // Start the Restlet component
        Component component = (Component) factory.getBean("component");
        component.start();
        Thread.sleep(500);
        component.stop();
    }

    public void testSpringServerProperties() {
        ClassPathResource resource = new ClassPathResource(
                "org/restlet/test/ext/spring/SpringTestCase.xml");
        BeanFactory factory = new XmlBeanFactory(resource);

        Server server = (Server) factory.getBean("server");

        assertEquals("value1", server.getContext().getParameters()
                .getFirstValue("key1"));
        assertEquals("value2", server.getContext().getParameters()
                .getFirstValue("key2"));

    }

}
