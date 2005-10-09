package com.sun.gi.comm.users.protocol;

import java.nio.ByteBuffer;

import javax.security.auth.callback.Callback;

public interface TransportProtocolServer {

	
	void rcvUnicastMsg(boolean reliable, byte[] chanID, byte[] from, byte[] to, ByteBuffer databuff);

	void rcvMulticastMsg(boolean reliable, byte[] chanID, byte[] from, byte[][] tolist, ByteBuffer databuff);

	void rcvBroadcastMsg(boolean reliable, byte[] chanID, byte[] from, ByteBuffer databuff);

	void rcvReconnectReq(byte[] user, byte[] key);

	void rcvConnectReq();

	void rcvValidationResp(Callback[] cbs);

	void rcvReqJoinChan(byte[] chanID, byte[] user);
	

	  
}
