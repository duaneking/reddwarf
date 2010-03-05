/*
 *
 * Copyright (c) 2007-2010, Oracle and/or its affiliates.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in
 *       the documentation and/or other materials provided with the
 *       distribution.
 *     * Neither the name of Sun Microsystems, Inc. nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.sun.sgs.service;


/**
 * This is the base interface used for all services. Services support
 * specific funcationality and work in a transactional context. 
 * See {@code TransactionParticipant} for details on when interaction
 * between {@code Service}s is allowed.
 * <p>
 * On startup of an application, services are constructed (see details
 * below). This provides access to the non-transactional core components
 * of the system as well as the other {@code Service}s that have already
 * been created. {@code Service}s are created in a known order based
 * on dependencies: {@code DataService}, {@code WatchdogService},
 * {@code NodeMappingService}, {@code TaskService},
 * {@code ClientSessionService}, and the {@code ChannelManager},
 * finishing with any custom {@code Service}s ordered based on the
 * application's configuration.
 * <p>
 * All implementations of {@code Service} must have a constructor with
 * parameters of types {@code Properties}, {@code ComponentRegistry}, and
 * {@code TransactionProxy}. This is how the {@code Service} is created
 * on startup. The {@code Properties} parameter provides application and
 * service-specific properties. The {@code ComponentRegistry} provides
 * access to non-transactional kernel and system components like the
 * {@code TransactionScheduler}. The {@code TransactionProxy} provides
 * access to transactional state (when active) and the other available
 * {@code Service}s. If any error occurs in creating a {@code Service},
 * the constructor may throw any {@code Exception}, causing the application
 * to shutdown.
 * <p>
 * Note that {@code Service}s are not created in the context of a
 * transaction. If a given constructor needs to do any work transactionally,
 * it may do so by calling {@code TransactionScheduler.runTask}.
 */
public interface Service {

    /**
     * Returns the name used to identify this service.
     *
     * @return the service's name
     */
    String getName();

    /**
     * Notifies this {@code Service} that the application is fully
     * configured and ready to start running. This means that all other {@code
     * Service}s associated with this application have been successfully
     * created. If the method throws an exception, then the application will be
     * shutdown.
     *
     * @throws Exception if an error occurs
     */
    void ready() throws Exception;

    /**
     * Shuts down this service. Any call to this method will block
     * until the shutdown has completed. If a shutdown has been completed
     * already, this method will return immediately.<p>
     *
     * This method does not require a transaction, and should not be called
     * from one because this method will typically not succeed if there are
     * outstanding transactions. <p>
     *
     * When this method returns, it is assumed that the service has been 
     * shutdown.<p>
     *
     * Callers should assume that, in a worst case, this method may block
     * indefinitely, and so should arrange to take other action (for example,
     * calling {@link System#exit System.exit}) if the call fails to complete
     * successfully in a certain amount of time.
     */
    void shutdown();
}
