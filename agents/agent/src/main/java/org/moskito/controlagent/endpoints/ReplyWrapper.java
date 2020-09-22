package org.moskito.controlagent.endpoints;

/**
 * The reply wrapper object for bringing in protocol version and timestamp into the return object.
 *
 * @author lrosenberg
 * @since 14.06.13 10:02
 */
public class ReplyWrapper {
	private final int protocolVersion = 2;
	private final long timestamp = System.currentTimeMillis();
	private Object reply;

	public ReplyWrapper(Object aReply){
		reply = aReply;
	}

	public Object getReply() {
		return reply;
	}

	public void setReply(Object reply) {
		this.reply = reply;
	}

	public int getProtocolVersion() {
		return protocolVersion;
	}

	public long getTimestamp() {
		return timestamp;
	}
}
