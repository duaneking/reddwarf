/*
 * Copyright 2007-2009 Sun Microsystems, Inc.
 *
 * This file is part of Project Darkstar Server.
 *
 * Project Darkstar Server is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation and
 * distributed hereunder to you.
 *
 * Project Darkstar Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sun.sgs.impl.service.data.store.cache;

import static com.sun.sgs.impl.util.DataStreamUtil.readByteArrays;
import static com.sun.sgs.impl.util.DataStreamUtil.readLongs;
import static com.sun.sgs.impl.util.DataStreamUtil.readString;
import static com.sun.sgs.impl.util.DataStreamUtil.readStrings;
import static com.sun.sgs.impl.util.DataStreamUtil.writeByteArrays;
import static com.sun.sgs.impl.util.DataStreamUtil.writeLongs;
import static com.sun.sgs.impl.util.DataStreamUtil.writeString;
import static com.sun.sgs.impl.util.DataStreamUtil.writeStrings;
import com.sun.sgs.service.SimpleCompletionHandler;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;

/** The subclass of all requests used by {@link UpdateQueue}. */
abstract class UpdateQueueRequest implements Request {

    /** The identifier for {@link Commit} requests. */
    static final byte COMMIT = 1;

    /** The identifier for {@link EvictObject} requests. */
    static final byte EVICT_OBJECT = 2;

    /** The identifier for {@link DowngradeObject} requests. */
    static final byte DOWNGRADE_OBJECT = 3;

    /** The identifier for {@link EvictBinding} requests. */
    static final byte EVICT_BINDING = 4;

    /** The identifier for {@link DowngradeBinding} requests. */
    static final byte DOWNGRADE_BINDING = 5;

    /** The request handler used to implement {@link UpdateQueue}. */
    static class UpdateQueueRequestHandler
	implements Request.RequestHandler<UpdateQueueRequest>
    {
	/** The underlying server that handles requests. */
	private final CachingDataStoreServerImpl server;

	/** The node ID associated with this queue. */
	private final long nodeId;

	/**
	 * Creates an instance of this class.
	 *
	 * @param	server the underlying server
	 * @param	nodeId the node ID associated with this queue
	 */
	UpdateQueueRequestHandler(
	    CachingDataStoreServerImpl server, long nodeId)
	{
	    this.server = server;
	    this.nodeId = nodeId;
	}

	/* -- Implement RequestHandler -- */

	/** {@inheritDoc} */
	public UpdateQueueRequest readRequest(DataInput in)
	    throws IOException
	{
	    byte requestType = in.readByte();
	    switch (requestType) {
	    case COMMIT:
		return new Commit(in);
	    case EVICT_OBJECT:
		return new EvictObject(in);
	    case DOWNGRADE_OBJECT:
		return new DowngradeObject(in);
	    case EVICT_BINDING:
		return new EvictBinding(in);
	    case DOWNGRADE_BINDING:
		return new DowngradeBinding(in);
	    case -1:
		throw new EOFException("End of file while reading request");
	    default:
		throw new IOException("Unknown request type: " + requestType);
	    }
	}

	/** {@inheritDoc} */
	public void performRequest(UpdateQueueRequest request)
	    throws Exception
	{
	    request.performRequest(server, nodeId);
	}
    }

    /**
     * Performs a request using the specified server and node ID.
     *
     * @param	server the underlying server
     * @param	nodeId the node ID associated with the update queue
     * @throws	Exception if the request fails
     */
    abstract void performRequest(
	CachingDataStoreServerImpl server, long nodeId)
	throws Exception;

    /**
     * A subclass of {@code UpdateQueueRequest} for requests that contain a
     * completion handler.
     */
    abstract static class UpdateQueueRequestWithCompletion
	extends UpdateQueueRequest
    {
	/**
	 * The {@code SimpleCompletionHandler} to call when the request is
	 * completed, or {@code null} if there is no handler.
	 */
	private final SimpleCompletionHandler completionHandler;

	/** Creates an instance with no completion handler. */
	UpdateQueueRequestWithCompletion() {
	    completionHandler = null;
	}

	/**
	 * Creates an instance with the specified completion handler.
	 *
	 * @param	completionHandler the completion handler
	 */
	UpdateQueueRequestWithCompletion(
	    SimpleCompletionHandler completionHandler)
	{
	    this.completionHandler = completionHandler;
	}

	public void completed(Throwable exception) {
	    /* FIXME: Need to handle exception, which should cause a shutdown */
	    if (completionHandler != null) {
		completionHandler.completed();
	    }
	}
    }

    /** Represents a call to {@link UpdateQueue#commit}. */
    static class Commit extends UpdateQueueRequest {
	private final long[] oids;
	private final byte[][] oidValues;
	private final String[] names;
	private final long[] nameValues;

	Commit(
	    long[] oids, byte[][] oidValues, String[] names, long[] nameValues)
	{
	    this.oids = oids;
	    this.oidValues = oidValues;
	    this.names = names;
	    this.nameValues = nameValues;
	}

	Commit(DataInput in) throws IOException {
	    oids = readLongs(in);
	    oidValues = readByteArrays(in);
	    names = readStrings(in);
	    nameValues = readLongs(in);
	}

	void performRequest(
	    CachingDataStoreServerImpl server, long nodeId)
	    throws CacheConsistencyException
	{
	    server.commit(nodeId, oids, oidValues, names, nameValues);
	}

	public void writeRequest(DataOutput out) throws IOException {
	    out.write(COMMIT);
	    writeLongs(oids, out);
	    writeByteArrays(oidValues, out);
	    writeStrings(names, out);
	    writeLongs(nameValues, out);
	}

	public void completed(Throwable exception) {
	    /* FIXME: Need to handle exception, which should cause a shutdown */
	}
    }

    /** Represents a call to {@link UpdateQueue#evictObject}. */
    static class EvictObject extends UpdateQueueRequestWithCompletion {
	private final long oid;

	EvictObject(long oid, SimpleCompletionHandler completionHandler) {
	    super(completionHandler);
	    this.oid = oid;
	}

	EvictObject(DataInput in) throws IOException {
	    oid = in.readLong();
	}

	void performRequest(CachingDataStoreServerImpl server, long nodeId)
	    throws CacheConsistencyException
	{
	    server.evictObject(nodeId, oid);
	}

	public void writeRequest(DataOutput out) throws IOException {
	    out.write(EVICT_OBJECT);
	    out.writeLong(oid);
	}
    }

    /** Represents a call to {@link UpdateQueue#downgradeObject}. */
    static class DowngradeObject
	extends UpdateQueueRequestWithCompletion
    {
	private final long oid;

	DowngradeObject(long oid, SimpleCompletionHandler completionHandler) {
	    super(completionHandler);
	    this.oid = oid;
	}

	DowngradeObject(DataInput in) throws IOException {
	    oid = in.readLong();
	}

	void performRequest(CachingDataStoreServerImpl server, long nodeId) 
	    throws CacheConsistencyException
	{
	    server.downgradeObject(nodeId, oid);
	}

	public void writeRequest(DataOutput out) throws IOException {
	    out.write(DOWNGRADE_OBJECT);
	    out.writeLong(oid);
	}
    }

    /** Represents a call to {@link UpdateQueue#evictBinding}. */
    static class EvictBinding
	extends UpdateQueueRequestWithCompletion
    {
	private final String name;

	EvictBinding(String name, SimpleCompletionHandler completionHandler) {
	    super(completionHandler);
	    this.name = name;
	}

	EvictBinding(DataInput in) throws IOException {
	    name = readString(in);
	}

	void performRequest(CachingDataStoreServerImpl server, long nodeId)
	    throws CacheConsistencyException
	{
	    server.evictBinding(nodeId, name);
	}

	public void writeRequest(DataOutput out) throws IOException {
	    out.write(EVICT_BINDING);
	    writeString(name, out);
	}
    }

    /** Represents a call to {@link UpdateQueue#downgradeBinding}. */
    static class DowngradeBinding
	extends UpdateQueueRequestWithCompletion
    {
	private final String name;
	DowngradeBinding(
	    String name, SimpleCompletionHandler completionHandler)
	{
	    super(completionHandler);
	    this.name = name;
	}

	DowngradeBinding(DataInput in) throws IOException {
	    name = readString(in);
	}

	void performRequest(CachingDataStoreServerImpl server, long nodeId)
	    throws CacheConsistencyException
	{
	    server.downgradeBinding(nodeId, name);
	}

	public void writeRequest(DataOutput out) throws IOException {
	    out.write(DOWNGRADE_BINDING);
	    writeString(name, out);
	}
    }
}
