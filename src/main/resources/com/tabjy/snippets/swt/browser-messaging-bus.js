(() => {
    if (!window.messagingBus) {
        return
    }
    
    class MessagingBus {
        constructor() {
            // TODO:
        }

        addMessageListener(channel, listener) {
            
        }

        removeMessageListener(channel, listener) {
            
        }

        message(channel, ...payload) {
            
        }

        setInvocationHandler(channel, handler) {
            
        }
        
        removeInvocationHandler(channel){
            
        }
        
        involk(channel, ...args) {
            
        }
        
        _onRawMessage(channel, ...payload) {
            
        }
    }

    MessagingBus.prototype._postRawMessage = __messageBusPostRaw

    window.messageBus = MessagingBus;
})()
