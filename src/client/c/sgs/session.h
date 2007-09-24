/*
 * This file provides declarations relating to client-server sessions.
 */

#ifndef SGS_SESSION_H
#define SGS_SESSION_H 1

#ifdef __cplusplus
extern "C" {
#endif

#include "sgs/config.h"

typedef struct sgs_session_impl sgs_session;

#include "sgs/id.h"

/*
 * function: sgs_session_direct_send()
 *
 * Sends a message directly to the server (i.e. not on a channel).  This is
 * sometimes used to implement application-specific messaging.
 *
 * args:
 *   session: the session to send a message
 *      data: array containing the message to send
 *   datalen: length of the message
 *
 * returns:
 *    0: success
 *   -1: failure (errno is set to specific error code)
 */
int sgs_session_direct_send(sgs_session* session, const uint8_t* data,
    size_t datalen);

/*
 * function: sgs_session_get_reconnectkey()
 *
 * Returns the reconnection-key for this session.
 */
const sgs_id* sgs_session_get_reconnectkey(const sgs_session* session);

/*
 * function: sgs_session_get_id()
 *
 * Returns this session's unique ID.
 */
const sgs_id* sgs_session_get_id(const sgs_session* session);

#ifdef __cplusplus
}
#endif

#endif /* !SGS_SESSION_H */
