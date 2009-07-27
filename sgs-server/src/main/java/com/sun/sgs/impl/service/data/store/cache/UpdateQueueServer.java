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

/** The interface for operations on the server's update queue. */
public interface UpdateQueueServer {

    /**
     * Commits changes to the server for the node identified by {@code nodeId}.
     * The {@code oids} parameter contains the object IDs of the objects that
     * have been changed.  For each element of that array, the element of the
     * {@code oidValues} array in the same position contains the new value for
     * the object ID, or {@code null} if the object should be removed.  The
     * {@code names} parameter contains the names whose bindings have been
     * changed.  For each element of that array, the element of the {@code
     * nameValues} array in the same position contains the new value for the
     * name binding, or {@code -1} if the name binding should be removed.
     *
     * @param	nodeId the node ID
     * @param	oids the object IDs
     * @param	oidValues the associated data values
     * @param	names the names
     * @param	nameValues the associated name bindings
     * @throws	CacheConsistencyException if the node does not have the
     *		appropriate access to the objects or bindings being committed
     * @throws	IllegalArgumentException if {@code nodeId} has not been
     *		registered, if {@code oids} and {@code oidValues} are not the
     *		same length, if {@code oids} contains a negative value, if
     *		{@code names} and {@code nameValues} are not the same length,
     *		or if {@code nameValues} contains a negative value
     */
    void commit(long nodeId,
		long[] oids,
		byte[][] oidValues,
		String[] names,
		long[] nameValues)
	throws CacheConsistencyException;

    /**
     * Evicts an object from the cache for the node identified by {@code
     * nodeId}.
     *
     * @param	nodeId the node ID
     * @param	oid the ID of the object to evict
     * @throws	CacheConsistencyException if the node does not have the
     *		specified object cached
     * @throws	IllegalArgumentException if {@code nodeId} has not been
     *		registered or if {@code oid} is negative
     */
    void evictObject(long nodeId, long oid)
	throws CacheConsistencyException;

    /**
     * Downgrades access to an object in the cache for the node identified by
     * {@code nodeId}.
     *
     * @param	nodeId the node ID
     * @param	oid the ID of the object to downgrade
     * @throws	CacheConsistencyException if the node does not have the
     *		specified object cached for write
     * @throws	IllegalArgumentException if {@code nodeId} has not been
     *		registered or if {@code oid} is negative
     */
    void downgradeObject(long nodeId, long oid)
	throws CacheConsistencyException;

    /**
     * Evicts a name binding from the cache for the node identified by {@code
     * nodeId}.  If {@code name} is {@code null}, then the binding represents
     * the last name beyond all possible names.
     *
     * @param	nodeId the node ID
     * @param	name the name or {@code null}
     * @throws	CacheConsistencyException if the node does not have the
     *		specified binding cached
     * @throws	IllegalArgumentException if {@code nodeId} has not been
     *		registered
     */
    void evictBinding(long nodeId, String name)
	throws CacheConsistencyException;

    /**
     * Downgrades access to a name binding in the cache for the node identified
     * by {@code nodeId}.  If {@code name} is {@code null}, then the binding
     * represents the last name beyond all possible names.
     *
     * @param	nodeId the node ID
     * @param	name the name or {@code null}
     * @throws	CacheConsistencyException if the node does not have the
     *		specified binding cached for write
     * @throws	IllegalArgumentException if {@code nodeId} has not been
     *		registered
     */
    void downgradeBinding(long nodeId, String name)
	throws CacheConsistencyException;
}
