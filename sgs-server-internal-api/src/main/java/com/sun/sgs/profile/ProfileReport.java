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

package com.sun.sgs.profile;

import com.sun.sgs.auth.Identity;

import com.sun.sgs.kernel.KernelRunnable;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * The interface used to report profiling data associated with a complete
 * task run through the scheduler.
 */
public interface ProfileReport {

    /**
     * Returns the run task that generated this report.
     *
     * @return the <code>KernelRunnable</code> that was run
     */
    KernelRunnable getTask();

    /**
     * Returns the owner of the run task.
     *
     * @return the <code>Identity</code> of the task owner
     */
    Identity getTaskOwner();

    /**
     * Returns whether any of the task was transactional.
     *
     * @return <code>true</code> if any part of the task ran transactionally,
     *         <code>false</code> otherwise
     */
    boolean wasTaskTransactional();

    /**
     * Returns the identifier for the task's transaction, or <code>null</code>
     * if the task was not transactional.
     *
     * @return the transaction identifier or <code>null</code>
     */
    byte [] getTransactionId();
    
    /**
     * Returns detail about each participant in the transaction, or an
     * empty <code>Set</code> if the task was not transactional.
     *
     * @return a <code>Set</code> of <code>ProfileParticipantDetail</code>
     */
    Set<ProfileParticipantDetail> getParticipantDetails();

    /**
     * Returns detail about each listener for the transaction, or an
     * empty <code>Set</code> if the task was not transactional or had
     * no listeners.
     *
     * @return a <code>Set</code> of <code>TransactionListenerDetail</code>
     */
    Set<TransactionListenerDetail> getListenerDetails();

    /**
     * Returns whether the task successfully ran to completion. If this
     * task was transactional, then this means that the task committed
     * successfully.
     *
     * @return <code>true</code> if this task completed successfully,
     *         <code>false</code> otherwise
     */
    boolean wasTaskSuccessful();

    /**
     * Returns the time at which that task was scheduled to run.
     *
     * @return the requested starting time for the task in milliseconds
     *         since January 1, 1970
     */
    long getScheduledStartTime();

    /**
     * Returns the time at which the task actually started running.
     *
     * @return the actual starting time for the task in milliseconds
     *         since January 1, 1970
     */
    long getActualStartTime();

    /**
     * Returns the length of time spent running the task. Note that this
     * is wall-clock time, not the time actually spent running on the
     * processor.
     *
     * @return the length in milliseconds to execute the task
     */
    long getRunningTime();

    /**
     * Returns the number of times this task has been tried. If this is
     * the first time the task has been run, then this method returns 1.
     * 
     * @return the number of times this task has been tried
     */
    int getRetryCount();

    /**
     * Returns the operations that were reported as executed during the
     * running of the task. If no operations were reported, then an
     * empty <code>List</code> is returned.
     *
     * @return a {@code List} of {@code String}s of the names of the reported
     *         operations, in the order they were reported
     */
    List<String> getReportedOperations();

    /**
     * Returns the values of the task-local counters that were updated
     * during the running of the task. If no task-local counters were
     * updated, then an empty {@code Map} is returned. The
     * <code>Map</code> is a mapping from counter name to counter
     * value.
     *
     * @return a <code>Map</code> from counter name to observed value
     */
    Map<String, Long> getUpdatedTaskCounters();

    /**
     * Returns the list of values for the task-local samples that were
     * updated during the running of the task. If no task-local
     * samples were updated, then an empty {@code Map} is
     * returned. The <code>Map</code> is a mapping from sample name
     * to an oldest-first list of sample values.
     *
     * @return a <code>Map</code> from sample name to a list of values
     *         added during the task
     */
    Map<String, List<Long>> getUpdatedTaskSamples();  

    /**
     * Returns detail of the object accesses as reported by the
     * <code>AccessCoordinator</code> or <code>null</code> if no
     * objects were accessed, no accesses reported, or if this
     * report is for a non-transactional task.
     *
     * @return the associated access detail or <code>null</code>
     */
    AccessedObjectsDetail getAccessedObjectsDetail();

    /**
     * Returns the number of tasks in the scheduler and ready to run when 
     * this report's task was started. 
     *
     * @return the number of ready tasks
     */
    int getReadyCount();

    /**
     * Returns any failure that occurred during the execution of
     * this report's task, or <code>null</code> if no failure
     * occurred.  This <code>Throwable</code> will always be
     * <code>null</code> if {@link #wasTaskSuccessful()} returns
     * <code>true</code>.
     * 
     * @return the <code>Throwable</code> thrown during task execution
     *         or <code>null</code> if no failure occurred
     */
    Throwable getFailureCause();

}
