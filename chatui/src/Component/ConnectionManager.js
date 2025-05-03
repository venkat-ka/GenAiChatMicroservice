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
            <button className='cursor' onClick={() => connect()}>Connect</button>
            <button className='cursor' onClick={() => disconnect()}>Disconnect</button>
        </>
    );
}