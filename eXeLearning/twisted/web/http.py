# -*- test-case-name: twisted.web.test.test_http -*-

# Copyright (c) 2001-2004 Twisted Matrix Laboratories.
# See LICENSE for details.


"""HyperText Transfer Protocol implementation.

This is used by twisted.web.

API Stability: stable

Future Plans:
 - HTTP client support will at some point be refactored to support HTTP/1.1.
 - Accept chunked data from clients in server.
 - Other missing HTTP features from the RFC.

Maintainer: U{Itamar Shtull-Trauring<mailto:twisted@itamarst.org>}
"""

# system imports
from cStringIO import StringIO
import tempfile
import base64, binascii
import cgi
import socket
import math
import time
import calendar
import warnings
import os

from zope.interface import implements

# twisted imports
from twisted.internet import interfaces, reactor, protocol, address, task
from twisted.protocols import policies, basic
from twisted.python import log, components
try: # try importing the fast, C version
    from twisted.protocols._c_urlarg import unquote
except ImportError:
    from urllib import unquote


protocol_version = "HTTP/1.1"

_CONTINUE = 100
SWITCHING = 101

OK                              = 200
CREATED                         = 201
ACCEPTED                        = 202
NON_AUTHORITATIVE_INFORMATION   = 203
NO_CONTENT                      = 204
RESET_CONTENT                   = 205
PARTIAL_CONTENT                 = 206
MULTI_STATUS                    = 207

MULTIPLE_CHOICE                 = 300
MOVED_PERMANENTLY               = 301
FOUND                           = 302
SEE_OTHER                       = 303
NOT_MODIFIED                    = 304
USE_PROXY                       = 305
TEMPORARY_REDIRECT              = 307

BAD_REQUEST                     = 400
UNAUTHORIZED                    = 401
PAYMENT_REQUIRED                = 402
FORBIDDEN                       = 403
NOT_FOUND                       = 404
NOT_ALLOWED                     = 405
NOT_ACCEPTABLE                  = 406
PROXY_AUTH_REQUIRED             = 407
REQUEST_TIMEOUT                 = 408
CONFLICT                        = 409
GONE                            = 410
LENGTH_REQUIRED                 = 411
PRECONDITION_FAILED             = 412
REQUEST_ENTITY_TOO_LARGE        = 413
REQUEST_URI_TOO_LONG            = 414
UNSUPPORTED_MEDIA_TYPE          = 415
REQUESTED_RANGE_NOT_SATISFIABLE = 416
EXPECTATION_FAILED              = 417

INTERNAL_SERVER_ERROR           = 500
NOT_IMPLEMENTED                 = 501
BAD_GATEWAY                     = 502
SERVICE_UNAVAILABLE             = 503
GATEWAY_TIMEOUT                 = 504
HTTP_VERSION_NOT_SUPPORTED      = 505
INSUFFICIENT_STORAGE_SPACE      = 507
NOT_EXTENDED                    = 510

RESPONSES = {
    # 100
    _CONTINUE: "Continue",
    SWITCHING: "Switching Protocols",

    # 200
    OK: "OK",
    CREATED: "Created",
    ACCEPTED: "Accepted",
    NON_AUTHORITATIVE_INFORMATION: "Non-Authoritative Information",
    NO_CONTENT: "No Content",
    RESET_CONTENT: "Reset Content.",
    PARTIAL_CONTENT: "Partial Content",
    MULTI_STATUS: "Multi-Status",

    # 300
    MULTIPLE_CHOICE: "Multiple Choices",
    MOVED_PERMANENTLY: "Moved Permanently",
    FOUND: "Found",
    SEE_OTHER: "See Other",
    NOT_MODIFIED: "Not Modified",
    USE_PROXY: "Use Proxy",
    # 306 not defined??
    TEMPORARY_REDIRECT: "Temporary Redirect",

    # 400
    BAD_REQUEST: "Bad Request",
    UNAUTHORIZED: "Unauthorized",
    PAYMENT_REQUIRED: "Payment Required",
    FORBIDDEN: "Forbidden",
    NOT_FOUND: "Not Found",
    NOT_ALLOWED: "Method Not Allowed",
    NOT_ACCEPTABLE: "Not Acceptable",
    PROXY_AUTH_REQUIRED: "Proxy Authentication Required",
    REQUEST_TIMEOUT: "Request Time-out",
    CONFLICT: "Conflict",
    GONE: "Gone",
    LENGTH_REQUIRED: "Length Required",
    PRECONDITION_FAILED: "Precondition Failed",
    REQUEST_ENTITY_TOO_LARGE: "Request Entity Too Large",
    REQUEST_URI_TOO_LONG: "Request-URI Too Long",
    UNSUPPORTED_MEDIA_TYPE: "Unsupported Media Type",
    REQUESTED_RANGE_NOT_SATISFIABLE: "Requested Range not satisfiable",
    EXPECTATION_FAILED: "Expectation Failed",

    # 500
    INTERNAL_SERVER_ERROR: "Internal Server Error",
    NOT_IMPLEMENTED: "Not Implemented",
    BAD_GATEWAY: "Bad Gateway",
    SERVICE_UNAVAILABLE: "Service Unavailable",
    GATEWAY_TIMEOUT: "Gateway Time-out",
    HTTP_VERSION_NOT_SUPPORTED: "HTTP Version not supported",
    INSUFFICIENT_STORAGE_SPACE: "Insufficient Storage Space",
    NOT_EXTENDED: "Not Extended"
    }

CACHED = """Magic constant returned by http.Request methods to set cache
validation headers when the request is conditional and the value fails
the condition."""

# backwards compatability
responses = RESPONSES


# datetime parsing and formatting
weekdayname = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
monthname = [None,
             'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
             'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']

def parse_qs(qs, keep_blank_values=0, strict_parsing=0, unquote=unquote):
    """like cgi.parse_qs, only with custom unquote function"""
    d = {}
    items = [s2 for s1 in qs.split("&") for s2 in s1.split(";")]
    for item in items:
        try:
            k, v = item.split("=", 1)
        except ValueError:
            if strict_parsing:
                raise
            continue
        if v or keep_blank_values:
            k = unquote(k.replace("+", " "))
            v = unquote(v.replace("+", " "))
            if k in d:
                d[k].append(v)
            else:
                d[k] = [v]
    return d

def datetimeToString(msSinceEpoch=None):
    """Convert seconds since epoch to HTTP datetime string."""
    if msSinceEpoch == None:
        msSinceEpoch = time.time()
    year, month, day, hh, mm, ss, wd, y, z = time.gmtime(msSinceEpoch)
    s = "%s, %02d %3s %4d %02d:%02d:%02d GMT" % (
        weekdayname[wd],
        day, monthname[month], year,
        hh, mm, ss)
    return s

def datetimeToLogString(msSinceEpoch=None):
    """Convert seconds since epoch to log datetime string."""
    if msSinceEpoch == None:
        msSinceEpoch = time.time()
    year, month, day, hh, mm, ss, wd, y, z = time.gmtime(msSinceEpoch)
    s = "[%02d/%3s/%4d:%02d:%02d:%02d +0000]" % (
        day, monthname[month], year,
        hh, mm, ss)
    return s


# a hack so we don't need to recalculate log datetime every hit,
# at the price of a small, unimportant, inaccuracy.
_logDateTime = None
_logDateTimeUsers = 0
_resetLogDateTimeID = None

def _resetLogDateTime():
    global _logDateTime
    global _resetLogDateTime
    global _resetLogDateTimeID
    _logDateTime = datetimeToLogString()
    _resetLogDateTimeID = reactor.callLater(1, _resetLogDateTime)

def _logDateTimeStart():
    global _logDateTimeUsers
    if not _logDateTimeUsers:
        _resetLogDateTime()
    _logDateTimeUsers += 1

def _logDateTimeStop():
    global _logDateTimeUsers
    _logDateTimeUsers -= 1;
    if (not _logDateTimeUsers and _resetLogDateTimeID
        and _resetLogDateTimeID.active()):
        _resetLogDateTimeID.cancel()

def timegm(year, month, day, hour, minute, second):
    """Convert time tuple in GMT to seconds since epoch, GMT"""
    EPOCH = 1970
    assert year >= EPOCH
    assert 1 <= month <= 12
    days = 365*(year-EPOCH) + calendar.leapdays(EPOCH, year)
    for i in range(1, month):
        days = days + calendar.mdays[i]
    if month > 2 and calendar.isleap(year):
        days = days + 1
    days = days + day - 1
    hours = days*24 + hour
    minutes = hours*60 + minute
    seconds = minutes*60 + second
    return seconds

def stringToDatetime(dateString):
    """Convert an HTTP date string to seconds since epoch."""
    parts = dateString.split(' ')
    day = int(parts[1])
    month = int(monthname.index(parts[2]))
    year = int(parts[3])
    hour, min, sec = map(int, parts[4].split(':'))
    return int(timegm(year, month, day, hour, min, sec))

def toChunk(data):
    """Convert string to a chunk.
    
    @returns: a tuple of strings representing the chunked encoding of data"""
    return ("%x\r\n" % len(data), data, "\r\n")

def fromChunk(data):
    """Convert chunk to string.

    @returns: tuple (result, remaining), may raise ValueError.
    """
    prefix, rest = data.split('\r\n', 1)
    length = int(prefix, 16)
    if not rest[length:length+2] == '\r\n':
        raise ValueError, "chunk must end with CRLF"
    return rest[:length], rest[length+2:]


def parseContentRange(header):
    """Parse a content-range header into (start, end, realLength).

    realLength might be None if real length is not known ('*').
    """
    kind, other = header.strip().split()
    if kind.lower() != "bytes":
        raise ValueError, "a range of type %r is not supported"
    startend, realLength = other.split("/")
    start, end = map(int, startend.split("-"))
    if realLength == "*":
        realLength = None
    else:
        realLength = int(realLength)
    return (start, end, realLength)


class StringTransport:
    """
    I am a StringIO wrapper that conforms for the transport API. I support
    the `writeSequence' method.
    """
    def __init__(self):
        self.s = StringIO()
    def writeSequence(self, seq):
        self.s.write(''.join(seq))
    def __getattr__(self, attr):
        return getattr(self.__dict__['s'], attr)


class HTTPClient(basic.LineReceiver):
    """A client for HTTP 1.0

    Notes:
    You probably want to send a 'Host' header with the name of
    the site you're connecting to, in order to not break name
    based virtual hosting.
    """
    length = None
    firstLine = 1
    __buffer = ''

    def sendCommand(self, command, path):
        self.transport.write('%s %s HTTP/1.0\r\n' % (command, path))

    def sendHeader(self, name, value):
        self.transport.write('%s: %s\r\n' % (name, value))

    def endHeaders(self):
        self.transport.write('\r\n')

    def lineReceived(self, line):
        if self.firstLine:
            self.firstLine = 0
            l = line.split(None, 2)
            version = l[0]
            status = l[1]
            try:
                message = l[2]
            except IndexError:
                # sometimes there is no message
                message = ""
            self.handleStatus(version, status, message)
            return
        if line:
            key, val = line.split(':', 1)
            val = val.lstrip()
            self.handleHeader(key, val)
            if key.lower() == 'content-length':
                self.length = int(val)
        else:
            self.handleEndHeaders()
            self.setRawMode()

    def connectionLost(self, reason):
        self.handleResponseEnd()

    def handleResponseEnd(self):
        if self.__buffer != None:
            b = self.__buffer
            self.__buffer = None
            self.handleResponse(b)
    
    def handleResponsePart(self, data):
        self.__buffer += data

    def connectionMade(self):
        pass

    handleStatus = handleHeader = handleEndHeaders = lambda *args: None

    def rawDataReceived(self, data):
        if self.length is not None:
            data, rest = data[:self.length], data[self.length:]
            self.length -= len(data)
        else:
            rest = ''
        self.handleResponsePart(data)
        if self.length == 0:
            self.handleResponseEnd()
            self.setLineMode(rest)


# response codes that must have empty bodies
NO_BODY_CODES = (204, 304)

class Request:
    """A HTTP request.

    Subclasses should override the process() method to determine how
    the request will be processed.
    
    @ivar method: The HTTP method that was used.
    @ivar uri: The full URI that was requested (includes arguments).
    @ivar path: The path only (arguments not included).
    @ivar args: All of the arguments, including URL and POST arguments.
    @type args: A mapping of strings (the argument names) to lists of values.
                i.e., ?foo=bar&foo=baz&quux=spam results in
                {'foo': ['bar', 'baz'], 'quux': ['spam']}.
    @ivar received_headers: All received headers
    """

    implements(interfaces.IConsumer)

    producer = None
    finished = 0
    code = OK
    code_message = RESPONSES[OK]
    method = "(no method yet)"
    clientproto = "(no clientproto yet)"
    uri = "(no uri yet)"
    startedWriting = 0
    chunked = 0
    sentLength = 0 # content-length of response, or total bytes sent via chunking
    etag = None
    lastModified = None
    _forceSSL = 0
    
    def __init__(self, channel, queued):
        """
        @param channel: the channel we're connected to.
        @param queued: are we in the request queue, or can we start writing to
            the transport?
        """
        self.channel = channel
        self.queued = queued
        self.received_headers = {}
        self.received_cookies = {}
        self.headers = {} # outgoing headers
        self.cookies = [] # outgoing cookies

        if queued:
            self.transport = StringTransport()
        else:
            self.transport = self.channel.transport

    def _cleanup(self):
        """Called when have finished responding and are no longer queued."""
        if self.producer:
            log.err(RuntimeError("Producer was not unregistered for %s" % self.uri))
            self.unregisterProducer()
        self.channel.requestDone(self)
        del self.channel
        try:
            self.content.close()
        except OSError:
            # win32 suckiness, no idea why it does this
            pass
        del self.content

    # methods for channel - end users should not use these

    def noLongerQueued(self):
        """Notify the object that it is no longer queued.

        We start writing whatever data we have to the transport, etc.

        This method is not intended for users.
        """
        if not self.queued:
            raise RuntimeError, "noLongerQueued() got called unnecessarily."

        self.queued = 0

        # set transport to real one and send any buffer data
        data = self.transport.getvalue()
        self.transport = self.channel.transport
        if data:
            self.transport.write(data)

        # if we have producer, register it with transport
        if (self.producer is not None) and not self.finished:
            self.transport.registerProducer(self.producer, self.streamingProducer)

        # if we're finished, clean up
        if self.finished:
            self._cleanup()

    def gotLength(self, length):
        """Called when HTTP channel got length of content in this request.

        This method is not intended for users.
        """
        if length < 100000:
            self.content = StringIO()
        else:
            self.content = tempfile.TemporaryFile()

    def parseCookies(self):
        """Parse cookie headers.

        This method is not intended for users."""
        cookietxt = self.getHeader("cookie")
        if cookietxt:
            for cook in cookietxt.split(';'):
                cook = cook.lstrip()
                try:
                    k, v = cook.split('=')
                    self.received_cookies[k] = v
                except ValueError:
                    pass

    def handleContentChunk(self, data):
        """Write a chunk of data.

        This method is not intended for users.
        """
        self.content.write(data)

    def requestReceived(self, command, path, version):
        """Called by channel when all data has been received.

        This method is not intended for users.
        """
        self.content.seek(0,0)
        self.args = {}
        self.stack = []

        self.method, self.uri = command, path
        self.clientproto = version
        x = self.uri.split('?')

        if len(x) == 1:
            self.path = self.uri
        else:
            if len(x) != 2:
                log.msg("May ignore parts of this invalid URI: %s"
                        % repr(self.uri))
            self.path, argstring = x[0], x[1]
            self.args = parse_qs(argstring, 1)

        # cache the client and server information, we'll need this later to be
        # serialized and sent with the request so CGIs will work remotely
        self.client = self.channel.transport.getPeer()
        self.host = self.channel.transport.getHost()

        # Argument processing
        args = self.args
        ctype = self.getHeader('content-type')
        if self.method == "POST" and ctype:
            mfd = 'multipart/form-data'
            key, pdict = cgi.parse_header(ctype)
            if key == 'application/x-www-form-urlencoded':
                args.update(parse_qs(self.content.read(), 1))
            elif key == mfd:
                try:
                    args.update(cgi.parse_multipart(self.content, pdict))
                except KeyError, e:
                    if e.args[0] == 'content-disposition':
                        # Parse_multipart can't cope with missing
                        # content-dispostion headers in multipart/form-data
                        # parts, so we catch the exception and tell the client
                        # it was a bad request.
                        self.channel.transport.write(
                                "HTTP/1.1 400 Bad Request\r\n\r\n")
                        self.channel.transport.loseConnection()
                        return
                    raise
            else:
                pass

        self.process()

    def __repr__(self):
        return '<%s %s %s>'% (self.method, self.uri, self.clientproto)

    def process(self):
        """Override in subclasses.

        This method is not intended for users.
        """
        pass


    # consumer interface

    def registerProducer(self, producer, streaming):
        """Register a producer."""
        if self.producer:
            raise ValueError, "registering producer %s before previous one (%s) was unregistered" % (producer, self.producer)
        
        self.streamingProducer = streaming
        self.producer = producer
        
        if self.queued:
            producer.pauseProducing()
        else:
            self.transport.registerProducer(producer, streaming)

    def unregisterProducer(self):
        """Unregister the producer."""
        if not self.queued:        
            self.transport.unregisterProducer()
        self.producer = None

    # private http response methods

    def _sendError(self, code, resp=''):
        self.transport.write('%s %s %s\r\n\r\n' % (self.clientproto, code, resp))


    # The following is the public interface that people should be
    # writing to.

    def getHeader(self, key):
        """Get a header that was sent from the network.
        """
        return self.received_headers.get(key.lower())

    def getCookie(self, key):
        """Get a cookie that was sent from the network.
        """
        return self.received_cookies.get(key)

    def finish(self):
        """We are finished writing data."""
        if self.finished:
            warnings.warn("Warning! request.finish called twice.", stacklevel=2)
            return

        if not self.startedWriting:
            # write headers
            self.write('')

        if self.chunked:
            # write last chunk and closing CRLF
            self.transport.write("0\r\n\r\n")
        
        # log request
        if hasattr(self.channel, "factory"):
            self.channel.factory.log(self)

        self.finished = 1
        if not self.queued:
            self._cleanup()

    def write(self, data):
        """
        Write some data as a result of an HTTP request.  The first
        time this is called, it writes out response data.
        """
        if not self.startedWriting:
            self.startedWriting = 1
            version = self.clientproto
            l = []
            l.append('%s %s %s\r\n' % (version, self.code,
                                       self.code_message))
            # if we don't have a content length, we send data in
            # chunked mode, so that we can support pipelining in
            # persistent connections.
            if ((version == "HTTP/1.1") and
                (self.headers.get('content-length', None) is None) and
                self.method != "HEAD" and self.code not in NO_BODY_CODES):
                l.append("%s: %s\r\n" % ('Transfer-encoding', 'chunked'))
                self.chunked = 1
            if self.lastModified is not None:
                if self.headers.has_key('last-modified'):
                    log.msg("Warning: last-modified specified both in"
                            " header list and lastModified attribute.")
                else:
                    self.setHeader('last-modified',
                                   datetimeToString(self.lastModified))
            if self.etag is not None:
                self.setHeader('ETag', self.etag)
            for name, value in self.headers.items():
                l.append("%s: %s\r\n" % (name.capitalize(), value))
            for cookie in self.cookies:
                l.append('%s: %s\r\n' % ("Set-Cookie", cookie))
            l.append("\r\n")

            self.transport.writeSequence(l)

            # if this is a "HEAD" request, we shouldn't return any data
            if self.method == "HEAD":
                self.write = lambda data: None
                return

            # for certain result codes, we should never return any data
            if self.code in NO_BODY_CODES:
                self.write = lambda data: None
                return

        self.sentLength = self.sentLength + len(data)
        if data:
            if self.chunked:
                self.transport.writeSequence(toChunk(data))
            else:
                self.transport.write(data)

    def addCookie(self, k, v, expires=None, domain=None, path=None, max_age=None, comment=None, secure=None):
        """Set an outgoing HTTP cookie.

        In general, you should consider using sessions instead of cookies, see
        twisted.web.server.Request.getSession and the
        twisted.web.server.Session class for details.
        """
        cookie = '%s=%s' % (k, v)
        if expires != None:
            cookie = cookie +"; Expires=%s" % expires
        if domain != None:
            cookie = cookie +"; Domain=%s" % domain
        if path != None:
            cookie = cookie +"; Path=%s" % path
        if max_age != None:
            cookie = cookie +"; Max-Age=%s" % max_age
        if comment != None:
            cookie = cookie +"; Comment=%s" % comment
        if secure:
            cookie = cookie +"; Secure"
        self.cookies.append(cookie)

    def setResponseCode(self, code, message=None):
        """Set the HTTP response code.
        """
        self.code = code
        if message:
            self.code_message = message
        else:
            self.code_message = RESPONSES.get(code, "Unknown Status")

    def setHeader(self, k, v):
        """Set an outgoing HTTP header.
        """
        self.headers[k.lower()] = v

    def redirect(self, url):
        """Utility function that does a redirect.

        The request should have finish() called after this.
        """
        self.setResponseCode(FOUND)
        self.setHeader("location", url)
    
    def setLastModified(self, when):
        """Set the X{Last-Modified} time for the response to this request.

        If I am called more than once, I ignore attempts to set
        Last-Modified earlier, only replacing the Last-Modified time
        if it is to a later value.

        If I am a conditional request, I may modify my response code
        to L{NOT_MODIFIED} if appropriate for the time given.

        @param when: The last time the resource being returned was
            modified, in seconds since the epoch.
        @type when: number
        @return: If I am a X{If-Modified-Since} conditional request and
            the time given is not newer than the condition, I return
            L{http.CACHED<CACHED>} to indicate that you should write no
            body.  Otherwise, I return a false value.
        """
        # time.time() may be a float, but the HTTP-date strings are
        # only good for whole seconds.
        when = long(math.ceil(when))
        if (not self.lastModified) or (self.lastModified < when):
            self.lastModified = when

        modified_since = self.getHeader('if-modified-since')
        if modified_since:
            modified_since = stringToDatetime(modified_since)
            if modified_since >= when:
                self.setResponseCode(NOT_MODIFIED)
                return CACHED
        return None

    def setETag(self, etag):
        """Set an X{entity tag} for the outgoing response.

        That's \"entity tag\" as in the HTTP/1.1 X{ETag} header, \"used
        for comparing two or more entities from the same requested
        resource.\"

        If I am a conditional request, I may modify my response code
        to L{NOT_MODIFIED} or L{PRECONDITION_FAILED}, if appropriate
        for the tag given.

        @param etag: The entity tag for the resource being returned.
        @type etag: string
        @return: If I am a X{If-None-Match} conditional request and
            the tag matches one in the request, I return
            L{http.CACHED<CACHED>} to indicate that you should write
            no body.  Otherwise, I return a false value.
        """
        if etag:
            self.etag = etag

        tags = self.getHeader("if-none-match")
        if tags:
            tags = tags.split()
            if (etag in tags) or ('*' in tags):
                self.setResponseCode(((self.method in ("HEAD", "GET"))
                                      and NOT_MODIFIED)
                                     or PRECONDITION_FAILED)
                return CACHED
        return None

    def getAllHeaders(self):
        """Return dictionary of all headers the request received."""
        return self.received_headers

    def getRequestHostname(self):
        """Get the hostname that the user passed in to the request.

        This will either use the Host: header (if it is available) or the
        host we are listening on if the header is unavailable.
        """
        return (self.getHeader('host') or
                socket.gethostbyaddr(self.getHost()[1])[0]
                ).split(':')[0]

    def getHost(self):
        """Get my originally requesting transport's host.

        Don't rely on the 'transport' attribute, since Request objects may be
        copied remotely.  For information on this method's return value, see
        twisted.internet.tcp.Port.
        """
        return self.host

    def setHost(self, host, port, ssl=0):
        """Change the host and port the request thinks it's using.

        This method is useful for working with reverse HTTP proxies (e.g.
        both Squid and Apache's mod_proxy can do this), when the address
        the HTTP client is using is different than the one we're listening on.

        For example, Apache may be listening on https://www.example.com, and then
        forwarding requests to http://localhost:8080, but we don't want HTML produced
        by Twisted to say 'http://localhost:8080', they should say 'https://www.example.com',
        so we do::

           request.setHost('www.example.com', 443, ssl=1)

        This method is experimental.
        """
        self._forceSSL = ssl
        self.received_headers["host"] = host
        self.host = address.IPv4Address("TCP", host, port)

    def getClientIP(self):
        if isinstance(self.client, address.IPv4Address):
            return self.client.host
        else:
            return None

    def isSecure(self):
        if self._forceSSL:
            return True
        transport = getattr(getattr(self, 'channel', None), 'transport', None)
        if interfaces.ISSLTransport(transport, default=None) is not None:
            return True
        return False

    def _authorize(self):
        # Authorization, (mostly) per the RFC
        try:
            authh = self.getHeader("Authorization")
            if not authh:
                self.user = self.password = ''
                return
            bas, upw = authh.split()
            if bas.lower() != "basic":
                raise ValueError
            upw = base64.decodestring(upw)
            self.user, self.password = upw.split(':', 1)
        except (binascii.Error, ValueError):
            self.user = self.password = ""
        except:
            log.err()
            self.user = self.password = ""
    
    def getUser(self):
        try:
            return self.user
        except:
            pass
        self._authorize()
        return self.user

    def getPassword(self):
        try:
            return self.password
        except:
            pass
        self._authorize()
        return self.password

    def getClient(self):
        if self.client[0] not in ('INET', 'SSL'):
            return None
        host = self.client[1]
        try:
            name, names, addresses = socket.gethostbyaddr(host)
        except socket.error:
            return host
        names.insert(0, name)
        for name in names:
            if '.' in name:
                return name
        return names[0]

    def connectionLost(self, reason):
        """connection was lost"""
        pass
components.backwardsCompatImplements(Request)

class HTTPChannel(basic.LineReceiver, policies.TimeoutMixin):
    """A receiver for HTTP requests."""

    maxHeaders = 500 # max number of headers allowed per request
    
    length = 0
    persistent = 1
    __header = ''
    __first_line = 1
    __content = None

    # set in instances or subclasses
    requestFactory = Request

    _savedTimeOut = None

    def __init__(self):
        # the request queue
        self.requests = []

    def connectionMade(self):
        self.setTimeout(self.timeOut)
    
    def lineReceived(self, line):
        self.resetTimeout()

        if self.__first_line:
            # if this connection is not persistent, drop any data which
            # the client (illegally) sent after the last request.
            if not self.persistent:
                self.dataReceived = self.lineReceived = lambda *args: None
                return

            # IE sends an extraneous empty line (\r\n) after a POST request;
            # eat up such a line, but only ONCE
            if not line and self.__first_line == 1:
                self.__first_line = 2
                return

            # create a new Request object
            request = self.requestFactory(self, len(self.requests))
            self.requests.append(request)
            
            self.__first_line = 0
            parts = line.split()
            if len(parts) != 3:
                self.transport.write("HTTP/1.1 400 Bad Request\r\n\r\n")
                self.transport.loseConnection()
                return
            command, request, version = parts
            self._command = command
            self._path = request
            self._version = version
        elif line == '':
            if self.__header:
                self.headerReceived(self.__header)
            self.__header = ''
            self.allHeadersReceived()
            if self.length == 0:
                self.allContentReceived()
            else:
                self.setRawMode()
        elif line[0] in ' \t':
            self.__header = self.__header+'\n'+line
        else:
            if self.__header:
                self.headerReceived(self.__header)
            self.__header = line

    def headerReceived(self, line):
        """Do pre-processing (for content-length) and store this header away.
        """
        header, data = line.split(':', 1)
        header = header.lower()
        data = data.strip()
        if header == 'content-length':
            self.length = int(data)
        reqHeaders = self.requests[-1].received_headers
        reqHeaders[header] = data
        if len(reqHeaders) > self.maxHeaders:
            self.transport.write("HTTP/1.1 400 Bad Request\r\n\r\n")
            self.transport.loseConnection()
            
    def allContentReceived(self):
        command = self._command
        path = self._path
        version = self._version

        # reset ALL state variables, so we don't interfere with next request
        self.length = 0
        self._header = ''
        self.__first_line = 1
        del self._command, self._path, self._version

        # Disable the idle timeout, in case this request takes a long
        # time to finish generating output.
        if self.timeOut:
            self._savedTimeOut = self.setTimeout(None)

        req = self.requests[-1]
        req.requestReceived(command, path, version)

    def rawDataReceived(self, data):
        if len(data) < self.length:
            self.requests[-1].handleContentChunk(data)
            self.length = self.length - len(data)
        else:
            self.requests[-1].handleContentChunk(data[:self.length])
            extraneous = data[self.length:]
            self.allContentReceived()
            self.setLineMode(extraneous)

    def allHeadersReceived(self):
        req = self.requests[-1]
        req.parseCookies()
        self.persistent = self.checkPersistence(req, self._version)
        req.gotLength(self.length)

    def checkPersistence(self, request, version):
        """Check if the channel should close or not."""
        connection = request.getHeader('connection')
        if connection:
            tokens = map(lambda x: x.lower(), connection.split(' '))
        else:
            tokens = []

        # HTTP 1.0 persistent connection support is currently disabled,
        # since we need a way to disable pipelining. HTTP 1.0 can't do
        # pipelining since we can't know in advance if we'll have a
        # content-length header, if we don't have the header we need to close the
        # connection. In HTTP 1.1 this is not an issue since we use chunked
        # encoding if content-length is not available.

        #if version == "HTTP/1.0":
        #    if 'keep-alive' in tokens:
        #        request.setHeader('connection', 'Keep-Alive')
        #        return 1
        #    else:
        #        return 0
        if version == "HTTP/1.1":
            if 'close' in tokens:
                request.setHeader('connection', 'close')
                return 0
            else:
                return 1
        else:
            return 0

    def requestDone(self, request):
        """Called by first request in queue when it is done."""
        if request != self.requests[0]: raise TypeError
        del self.requests[0]

        if self.persistent:
            # notify next request it can start writing
            if self.requests:
                self.requests[0].noLongerQueued()
            else:
                if self._savedTimeOut:
                    self.setTimeout(self._savedTimeOut)
        else:
            self.transport.loseConnection()
    
    def timeoutConnection(self):
        log.msg("Timing out client: %s" % str(self.transport.getPeer()))
        policies.TimeoutMixin.timeoutConnection(self)

    def connectionLost(self, reason):
        self.setTimeout(None)
        for request in self.requests:
            request.connectionLost(reason)


class HTTPFactory(protocol.ServerFactory):
    """Factory for HTTP server."""

    protocol = HTTPChannel

    logPath = None
    
    timeOut = 60 * 60 * 12

    def __init__(self, logPath=None, timeout=60*60*12):
        if logPath is not None:
            logPath = os.path.abspath(logPath)
        self.logPath = logPath
        self.timeOut = timeout

    def buildProtocol(self, addr):
        p = protocol.ServerFactory.buildProtocol(self, addr)
        # timeOut needs to be on the Protocol instance cause
        # TimeoutMixin expects it there
        p.timeOut = self.timeOut
        return p

    def startFactory(self):
        _logDateTimeStart()
        if self.logPath:
            self.logFile = self._openLogFile(self.logPath)
        else:
            self.logFile = log.logfile

    def stopFactory(self):
        if hasattr(self, "logFile"):
            if self.logFile != log.logfile:
                self.logFile.close()
            del self.logFile
        _logDateTimeStop()

    def _openLogFile(self, path):
        """Override in subclasses, e.g. to use twisted.python.logfile."""
        f = open(path, "a", 1)
        f.seek(2, 0)
        return f

    def _escape(self, s):
        # pain in the ass. Return a string like python repr, but always
        # escaped as if surrounding quotes were "".
        r = repr(s)
        if r[0] == "'":
            return r[1:-1].replace('"', '\\"').replace("\\'", "'")
        return r[1:-1]

    def log(self, request):
        """Log a request's result to the logfile, by default in combined log format."""
        if hasattr(self, "logFile"):
            line = '%s - - %s "%s" %d %s "%s" "%s"\n' % (
                request.getClientIP(),
                # request.getUser() or "-", # the remote user is almost never important
                _logDateTime,
                '%s %s %s' % (self._escape(request.method),
                              self._escape(request.uri),
                              self._escape(request.clientproto)),
                request.code,
                request.sentLength or "-",
                self._escape(request.getHeader("referer") or "-"),
                self._escape(request.getHeader("user-agent") or "-"))
            self.logFile.write(line)
