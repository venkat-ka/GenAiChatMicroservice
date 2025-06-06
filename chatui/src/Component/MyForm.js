import React, { useState } from 'react';
import { soket } from '../socket.js';

export function MyForm() {
    const [value, setValue] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    function onSubmit(event) {
        event.preventDefault();
        setIsLoading(true);

        soket.timeout(5000).emit('create-something', value, () => {
            setIsLoading(false);
        });
    }

    return (
        <form onSubmit={onSubmit}>
            <input onChange={e => setValue(e.target.value)} />

            <button type="submit" disabled={isLoading}>Submit</button>
        </form>
    );
}