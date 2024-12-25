import React, { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';

const WebS = () => {
    const [messages, setMessages] = useState([]);
    const [client, setClient] = useState(null);

    useEffect(() => {
        const sock = new SockJS('http://localhost:8082/kafka-prod/chat/ws-chat',);
        setClient(sock);

        sock.onopen = () => {
            console.log('Connection opened');
        };

        sock.onmessage = (e) => {
            setMessages((prevMessages) => [...prevMessages, e.data]);
        };

        sock.onclose = () => {
            console.log('Connection closed');
        };

        return () => {
            sock.close();
        };
    }, []);

    const sendMessage = () => {
        if (client) {
            client.send('Hello, SockJS!');
        }
    };

    return (
        <div>
            <button onClick={sendMessage}>Send Message</button>
            <ul>
                {messages.map((msg, index) => (
                    <li key={index}>{msg}</li>
                ))}
            </ul>
        </div>
    );
};

export default WebS;
