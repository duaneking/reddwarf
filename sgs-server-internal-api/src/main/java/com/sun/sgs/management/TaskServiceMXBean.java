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

package com.sun.sgs.management;

import com.sun.sgs.service.TaskService;

/**
 * The management interface for the task service.
 * <p>
 * An instance implementing this MBean can be obtained from the from the 
 * {@link java.lang.management.ManagementFactory.html#getPlatformMBeanServer() 
 * getPlatformMBeanServer} method.
 * <p>
 * The {@code ObjectName} for uniquely identifying this MBean is
 * {@value #MXBEAN_NAME}.
 * 
 */
public interface TaskServiceMXBean {
    /** The name for uniquely identifying this MBean. */
    String MXBEAN_NAME = "com.sun.sgs.service:type=TaskService";
    
    /**
     * Returns the number of times 
     * {@link TaskService#scheduleNonDurableTask(KernelRunnable, boolean) 
     * scheduleNonDurableTask} has been called.
     * @return the number of times {@code scheduleNonDurableTask} 
     *         has been called
     */
    long getScheduleNonDurableTaskCalls();
    
    /**
     * Returns the number of times 
     * {@link TaskService#scheduleNonDurableTask(KernelRunnable, long, boolean) 
     * scheduleNonDurableTask} has been called with a delay.
     * @return the number of times {@code scheduleNonDurableTask} 
     *         has been called with a delay
     */
    long getScheduleNonDurableTaskDelayedCalls();
    
    /**
     * Returns the number of times 
     * {@link TaskService#scheduleTask(Task) scheduleTask} has been called.
     * @return the number of times {@code scheduleTask} has been called
     */
    long getScheduleTaskCalls();
    
    /**
     * Returns the number of times 
     * {@link TaskService#scheduleTask(Task, long) scheduleTask} 
     * has been called with a delay.
     * @return the number of times {@code scheduleTask} has been called
     *         with a delay.
     */
    long getScheduleDelayedTaskCalls();
    
    /**
     * Returns the number of times
     * {@link TaskService#schedulePeriodicTask(Task, long, long)
     * schedulePeriodicTask} has been called.
     * @return the number of times {@code schedulPeriodicTask} has been called
     */
    long getSchedulePeriodicTaskCalls();
}
