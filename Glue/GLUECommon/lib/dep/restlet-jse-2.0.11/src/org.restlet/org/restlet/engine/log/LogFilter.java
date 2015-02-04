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

package org.restlet.engine.log;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.engine.Engine;
import org.restlet.engine.component.ChildContext;
import org.restlet.routing.Filter;
import org.restlet.routing.Template;
import org.restlet.service.LogService;

/**
 * Filter logging all calls after their handling by the target Restlet. The
 * current format is similar to IIS 6 logs. The logging is based on the
 * java.util.logging package.
 * 
 * Concurrency note: instances of this class or its subclasses can be invoked by
 * several threads at the same time and therefore must be thread-safe. You
 * should be especially careful when storing state in member variables.
 * 
 * @author Jerome Louvel
 */
public class LogFilter extends Filter {
    /** The log service. */
    protected volatile LogService logService;

    /** The log template to use. */
    protected volatile Template logTemplate;

    /** The log service logger. */
    private volatile Logger logLogger;

    /**
     * Constructor.
     * 
     * @param context
     *            The context.
     * @param logService
     *            The log service descriptor.
     */
    public LogFilter(Context context, LogService logService) {
        super(context);
        this.logService = logService;

        if (logService != null) {
            this.logTemplate = (logService.getLogFormat() == null) ? null
                    : new Template(logService.getLogFormat());

            if (logService.getLoggerName() != null) {
                this.logLogger = Engine.getLogger(logService.getLoggerName());
            } else if ((context != null)
                    && (context.getLogger().getParent() != null)) {
                this.logLogger = Engine.getLogger(context.getLogger()
                        .getParent().getName()
                        + "."
                        + ChildContext.getBestClassName(logService.getClass()));
            } else {
                this.logLogger = Engine.getLogger(ChildContext
                        .getBestClassName(logService.getClass()));
            }
        }
    }

    /**
     * Allows filtering after processing by the next Restlet. Logs the call.
     * 
     * @param request
     *            The request to handle.
     * @param response
     *            The response to update.
     */
    @Override
    protected void afterHandle(Request request, Response response) {
        if (this.logLogger.isLoggable(Level.INFO)) {
            // Format the call into a log entry
            if (this.logTemplate != null) {
                this.logLogger.log(Level.INFO, format(request, response));
            } else {
                long startTime = (Long) request.getAttributes().get(
                        "org.restlet.startTime");
                int duration = (int) (System.currentTimeMillis() - startTime);
                this.logLogger.log(Level.INFO, formatDefault(request, response,
                        duration));
            }
        }
    }

    /**
     * Allows filtering before processing by the next Restlet. Saves the start
     * time.
     * 
     * @param request
     *            The request to handle.
     * @param response
     *            The response to update.
     * @return The continuation status.
     */
    @Override
    protected int beforeHandle(Request request, Response response) {
        request.getAttributes().put("org.restlet.startTime",
                System.currentTimeMillis());

        return CONTINUE;
    }

    /**
     * Format a log entry.
     * 
     * @param request
     *            The request to log.
     * @param response
     *            The response to log.
     * @return The formatted log entry.
     */
    protected String format(Request request, Response response) {
        return this.logTemplate.format(request, response);
    }

    /**
     * Format a log entry using the default format.
     * 
     * @param request
     *            The request to log.
     * @param response
     *            The response to log.
     * @param duration
     *            The call duration (in milliseconds).
     * @return The formatted log entry.
     */
    protected String formatDefault(Request request, Response response,
            int duration) {
        StringBuilder sb = new StringBuilder();
        long currentTime = System.currentTimeMillis();

        // Append the date of the request
        sb.append(String.format("%tF", currentTime));
        sb.append('\t');

        // Append the time of the request
        sb.append(String.format("%tT", currentTime));
        sb.append('\t');

        // Append the client IP address
        String clientAddress = request.getClientInfo().getUpstreamAddress();
        sb.append((clientAddress == null) ? "-" : clientAddress);
        sb.append('\t');

        // Append the user name (via IDENT protocol)
        if (this.logService.isIdentityCheck()) {
            IdentClient ic = new IdentClient(request.getClientInfo()
                    .getUpstreamAddress(), request.getClientInfo().getPort(),
                    response.getServerInfo().getPort());
            sb.append((ic.getUserIdentifier() == null) ? "-" : ic
                    .getUserIdentifier());
        } else if ((request.getChallengeResponse() != null)
                && (request.getChallengeResponse().getIdentifier() != null)) {
            sb.append(request.getChallengeResponse().getIdentifier());
        } else {
            sb.append('-');
        }
        sb.append('\t');

        // Append the server IP address
        String serverAddress = response.getServerInfo().getAddress();
        sb.append((serverAddress == null) ? "-" : serverAddress);
        sb.append('\t');

        // Append the server port
        Integer serverport = response.getServerInfo().getPort();
        sb.append((serverport == null) ? "-" : serverport.toString());
        sb.append('\t');

        // Append the method name
        String methodName = (request.getMethod() == null) ? "-" : request
                .getMethod().getName();
        sb.append((methodName == null) ? "-" : methodName);

        // Append the resource path
        sb.append('\t');
        String resourcePath = (request.getResourceRef() == null) ? "-"
                : request.getResourceRef().getPath();
        sb.append((resourcePath == null) ? "-" : resourcePath);

        // Append the resource query
        sb.append('\t');
        String resourceQuery = (request.getResourceRef() == null) ? "-"
                : request.getResourceRef().getQuery();
        sb.append((resourceQuery == null) ? "-" : resourceQuery);

        // Append the status code
        sb.append('\t');
        sb.append((response.getStatus() == null) ? "-" : Integer
                .toString(response.getStatus().getCode()));

        // Append the returned size
        sb.append('\t');

        if (!response.isEntityAvailable()
                || Status.REDIRECTION_NOT_MODIFIED.equals(response.getStatus())
                || Status.SUCCESS_NO_CONTENT.equals(response.getStatus())
                || Method.HEAD.equals(request.getMethod())) {
            sb.append('0');
        } else {
            sb.append((response.getEntity().getSize() == -1) ? "-" : Long
                    .toString(response.getEntity().getSize()));
        }

        // Append the received size
        sb.append('\t');

        if (request.getEntity() == null) {
            sb.append('0');
        } else {
            sb.append((request.getEntity().getSize() == -1) ? "-" : Long
                    .toString(request.getEntity().getSize()));
        }

        // Append the duration
        sb.append('\t');
        sb.append(duration);

        // Append the host reference
        sb.append('\t');
        sb.append((request.getHostRef() == null) ? "-" : request.getHostRef()
                .toString());

        // Append the agent name
        sb.append('\t');
        String agentName = request.getClientInfo().getAgent();
        sb.append((agentName == null) ? "-" : agentName);

        // Append the referrer
        sb.append('\t');
        sb.append((request.getReferrerRef() == null) ? "-" : request
                .getReferrerRef().getIdentifier());

        return sb.toString();
    }

}
