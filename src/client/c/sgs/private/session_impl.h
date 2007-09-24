#ifndef SGS_SESSION_IMPL_H
#define SGS_SESSION_IMPL_H 1

#ifdef __cplusplus
extern "C" {
#endif

#include "sgs/config.h"

typedef struct sgs_session_impl sgs_session_impl;

#include "sgs/private/connection_impl.h"
#include "sgs/private/map.h"
#include "sgs/protocol.h"

struct sgs_session_impl {
    /** The underlying network connection. */
    sgs_connection_impl* connection;
    
    /** Server-assigned unique ID for this session. */
    sgs_id* session_id;
    
    /** Server-assigned key used to reconnect after disconnect. */
    sgs_id* reconnect_key;
    
    /** Map of channels to which this session is currently a member. */
    sgs_map* channels;
    
    /**
     * Sequence number used in some messages (increment after each use).  We
     * have to store this as two 32-bit ints instead of just a single 64-bit
     * int so that we can use htonl() and ntohl() of which there are no 64-bit
     * versions.
     */
    uint32_t seqnum_hi;
    uint32_t seqnum_lo;
    
    /**
     * Used as the backing array for any sgs_messages (more efficient to just
     * declare once and keep it around than to malloc() every time we need one).
     */
    uint8_t msg_buf[SGS_MSG_MAX_LENGTH];
};

/*
 * function: sgs_session_impl_destroy()
 *
 * Performs any necessary memory deallocations to dispose of an sgs_session.
 */
void sgs_session_impl_destroy(sgs_session_impl* session);

/*
 * function: sgs_session_impl_login()
 *
 * Creates and sends a login request message to the server with the specified
 * login and password values.  Returns 0 on success and -1 on failure, with
 * errno set to the specific error code.
 */
int sgs_session_impl_login(sgs_session_impl* session, const char* login,
    const char* password);

/*
 * function: sgs_session_impl_logout()
 *
 * Creates and sends a logout request message to the server.  Returns 0 on
 * success and -1 on failure, with errno set to the specific error code.
 */
int sgs_session_impl_logout(sgs_session_impl* session);

/*
 * function: sgs_session_impl_create()
 *
 * Creates a new sgs_session from the specified connection.  Returns null on
 * failure.
 */
sgs_session_impl* sgs_session_impl_create(sgs_connection_impl* connection);

/*
 * function: sgs_session_impl_recv_msg()
 *
 * Notifies the session that a new sgs_message has been received and written
 * into the session's internal msg_buf field, ready to be processed.  Returns 0
 * on success and -1 on failure, with errno set to the specific error code.
 */
int sgs_session_impl_recv_msg(sgs_session_impl* session);

/*
 * function: sgs_session_impl_send_msg()
 *
 * Notifies the session that a new sgs_message has been created and written
 * into the session's internal msg_buf field, ready to be sent to the server.
 * Returns 0 on success and -1 on failure, with errno set to the specific error
 * code.
 */
int sgs_session_impl_send_msg(sgs_session_impl* session);

#ifdef __cplusplus
}
#endif

#endif /* !SGS_SESSION_IMPL_H */
