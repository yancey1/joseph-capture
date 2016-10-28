package slh.capture.service.impl.unified;


import java.io.IOException;  

import org.apache.http.Header;  
import org.apache.http.HttpException;  
import org.apache.http.HttpResponse;  
import org.apache.http.HttpResponseFactory;  
import org.apache.http.HttpVersion;  
import org.apache.http.conn.ClientConnectionOperator;  
import org.apache.http.conn.OperatedClientConnection;  
import org.apache.http.conn.scheme.SchemeRegistry;  
import org.apache.http.impl.conn.BasicClientConnectionManager;  
import org.apache.http.impl.conn.DefaultClientConnection;  
import org.apache.http.impl.conn.DefaultClientConnectionOperator;  
import org.apache.http.impl.conn.DefaultHttpResponseParser;  
import org.apache.http.io.HttpMessageParser;  
import org.apache.http.io.SessionInputBuffer;  
import org.apache.http.message.BasicHeader;  
import org.apache.http.message.BasicHttpResponse;  
import org.apache.http.message.BasicLineParser;  
import org.apache.http.message.BasicStatusLine;  
import org.apache.http.message.LineParser;  
import org.apache.http.params.HttpParams;  
import org.apache.http.util.CharArrayBuffer;  
public class MyBasicClientConnectionManager extends
		BasicClientConnectionManager {

	public MyBasicClientConnectionManager() {
		super();
	}

	@Override
	protected ClientConnectionOperator createConnectionOperator(
			final SchemeRegistry sr) {
		return new MyClientConnectionOperator(sr);
	}

	class MyLineParser extends BasicLineParser {
		@Override
		public Header parseHeader(final CharArrayBuffer buffer) {
			try {
				return super.parseHeader(buffer);
			} catch (Exception ex) {
				// 压制ParseException异常
				return new BasicHeader("invalid", buffer.toString());
			}
		}
	}

	class MyClientConnection extends DefaultClientConnection {
		@Override
		protected HttpMessageParser createResponseParser(
				final SessionInputBuffer buffer,
				final HttpResponseFactory responseFactory,
				final HttpParams params) {
			return new MyDefaultHttpResponseParser(buffer, new MyLineParser(),
					responseFactory, params);
		}
	}

	class MyDefaultHttpResponseParser extends DefaultHttpResponseParser {
		public MyDefaultHttpResponseParser(SessionInputBuffer buffer,
				LineParser parser, HttpResponseFactory responseFactory,
				HttpParams params) {
			super(buffer, parser, responseFactory, params);
		}

		@Override
		protected HttpResponse parseHead(final SessionInputBuffer sessionBuffer)
				throws IOException, HttpException {
			try {
				return super.parseHead(sessionBuffer);
			} catch (Exception ex) {
				// 压制ParseException异常
				return new BasicHttpResponse(new BasicStatusLine(
						HttpVersion.HTTP_1_1, 200, ""));
			}
		}
	}

	class MyClientConnectionOperator extends DefaultClientConnectionOperator {
		public MyClientConnectionOperator(final SchemeRegistry sr) {
			super(sr);
		}

		@Override
		public OperatedClientConnection createConnection() {
			return new MyClientConnection();
		}
	}
}