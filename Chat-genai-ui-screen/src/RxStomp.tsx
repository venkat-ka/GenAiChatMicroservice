import React, { useState, useEffect } from 'react';
import { soket } from './socket.js';
import { ConnectionState } from './Component/ConnectionState.js';
import { ConnectionManager } from './Component/ConnectionManager.js';
import { Events } from './Component/Events.js';
import { MyForm } from './Component/MyForm.js';
//https://socket.io/how-to/use-with-react#example
function RxStomp() {
    const [isConnected, setIsConnected] = useState(soket.connected);
    const [fooEvents, setFooEvents] = useState([]);

    useEffect(() => {
        function onConnect() {
            setIsConnected(true);
        }

        function onDisconnect() {
            setIsConnected(false);
        }

        function onFooEvent(value) {
            setFooEvents(previous => [...previous, value]);
        }

        soket.on('connect', onConnect);
        soket.on('disconnect', onDisconnect);
        soket.on('foo', onFooEvent);

        return () => {
            soket.off('connect', onConnect);
            soket.off('disconnect', onDisconnect);
            soket.off('foo', onFooEvent);
        };
    }, []);

    return (
        <div className="App">
            <ConnectionState isConnected={isConnected} />
            <Events events={fooEvents} />
            <ConnectionManager />
            <MyForm />
        </div>
    );
}

export default RxStomp;