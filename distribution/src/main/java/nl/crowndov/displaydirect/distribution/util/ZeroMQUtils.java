package nl.crowndov.displaydirect.distribution.util;

import org.zeromq.ZFrame;
import org.zeromq.ZMsg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

public class ZeroMQUtils {
	
	public static String[] gunzipMultifameZMsg(ZMsg msg) throws IOException {
		Iterator<ZFrame> frames = msg.iterator();
		// pop off first frame, which contains "/GOVI/KV8" (the feed name)
		// (isn't there a method for this?)
		String header = frames.next().toString();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		while (frames.hasNext()) {
			ZFrame frame = frames.next();
			byte[] frameData = frame.getData();
			buffer.write(frameData);
		}
		if (buffer.size() == 0) {
			return null;
		}
		// chain input streams to gunzip contents of byte buffer
		InputStream gzippedMessageStream = new ByteArrayInputStream(
				buffer.toByteArray());
		InputStream messageStream = new GZIPInputStream(gzippedMessageStream);
		// copy input stream back to transport stream
		buffer.reset();
		byte[] b = new byte[4096];
		for (int n; (n = messageStream.read(b)) != -1;) {
			buffer.write(b, 0, n);
		}
		return new String[] { header, buffer.toString() };
	}
}
