import React from 'react';
import SockJsClient from 'react-stomp';

class SampleComponent extends React.Component {
    constructor(props) {
        super(props);
    }

    sendMessage = (msg) => {
        this.clientRef.sendMessage('/topics/all', msg);
    }

    render() {
        return (
            <div>
                <SockJsClient url='http://localhost:8082/kafka-prod/chat/ws-chat' topics={['/topic/group']}
                    onMessage={(msg) => { console.log(msg); }}
                    ref={(client) => { this.clientRef = client }}
                    headers={{ headers: { "Auth": "fff" } }} />
            </div>
        );
    }
}
export default SampleComponent;