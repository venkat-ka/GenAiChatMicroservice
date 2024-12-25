import React from 'react';
import { soket } from '../socket.js';

export function ConnectionManager() {
    function connect() {
        console.log('testtt')
        soket.connect();
    }

    function disconnect() {
        soket.disconnect();
    }

    return (
        <>
            <button onClick={() => connect()}>Connect</button>
            <button onClick={() => disconnect()}>Disconnect</button>
        </>
    );
}